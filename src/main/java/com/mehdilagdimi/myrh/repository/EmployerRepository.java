package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.Agent;
import com.mehdilagdimi.myrh.model.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
