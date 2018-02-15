package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.entities.Commentaire;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class CommentaireService implements DatabaseConstants {

    /**
     * Insert a comment in the database
     *
     * @param commentaire
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire create(Commentaire commentaire) throws SQLException {
        commentaire.setDateCreation(System.currentTimeMillis());
        CommentaireCRUD.create(commentaire);
        return commentaire;
    }

    /**
     * Update a comment in the database
     *
     * @param commentaire
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire update(Commentaire commentaire) throws SQLException {
        commentaire.setDateMiseAJour(System.currentTimeMillis());
        CommentaireCRUD.update(commentaire);
        return commentaire;
    }

    /**
     * Delete a comment in the database by its id
     *
     * @param id
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire delete(int id) throws SQLException {
        Commentaire commentaire = read(id);
        CommentaireCRUD.delete(commentaire);
        return commentaire;
    }

    /**
     * Get a comment in the database by its id
     *
     * @param id
     * @return comment
     * @throws SQLException
     */
    public Commentaire read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Commentaire> commentaires = CommentaireCRUD.read(criterias);
        return commentaires.get(0);
    }

    /**
     * Get all the comments of the given entity in the database
     *
     * @param entityType
     * @param entityId
     * @return comments list
     * @throws SQLException
     */
    public List<Commentaire> getByEntity(EntityType entityType, int entityId) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), null));
        return CommentaireCRUD.read(criterias);
    }

    public List<Commentaire> getCommentByEntity(EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CommentaireCRUD.read(criterias);
    }

    public List<Commentaire> getCommentByEntity(EntityType entityType, String startDate, String endDate) throws SQLException, ParseException {
        EntityManager em = Persistence.createEntityManagerFactory(DatabaseConstants.PERSISTENCE_UNIT).createEntityManager();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        long dateDebut = formatter.parse(startDate).getTime();
        long dateFin = formatter.parse(endDate).getTime();
        TypedQuery<Commentaire> query = em.createQuery(
                "SELECT e FROM Commentaire e WHERE e.dateCreation BETWEEN ?1 AND ?2", Commentaire.class);
        List<Commentaire> commentaires = query.setParameter(1, dateDebut)
                .setParameter(2, dateFin)
                .getResultList();
        return commentaires;
    }

    /**
     * Get all the comments of the database matching the given request
     *
     * @param request
     * @return comments list
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws DatabaseException
     * @throws InvocationTargetException
     */
    public List<Commentaire> readAll(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Commentaire> commentaires = null;
        if (criterias == null && fields == null) {
            commentaires = CommentaireCRUD.read();
        } else if (criterias != null && fields == null) {
            commentaires = CommentaireCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            commentaires = CommentaireCRUD.read(fields);
        } else {
            commentaires = CommentaireCRUD.read(criterias, fields);
        }

        return commentaires;
    }

    /**
     * Check if a comment exists
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean exists(int id) throws Exception {
        Commentaire commentaire = read(id);
        if (commentaire != null) {
            return true;
        }
        return false;
    }

    /**
     * Count all the comments of the given entity type
     *
     * @param entityType
     * @return count
     * @throws SQLException
     */
    public int getAllCommentByEntity(EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CommentaireCRUD.read(criterias).size();
    }

}
