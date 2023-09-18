/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

/**
 *
 * @author ljubo
 */
public final class PeopleAndMovie {
    private Integer Id;
    private Integer MoviesID;    
    private Integer PeopleID;
    
    public PeopleAndMovie(Integer Id, Integer MoviesID, Integer PeopleID) {
        this.Id = Id;
        this.MoviesID = MoviesID;
        this.PeopleID = PeopleID;
    }

    public PeopleAndMovie() {
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public void setMoviesID(Integer MoviesID) {
        this.MoviesID = MoviesID;
    }

    public void setPeopleID(Integer PeopleID) {
        this.PeopleID = PeopleID;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getMoviesID() {
        return MoviesID;
    }

    public Integer getPeopleID() {
        return PeopleID;
    }

    
}
