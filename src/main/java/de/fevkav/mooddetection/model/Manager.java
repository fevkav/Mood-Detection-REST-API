package de.fevkav.mooddetection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Manager {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String vorname;

    private String fachbereich;

    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private List<Employee> employees;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Manager() {}

    public Manager(String name, String vorname, String fachbereich) {
        this.name = name;
        this.vorname = vorname;
        this.fachbereich = fachbereich;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getFachbereich() {
        return fachbereich;
    }

    public void setFachbereich(String fachbereich) {
        this.fachbereich = fachbereich;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


    @Override
    public String toString() {
        return "Manager: id: " + this.getId() + " | name: " + this.getName() + " | vorname: " + this.vorname
                + " | fachbereich: " + this.getFachbereich();
    }
}
