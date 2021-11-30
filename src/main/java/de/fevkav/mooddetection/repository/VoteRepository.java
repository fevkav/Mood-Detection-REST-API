package de.fevkav.mooddetection.repository;

import de.fevkav.mooddetection.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {


    List<Vote> findByCalendarweek(int calendarweek);

    @Query("select v.calendarweek from Vote v group by v.calendarweek order by v.calendarweek ASC")
    List<Integer> findAllVotedCalendarweeks();

    //die Anzahl der abgegebenen Moods für alle KWs sollen gruppiert ausgegeben werden
    @Query("select v.mood, count(v.mood) from Vote v group by v.mood")
    List<Object[]> amountOfMoodsTotal();

    // Anzahl Stimmen für bestimmte KW
    @Query( "select new de.fevkav.mooddetection.model.VoteMoodAmount(v.mood, count(v.mood)) " +
            "from Vote v where v.calendarweek = :week and (v.employee in :employees) group by v.mood")
    List<VoteMoodAmount> getAmountMoods(@Param("week") int week, @Param("employees") List<Employee> employees);

    /**
     * Durchschnitt für einen Fachbereich. Für die Auswertung werden alle Mitarbeiter des jeweiligen Fachbereichs
     * herangezogen.
     */

    @Query( "select new de.fevkav.mooddetection.model.VoteAverage(v.calendarweek, avg (v.mood)) " +
            "from Vote v where (v.employee in :employees) group by v.calendarweek")
    List<VoteAverage> getAverage(@Param("employees") List<Employee> employees);

    // Finde anzahl votes für jede kw innerhalb eines Fachbereichs
    @Query( "select new de.fevkav.mooddetection.model.VoteAmount(v.calendarweek, count (v)) " +
            "from Vote v where (v.employee in :employees) group by v.calendarweek")
    List<VoteAmount> getAmountVotes(@Param("employees") List<Employee> employees);

    /**
     * Gibt die Abstimmung für die aktuelle Kalenderwoche eines Mitarbeiters zurück.
     */
    Vote findByEmployeeAndCalendarweek(Employee employee, int calendarweek);

    /**
     * Liefert eine Referenz auf den Datensatz mit der angegebenen Id
     */
    Vote getOne(Long id);

    /**
     * Liefert alle Abstimmung, die der angegebene Mitarbeiter getätigt hat
     */
    List<Vote> findByEmployee(Employee employee);

    /**
     * Liefert alle Votes zu den angegebenen Mitarbeitern zurück.
     * @param employees
     * @return
     */
    @Query( "select count (v) from Vote v where (v.employee in :employees) group by v.employee")
    List<Object> getVotedEmployees(@Param("employees") List<Employee> employees);   //TODO gibt alle Votes aus










}
