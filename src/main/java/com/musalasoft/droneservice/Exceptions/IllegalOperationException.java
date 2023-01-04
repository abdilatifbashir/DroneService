package com.musalasoft.droneservice.Exceptions;

/**
 * @author Abdilatif
 * @created 10/12/2022
 **/
public class IllegalOperationException extends IllegalArgumentException{
    public IllegalOperationException(String s) {
        super (s);
    }
}
