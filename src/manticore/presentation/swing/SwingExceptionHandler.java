package manticore.presentation.swing;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import manticore.Debug;

/**
 * Handles uncaught exceptions that are thrown in gui threads and are not caught.
 * @author hector
 */
public class SwingExceptionHandler
{
    /**
     * Enables the SwingExceptionHandler.
     */
    public static void enable()
    {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                handleException(e);
            }
        });
    }
    
    /**
     * Handles a given throwable showing an OptionPane with info about the exception occurred.
     * @param e The throwable exception that needs to be handled
     */
    public static void handleException(Throwable e)
    {
        Debug.println("Handling uncaught exception with SwingExceptionHandler");
        JOptionPane exceptionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
        JDialog exceptionDialog = exceptionPane.createDialog("Something went wrong...");
        
        exceptionDialog.setAlwaysOnTop(true);
        exceptionDialog.setVisible(true);
        
        if(Debug.isEnabled())
            e.printStackTrace();
    }
}
