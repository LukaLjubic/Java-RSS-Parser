/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import hr.algebra.dal.Repository;

/**
 *
 * @author ljubo
 */
public class SqlRepository implements Repository {

    private static final String IDUSER = "IDUser";
    private static final String IDMOVIE = "IDMovie";
    private static final String MOVIE_ID = "MovieID";
    private static final String USER_ID = "UserID";

    private static final String USERNAME = "username";
    private static final String PEOPLE_ID = "PeopleID";

    private static final String PASSWORDS = "passwords";
    private static final String ADMINISTRATOR = "administrator";

    private static final String PEOPLE_NAMES = "names";
    private static final String IDPEOPLE = "IDUserForMovies";

    private static final String MOVIE_NAME = "MovieName";

    private static final String MOVIE_DURATION = "MovieDuration";
    private static final String MOVIE_DESCRIPTION = "MovieDescription";
    private static final String MOVIE_PHOTO = "PhotoPath";

    private static final String ACTORS_AND_MOVIE_ID = "IDActorAndMovie";
    private static final String DIRECTORS_AND_MOVIE_ID = "IDDirectorAndMovie";
    private static final String ID_USER_FOR_MOVIES = "IDUserForMovies";    
    private static final String PHOTO_PATH = "PhotoPath";


    private static final String AUTH_USER = "{ CALL authUser (?,?) }";
    private static final String CREATE_USER = "{ CALL createUser (?,?,?) }";
    private static final String SELECT_USER = "{ CALL selectUser (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIE_ACTORS = "{ CALL selectMovieActors (?) }";
    private static final String SELECT_MOVIE_DIRECTORS = "{ CALL selectMovieDirectors (?) }";
    private static final String DELETE_ACTORS = "{ CALL deleteActors (?) }";
    private static final String DELETE_DIRECTORS = "{ CALL deleteDirectors (?) }";
    private static final String DELETE_ALL = "{ CALL deleteAll }";

