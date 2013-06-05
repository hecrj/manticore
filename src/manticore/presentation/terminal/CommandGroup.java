package manticore.presentation.terminal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import manticore.Options;
import manticore.Utils;
import manticore.business.BusinessException;
import manticore.presentation.terminal.annotation.Command;

/**
 * Represents a group of terminal commands under a common subject.
 * @author hector
 */
public class CommandGroup
{
    /**
     * Padding of the help command
     */
    private static final int HELP_PADDING = 20;
    
    /**
     * The input/output streams to read/print data from/to the user
     */
    protected IOStream iostream;
    
    /**
     * The name of the command subject
     */
    private String name = "unknown";
    
    /**
     * The description of the command group
     */
    private String description = "No description";
    
    /**
     * Map of available commands
     */
    private Map<String, CommandAction> commands;
    
    /**
     * Creates a new command group without name, description and iostream.
     */
    public CommandGroup()
    {
        scanCommands();
    }
    
    /**
     * Sets the name of the command group.
     * @param name Name of the command group
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets the name of the command group.
     * @return Name of the command group
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the description of the command group.
     * @param description The description of the command group
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * Gets the description of the command group.
     * @return The description of the command group
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets the input/output streams.
     * @param iostream The input/output stream to set
     */
    public void setIOStream(IOStream iostream)
    {
        this.iostream = iostream;
    }
    
    /**
     * Scans the command actions available in the command group.
     */
    private void scanCommands()
    {
        commands = new HashMap();
        
        for(Method method : getClass().getMethods())
        {
            if(! method.isAnnotationPresent(Command.class))
                continue;
            
            Command commandInfo = method.getAnnotation(Command.class);
            
            CommandAction command = new CommandAction(this, method, commandInfo.value());
            commands.put(command.getName(), command);
        }
    }
    
    /**
     * Executes a command action given its name.
     * @param commandName The command name
     * @throws Throwable 
     */
    public void exec(String commandName) throws Throwable
    {
        if(! commands.containsKey(commandName))
            throw new BusinessException(commandName + " command not found on subject " + name + ".");
        
        CommandAction command = commands.get(commandName);
         
        Options currentOptions = getOptions(command);
        commands.get(commandName).exec(currentOptions);
    }
    
    /**
     * Reads and returns the enabled options from the input stream for the given command action.
     * @param command The command action
     * @return The enabled options
     */
    private Options getOptions(CommandAction command)
    {
        Options options = new Options();
        
        for(String commandOption : command.getOptions())
            options.disable(commandOption);
        
        while(iostream.hasNext(command.getOptionsPattern()))
        {
            String option = command.getOptionName(iostream.readString());
            options.enable(option);
        }
        
        return options;
    }
    
    /**
     * Prints in the output stream information about the command group and its command actions.
     */
    @Command("Obtain detailed command information")
    public void help()
    {
        iostream.println("Available commands for " + name + ":");
        
        for(CommandAction command : commands.values())
        {
            iostream.println("    " + Utils.padRight(command.getName() + " " + name, HELP_PADDING)
                    + command.getDescription());
        }
    }
}
