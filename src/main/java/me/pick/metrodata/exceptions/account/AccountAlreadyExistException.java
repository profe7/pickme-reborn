package me.pick.metrodata.exceptions.account;

public class AccountAlreadyExistException extends RuntimeException{
    public AccountAlreadyExistException(String username) {
        super("Account with username " + username + " already exist");
    }
}
