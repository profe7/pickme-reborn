package me.pick.metrodata.exceptions.client;

public class ClientDoesNotExistException extends RuntimeException{
    public ClientDoesNotExistException(Long id) {
        super("Client with id " + id + " does not exist");
    }
}
