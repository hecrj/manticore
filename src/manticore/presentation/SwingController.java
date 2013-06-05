package manticore.presentation;

import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import manticore.Event;
import manticore.business.BusinessController;
import manticore.presentation.swing.SwingException;
import manticore.presentation.swing.SwingExceptionHandler;

/**
 * Represents a Swing graphical user interface presentation controller.
 * @author hector
 */
public class SwingController extends PresentationController
{
    /**
     * The class of the main view to load on initialization
     */
    private Class mainView;
    
    /**
     * The view loader used to load and manage views
     */
    private ViewLoader vloader;
    
    /**
     * Business controller that this presentation controller can access
     */
    private Map<Class, BusinessController> businessControllers;
    
    /**
     * Constructs a SwingController given the main view that should be loaded on initialization.
     * @param mainView Class of the main view that should be loaded on initialization
     */
    public SwingController(Class<? extends JFrame> mainView)
    {
        super();
        
        this.mainView = mainView;
        vloader = new ViewLoader(this);
        businessControllers = new HashMap();
        
        SwingExceptionHandler.enable();
    }
    
    /**
     * Initializes and runs the SwingController.
     */
    @Override
    public void init()
    {
        show(mainView);
    }
    
    /**
     * Receives an event and notifies it to the views that are listening to that event.
     * @param event The event occurred
     */
    @Override
    public void notify(Event event)
    {
        vloader.notify(event);
        
    }
    
    /**
     * Adds a business controller on top of the SwingController.
     * @param controller The business controller
     */
    @Override
    public void addBusinessController(BusinessController controller)
    {
        businessControllers.put(controller.getClass(), controller);
        
        super.addBusinessController(controller);
    }
    
    /**
     * Gets a business controller that has been added previously to this SwingController.
     * @param <T> Controller class
     * @param controllerClass The business controller class
     * @return Instance of the controllerClass business controller
     */
    public <T extends BusinessController> T getBusinessController(Class<T> controllerClass)
    {
        if(! businessControllers.containsKey(controllerClass))
            throw new SwingException("Business controller not found: " + controllerClass.getName());
        
        return (T) businessControllers.get(controllerClass);
    }
    
    /**
     * Returns a collection of all the business controllers that this presentation controller can access.
     * @return Collection of all the business controllers that this presentation controller can access
     */
    public Collection<BusinessController> getBusinessControllers()
    {
        return businessControllers.values();
    }
    
    /**
     * Loads a view on memory given its name.
     * @param viewClass The class of the view to load
     */
    public void load(Class<? extends Component> viewClass)
    {
        if(vloader.isLoaded(viewClass))
            vloader.get(viewClass).setVisible(false);
        
        vloader.load(viewClass);
    }
    
    /**
     * Shows a loaded view.
     * @param viewClass The class of the view to show
     */
    public void show(Class<? extends Component> viewClass)
    {
        get(viewClass).setVisible(true);
    }
    
    /**
     * Creates a new instance of the view and shows it.
     * @param viewClass Class of the view to create and show
     */
    public void showNew(Class<? extends Component> viewClass)
    {
        getNew(viewClass).setVisible(true);
    }
    
    /**
     * Creates, stores and returns a new instance of the view.
     * @param viewClass Class of the view to create
     * @return The created view
     */
    public <T extends Component> T getNew(Class<T> viewClass)
    {
        load(viewClass);
        
        return get(viewClass);
    }
    
    /**
     * Obtains a loaded view, or creates a new one if not loaded.
     * @param viewClass The class of the view to obtain
     * @return The loaded view
     */
    public <T extends Component> T get(Class<T> viewClass)
    {
        return (T) vloader.get(viewClass);
    }
    
    /**
     * Unloads the view with the given name.
     * @param viewClass The name of the view to unload
     */
    public void close(Class<? extends Component> viewClass)
    {
        vloader.unload(viewClass);
    }
}
