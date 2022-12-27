package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.User;
import com.mehdilagdimi.myrh.model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
}
