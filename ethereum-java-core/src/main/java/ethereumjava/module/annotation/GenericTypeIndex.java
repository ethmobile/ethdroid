package ethereumjava.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define generic type declaration position in the signature.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenericTypeIndex {
    /**
     * The index in the signature. Must be in the range [0..n] (with n == number of parameters)
     *
     * @return the index in the signature.
     */
    int value();
}
