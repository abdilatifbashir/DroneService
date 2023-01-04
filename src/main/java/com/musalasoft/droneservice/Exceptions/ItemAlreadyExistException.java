package com.musalasoft.droneservice.Exceptions;

/**
 * @author Abdilatif
 * @created 10/12/2022
 **/
public class ItemAlreadyExistException extends IllegalArgumentException{
    public ItemAlreadyExistException(String s) {
        super (s);
    }
}
