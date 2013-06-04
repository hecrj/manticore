package manticore;

import java.awt.Rectangle;

/**
 * Simple class for enable/disable debugging mesages.
 * @author hector
 */
public class Debug
{
    /**
     * Tells whether the debug mode is enabled or not.
     */
    private static boolean enabled = false;
    
    /**
     * Enables the debug mode.
     */
    public static void enable()
    {
        enabled = true;
    }
    
    /**
     * Disables the debug mode.
     */
    public static void disable()
    {
        enabled = false;
    }
    
    /**
     * If the debug mode is enabled, prints as a line the given string.
     * @param s The string to print
     */
    public static void println(String s)
    {
        if(enabled)
            System.err.println("[DEBUG] " + s);
    }
    
    /**
     * Preints information about the given rectangle, if debug mode is enabled.
     * @param r Rectangle to print
     */
    public static void print(Rectangle r)
    {
        println("Location: (" + r.x + ", " + r.y + ")");
        println("Size: (" + r.width + ", " + r.height + ")");
    }
    
    /**
     * Tells whether the debug mode is enabled or not
     * @return True if the debug mode is enabled, false otherwise
     */
    public static boolean isEnabled()
    {
        return enabled;
    }
}
