package de.fevkav.mooddetection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Diese Fachklasse repräsentiert eine Abstimmung, die mit JPA persistiert werden soll (Stichpunkt: O/R Mapping)
 * Beachte: Ohne der Annotation @Table wird diese Entität auf eine gleichnamige Tabelle in der Datenbank gemappt.
 * Das gleiche gilt auch für das Mappen der Tabellenspalten => @Column(name="...").
 */
@Entity
public class Vote {

    /** Gleicht dem Primärschlüssel */
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    /** Beinhaltet den Wert der Stimmung (sollte 1-5 sein) */
    private int mood;

    /** Gibt an, in welcher Kalenderwoche die Stimme abgegeben worden ist. */
    private int calendarweek;

    /** Das Jahr in dem die Abstimmung erfolgte */
    private int year;

    @ManyToOne
    @JoinColumn(name ="id_employee", nullable = false)
    @JsonIgnore
    private Employee employee;


    public Vote() {}

    /* Muss später automatisch die aktuelle KW eintragen */
    public Vote(int mood, int calendarweek, int year, Employee employee) {
        this.mood = mood;
        this.calendarweek = calendarweek;
        this.year = year;
        this.employee = employee;
    }

    public Long getId() { return id; }

    public int getMood() { return mood; }

    public void setMood(int mood) { this.mood = mood; }

    public int getCalendarweek() { return calendarweek; }

    public void setCalendarweek(int calendarweek) { this.calendarweek = calendarweek; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    @Override
    public String toString() {
        return "id: " + this.id + " | mood: " + this.mood + " | calendarweek: " + this.calendarweek + " | year: " + this.year;
    }







}
