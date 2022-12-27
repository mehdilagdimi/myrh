package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.Offre;
import com.mehdilagdimi.myrh.model.entity.OffreDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreDetailsRepository extends JpaRepository<OffreDetails, Long> {
}
