/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author ljubo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class Person {
    @XmlElement
    private Integer Id;
    @XmlElement
    private String Name;

    @Override
    public String toString() {
        return Name;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Person(String Name) {
        this.Name = Name;
    }

    public Person() {
    }

    public Person(Integer Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }
}
