package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.OfferImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface OfferImageRepository extends JpaRepository<OfferImage, Long> {

    Optional<OfferImage> findByName(String name);
    Optional<OfferImage> findById(Long id);
}
