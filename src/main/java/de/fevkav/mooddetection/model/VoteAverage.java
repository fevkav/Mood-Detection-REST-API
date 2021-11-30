package de.fevkav.mooddetection.model;

/**
 * Diese Klasse ist nicht als Entity definiert und soll lediglich eine Hülle für das Abfrageergebnis
 * bereitstellen, die den Durchschnitt der Votes für die einzelnen KWs berechnet.
 *
 */
public class VoteAverage {

    public int calendarweek;
    public double average;

    public VoteAverage(int calendarweek, double average) {
        this.calendarweek = calendarweek;
        this.average = Math.round(average * 100.0) / 100.0;
    }

}
