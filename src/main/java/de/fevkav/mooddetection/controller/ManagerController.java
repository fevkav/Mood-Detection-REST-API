package de.fevkav.mooddetection.controller;

import de.fevkav.mooddetection.model.Employee;
import de.fevkav.mooddetection.model.User;
import de.fevkav.mooddetection.repository.ManagerRepository;
import de.fevkav.mooddetection.repository.UserRepository;
import de.fevkav.mooddetection.repository.VoteRepository;
import de.fevkav.mooddetection.security.AppUser;
import de.fevkav.mooddetection.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Diese Klasse stellt Methoden zur Verfügung, die bei HTTP Anfragen an die Endpunkte "/login", "authmanager",
 * "/managers/employees" ausgeführt werden. Diese Endpunkte werden clientseitig vom Vorgesetzten angefragt.
 */
@RestController
public class ManagerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ManagerRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public ManagerController(PasswordEncoder passwordEncoder,
                             TokenService tokenService,
                             AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("authmanager")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<?> authenticate() {

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("MANAGER"))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


    }

    @PostMapping("/login")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")     //TODO funtioniert ohne, wieso?
    public ResponseEntity<?> login(@Valid @RequestBody User loginManager) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginManager.getUsername(), loginManager.getPassword());

        try {
            this.authenticationManager.authenticate(authenticationToken);
            String token = tokenService.createToken(loginManager.getUsername(), true);
            return ResponseEntity.ok(token);
        }
        catch (AuthenticationException e) {
            System.out.println("Authentication failed");
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gibt die Anzahl der Mitarbeiter eines Fachbereichs zurück, die sich für die App registriert
     * und einen Token erhalten haben und die Anzahl aus diesen Mitarbeitern mit mind. einem Vote.
     * Wenn ein Mitarbeiter seinen Token verliert, wird ein neuer Datensatz erstellt und der "tote"
     * Datensatz besteht weiterhin.
     * TODO: Mitarbeiter-Datensatz nach Gültigkeitsablauf/Verlorengehen des Tokens löschen.
     *
     * @return Anzahl Mitarbeiter-Token, die bis dato vergeben wurden (innerhalb eines Fachbereichs)
     */
    @GetMapping("/managers/employees")
    @CrossOrigin("http://localhost:8100")
//    @CrossOrigin("http://localhost:5584")
    public ResponseEntity<int[]> getAmountEmployees() {

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getAuthorities().equals(AuthorityUtils.createAuthorityList("MANAGER"))) {
            User userManager = userRepository.findByUsername(appUser.getUsername());

            // alle registrierten Mitarbeiter in einem Fachbereich
            List<Employee> allEmployees = userManager.getManager().getEmployees();

            // alle Mitarbeiter in einem Fachbereich, die min. einmal abgestimmt haben
            List<Object> employeesWithVote = voteRepository.getVotedEmployees(allEmployees);

            // Anzahl registrierter Mitglieder und Anzahl Mitglieder davon mit Votes zurückgeben
            int[] result = {allEmployees.size(), employeesWithVote.size()};

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
