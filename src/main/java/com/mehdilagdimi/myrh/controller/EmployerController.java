package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.model.OfferRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.Offer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.OffreService;
import com.mehdilagdimi.myrh.service.UserService;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    @Autowired
    UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<Response> getEmployer(
            @PathVariable Long id
            ){
        Response response = null;
        try{
            Employer employer = userService.getEmployer(id);

            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Employer",
                    "employer",
                    employer
            );

        }catch (NoSuchElementException e){
            e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "No Result was found");

        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }
    }


}
