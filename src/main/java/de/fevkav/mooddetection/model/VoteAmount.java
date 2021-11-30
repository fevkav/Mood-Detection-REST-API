package de.fevkav.mooddetection.model;

/**
 * Diese Klasse ist nicht als Entity definiert und soll lediglich eine Hülle für das Abfrageergebnis
 * bereitstellen, die die Anzahl der Votes für die jeweilige KW berechnet.
 *
 */
public class VoteAmount {

    public int calendarweek;
    public long amount;

    public VoteAmount(int calendarweek, long amount) {
        this.calendarweek = calendarweek;
        this.amount = amount;
    }

}