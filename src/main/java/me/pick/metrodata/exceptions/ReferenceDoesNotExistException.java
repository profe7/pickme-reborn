package me.pick.metrodata.exceptions;

public class ReferenceDoesNotExistException extends RuntimeException{
    public ReferenceDoesNotExistException(Long id) {
        super("Reference with id " + id + " does not exist");
    }
}
