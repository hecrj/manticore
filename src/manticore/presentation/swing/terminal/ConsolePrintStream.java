package manticore.presentation.swing.terminal;

import java.awt.Color;
import java.io.PrintStream;
import javax.swing.text.JTextComponent;

/**
 * Print stream that can be related to a Swing JTextComponent to be the output of a terminal.
 * @author hector0193
 */
public class ConsolePrintStream extends PrintStream
{
    /**
     * Creates a new ConsolePrintStream that uses textComponent as output destination with color textColor in
     * append mode.
     * @param textComponent The JTextComponent that is going to show the output
     * @param textColor Color of the output
     */
    public ConsolePrintStream(JTextComponent textComponent, Color color)
    {
        this(new ConsoleOutputStream(textComponent, color));
    }
    
    /**
     * Creats a ConsolePrintStream that uses the given ConsoleOutputStream as output destination.
     * @param consoleOutput The ConsoleOutputStream that should be used
     */
    public ConsolePrintStream(ConsoleOutputStream consoleOutput)
    {
        super(consoleOutput, true);
    }
}
