package com.mehdilagdimi.myrh.controller;

import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/upload")
    public Long uploadImage(@RequestParam MultipartFile multipartImage) throws Exception {
        Long id = 452L;
        return imageService.saveImage(id, multipartImage);
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<Resource>  downloadImage(@PathVariable Long id) {
        byte[] image = null;
        Response response = null;
        try{
            image = imageService.getImage(id);
            System.out.println(" inside image download contr");
            response = new Response(
                    HttpStatus.OK,
                    "Successfully Retrieved Image");

       } catch (ResponseStatusException e){
           e.printStackTrace();
            response = new Response(HttpStatus.BAD_REQUEST, "Failed fetching image");
       } finally {
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