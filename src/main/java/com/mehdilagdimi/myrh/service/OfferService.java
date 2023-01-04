package com.mehdilagdimi.myrh.service;


import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.Offer;
import com.mehdilagdimi.myrh.model.entity.OfferDetails;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.repository.EmployerRepository;
import com.mehdilagdimi.myrh.repository.OffreDetailsRepository;
import com.mehdilagdimi.myrh.repository.OffreRepository;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
public class OfferService {

    @Autowired
    OffreRepository offreRepository;

    @Autowired
    EmployerRepository employerRepository;

    @Autowired
    OffreDetailsRepository offreDetailsRepository;

    public Page<Offer> getOffresPaginated(int maxItems, int requestedPage) throws EmptyResultDataAccessException{
        Pageable pageableOffres = PageRequest.of(
                requestedPage, maxItems,
                Sort.by("publicationDate").descending().and(Sort.by("isExpired"))
        );

        Page<Offer> offers = offreRepository.findAll(pageableOffres);

        if(offers.isEmpty()) throw new EmptyResultDataAccessException("List of offre records is empty", maxItems);
        return offers;
    }

    public Offer getOffreById(Long id){
        return offreRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException());
    }

    public Offer saveOffre(User principal, OfferRequest req) throws PersistenceException, UsernameNotFoundException, NullPointerException {
        Employer employer = employerRepository.findById(principal.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found......"));
        System.out.println(" logging re" + req.toString());
        if(ObjectUtils.anyNull(req)) throw new NullPointerException();
        Offer offer = new Offer(
                req.getTitle(),
                req.getOfferType(),
                req.getProfile(),
                req.getVille(),
                req.getEducation(),
                req.getSalary()
        );
        employer.addOffer(offer);

        OfferDetails offerDetails = new OfferDetails(offer, req.getDescription());
        offer.setOfferDetails(offerDetails);

        offer = offreRepository.save(offer);
        if(offer == null) throw new PersistenceException();
        return offer;
    }

    public Offer updateOffer(Long id, Consumer<Offer> updateImpl) throws NoSuchElementException{
        Offer offer = offreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
        //execute the implemenation to update a certain field
//        updateImpl.update(offer);
        updateImpl.accept(offer);

        offreRepository.save(offer);
        return offer;
    }
}
