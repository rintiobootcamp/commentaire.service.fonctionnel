package com.bootcamp.controllers;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.entities.Commentaire;
import com.bootcamp.services.CommentaireService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController("CommentaireController")
@RequestMapping("/commentaires")
@Api(value = "Commentaire API", description = "Commentaire API")
@CrossOrigin(origins = "*")
public class CommentaireController {

    @Autowired
    CommentaireService commentaireService;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a new commentaire", notes = "Create a new commentaire")
    public ResponseEntity<Commentaire> create(@RequestBody @Valid Commentaire comment) {

        HttpStatus httpStatus = null;

        try {
            comment = commentaireService.create(comment);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<Commentaire>(comment, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
    public ResponseEntity<Commentaire> read(@PathVariable(name = "id") int id) {

        Commentaire commentaire = new Commentaire();
        HttpStatus httpStatus = null;

        try {
            commentaire = commentaireService.read(id);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Commentaire>(commentaire, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
    public ResponseEntity<List<Commentaire>> readByEntity(@PathVariable("entityId") int entityId, @PathVariable("entityType") String entityType) {
        EntityType entite = EntityType.valueOf(entityType);
        List<Commentaire> commentaire = new ArrayList<Commentaire>();
        HttpStatus httpStatus = null;

        try {
            commentaire = commentaireService.getByEntity(entityId, entite);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<Commentaire>>(commentaire, httpStatus);
    }
}
