package me.pick.metrodata.exceptions.privilege;

public class PrivilegeDoesNotExistException extends RuntimeException{
    public PrivilegeDoesNotExistException(Long id) {
        super("Privilege with id " + id + " does not exist");
    }
}
