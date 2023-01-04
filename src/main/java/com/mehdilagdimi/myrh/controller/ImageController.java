package com.mehdilagdimi.myrh.controller;

import com.mehdilagdimi.myrh.model.Image;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.Offer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.ImageService;
import com.mehdilagdimi.myrh.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/images")
class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    OfferService offerService;


    @PostMapping("/upload/{offerId}")
    public Long uploadImage(
            Authentication authentication,
            @RequestParam MultipartFile image,
            @PathVariable Long offerId
    ) {
        //for testing upload for offer
        Long imageId;
        try{
            imageId = imageService.saveImage((User)authentication.getPrincipal(), offerId, image);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return imageId;

    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<?>  downloadImage(@PathVariable Long id, @RequestParam("for") String getFor) {
        byte[] image = null;
        Response response = null;
        try{
            switch (getFor){
                case "offer": image = imageService.getOfferImage(id);
                        break;
                case "user": image = imageService.getProfileImage(id);
                        break;
            }

            System.out.println(" inside image download contr");
            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Image");

       } catch (ResponseStatusException e){
           e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "Failed fetching image");
       } finally {
            if(image == null || response == null)
                return ResponseEntity.internalServerError().body("Failed fetching image");

            return new ResponseEntity<>(new ByteArrayResource(image), response.getStatus());
        }
    }


//    @GetMapping("/get-image-dynamic-type")
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> getImageDynamicType(@RequestParam("jpg") boolean jpg) {
//        MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
//        InputStream in = jpg ?
//                getClass().getResourceAsStream("/com/baeldung/produceimage/image.jpg") :
//                getClass().getResourceAsStream("/com/baeldung/produceimage/image.png");
//        return ResponseEntity.ok()
//                .contentType(contentType)
//                .body(new InputStreamResource(in));
//    }
}