package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
}
