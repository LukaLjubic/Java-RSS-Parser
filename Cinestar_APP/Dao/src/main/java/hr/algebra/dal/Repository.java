/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ljubo
 */
public interface Repository {
    Optional<User> authUser(String username, String password) throws Exception;    
    int createUser(String username, String password) throws Exception;   
    
    int createPeople(String nameAndlastname) throws Exception;    
    Optional<User> getUser(int ID) throws Exception;    
    Optional<Movie> getMovie(int ID) throws Exception;    
    void deleteActors(int ID) throws Exception;       
    void deleteDirectors(int ID) throws Exception;       
    List<Person> getMovieActors(int ID) throws Exception;    
    List<Person> getMovieDirectors(int ID) throws Exception;    
    List<Person> getPeople() throws Exception;    
    List<Movie> getMovies() throws Exception;
    int saveMovie(Movie movie) throws Exception;    
    void updateMovie(Movie movie) throws Exception;
    int saveActors(int movieID, int userID)throws Exception;
    int saveDirectors(int movieID, int userID)throws Exception;    
    void deleteAll()throws Exception;
    void loadMovies(List<Movie> movies) throws Exception;
    
    //Ovo je sranjeee! Trebalo bi napravit Repository<T> da je scalable! i onda recimo umjesto List<People> getMovieActors(int ID) throws Exception,
    //bi imali List<T> get(int ID); I sad i burek moze unutra a ne samo People...

}

