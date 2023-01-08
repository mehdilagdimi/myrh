package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.base.enums.OfferStatus;
import com.mehdilagdimi.myrh.model.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OffreRepository extends JpaRepository<Offer, Long> {

    Page<Offer> findAllByOfferStatus(OfferStatus offerStatus, Pageable pageable);
}
