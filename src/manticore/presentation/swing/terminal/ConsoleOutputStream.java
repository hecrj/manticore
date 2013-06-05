package manticore.presentation.swing.terminal;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Byte array output stream that can be related to a Swing JTextComponent to be the output of a terminal.
 * @author hector0193
 */
public class ConsoleOutputStream extends ByteArrayOutputStream
{
    private JTextComponent textComponent;
    private Document document;
    private SimpleAttributeSet attributes;
    private StringBuffer buffer = new StringBuffer(80);
    private boolean isFirstLine;
    private boolean isAppend;
    
    /**
     * Creates a new ConsoleOutputStream that uses textComponent as output destination with color textColor in
     * append mode.
     * @param textComponent The JTextComponent that is going to show the output
     * @param textColor Color of the output
     */
    public ConsoleOutputStream(JTextComponent textComponent, Color textColor)
    {
        this(textComponent, textColor, true);
    }
    
    /**
     * Creates a new ConsoleOutputStream that uses textComponent as output destination with color textColor.
     * @param textComponent The JTextComponent that is going to show the output
     * @param textColor Color of the output
     * @param isAppend True if append mode should be enabled, false otherwise
     */
    public ConsoleOutputStream(JTextComponent textComponent, Color textColor,  boolean isAppend)
    {
        this.textComponent = textComponent;
        document = textComponent.getDocument();

        if (textColor != null)
        {
            attributes = new SimpleAttributeSet();
            StyleConstants.setForeground(attributes, textColor);
        }

        this.isAppend = isAppend;

        if (isAppend)
            isFirstLine = true;
    }
    
    /**
     * Sets the JTextComponent that is going to show the output
     * @param textComponent The JTextComponent that is going to show the output
     */
    public void setTextComponent(JTextComponent textComponent)
    {
        this.textComponent = textComponent;
        document = textComponent.getDocument();
    }
    
    /**
     * Flushes the output stream.
     */
    @Override
    public void flush()
    {
        String message = toString();

        if (message.length() == 0)
            return;

        if (isAppend)
            handleAppend(message);
        else
            handleInsert(message);
        
        reset();
    }
    
    /**
     * Handles a flush using append mode.
     * @param message Message to print
     */
    private void handleAppend(String message)
    {
        if (message.endsWith("\r") || message.endsWith("\n"))
            buffer.append(message);
        
        else
        {
            buffer.append(message);
            clearBuffer();
        }
    }
    
    /**
     * Handles a flush using insert mode.
     * @param message Message to print
     */
    private void handleInsert(String message)
    {
        buffer.append(message);

        if (message.endsWith("\r") || message.endsWith("\n"))
            clearBuffer();
    }
    
    /**
     * Clears the output buffer.
     */
    private void clearBuffer()
    {
        String line = buffer.toString();
        
        if (textComponent == null)
            return;
        
        if (isFirstLine && document.getLength() != 0)
            buffer.insert(0, "\n");

        isFirstLine = false;
        
        try
        {
            if(isAppend)
            {
                int offset = document.getLength();
                document.insertString(offset, line, attributes);
                textComponent.setCaretPosition(document.getLength());
            }
            else
            {
                document.insertString(0, line, attributes);
                textComponent.setCaretPosition(0);
            }
        }
        catch (BadLocationException ble)
        {
            // ?
        }
        
        buffer.setLength(0);
    }
}
