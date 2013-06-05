package manticore.presentation.terminal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a command group.
 * @author hector0193
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandSubject
{
    /**
     * Gets the name of the command group.
     * @return The name of the command group
     */
    String name();
    
    /**
     * Gets the description of the command group.
     * @return The description of the command group
     */
    String description() default "No description available";
}
