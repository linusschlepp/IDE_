package app.exceptions;

import org.slf4j.Logger;

/**
 * Standard-Exception, used for this project
 */
public class IDEException extends Exception{

    public IDEException(final String errorMessage, final Object... formatFiller) {

        super(String.format(errorMessage, formatFiller));
    }

    public void throwWithLogging(final Logger log) throws IDEException {
        log.error(this.getMessage());
        throw this;
    }


}
