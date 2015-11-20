package it.playfellas.superapp.logic.db;

/**
 * Created by Stefano Cappa on 04/08/15.
 */

import lombok.Getter;

/**
 * Exception for Database.
 */
public class DbException extends Exception {

    public enum Reason {NOTADDED, NOTAVAILABLE}

    @Getter
    private Reason reason;

    public DbException() {
        super();
    }

    /**
     * Constructor
     * @param message String message
     * @param cause   The throwable object
     */
    public DbException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * @param message String message
     */
    public DbException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param cause String message
     */
    public DbException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * @param reason Enumeration that represents the exception's reason.
     */
    public DbException(Reason reason) {
        this.reason = reason;
    }
}