package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.base.enums.Education;
import com.mehdilagdimi.myrh.base.enums.OfferStatus;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.base.enums.Profile;
import com.mehdilagdimi.myrh.model.FilterParams;
import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Offer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.repository.specification.SearchCriteria;
import com.mehdilagdimi.myrh.service.OfferService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/offers")
public class OffreController {

    @Autowired
    OfferService offerService;

    @GetMapping
    public ResponseEntity<Response> getOffers(
            Authentication authentication,
            @RequestParam(defaultValue = "100") Integer maxItems,
            @RequestParam(defaultValue = "0") Integer requestedPage,
            @RequestParam(name = "employer",required = false) Long employerId,
            @RequestParam(name = "status",required = false) OfferStatus status,
            @RequestParam(required = false) Map<String, String> searchFilters
            ){
        Response response = null;
        try{

//            System.out.println(" id before  " + employerId);
            Page<Offer> offers = null;
            if(employerId != null) {
                offers = offerService.getAllOffersByEmployerPaginated(employerId, maxItems, requestedPage);
            } else if(searchFilters.size() > 0) {
                offers = offerService.getSearchedOffers(searchFilters, maxItems, requestedPage);
            } else {
                if(status == null) offers = offerService.getAllOffersByStatusPaginated(authentication, maxItems, requestedPage, OfferStatus.ACCEPTED);
                else offers = offerService.getAllOffersByStatusPaginated(authentication, maxItems, requestedPage, status);
            }

            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Offers Page" + requestedPage,
                    "data",
                    offers.getContent()
            );

        }catch (EmptyResultDataAccessException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @RolesAllowed("EMPLOYER")
    @PostMapping("/add")
    public ResponseEntity<Response> addOffer(
            Authentication authentication,
            @RequestBody OfferRequest offerRequest
    ){
        Response response = null;
        try{
            System.out.println(" inside control offer req " + offerRequest.toString());
            Offer offer = offerService.saveOffre((User)authentication.getPrincipal(), offerRequest);
            response = new Response(
                    HttpStatus.CREATED,
                    "Successfully saved Offer"
            );

            response.addData("data", offer);

        } catch (PersistenceException | UsernameNotFoundException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.INTERNAL_SERVER_ERROR,"Failed saving offer");
        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
    @GetMapping("/fields-options-list")
    public ResponseEntity<Response> addOffer(

    ){
        Response response = null;
        try{
            response = new Response(
                    HttpStatus.OK,
                    "Successfully retrieved list of offer fields"
            );

            response.addData("education", Education.values());
            response.addData("offerType", OfferType.values());
            response.addData("profile", Profile.values());
        } catch (PersistenceException | UsernameNotFoundException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.INTERNAL_SERVER_ERROR,"Failed retrieving list of offer fields");
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
            Offer offer = offerService.getOffreById(id);

            Map<String, Object> data = new HashMap<>(Map.ofEntries(
                    Map.entry("data", offer)
            ));

            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Offer",
                    data
            );

        }catch (NoSuchElementException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }


    @RolesAllowed("AGENT")
    @PostMapping("/update-status")
    public ResponseEntity<Response> updateOfferStatus(
            @RequestBody OfferRequest offerRequest
    ){
        Response response = null;

        System.out.println(" offer req " + offerRequest.getOfferStatus());
        System.out.println(offerRequest.toString());
        try{
//            OfferFI updateStatusImpl = (offer) -> offer.setOfferStatus(offerRequest.getOfferStatus());
            Consumer<Offer> updateStatusImpl = (offer) -> offer.setOfferStatus(offerRequest.getOfferStatus());
            Offer offer = offerService.updateOffer(offerRequest.getId(), updateStatusImpl);


            response = new Response(
                    HttpStatus.OK,
                    "Successfully Updated Offer",
                    "data",
                    offer
            );

        }catch (NoSuchElementException | NullPointerException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "Failed Updating Offer; No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

}
