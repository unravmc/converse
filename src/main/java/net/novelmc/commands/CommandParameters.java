package net.novelmc.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    String description();

    String usage();

    String aliases() default "";
}
