package manticore;

import java.util.ArrayList;
import java.util.List;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;
import manticore.presentation.PresentationController;

/**
 * Application represents a Java application using the 3-tier design pattern.
 * @author hector
 */
public class Application
{
    /**
     * The controller of the application data layer
     */
    private JAXBDataController data;
    
    /**
     * The controllers of the application business layer
     */
    private List<BusinessController> business;
    
    /**
     * The controllers of the application presentation layer
     */
    private List<PresentationController> presentations;
    
    /**
     * Application constructor.
     * @param appPkg Application package used to find the business controllers
     */
    public Application()
    {
        business = new ArrayList();
        presentations = new ArrayList();
    }
    
    /**
     * Initializes the application.
     * This method basically initializes the presentation layer which can start making requests to the
     * business.
     */
    public void init()
    {
        try {
            for(PresentationController presentation : presentations)
                presentation.init();
        }
        catch(Exception e) {
            if(Debug.isEnabled())
                e.printStackTrace();
        }
    }
    
    /**
     * Sets the controller of the data layer.
     * This data controller is injected to the business controllers on creation.
     * @param data The controller of the data layer
     */
    public void setDataController(JAXBDataController data)
    {
        this.data = data;
    }
    
    /**
     * Adds a presentation controller to the presentation layer.
     * This operation injects the business controllers of the application to the presentation controller.
     * @param presentation The presentation controller
     */
    public void addPresentation(PresentationController presentation)
    {
        presentations.add(presentation);
        
        for(BusinessController controller : business)
            presentation.addBusinessController(controller);
    }
    
    /**
     * Uses reflection to add business controllers to the business layer dinamically!
     * Every controller is injected with the data controller when constructed.
     * @param controllerClass Class of the business controller
     */
    public void addBusiness(Class<? extends BusinessController> controllerClass)
    {
        try
        {
            BusinessController controller = (BusinessController) controllerClass.getConstructor(
                    JAXBDataController.class).newInstance(data);
            
            addBusiness(controller);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Add a controller in the business layer without using reflection.
     * @param controller The business controller
     */
    public void addBusiness(BusinessController controller)
    {
        business.add(controller);
    }
}
