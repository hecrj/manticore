package interiores.core.presentation;

import interiores.core.Event;
import interiores.core.business.BusinessController;
import interiores.core.presentation.swing.SwingException;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

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
    
    private Map<Class, BusinessController> businessControllers;
    
    /**
     * Constructs a SwingController given the package where the views are located
     * @param viewsPackage The package where the views of this controller are located 
     */
    public SwingController(Class<? extends Component> mainView)
    {
        super();
        
        this.mainView = mainView;
        vloader = new ViewLoader(this);
        businessControllers = new HashMap();
    }
    
    /**
     * Initializes and runs the SwingController
     * @throws Exception 
     */
    @Override
    public void init() throws Exception
    {
        show(mainView);
    }
    
    /**
     * Recieves an event and notifies it to the views that are listening to that event.
     * @param name Name of the event
     * @param data Data related with the event
     */
    @Override
    public void notify(Event event)
    {
        vloader.notify(event);
        
    }

    @Override
    public void addBusinessController(BusinessController controller) {
        businessControllers.put(controller.getClass(), controller);
        
        super.addBusinessController(controller);
    }
    
    public <T extends BusinessController> T getBusinessController(Class<T> controllerClass)
            throws SwingException
    {
        if(! businessControllers.containsKey(controllerClass))
            throw new SwingException("Business controller not found: " + controllerClass.getName());
        
        return (T) businessControllers.get(controllerClass);
    }
    
    /**
     * Loads a view on memory given its name.
     * @param viewClass The class of the view to load
     * @throws Exception 
     */
    public void load(Class<? extends Component> viewClass) throws SwingException
    {            
        vloader.load(viewClass);
    }
    
    /**
     * Shows a loaded view.
     * @param viewClass The class of the view to show
     */
    public void show(Class<? extends Component> viewClass) throws SwingException
    {
        get(viewClass).setVisible(true);
    }
    
    /**
     * Obtains a loaded view.
     * @param viewClass The class of the view to obtain
     * @return The loaded view
     */
    public <T extends Component> T get(Class<T> viewClass) throws SwingException
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
