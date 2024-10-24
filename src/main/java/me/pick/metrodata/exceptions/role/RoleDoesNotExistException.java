package me.pick.metrodata.exceptions.role;

public class RoleDoesNotExistException extends RuntimeException {
    public RoleDoesNotExistException(Long roleId) {
        super("Role with id " + roleId + " does not exist");
    }
}
