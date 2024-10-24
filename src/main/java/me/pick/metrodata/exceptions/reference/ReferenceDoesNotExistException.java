package me.pick.metrodata.exceptions.reference;

public class ReferenceDoesNotExistException extends RuntimeException{
    public ReferenceDoesNotExistException(Long id) {
        super("Reference with id " + id + " does not exist");
    }
}
