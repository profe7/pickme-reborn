package me.pick.metrodata.exceptions.recommendation;

public class RecommendationDoesNotExistException extends RuntimeException{
    public RecommendationDoesNotExistException(Long id) {
        super("Recommendation with id " + id + " does not exist");
    }
}
