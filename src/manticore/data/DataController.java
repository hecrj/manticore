package manticore.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * You can use this class to store and get objects in memory. Additionally, 
 * the class can load and save objects represented as XML files.
 * 
 * Example:
 * DataController data = new DataController();
 * 
 * Foo foo = new Foo();
 * // Do some fun stuff with foo
 * // [...]
 * 
 * // Save foo as XML into foobar.xml
 * data.save(foo, "foobar.xml");
 * 
 * // Load foobar.xml as Foo object
 * foo = data.load(Foo.class, "foobar.xml");
 * 
 * @author hector
 */
public class DataController implements JAXBDataController
{
    private Map<String, Object> data;
    
    /**
     * Creates a new data controller.
     */
    public DataController()
    {
        data = new HashMap();
    }
    
    /**
     * Saves the given object as XML in the given path.
     * @param o Object to save as XML
     * @param path Path where to save the file with the generated XML
     * @throws JAXBException 
     */
    @Override
    public void save(Object o, String path) throws JAXBException
    {
        Class[] boundClasses = { o.getClass() };
        
        save(o, path, boundClasses);
    }
    
    /**
     * Load the given object as XML in the given path using the boundClasses to translate all the data.
     * This method is useful to say explicitly which classes JAXB has to take into account to save the data.
     * @param o Object to save as XML
     * @param path Path where to save the file with the generated XML
     * @param boundClasses Classes that JAXB has to take into account
     * @throws JAXBException 
     */
    @Override
    public void save(Object o, String path, Class[] boundClasses) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(boundClasses);
        Marshaller m = jc.createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        
        m.marshal(o, new File(path));
    }
    
    /**
     * Load an instance of the given class from the XML found in path
     * @param c Class of the object to load
     * @param path Path to the XML file to load
     * @return The loaded object
     * @throws JAXBException 
     */
    @Override
    public Object load(Class c, String path) throws JAXBException
    {
        Class[] boundClasses = { c };
        
        return load(boundClasses, path);
    }
    
    /**
     * Load an instance from the XML found in path using the boundClasses to understand all the data.
     * This method is useful to say explicitly which classes JAXB has to take into account to load the data.
     * @param boundClasses Classes that JAXB has to look at
     * @param path Path to the XML file to load
     * @return The loaded object
     * @throws JAXBException 
     */
    @Override
    public Object load(Class[] boundClasses, String path) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(boundClasses);
        Unmarshaller u = jc.createUnmarshaller();
        
        return u.unmarshal(new File(path));
    }
    
    /**
     * Returns the object previosuly saved with the name given.
     * @param name Name of the object to get
     * @return Object previously saved as name
     */
    @Override
    public Object get(String name)
    {
        return data.get(name);
    }
    
    /**
     * Stores an object with the name given.
     * @param name Name of the object
     * @param o Object to store
     */
    @Override
    public void set(String name, Object o)
    {
        data.put(name, o);
    }
    
    @Override
    public boolean has(String name)
    {
        return data.containsKey(name);
    }
}
