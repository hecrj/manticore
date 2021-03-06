package manticore.presentation;

import manticore.Observer;
import manticore.business.BusinessController;

/**
 * Represents a controller of the application presentation layer.
 * @author hector
 */
abstract public class PresentationController implements Observer
{
    /**
     * Initializes and runs the presentation controller.
     * @throws Exception 
     */
    abstract public void init();
    
    /**
     * Adds a business controller on top of this presentation controller.
     * @param name Name to identify the business controller
     * @param controller The business controller to add
     */
    public void addBusinessController(BusinessController controller)
    {
        controller.addListener(this);
    }
}
