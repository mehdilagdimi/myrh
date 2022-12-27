package com.mehdilagdimi.myrh.service;


import com.mehdilagdimi.myrh.model.entity.Offre;
import com.mehdilagdimi.myrh.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OffreService{

    @Autowired
    OffreRepository offreRepository;

    public Page<Offre> getOffresPaginated(int maxItems, int requestedPage) throws EmptyResultDataAccessException{
        Pageable pageableOffres = PageRequest.of(
                requestedPage, maxItems,
                Sort.by("publicationDate").descending().and(Sort.by("isExpired"))
        );

        Page<Offre> offres = offreRepository.findAll(pageableOffres);

        if(offres.isEmpty()) throw new EmptyResultDataAccessException("List of offre records is empty", maxItems);
        return offres;
    }

}
