package de.fevkav.mooddetection.repository;

import de.fevkav.mooddetection.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