    private static final String CREATE_PEOPLE = "{ CALL createPeople (?,?) }";
    private static final String SELECT_PEOPLE = "{ CALL selectPeople }";
    private static final String SAVE_MOVIE = "{ CALL createMovies (?,?,?,?,?)}";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?)}";
    private static final String GET_MOVIES = "{ CALL selectMovies }";
    private static final String SAVE_ACTOR_AND_MOVIE = "{ CALL createActorAndMovie (?,?,?)}";
    private static final String SAVE_DIRECTOR_AND_MOVIE = "{ CALL createDirectorAndMovie (?,?,?)}";

    @Override
    public Optional<User> authUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(AUTH_USER);) {
            stmt.setString(USERNAME, username);
            stmt.setString(PASSWORDS, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(IDUSER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORDS),
                            rs.getString(ADMINISTRATOR)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public int createUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_USER);) {
            stmt.setString(USERNAME, username);
            stmt.setString(PASSWORDS, password);
            stmt.registerOutParameter(IDUSER, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(IDUSER);
        }
    }

    @Override
    public Optional<User> getUser(int ID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USER);) {

            stmt.setInt(IDUSER, ID);

            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(IDUSER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORDS),
                            rs.getString(ADMINISTRATOR)
                    ));

                }
            }

        }
        return Optional.empty();
    }

    @Override
    public int createPeople(String nameAndlastname) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_PEOPLE);) {
            stmt.setString(PEOPLE_NAMES, nameAndlastname);
            stmt.registerOutParameter(IDPEOPLE, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(IDPEOPLE);
        }
    }

    @Override
    public List<Person> getPeople() throws Exception {
        List<Person> peoples = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_PEOPLE); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                peoples.add(new Person(
                        rs.getInt(IDPEOPLE),
                        rs.getString(PEOPLE_NAMES)));
            }
        }
        return peoples;
    }

    @Override
    public List<Movie> getMovies() throws Exception {
        List<Movie> movie = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(GET_MOVIES); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movie.add(new Movie(
                        rs.getInt(IDMOVIE),
                        rs.getString(MOVIE_NAME),
                        rs.getString(MOVIE_DESCRIPTION),
                        rs.getString("Duration"),
                        rs.getString(MOVIE_PHOTO),
                        getMovieActors(rs.getInt(IDMOVIE)),
                        getMovieDirectors(rs.getInt(IDMOVIE))
                ));
            }
        }
        return movie;
    }

    @Override
    public int saveMovie(Movie movie) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SAVE_MOVIE);) {
            stmt.setString(MOVIE_NAME, movie.getTitle());
            stmt.setString(MOVIE_DURATION, movie.getDuration());
            stmt.setString(MOVIE_DESCRIPTION, movie.getDescription());            
            stmt.setString(PHOTO_PATH, movie.getPhotoPath());
            stmt.registerOutParameter(IDMOVIE, Types.INTEGER);
            stmt.executeUpdate();
            int movieID = stmt.getInt(IDMOVIE);

            if (movie.getActors() != null) {
                for (Person actor : movie.getActors()) {
                    saveActors(movieID, createPeople(actor.getName()));
                }
            }
            if (movie.getDirectors() != null) {
                for (Person director : movie.getDirectors()) {
                    saveDirectors(movieID, createPeople(director.getName()));
                }
            }
            return stmt.getInt(IDMOVIE);
        }
    }

    @Override
    public int saveActors(int movieID, int userID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SAVE_ACTOR_AND_MOVIE);) {
            stmt.setInt(MOVIE_ID, movieID);
            stmt.setInt(PEOPLE_ID, userID);
            stmt.registerOutParameter(ACTORS_AND_MOVIE_ID, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(ACTORS_AND_MOVIE_ID);
        }
    }

    @Override
    public int saveDirectors(int movieID, int userID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SAVE_DIRECTOR_AND_MOVIE);) {
            stmt.setInt(MOVIE_ID, movieID);
            stmt.setInt(PEOPLE_ID, userID);
            stmt.registerOutParameter(DIRECTORS_AND_MOVIE_ID, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(DIRECTORS_AND_MOVIE_ID);
        }
    }

    @Override
    public Optional<Movie> getMovie(int ID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIE);) {

            stmt.setInt(IDMOVIE, ID);

            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return Optional.of(new Movie(
                            rs.getInt(IDMOVIE),
                            rs.getString(MOVIE_NAME),
                            rs.getString(MOVIE_DESCRIPTION),
                            rs.getString("duration"),
                            rs.getString(MOVIE_PHOTO)
                    ));
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public List<Person> getMovieActors(int ID) throws Exception {
        List<Person> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIE_ACTORS);) {
            stmt.setInt(IDMOVIE, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Person(
                            rs.getInt(ID_USER_FOR_MOVIES),
                            rs.getString(PEOPLE_NAMES)
                    ));
                }
            }
        }
        return actors;
    }

    @Override
    public List<Person> getMovieDirectors(int ID) throws Exception {
        List<Person> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIE_DIRECTORS);) {
            stmt.setInt(IDMOVIE, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    directors.add(new Person(
                            rs.getInt(ID_USER_FOR_MOVIES),
                            rs.getString(PEOPLE_NAMES)
                    ));
                }
            }
        }
        return directors;
    }

    @Override
    public void deleteActors(int ID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ACTORS);) {
            stmt.setInt(IDMOVIE, ID);
            stmt.execute();
        }
    }

    @Override
    public void deleteDirectors(int ID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_DIRECTORS);) {
            stmt.setInt(IDMOVIE, ID);
            stmt.execute();
        }
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_MOVIE);) {
            stmt.setInt(IDMOVIE, movie.getId());
            stmt.setString(MOVIE_NAME, movie.getTitle());
            stmt.setString(MOVIE_DURATION, movie.getDuration());
            stmt.setString(MOVIE_DESCRIPTION, movie.getDescription());
            stmt.setString(PHOTO_PATH, movie.getPhotoPath());
            //stmt.setString(MOVIE_PHOTO, movie.getPhotoPath());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAll() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ALL);) {
            stmt.execute();
        }
    }

    @Override
    public void loadMovies(List<Movie> movies) throws Exception {
        for (Movie movie : movies) {
            saveMovie(movie);
        }
    }
}
