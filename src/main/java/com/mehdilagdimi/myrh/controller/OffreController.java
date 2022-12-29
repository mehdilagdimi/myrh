package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.base.OfferFI;
import com.mehdilagdimi.myrh.base.enums.OfferStatus;
import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Offer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.OffreService;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/offers")
public class OffreController {

    @Autowired
    OffreService offreService;

    @GetMapping
    public ResponseEntity<Response> getOffers(
            @RequestParam(defaultValue = "10") Integer maxItems,
            @RequestParam(defaultValue = "0") Integer requestedPage
            ){
        Response response = null;
        try{
            Page<Offer> offers = offreService.getOffresPaginated(maxItems, requestedPage);
            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Offres Page" + requestedPage,
                    "offers",
                    offers
            );

        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addOffer(
            Authentication authentication,
            @RequestBody OfferRequest offerRequest
    ){
        Response response = null;
        try{
            Offer offer = offreService.saveOffre((User)authentication.getPrincipal(), offerRequest);
            response = new Response(
                    HttpStatus.CREATED,
                    "Successfully saved Offer"
            );

            response.addData("offer", offer);
            response.addData("offerDetails", offer.getOfferDetails());
            response.addData("employer", offer.getEmployer());
        } catch (PersistenceException | UsernameNotFoundException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.INTERNAL_SERVER_ERROR,"Failed saving offer");
        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getOffer(
            @PathVariable Long id
            ){
        Response response = null;
        try{
            Offer offer = offreService.getOffreById(id);

            Map<String, Object> data = new HashMap<>(Map.ofEntries(
                    Map.entry("offre", offer)
            ));

            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Offre",
                    data
            );

        }catch (NoSuchElementException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Response> updateOfferStatus(
            @PathVariable Long id,
            @RequestBody OfferRequest offerRequest
    ){
        Response response = null;
        try{
            OfferFI updateStatusImpl = (offer) -> offer.setOfferStatus(offerRequest.getOfferStatus());
            Offer offer = offreService.updateOffer(id, updateStatusImpl);

            Map<String, Object> data = new HashMap<>(Map.ofEntries(
                    Map.entry("offer", offer),
                    Map.entry("offerDetails", offer.getOfferDetails())
            ));

            response = new Response(
                    HttpStatus.OK,
                    "Successfully Updated Offer",
                    data
            );

        }catch (NoSuchElementException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "Failed Updating Offer; No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

}
