package manticore.presentation.swing.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import manticore.Debug;

/**
 * Input stream that can be related to a Swing JTextPane to be the input of a terminal.
 * @author hector
 */
public class ConsoleInputStream extends InputStream
{
    TerminalFilter terminalFilter;
    AbstractDocument document;
    Queue<Character> contents;
    boolean isConfirmed = true;
    
    /**
     * Creates a new ConsoleInputStream using textPane as input source.
     * @param textPane A JTextPane that should be used as input source
     */
    public ConsoleInputStream(final JTextPane textPane)
    {
        terminalFilter = new TerminalFilter(this);
        document = (AbstractDocument) textPane.getDocument();
        document.setDocumentFilter(terminalFilter);
        
        contents = new LinkedList();
    }
    
    /**
     * Overrides read operation from InputStream to support reading from a Swing JTextPane.
     * @return Next byte of data read
     * @throws IOException 
     */
    @Override
    synchronized public int read() throws IOException
    {
        while(contents.isEmpty()) {
            if(! isConfirmed) {
                isConfirmed = true;
                return -1; // End of current stream
            }
            
            try {
                terminalFilter.unlock();
                this.wait(); // Wait until input
                contents = terminalFilter.lock(document);
            }
            catch(InterruptedException e) {
                if(Debug.isEnabled())
                    e.printStackTrace();
            }
        }
        
        isConfirmed = false;
        
        return contents.poll();
    }
}
