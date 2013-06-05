package manticore.presentation.terminal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import manticore.Debug;
import manticore.Options;
import manticore.presentation.terminal.annotation.CommandOptions;

/**
 * Represents a terminal command.
 * @author hector
 */
public class CommandAction
{
    /**
     * The options pattern of this command
     */
    private static final String OPTIONS_PATTERN = "--([a-z]+)";
    
    /**
     * The name of this command
     */
    private String name;
      
    /**
     * The description of this command
     */
    private String description;
    
    /**
     * The command group of this command
     */
    private CommandGroup commandGroup;
    
    /**
     * The options that this command accepts
     */
    private String[] options;
    
    /**
     * The action that this command represents
     */
    private Method action;
    
    /**
     * Creates a new command with the given command group, action and descripcion.
     * @param commandGroup The command group of this command
     * @param action Method of the command group that this command represents
     * @param description Description of the command
     */
    public CommandAction(CommandGroup commandGroup, Method action, String description)
    {
        name = action.getName();
        
        if(name.startsWith("_"))
            name = name.substring(1);
        
        this.action = action;
        this.description = description;
        this.commandGroup = commandGroup;
        
        if(action.isAnnotationPresent(CommandOptions.class))
            options = action.getAnnotation(CommandOptions.class).value();
        else
            options = new String[]{};
    }
    
    /**
     * Gets the name of the command.
     * @return The name of the command
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Gets the description of the command.
     * @return The description of the command
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Gets the options pattern of the command.
     * @return The options pattern of the command
     */
    public String getOptionsPattern()
    {
        return OPTIONS_PATTERN;
    }
    
    /**
     * Given an option match, returns the name of the option.
     * @param match An option match
     * @return The name of the option matched
     */
    public String getOptionName(String match)
    {
        return match.substring(2);
    }
    
    /**
     * Returns the options that this command accepts.
     * @return The options that this command accepts
     */
    public String[] getOptions()
    {
        return options;
    }
    
    /**
     * Executes the command with the given options,
     * @param currentOptions Options that should be used in the execution of the command
     * @throws Throwable 
     */
    public void exec(Options currentOptions) throws Throwable
    {
        try {
            if(options.length == 0) {
                if(currentOptions.hasOptions())
                    Debug.println("This command does not accept options.");
                
                action.invoke(commandGroup);
            }
            else
                action.invoke(commandGroup, currentOptions);
        }
        catch(InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
