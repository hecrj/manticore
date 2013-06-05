package manticore.presentation.swing;

/**
 * SwingException is an exception thrown by a SwingController.
 * @author hector
 */
public class SwingException extends RuntimeException
{
    /**
     * Creates a new SwingException with the given message.
     * @param msg Message of the exception
     */
    public SwingException(String msg) {
        super(msg);
    }
    
    /**
     * Creates a new SwingException with the given message and cause.
     * @param msg Message of the exception
     * @param cause Cause of the exception
     */
    public SwingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
