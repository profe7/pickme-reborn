package me.pick.metrodata.exceptions.vacancy;

public class VacancyNotExistException extends RuntimeException{
    public VacancyNotExistException(Long vacancyId){
        super("Vacancy with id " + vacancyId + " does not exist");
    }
}
