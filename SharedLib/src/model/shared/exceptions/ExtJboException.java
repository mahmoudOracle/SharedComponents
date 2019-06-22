package model.shared.exceptions;
import java.util.Locale;
import java.util.ResourceBundle;

import model.shared.exceptions.messages.BundleUtils;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.JboException;

public class ExtJboException extends JboException {

    /**
     * For testing purposes; remove or comment if not needed
     * @param args
     */
    public static void main(final String[] args) {
        // throw a custom exception with error code "00001" and two parameters
        throw new ExtJboException("00001", new String[] { "FirstParameter", "SecondParameter" });
    }

    /**
     * Default constructor.
     */
    public ExtJboException() {
        super("");
    }

    /**
     * Constructor to create an exception using a standard error code and
     * error message parameters
     * @param errorCode, the error message code
     * @param errorParameters, the error message parameters
     */
    public ExtJboException(final String errorCode, final Object[] errorParameters) {
        super(ResourceBundle.class, errorCode, errorParameters);
    }

    /**
     * Constructor to create an exception using a standard error code.
     * @param errorCode, the error message code
     */
    public ExtJboException(final String errorCode) {
        super(ResourceBundle.class, errorCode, null);
    }

    /**
     * Construct using exception.
     * @param exception
     */
    public ExtJboException(final Exception exception) {
        super(exception);
    }

    /**
     * Constructs the exception message.
     * @return, the exception message
     */
    @Override
    public String getMessage() {
        return BundleUtils.loadMessage(this.getErrorCode(), this.getErrorParameters());
    }
}
