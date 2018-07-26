package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Commentaire;
import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class CommentaireService implements DatabaseConstants {
ElasticClient elasticClient;
@PostConstruct
public void CommentaireService(){
    elasticClient = new ElasticClient();
}
    /**
     * Insert a comment in the database
     *
     * @param commentaire
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire create(Commentaire commentaire) throws SQLException,Exception {
        commentaire.setDateCreation(System.currentTimeMillis());
        CommentaireCRUD.create(commentaire);
        createAllIndexCommentaire();
        return commentaire;
    }

    /**
     * Update a comment in the database
     *
     * @param commentaire
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire update(Commentaire commentaire) throws SQLException,Exception {
        commentaire.setDateMiseAJour(System.currentTimeMillis());
        CommentaireCRUD.update(commentaire);
        createAllIndexCommentaire();
        return commentaire;
    }

    /**
     * Delete a comment in the database by its id
     *
     * @param id
     * @return the inserted comment
     * @throws SQLException
     */
    public Commentaire delete(int id) throws Exception {
        Commentaire commentaire = read(id);
        CommentaireCRUD.delete(commentaire);
        createAllIndexCommentaire();
        return commentaire;
    }

    /**
     * Get a comment in the database by its id
     *
     * @param id
     * @return comment
     * @throws SQLException
     */
    public Commentaire read(int id) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", id));
//        List<Commentaire> commentaires = CommentaireCRUD.read(criterias);
//        return commentaires.get(0);
        return getAllCommentaire().stream().filter(t->t.getId()==id).findFirst().get();
    }

    /**
     * Get all the comments of the given entity in the database
     *
     * @param entityType
     * @param entityId
     * @return comments list
     * @throws SQLException
     */
    public List<Commentaire> getByEntity(EntityType entityType, int entityId) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), null));
//        return CommentaireCRUD.read(criterias);
        return getAllCommentaire().stream().filter(t->t.getEntityType().equals(entityType) && t.getEntityId()==entityId).collect(Collectors.toList());
    }

    public List<Commentaire> getCommentByEntity(EntityType entityType) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        return CommentaireCRUD.read(criterias);
        return getAllCommentaire().stream().filter(t->t.getEntityType().equals(entityType)).collect(Collectors.toList());
    }

    public List<Commentaire> getCommentByEntity(EntityType entityType, String startDate, String endDate) throws SQLException, ParseException {
        EntityManager em = Persistence.createEntityManagerFactory(DatabaseConstants.PERSISTENCE_UNIT).createEntityManager();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        long dateDebut = formatter.parse(startDate).getTime();
        long dateFin = formatter.parse(endDate).getTime();
        TypedQuery<Commentaire> query = em.createQuery(
                "SELECT e FROM Commentaire e WHERE e.entityType =?1 AND e.dateCreation BETWEEN ?2 AND ?3", Commentaire.class);
        List<Commentaire> commentaires = query.setParameter(1, entityType.name())
                                              .setParameter(2, dateDebut)
                                              .setParameter(3, dateFin)
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
    public List<Commentaire> readAll(HttpServletRequest request) throws SQLException, Exception, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Commentaire> commentaires = null;
        if (criterias == null && fields == null) {
            commentaires = getAllCommentaire();
        } else if (criterias != null && fields == null) {
            commentaires = CommentaireCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            commentaires = CommentaireCRUD.read(fields);
        } else {
            commentaires = CommentaireCRUD.read(criterias, fields);
        }

        return commentaires;
    }

    public List<Commentaire> getAllCommentaire() throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("commentaires");
        ModelMapper modelMapper = new ModelMapper();
        List<Commentaire> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Commentaire.class));
        }
        return rest;
    }

    public boolean createAllIndexCommentaire()throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Commentaire> commentaires = CommentaireCRUD.read();
        for (Commentaire commentaire : commentaires){
            elasticClient.creerIndexObjectNative("commentaires","commentaire",commentaire,commentaire.getId());
//            LOG.info("Commentaire "+commentaire.getId()+" created");
        }
        return true;
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
    public int getAllCommentByEntity(EntityType entityType) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        return CommentaireCRUD.read(criterias).size();
        return getAllCommentaire().size();
    }

}
