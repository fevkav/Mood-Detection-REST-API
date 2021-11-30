package de.fevkav.mooddetection.controller;

import de.fevkav.mooddetection.model.Manager;
import de.fevkav.mooddetection.model.Employee;
import de.fevkav.mooddetection.model.User;
import de.fevkav.mooddetection.model.Vote;
import de.fevkav.mooddetection.repository.ManagerRepository;
import de.fevkav.mooddetection.repository.EmployeeRepository;
import de.fevkav.mooddetection.repository.UserRepository;
import de.fevkav.mooddetection.repository.VoteRepository;
import de.fevkav.mooddetection.security.AppUser;
import de.fevkav.mooddetection.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.IsoFields;

/**
 * Diese Klasse stellt Methoden zur Verfügung, die bei HTTP Anfragen an die Endpunkte "/register", "authemplpyee" und
 * "/currentweek" ausgeführt werden. Diese Endpunkte werden clientseitig vom Mitarbeiter angefragt.
 */
@RestController
public class EmployeeController {

    private final TokenService tokenService;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PasswordEncoder pe;

    public EmployeeController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Durch Aufruf dieses Endpunktes, der für jeden zugänglich ist, wird ein Token für den Mitarbeiter generiert und
     * zurückgegeben. Der Mitarbeiter schickt kein Benutzernamen/Passwort, nur eine Manager id, die angibt zu welchem
     * Fachbereich er zugehört. Form: { "id": "4" }
     *
     * @param manager = ein Manager Objekt mit der übergebenen ID
     * @return
     */
    @PostMapping(value = "/register")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> getToken(@RequestBody Manager manager) {

        // hole den Manager mit der übergebenen id
        Manager managerOfEmployee = managerRepository.findById(manager.getId());

        if (managerOfEmployee != null) {

            // Zuerst leeren Datensatz mit richtigem Manager einfügen, um generierte ID des Mitarbeiters zu holen
            // da schon vor der Token-Generierung der username gebraucht wird.
            Employee emptyEmployee = employeeRepository.save(new Employee(managerOfEmployee));

            //Username für JWT ist gleich ID in der Tabelle employee
            String username = emptyEmployee.getId().toString();

            // erstellt den neuen User als Mitarbeiter-Rolle und ohne Password, wobei Username gleich Id_Employee ist.
            userRepository.save(new User(username, null, "EMPLOYEE"));

            // holt eine Referenz auf den soeben erstellten Mitarbeiter ohne Token
            Employee updateEmployee = employeeRepository.getOne(emptyEmployee.getId());

            //generiert Token mit username = id
            final String employeeToken = this.tokenService.createToken(username, false);

            employeeRepository.save(updateEmployee);

            return ResponseEntity.ok(employeeToken);
        }

        return ResponseEntity.badRequest().body("Falsche Manager Id");

    }

    /**
     * Diese Methode dient nur zur Authentifizierung des Mitarbeiters. Wird bei jedem Start der App aufgerufen.
     * @return
     */
    @GetMapping("/authemployee")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> authenticate() {

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("EMPLOYEE"))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    /**
     * Diese Methode gibt alle Fachbereichsleiter mit deren ID zurück.
     *
     * @return HTTP-Response
     */
    @GetMapping("/managers")
    @CrossOrigin("http://localhost:8100")
    public ResponseEntity<?> getManagers() {

        return ResponseEntity.ok(managerRepository.findAll());
    }

    /**
     * Diese Methode gibt die aktuelle Kalenderwoche und die abgegebene Stimme für diese Woche, falls vorhanden, in der
     * Form { aktuelle KW, abgegebene Stimme }, z.B.: { 32, 1 } zurück.
     * @return
     */
    @GetMapping("/currentweek")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public int[] getCurrentWeek() {

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final int CALENDARWEEK = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        int[] currentweek = { CALENDARWEEK, 0};

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("EMPLOYEE"))) {

            Employee employee = employeeRepository.findById(Long.valueOf(appUser.getUsername()));

            Vote vote = voteRepository.findByEmployeeAndCalendarweek(employee, CALENDARWEEK);

            // falls für die aktuelle Woche gevotet wurde, füge Mood als zweiten Wert ein.
            if (vote != null) {
                currentweek[1] = vote.getMood();
                return currentweek;
            }

            return currentweek;
        }
        return currentweek;
    }
}
