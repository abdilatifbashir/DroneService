package com.musalasoft.droneservice.Exceptions;

/**
 * @author Abdilatif Bashir
 * @created 10/12/2022
 **/
public class ItemNotFoundException extends IllegalArgumentException{
    public ItemNotFoundException(String message) {
        super (message);
    }
}
