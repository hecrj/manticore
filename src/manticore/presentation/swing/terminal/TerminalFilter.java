package manticore.presentation.swing.terminal;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import manticore.Debug;

/**
 * Filters content of an input/output component to be used for terminal communication.
 * @author hector
 */
public class TerminalFilter extends DocumentFilter
{
    private final InputStream istream;
    private boolean isLocked;
    private int lockOffset;
    
    /**
     * Creates a new terminal filter for the given input stream.
     * @param istream The input stream
     */
    public TerminalFilter(InputStream istream)
    {
        this.istream = istream;
        lockOffset = 0;
        
        isLocked = true;
    }
    
    /**
     * Locks the input and returns the data that has been added to the given document since the last lock.
     * @param document The document from which retrieve data
     * @return The data added to the document since the last time being locked
     */
    public Queue<Character> lock(AbstractDocument document)
    {
        isLocked = true;

        String readString = "";
        
        try
        {
            int docLength = document.getLength();
            int lineLength = docLength - lockOffset;
            
            readString = document.getText(lockOffset, lineLength);
            
            Debug.println(readString);
            
            lockOffset = docLength;
        }
        catch(BadLocationException e)
        {
            if(Debug.isEnabled())
                e.printStackTrace();
        }
        
        Queue<Character> read = new LinkedList();
        
        for(char c : readString.toCharArray())
            read.add(c);
        
        return read;
    }
    
    /**
     * Unlocks the input stream.
     */
    public void unlock()
    {
        isLocked = false;
    }
    
    /**
     * Handles a removal from the document.
     * This is only allowed when the removal offset is bigger than the lock offset.
     * @param fb The document FilterByPass
     * @param offset Offset of the removal
     * @param length Length of the removal
     * @throws BadLocationException 
     */
    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException
    {
        if(lockOffset > offset)
            return;
        
        super.remove(fb, offset, length);
    }
    
    /**
     * Handles an insertion to the document.
     * This is only allowed when the input is locked, or when it is not locked and the insertion offset is
     * bigger than the last locked offset.
     * @param fb The document FilterByPass
     * @param offset Offset of the insertion
     * @param string String to insert
     * @param attr Attributes of the insertion
     * @throws BadLocationException 
     */
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException
    {
        int totalOffset = offset + string.length();
        
        if(isLocked) {
            if(totalOffset > lockOffset)
                lockOffset = totalOffset;
        }
        else if(lockOffset > offset)
            return;
        
        if(! string.endsWith("\n"))
            super.insertString(fb, offset, string, attr);
        
        else if(! isLocked) {
            super.insertString(fb, fb.getDocument().getLength(), string, attr);
            
            synchronized (istream) {
                istream.notify();
            }
        }
    }
    
    /**
     * Handles a replacement in the document.
     * It is handled in the same way as an insertion.
     * @param fb The document FilterByPass
     * @param offset Offset of the insertion
     * @param length Length of the replacement.
     * @param text Text of the replacement
     * @param attrs Attributes of the replacement
     * @throws BadLocationException 
     */
    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException
    {
        insertString(fb, offset, text, attrs);
    }
}
