package net.fisenko.utils.ids.snowflake;

/**
 * The exception that is thrown when a clock going backwards is detected.
 */
public class InvalidSystemClockException extends Exception {
    /**
     * Initializes a new instance of the {@link InvalidSystemClockException} class with a message that describes the error.
     * @param message The message that describes the exception. The caller of this constructor is required to ensure that this
     *                string has been localized for the current system culture.
     */
    public InvalidSystemClockException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@link InvalidSystemClockException} class.
     */
    public InvalidSystemClockException() {
        super("Invalid system clock");
    }

    /**
     * Initializes a new instance of the {@link InvalidSystemClockException} class with a message that describes
     * the error and underlying exception.
     * @param message        The message that describes the exception. The caller of this constructor is required to ensure that this
     *                       string has been localized for the current system culture.
     * @param innerException The exception that is the cause of the current {@link InvalidSystemClockException}. If the
     *                       innerException parameter is not null, the current exception is raised in a catch block that handles
     *                       the inner exception.
     */
    public InvalidSystemClockException(String message, Exception innerException) {
        super(message, innerException);
    }
}