package de.dieploegers.develop.idea.shellfilter.error;

import java.io.Serial;

/**
 * Shell command produced no output
 */
public class ShellCommandNoOutputException extends Exception {
    @Serial
    private static final long serialVersionUID = -6645513124637254208L;

    public ShellCommandNoOutputException(final String message) {
        super(message);
    }
}
