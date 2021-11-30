package de.fevkav.mooddetection.controller;

import de.fevkav.mooddetection.model.Employee;
import de.fevkav.mooddetection.model.User;
import de.fevkav.mooddetection.model.Vote;
import de.fevkav.mooddetection.repository.ManagerRepository;
import de.fevkav.mooddetection.repository.EmployeeRepository;
import de.fevkav.mooddetection.repository.UserRepository;
import de.fevkav.mooddetection.repository.VoteRepository;
import de.fevkav.mooddetection.security.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;



/**
 * Diese Klasse dient als REST-Controller um eingehende HTTP-Requests seitens der App entsprechend zu beantworten.
 * BEACHTE: Es werden nur Anfragen aus localhost:8100 akzeptiert (Stichwort: CrossOrigin)
 */
@RestController
public class VoteController {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Diese Methode verarbeitet HTTP GET-Requests an dem Endpunkt /votes/avg.
     * Es berechnet den Durchschnitt aller abgegebenen Stimmen in einer Kalenderwoche.
     *
     * @return
     */
    @GetMapping(value = "/votes/avg")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> getAverageVotes() {

        // hole den Benutzer, der sich authentifiziert hat.
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("MANAGER"))) {

            User userManager = userRepository.findByUsername(appUser.getUsername());

            return ResponseEntity.ok(repository.getAverage(userManager.getManager().getEmployees()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


    }

    /**
     * Hier wird die Anzahl der abgegebenen Stimmen zurückgegeben.
     * @param week keine Angabe bzw. week = 0 wird Anzahl für alle KWs angezeigt, ansonsten Anzahl Stimmen für KW = week
     * @return
     */
    @GetMapping(value= "/votes/amount")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> getAmountVotes(@RequestParam(required = false, defaultValue = "0") int week) {

        // hole den Benutzer, der sich authentifiziert hat.
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("MANAGER"))) {

            User userManager = userRepository.findByUsername(appUser.getUsername());

            if (week == 0) {
                return ResponseEntity.ok(repository.getAmountVotes(userManager.getManager().getEmployees()));
            }
            else {
                return ResponseEntity.ok(repository.getAmountMoods(week, userManager.getManager().getEmployees()));
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    /*
    =========================================== Mitarbeiter ============================================================
     */
    /**
     * Hier schickt der Client seine Abstimmung in der Form "{ "mood": "4" }" zu.
     */
    @PostMapping(value = "/voting")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> postVote(@Valid @RequestBody Vote inputVote) {

        // hole den Benutzer, der sich authentifiziert hat.
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        // Es dürfen nur User in Mitarbeiterposition abstimmen.
        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("EMPLOYEE"))) {

            final int CURRENTCALENDARWEEK = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            final int CURRENTYEAR = LocalDate.now().getYear();

            // hole Mitarbeiter aus der Datenbank

            Employee employee = employeeRepository.findById(Long.valueOf(appUser.getUsername()));


            // Prüfe, ob Mitarbeiter schon für die aktuelle Woche abgestimmt hat.
            Vote vote = repository.findByEmployeeAndCalendarweek(employee, CURRENTCALENDARWEEK);

            if (vote != null) {
                // Mitarbeiter hat schon abgestimmt, also Abstimmung bearbeiten.
                Vote voteToUpdate = repository.getOne(vote.getId());
                voteToUpdate.setMood(inputVote.getMood());
                repository.save(voteToUpdate);
            } else {
                // Mitarbeiter hat nicht abgestimmt, also neuer Datensatz
                repository.save(new Vote(inputVote.getMood(), CURRENTCALENDARWEEK, CURRENTYEAR, employee));
            }

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Hier soll dem Client seine persönlichen Abstimmungen gesendet werden.
     */
    @GetMapping("/votestatus")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> getVotesOfEmployee() {

        // hole den Mitarbeiter, der den Request getätigt hat
        AppUser appUser =
                (AppUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        Employee employee = employeeRepository.findById(Long.valueOf(appUser.getUsername()));

        // finde alle Abstimmungen des Mitarbeiters
        List<Vote> votesOfEmployee = repository.findByEmployee(employee);

        return ResponseEntity.ok(votesOfEmployee);
    }



}
