/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

/**
 *
 * @author ljubo
 */
public class User {
    private Integer Id;
    private String Username;
    private String Password;
    private String Adminsitrator;

    public User(Integer Id, String Username, String Password, String Adminsitrator) {
        this.Id = Id;        
        this.Username = Username;
        this.Password = Password;
        this.Adminsitrator = Adminsitrator;
    }

    public String getUsername() {
        return Username;
    }

    public Integer getId() {
        return Id;
    }

    public String getPassword() {
        return Password;
    }

    public String getAdminsitrator() {
        return Adminsitrator;
    }

    public User() {
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setAdminsitrator(String Adminsitrator) {
        this.Adminsitrator = Adminsitrator;
    }
    
}
