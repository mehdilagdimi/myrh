package com.mehdilagdimi.myrh.service;


import com.mehdilagdimi.myrh.model.Image;
import com.mehdilagdimi.myrh.model.entity.*;
import com.mehdilagdimi.myrh.repository.OfferImageRepository;
import com.mehdilagdimi.myrh.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@Service
public class ImageService {

    @Autowired
    UserService userService;
    @Autowired
    OfferService offerService;
    @Autowired
    OfferImageRepository offerImageImageRepository;
    @Autowired
    ProfileImageRepository profileImageImageRepository;


    public Long saveImage(User employer, Long id, MultipartFile multipartImage) throws IOException, NoSuchElementException, InvalidParameterException {
        Offer offer = offerService.getOffreById(id);

        if(employer.getId() != offer.getEmployer().getId()) throw new InvalidParameterException();

        Image image;

        if(offer.getImage() == null){ image = new OfferImage(); }
        else { image = offer.getImage();}

        image.setName(multipartImage.getName());
        image.setContent(multipartImage.getBytes());
        ((OfferImage)image).setOffer(offer);
        offer.setImage(((OfferImage)image));

        return offerImageImageRepository
                .save((OfferImage)image).getId();
    }
    public Long saveImageFor(Class<?> uploadFor, Long id, MultipartFile multipartImage) throws IOException, NoSuchElementException {
        Long imgId = null;
        System.out.println(" upload for simple name "+ uploadFor.getSimpleName());
        switch (uploadFor.getSimpleName()){
            case "User":
                User user = userService.getUser(id);
                Image pImage;

                if(user.getImage() == null){ pImage = new ProfileImage(); }
                else { pImage = user.getImage();}

                pImage.setName(multipartImage.getName());
                pImage.setContent(multipartImage.getBytes());
                ((ProfileImage)pImage).setUser(user);
                user.setImage(((ProfileImage)pImage));

                imgId = profileImageImageRepository
                        .save((ProfileImage)pImage).getId();
                break;

            case "Offer":
                Offer offer = offerService.getOffreById(id);
                Image oImage;
                if(offer.getImage() == null){ oImage = new OfferImage(); }
                else { oImage = offer.getImage();}

                oImage.setName(multipartImage.getName());
                oImage.setContent(multipartImage.getBytes());
                ((OfferImage)oImage).setOffer(offer);
                offer.setImage(((OfferImage)oImage));

                imgId = offerImageImageRepository
                        .save((OfferImage)oImage).getId();
                break;
        }

        return imgId;
    }

    public byte[] getOfferImage(Long id) throws ResponseStatusException{
        return offerImageImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
    }
    public byte[] getProfileImage(Long id) throws ResponseStatusException{
        return profileImageImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
    }
}
