package model.shared.exceptions;



import java.util.ArrayList;
import java.util.MissingResourceException;

import model.shared.exceptions.messages.BundleUtils;

/**
 * Recipe: Handling and customizing database-related errors.
 */
public class DatabaseException extends ExtJboException {
    // the error code for application-specific messages generated by the database
    // application-specific business code
    final private String APPLICATION_ERROR_CODE = "20200";
    // indicates using original raw message
    private boolean useRawMessage = false;
    // the error message parameters
    private ArrayList parameters = null;
    // the actual error message code
    private String errorMessageCode = "";
    // original exception
    private Exception exception;

    /**
     * Construct using exception.
     * @param exception
     */
    public DatabaseException(final Exception exception) {
        // save the original exception
        this.exception = exception;

        // get original message
        String errorMessageRaw = exception.getMessage();

        // check for null/empty error message, for database error message and for end of database error code indicator
        if (errorMessageRaw == null || "".equals(errorMessageRaw) || errorMessageRaw.indexOf("ORA-") == -1 ||
            errorMessageRaw.indexOf(":") == -1) {
            useRawMessage = true;
        }

        // handle database error
        if (!useRawMessage) {
            // check for end of database error code indicator
            int endIndex = errorMessageRaw.indexOf(":");
            // get the database error code
            errorMessageCode = errorMessageRaw.substring(4, endIndex);
            if (APPLICATION_ERROR_CODE.equals(errorMessageCode)) {
                int start = errorMessageRaw.indexOf("-", endIndex) + 1;
                int end = errorMessageRaw.indexOf(":", start);
                errorMessageCode = errorMessageRaw.substring(start, end);
            }
            // TODO: add additional error code handling here

            // get the error message parameters
            parameters = getErrorMessageParameters(errorMessageRaw);
        }
    }

    /**
     * Returns the exception message.
     * @return, the exception message
     */
    public String getMessage() {
        return useRawMessage ? exception.getMessage() : getMessageFromBundle();
    }

    /**
     * Helper, retrieves the actual error message from the application message bundle.
     *
     * @return the error message from the message bundle.
     */
    private String getMessageFromBundle() {
        String errorMessage = null;
        try {
            errorMessage = BundleUtils.loadMessage(errorMessageCode);
        } catch (MissingResourceException mre) {
            // return original message
            return exception.getMessage();
        }

        if (parameters != null && parameters.size() > 0) {
            // replace the message parameter placeholders with the actual parameter values
            int counter = 1;
            for (Object parameter : parameters) {
                // parameter placeholders appear in the message as {1}, {2}, and so on
                errorMessage = errorMessage.replace("{" + counter + "}", parameter.toString());
                counter++;
            }
        }
        return errorMessage;
    }

    /**
     * Helper to retrieve the parameters from the application-specific error message
     * generated by the database business code.
     *
     * @param errorMessageRaw
     * @return
     */
    private ArrayList getErrorMessageParameters(String errorMessageRaw) {
        // the parameter indicator in the database application-specific error
        final String PARAMETER_INDICATOR = "$";
        ArrayList parameters = new ArrayList();
        // get parameters from the error message
        for (int i = 1; i <= 10; i++) {
            int start = errorMessageRaw.indexOf(PARAMETER_INDICATOR + i) + 2;
            int end = errorMessageRaw.indexOf(PARAMETER_INDICATOR + i, start);
            if (end == -1) {
                parameters.add(i - 1, "");
            } else {
                parameters.add(i - 1, errorMessageRaw.substring(start, end));
            }
        }
        // return the parameters
        return parameters;
    }

}