package com.levelmc.core.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Flags {
    /**
     * @return description of the flags (in the same order as the identifiers)
     */
    String[] description() default {};

    String[] identifier();
}
