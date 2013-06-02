package manticore.presentation;

import manticore.Debug;
import manticore.Event;
import manticore.Utils;
import manticore.business.BusinessController;
import manticore.business.BusinessException;
import manticore.presentation.terminal.AdvancedCommandGroup;
import manticore.presentation.terminal.CommandGroup;
import manticore.presentation.terminal.IOStream;
import manticore.presentation.terminal.annotation.CommandSubject;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBException;

/**
 * Represents a terminal command line tool presentation controller.
 * @author hector
 */
public class TerminalController extends PresentationController
{
    /**
     * The padding for the commands name when showing the help
     */
    private static final int HELP_PADDING = 20;
    
    private static final String COMMENT_MARK = "#";
    
    /**
     * The package where the commands are located
     */
    private String commandsPackage;
    
    /**
     * The input/output stream that the terminal uses
     */
    private IOStream iostream;
    
    /**
     * Map of command groups identified by subject name
     */
    private Map<String, CommandGroup> commandGroups;
    
    /**
     * Map of available shortcuts for the commands subjects
     */
    private Map<String, String> shortcuts;
    
    /**
     * The welcoming message that the terminal shows on initialization
     */
    private String welcomeMsg;
    
    private boolean isInitialized;
    
    /**
     * Constructs a new TerminalController with the default System input/output.
     * @param commandsPackage The package where the commands are located
     */
    public TerminalController(String commandsPackage, InputStream istream, PrintStream ostream)
    {
        this.commandsPackage = commandsPackage;
        iostream = new IOStream(istream, ostream);
        commandGroups = new TreeMap();
        shortcuts = new HashMap();
        isInitialized = false;
    }
    
    public void setWelcomeMessage(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }
    
    /**
     * Initializes and runs the terminal.
     */
    @Override
    public void init()
    {
        if(isInitialized)
            throw new RuntimeException("The terminal is initialized already.");
        
        isInitialized = true;
        
        new Thread() {
            @Override
            public void run() {
                iostream.println(welcomeMsg);

                String line = iostream.readLine();

                while(line != null)
                {
                    line = line.trim();

                    if (! shouldLineBeIgnored(line)) {
                        // Set subcommand prompt
                        iostream.setPrompt(">>");
                        exec(line);
                    }

                    // Set command prompt
                    iostream.setPrompt(">");
                    line = iostream.readLine();
                }
                
                isInitialized = false;
            }
        }.start();
    }
    
    private boolean shouldLineBeIgnored(String line)
    {
        return line.isEmpty() || line.startsWith(COMMENT_MARK);
    }
    
    /**
     * Recieves events and, in debug mode, prints the information received.
     * @param name Name of the event
     * @param data Data related with the event
     */
    @Override
    public void notify(Event event)
    {
        // Does nothing by default
    }
    
    /**
     * Adds a business controller on top of the terminal creating a command subject of the same name and
     * associating the business controller to it.
     * These controllers are injected into the commands of the terminal using reflection.
     * @param name Name of the business controller
     * @param controller Business controller to add
     */
    @Override
    public void addBusinessController(BusinessController controller)
    {
        try
        {
            String name = controller.getClass().getSimpleName().replace("Controller", "");
            Class comGroupClass = Class.forName(commandsPackage + "." + name +
                    "Commands");
            
            if(!comGroupClass.isAnnotationPresent(CommandSubject.class))
                throw new Exception("There is no CommandSubject annotation in the " + name + " command "
                        + "group.");
            
            CommandSubject commandSubject = (CommandSubject)comGroupClass.getAnnotation(CommandSubject.class);
            String commandGroupName = commandSubject.name();
            
            if(! name.equals(commandGroupName))
                shortcuts.put(name, commandGroupName);
            
            AdvancedCommandGroup commandGroup = (AdvancedCommandGroup) comGroupClass.getConstructor(
                controller.getClass()).newInstance(controller);
            
            commandGroup.setName(commandGroupName);
            commandGroup.setDescription(commandSubject.description());
            commandGroup.setIOStream(iostream);
            commandGroup.scanCommands();
            
            commandGroups.put(commandGroupName, commandGroup);
            super.addBusinessController(controller);
        }
        catch(Exception e)
        {
            if(Debug.isEnabled())
                e.printStackTrace();
        }
    }
    
    /**
     * Executes a command line.
     * @param line The command line.
     */
    private void exec(String line)
    {
        iostream.putIntoInputBuffer(line);
        
        String actionName, commandName;
        commandName = iostream.readString();
        
        if(commandName.equals("quit")) {
            quit();
            return;
        }
        
        if(commandName.equals("help") && !iostream.hasNext()) {
            showHelp();
            return;
        }
        
        String shortcut = iostream.readString();
        String subjectName = getSubject(shortcut);
        
        Debug.println("Action is " + commandName + " on subject " + subjectName);
        
        try
        {         
            if(! commandGroups.containsKey(subjectName))
                throw new BusinessException("There is no subject known as " + subjectName);
            
            commandGroups.get(subjectName).exec(commandName);
        }
        catch(BusinessException e) {
            iostream.println("[Business error] " + e.getMessage());
        }
        catch(JAXBException e) {
            iostream.println("[Storage error] " + e.getLinkedException().getMessage());
            
            if(Debug.isEnabled())
                e.printStackTrace();
        }
        catch(NoSuchMethodException e) {
            iostream.println("[Terminal error] Command not found.");
        }
        catch(Throwable e) {
            if(Debug.isEnabled())
                e.printStackTrace();
        }
    }
    
    public void showHelp() {
        iostream.println("Available commands:");
        
        for(CommandGroup commandGroup : commandGroups.values())
            iostream.println("    " + Utils.padRight(commandGroup.getName(), HELP_PADDING) +
                    commandGroup.getDescription());
        
        iostream.println("    " + Utils.padRight("quit", HELP_PADDING) + "Closes the application");
        iostream.println("Use 'help [command]' to show more information about the command.");
    }
    
    public void quit() {
        boolean isConfirmed = iostream.readBoolean("Are you sure do you really want to quit?");
        
        if(isConfirmed)
            System.exit(0);
    }
    
    /**
     * Returns the full subject of the given shortcut, if the shortcut does not exist it returns the
     * shortcut itself.
     * @param shortcut The shortcut of the subject to get
     * @return The full subject
     */
    private String getSubject(String shortcut) {
        if(! shortcuts.containsKey(shortcut))
            return shortcut;
        
        return shortcuts.get(shortcut);
    }
}
