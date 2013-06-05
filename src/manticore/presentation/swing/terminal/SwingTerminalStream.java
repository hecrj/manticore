package manticore.presentation.swing.terminal;

import java.awt.Color;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JTextPane;

/**
 * Creates a two-way communication with a terminal using a Swing JTextPane for user input and output.
 * @author hector
 */
public class SwingTerminalStream
{
    private ConsoleInputStream istream;
    private ConsolePrintStream ostream;
    
    /**
     * Creates a new SwingTerminalStream using textPane as component for user input and output with the given
     * color for the output.
     * @param textPane The JTextPane that is going to be used in the communication
     * @param color Color of the output
     */
    public SwingTerminalStream(JTextPane textPane, Color color)
    {
        istream = new ConsoleInputStream(textPane);
        ostream = new ConsolePrintStream(textPane, Color.white);
    }
    
    /**
     * Gets the input stream that is used in this SwingTerminalStream.
     * @return The input stream
     */
    public InputStream getInputStream()
    {
        return istream;
    }
    
    /**
     * Gets the print stream that is used in this SwingTerminalStream.
     * @return The print stream
     */
    public PrintStream getPrintStream()
    {
        return ostream;
    }
}
