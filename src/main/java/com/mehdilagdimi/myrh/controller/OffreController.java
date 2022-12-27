package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Offre;
import com.mehdilagdimi.myrh.service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.Map;

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
                    "Successfully Retriefed Offres Page" + requestedPage,
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
}
