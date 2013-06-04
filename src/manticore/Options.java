package manticore;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple class that can be used to map booleans to strings.
 * @author hector
 */
public class Options
{
    private Map<String, Boolean> options;
    
    /**
     * Creates an instance with no enabled options.
     */
    public Options()
    {
        options = new HashMap();
    }
    
    /**
     * Enables an option.
     * @param option Name of the option
     */
    public void enable(String option)
    {
        options.put(option, Boolean.TRUE);
    }
    
    /**
     * Disables an option.
     * @param option Name of the option
     */
    public void disable(String option)
    {
        options.put(option, Boolean.FALSE);
    }
    
    /**
     * Tells whether an option is enabled or not.
     * @param option Name of the option
     * @return True if option is enabled, false otherwise
     */
    public boolean isEnabled(String option) {
        if(options.containsKey(option))
            return options.get(option);
        
        return false;
    }
    
    /**
     * Tells whether the instance has at least one option defined.
     * @return True if some option has been defined, false otherwise.
     */
    public boolean hasOptions() {
        return !options.isEmpty();
    }
}
