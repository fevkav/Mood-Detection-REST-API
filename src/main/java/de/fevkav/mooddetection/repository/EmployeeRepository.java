package de.fevkav.mooddetection.repository;

import de.fevkav.mooddetection.model.Employee;
import de.fevkav.mooddetection.model.Manager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {



    Employee findById(Long id);

    List<Employee> findByManager(Manager manager);



    // liefert eine Referenz des Datensatzes mit der gegebenen id
    Employee getOne(long id);

}
