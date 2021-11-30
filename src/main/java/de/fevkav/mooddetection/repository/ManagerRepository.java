package de.fevkav.mooddetection.repository;

import de.fevkav.mooddetection.model.Manager;
import de.fevkav.mooddetection.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Long> {

    Manager findById(Long id);
    Manager findByUser(User user);
    Manager getOne(long id);
}
