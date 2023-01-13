package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.base.enums.OfferStatus;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.model.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OffreRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    @Query("SELECT o FROM Offer o WHERE (?1 is null or o.title LIKE %?1%)"
            + " AND (?2 is null or o.ville LIKE %?2%)"
            + " OR (?3 is null or o.offreType = ?3)"
    )
    Page<Offer> searchByFilter(String title, String ville, OfferType offreType, Pageable pageable);
    Page<Offer> findAllByOfferStatus(OfferStatus offerStatus, Pageable pageable);
}
