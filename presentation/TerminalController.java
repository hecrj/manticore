package interiores.core.presentation;

import interiores.core.Utils;
import interiores.core.business.BusinessController;
import interiores.core.presentation.terminal.CommandGroup;
import interiores.core.presentation.terminal.IOStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hector
 */
public class TerminalController extends PresentationController
{
    private String commandsPath;
    private IOStream iostream;
    private Map<String, CommandGroup> commands;
    private Map<String, String> shortcuts;
    
    public TerminalController(String commandsPath)
    {
        this.commandsPath = commandsPath;
        iostream = new IOStream();
        commands = new HashMap();
        shortcuts = new HashMap();
    }
    
    @Override
    public void init()
    {
        iostream.setInputStream(System.in);
        iostream.setOutputStream(System.out);
        
        try
        {
            String line = iostream.readLine();

            while(line != null && !line.startsWith("quit"))
            {
                exec(line);
                line = iostream.readLine();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void notify(String name, Map<String, Object> data)
    {
        System.out.println(name);
        
        for(Map.Entry<String, Object> e : data.entrySet())
            iostream.println(e.getKey() + ": " + e.getValue().toString());
    }
    
    @Override
    public void addBusinessController(String name, BusinessController controller)
    {
        try
        {
            Class comgroupClass = Class.forName(commandsPath + "." + Utils.capitalize(name) +
                    "Commands");
            
            CommandGroup comgroup = (CommandGroup) comgroupClass.getConstructor(
                    controller.getClass()).newInstance(controller);
            
            comgroup.setIOStream(iostream);
            commands.put(name, comgroup);
            
            super.addBusinessController(name, controller);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addShortcut(String subject, String shortcut) {
        shortcuts.put(shortcut, subject);
    }
    
    public void exec(String line)
    {
        try
        {
            iostream.setInputBuffer(line);
            
            String action, method;
            action = method = iostream.readString();
            
            if(isReserved(action))
                method = "_" + action;
            
            String shortcut = iostream.readString();
            String subject = getSubject(shortcut);
            
            System.out.println("Action is " + action + " on subject " + subject);
            
            if(! commands.containsKey(subject))
                throw new Exception("There is no subject known as " + subject);
            
            CommandGroup comgroup = commands.get(subject);
            Class comgroupClass = comgroup.getClass();
            
            try
            {
                comgroupClass.getMethod(method).invoke(comgroup);
            }
            catch(InvocationTargetException e)
            {
                throw e.getCause();
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace(); // @TODO Improve exception handling
        }
    }
    
    private String getSubject(String shortcut) {
        if(! shortcuts.containsKey(shortcut))
            return shortcut;
        
        return shortcuts.get(shortcut);
    }
    
    private boolean isReserved(String s)
    {
        return (s.equals("new"));
    }
}
