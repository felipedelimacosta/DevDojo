package com.example.devdojo.controller;



import com.example.devdojo.dto.AnimePostDto;
import com.example.devdojo.dto.AnimePutDto;
import com.example.devdojo.model.Anime;
import com.example.devdojo.service.AnimeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@AllArgsConstructor
public class AnimeController {

    //private DateUtil dateUtil;
    @Autowired
    private AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        return new ResponseEntity<>(animeService.listAllNonPageable(), HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        //log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

        // Forma 2 de enviar http status code
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostDto animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutDto animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}