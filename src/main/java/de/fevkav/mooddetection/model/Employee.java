package de.fevkav.mooddetection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;


    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Vote> votes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_manager", nullable = false)
    private Manager manager;

    public Employee() {}

    public Employee(String token, Manager manager) {
        this.manager = manager;
    }

    public Employee(Manager manager) {
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public List<Vote> getVotes() { return votes; }

    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }



    @Override
    public String toString() {
        return "id: " + this.id + " | manager_id: " + this.getManager();
    }
}
