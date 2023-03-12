package com.example.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.library.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
