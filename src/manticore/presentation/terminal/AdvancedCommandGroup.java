package manticore.presentation.terminal;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a group of terminal commands under a common subject.
 * Provides some handy methods to read/print data from/to the user.
 * @author hector
 */
abstract public class AdvancedCommandGroup extends CommandGroup
{   
    /**
     * Reads a string from the user asking a question.
     * @param question The question to ask
     * @return The read string
     */
    public String readString(String question)
    {
        return iostream.readString(question);
    }
    
    /**
     * Reads all the strings left in the input buffer asking a question.
     * @param question The question to ask
     * @return The read strings
     */
    public Collection<String> readStrings(String question)
    {
        return iostream.readStrings(question);
    }
    
    /**
     * Reads a choice from the input asking a question.
     * If the answer of the user it's not a valid choice, the first choice parameter is taken as default.
     * @param question The question to ask
     * @param choices The valid choices
     * @return The choice that the user has selected
     */
    public String readChoice(String question, String ... choices)
    {
        List<String> list = Arrays.asList(choices);
        
        String available = "Available choices:";
        
        for(int i = 0; i < list.size(); ++i) {
            available += "\n  ";
            
            if(i == 0) available += "* ";
            else available += "  ";
            
            available += list.get(i);
        }
        
        String choice = readString(question + "\n" + available);
        
        if(! list.contains(choice))
            choice = list.get(0);
        
        return choice;
    }
    
    /**
     * Reads a choice from the input asking a question.
     * If the answer of the user it's not a valid choice, the first choice parameter is taken as default.
     * @param question The question to ask
     * @param choices The valid choices
     * @return The choice that the user has selected
     */
    public String readChoice(String question, Collection<String> choices)
    {
        String[] choicesArray = new String[choices.size()];
        choices.toArray(choicesArray);
        
        return readChoice(question, choicesArray);
    }
    
    /**
     * Reads an integer from the input asking a question.
     * @param question The question to ask
     * @return The read integer
     */
    public int readInt(String question)
    {       
        return iostream.readInt(question);
    }
    
    /**
     * Reads a float from the input asking a question.
     * @param question The question to ask
     * @return The read float
     */
    public float readFloat(String question)
    {
        return iostream.readFloat(question);
    }
    
    /**
     * Prints a line to the output stream.
     * @param line Line to print
     */
    public void println(String line)
    {
        iostream.println(line);
    }
    
    /**
     * Prints a collection of objects.
     * @param collection The collection of objects to print
     */
    public void print(Collection<?> collection)
    {
        for(Object o : collection)
            println(o.toString());
    }
}
