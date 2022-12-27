package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Offre;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.OffreService;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class OffreController {

    @Autowired
    OffreService offreService;

    @GetMapping("/offres")
    public ResponseEntity<Response> getOffres(
            @RequestParam(defaultValue = "10") Integer maxItems,
            @RequestParam(defaultValue = "0") Integer requestedPage
            ){
        Response response = null;
        try{
            Page<Offre> offres = offreService.getOffresPaginated(maxItems, requestedPage);
            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Offres Page" + requestedPage,
                    "offres",
                    offres
            );

        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PostMapping("/offres/add")
    public ResponseEntity<Response> addOffre(
            Authentication authentication,
            @RequestBody OfferRequest offerRequest
    ){
        Response response = null;
        try{
            System.out.println("name " + authentication.getName());
            System.out.println("authorities " + authentication.getAuthorities().toString());
            System.out.println("user role " + ((User)authentication.getPrincipal()).getRole().toString());
            System.out.println("user  email " + ((User)authentication.getPrincipal()).getEmail());
            Offre offre = offreService.saveOffre((User) authentication.getPrincipal(), offerRequest);
            System.out.println(" after offre save service");
            response = new Response(
                    HttpStatus.OK,
                    "Successfully saved Offer",
                    "offre",
                    offre

            );
            System.out.println(" respone object  "+ response);
        } catch (PersistenceException e){
            e.printStackTrace();
            response = new Response(HttpStatus.INTERNAL_SERVER_ERROR,"Failed saving offre");
        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/offres/{id}")
    public ResponseEntity<Response> getOffre(
            @PathVariable Long id
            ){
        Response response = null;
        try{
            Offre offre = offreService.getOffreById(id);

            Map<String, Object> data = new HashMap<>(Map.ofEntries(
                    Map.entry("offre", offre),
                    Map.entry("offreDetails", offre.getOffreDetails())
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


}
