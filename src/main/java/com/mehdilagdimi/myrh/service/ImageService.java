package com.mehdilagdimi.myrh.service;


import com.mehdilagdimi.myrh.model.Image;
import com.mehdilagdimi.myrh.model.entity.*;
import com.mehdilagdimi.myrh.repository.OfferImageRepository;
import com.mehdilagdimi.myrh.repository.OffreRepository;
import com.mehdilagdimi.myrh.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class ImageService {

    @Autowired
    OfferImageRepository offerImageImageRepository;
    @Autowired
    ProfileImageRepository profileImageImageRepository;

    @Autowired
    OffreRepository offreRepository;

    public Long saveImage(Long id, MultipartFile multipartImage) throws IOException, NoSuchElementException {
        Offer offer = offreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
        Image image = new OfferImage();
        image.setName(multipartImage.getName());
        image.setContent(multipartImage.getBytes());
        ((OfferImage)image).setOffer(offer);
        offer.setImage(((OfferImage)image));

        return offreRepository
                .save(offer).getId();
    }

    public byte[] getImage(Long id) throws ResponseStatusException{
        return offerImageImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
    }
}
