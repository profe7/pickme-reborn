package me.pick.metrodata.exceptions.institute;

public class InstituteDoesNotExistException extends RuntimeException{
    public InstituteDoesNotExistException(Long id) {
        super("Institute with id " + id + " does not exist");
    }
}
