package manticore.presentation.swing;

/**
 *
 * @author hector
 */
public class SwingException
    extends RuntimeException
{
    public SwingException(String msg) {
        super(msg);
    }
    
    public SwingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
