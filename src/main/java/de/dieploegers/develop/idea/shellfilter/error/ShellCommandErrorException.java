package de.dieploegers.develop.idea.shellfilter.error;

import java.io.Serial;

/**
 * The shell command exited with an error
 */
public class ShellCommandErrorException extends Exception {
    @Serial
    private static final long serialVersionUID = 7033532565240424095L;

    public ShellCommandErrorException(final String message) {
        super(message);
    }
}
