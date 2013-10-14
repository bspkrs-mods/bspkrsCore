package bspkrs.util;

/**
 * @author Risugami, bspkrs
 */

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Deprecated
@Retention(value = RUNTIME)
@Target(value = FIELD)
public @interface BSProp
{
    String info() default "";
    
    double max() default Double.POSITIVE_INFINITY;
    
    double min() default Double.NEGATIVE_INFINITY;
    
    String name() default "";
    
}