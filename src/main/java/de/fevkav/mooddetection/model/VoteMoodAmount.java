package de.fevkav.mooddetection.model;

public class VoteMoodAmount {

    public String mood;
    public long amount;

    public VoteMoodAmount(int mood, long amount) {
        if (mood == 1)
            this.mood = "sehr schlecht";
        else if (mood == 2)
            this.mood = "schlecht";
        else if (mood == 3)
            this.mood = "durchscnittlich";
        else if (mood == 4)
            this.mood = "gut";
        else if (mood == 5)
            this.mood = "sehr gut";
        this.amount = amount;
    }
}
