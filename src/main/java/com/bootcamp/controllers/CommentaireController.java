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

/**
 *
 * @author Bello
 */
@RestController("CommentaireController")
@RequestMapping("/commentaires")
@Api(value = "Commentaire API", description = "Commentaire API")
@CrossOrigin(origins = "*")
public class CommentaireController {

    @Autowired
    CommentaireService commentaireService;

    @Autowired
    HttpServletRequest request;

    /**
     * Create a new comment in the database
     *
     * @param comment
     * @return comment
     */
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

    /**
     * Get a comment by its id
     *
     * @param id
     * @return comment
     */
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

    /**
     * Count all the comments of a given entity type
     *
     * @param entityType
     * @return count
     */
    @RequestMapping(method = RequestMethod.GET, value = "/stats/{entityType}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read all comments for entity", notes = "Read all comments for entity")
    public ResponseEntity<Integer> readCommentByEntity(@PathVariable("entityType") String entityType) {
        EntityType entite = EntityType.valueOf(entityType);
        int nbCommentaire = 0;
        HttpStatus httpStatus = null;

        try {
            nbCommentaire = commentaireService.getAllCommentByEntity(entite);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Integer>(nbCommentaire, httpStatus);

    }

    /**
     * Get all the comments of a given entity
     *
     * @param entityType
     * @param entityId
     * @return comments list
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
    public ResponseEntity<List<Commentaire>> readByEntity(@PathVariable("entityType") String entityType, @PathVariable("entityId") int entityId) {
        EntityType entite = EntityType.valueOf(entityType);
        List<Commentaire> commentaire = new ArrayList<Commentaire>();
        HttpStatus httpStatus = null;

        try {
            commentaire = commentaireService.getByEntity(entite, entityId);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<Commentaire>>(commentaire, httpStatus);
    }

    /**
     * Get all the comments of the database
     *
     * @return comments list
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get list of commentaire by request", notes = "Get list of commentaires by request")
    public ResponseEntity<List<Commentaire>> findAll() throws Exception {
        HttpStatus httpStatus = null;
        List<Commentaire> commentaires = commentaireService.readAll(request);
        httpStatus = HttpStatus.OK;
        return new ResponseEntity<List<Commentaire>>(commentaires, httpStatus);
    }

    /**
     * Delete a comment by its id
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "delete commentaire ", notes = "delete commentaire by id")
    public ResponseEntity<Commentaire> delete(@PathVariable int id) throws Exception {
        HttpStatus httpStatus = null;
        Commentaire commentaire = commentaireService.delete(id);
        httpStatus = HttpStatus.OK;
        return new ResponseEntity<Commentaire>(commentaire, httpStatus);
    }

}
