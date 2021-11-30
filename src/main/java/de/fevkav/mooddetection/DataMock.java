package de.fevkav.mooddetection;

import de.fevkav.mooddetection.model.User;
import de.fevkav.mooddetection.repository.EmployeeRepository;
import de.fevkav.mooddetection.repository.ManagerRepository;
import de.fevkav.mooddetection.repository.UserRepository;
import de.fevkav.mooddetection.repository.VoteRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DataMock implements InitializingBean {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {

        // codiert alle Passwörter der 4 angelegten User (siehe resources/data.sql)
        for (long i = 1; i <= 4; i++) {
            User user = userRepository.findOne(i);
            user.encodePassword(passwordEncoder);
            userRepository.save(user);
        }



    }
}
