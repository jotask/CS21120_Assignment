package com.github.jotask.cs21120_Assignment;


/**
 * Exception thrown if trying to get a Match when the competition is over
 * @author ncm
 */
public class NoNextMatchException extends RuntimeException {
    /** Constructor for the exception
     * 
     * @param str An exception message of your choice
     */
    public NoNextMatchException(String str) {
        super(str);
    }
}
