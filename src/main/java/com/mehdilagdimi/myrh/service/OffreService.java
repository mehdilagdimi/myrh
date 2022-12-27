package com.mehdilagdimi.myrh.service;


import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.entity.Offre;
import com.mehdilagdimi.myrh.model.entity.OffreDetails;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.repository.OffreDetailsRepository;
import com.mehdilagdimi.myrh.repository.OffreRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OffreService{

    @Autowired
    OffreRepository offreRepository;

    @Autowired
    OffreDetailsRepository offreDetailsRepository;

    public Page<Offre> getOffresPaginated(int maxItems, int requestedPage) throws EmptyResultDataAccessException{
        Pageable pageableOffres = PageRequest.of(
                requestedPage, maxItems,
                Sort.by("publicationDate").descending().and(Sort.by("isExpired"))
        );

        Page<Offre> offres = offreRepository.findAll(pageableOffres);

        if(offres.isEmpty()) throw new EmptyResultDataAccessException("List of offre records is empty", maxItems);
        return offres;
    }

    public Offre getOffreById(Long id){
        return offreRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException());
    }

    public Offre saveOffre(User employer, OfferRequest req) throws PersistenceException{
        Offre offre = new Offre(
                employer,
                req.getTitle(),
                req.getOfferType(),
                req.getProfile(),
                req.getVille(),
                req.getEducation(),
                req.getSalary()
        );
        System.out.println(" inside save offer 1");
        OffreDetails offreDetails = new OffreDetails(offre, req.getDescription());
        offre.setOffreDetails(offreDetails);

        offre = offreRepository.save(offre);
        System.out.println(" inside save offer 2");
        if(offre == null) throw new PersistenceException();
        System.out.println(" inside save offer after throwing mentioned");
        return offre;
    }


}
