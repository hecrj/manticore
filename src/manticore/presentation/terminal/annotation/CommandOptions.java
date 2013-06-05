package manticore.presentation.terminal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the options that command action can recieve.
 * @author hector
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandOptions
{
    /**
     * Gets the options that a command action can recieve.
     * @return The options that a command action can recieve
     */
    String[] value() default {};
}
