package manticore.presentation.terminal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a command action.
 * @author hector0193
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command
{
    /**
     * Gets the description of the command action.
     * @return The description of the command action
     */
    String value() default "No description available";
}
