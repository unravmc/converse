package net.novelmc.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * This interface is to be used to annotate commands listed in net.novelmc.commands package.
 * The annotation can be used as follows:
 * @CommandParameters(description = "decsription", usage = "/<command> ", aliases = "alias1, alias2")
 */
public @interface CommandParameters {
    /**
     * Gets the command description.
     * @return a description of the command.
     */
    String description();

    /**
     * Gets the command usage. Typically, the usage will first be represented by the /<command> modifier
     * which is translated in the command handler to reflect the actual name of the command,
     * followed by any relevant arguments. Mandatory arguments should be placed within <> whereas optional
     * arguments should be placed in [].
     * @return the usage of the command.
     */
    String usage();

    /**
     * Gets the aliases that can also be used to trigger the commands action. These are represented
     * in a single string separated by ','. It is okay to omit this tag from the annotation.
     * @return a string which can be split into an array using StringUtils.
     */
    String aliases() default "";
}
