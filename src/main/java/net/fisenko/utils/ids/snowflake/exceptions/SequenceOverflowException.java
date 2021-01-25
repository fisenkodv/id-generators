package net.fisenko.utils.ids.snowflake.exceptions;

/**
 * The exception that is thrown when a sequence overflows (e.g. too many Id's generated within the same timespan (ms)).
 */
public class SequenceOverflowException extends Exception {

    /**
     * Initializes a new instance of the {@link SequenceOverflowException} class with a message that describes the error.
     *
     * @param message The message that describes the exception. The caller of this constructor is required to ensure that this string has been localized for the current system
     *                culture.
     */
    public SequenceOverflowException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@link SequenceOverflowException} class.
     */
    public SequenceOverflowException() {
        super("Sequence overflow");
    }

    /**
     * Initializes a new instance of the {@link SequenceOverflowException} class with a message that describes the error and underlying exception.
     *
     * @param message        The message that describes the exception. The caller of this constructor is required to ensure that this string has been localized for the current
     *                       system culture.
     * @param innerException The exception that is the cause of the current {@link SequenceOverflowException}. If the innerException parameter is not null, the current exception is
     *                       raised in a catch block that handles the inner exception.
     */
    public SequenceOverflowException(String message, Exception innerException) {
        super(message, innerException);
    }
}