package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.crud.ProjetCRUD;
import com.bootcamp.entities.Commentaire;
import com.bootcamp.entities.Projet;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class CommentaireService implements DatabaseConstants {

    public Commentaire create(Commentaire commentaire) throws SQLException {
        commentaire.setDateCreation(System.currentTimeMillis());
        commentaire.setDateMiseAJour(System.currentTimeMillis());
        CommentaireCRUD.create(commentaire);
        return commentaire;
    }

    public Commentaire update(Commentaire commentaire) throws SQLException {
        CommentaireCRUD.update(commentaire);
        return commentaire;
    }

    public Commentaire delete(int id) throws SQLException {
        Commentaire commentaire = read(id);
        CommentaireCRUD.delete(commentaire);
        return commentaire;
    }

    public Commentaire read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Commentaire> commentaires = CommentaireCRUD.read(criterias);
        return commentaires.get(0);
    }
    
    public List<Commentaire> getByEntity(EntityType entityType,int entityId) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), null));
        return CommentaireCRUD.read(criterias);
    }

    public int getAllCommentByEntity(EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CommentaireCRUD.read(criterias).size();
    }

    public List<Commentaire> readAll(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Commentaire> commentaires = null;
        if(criterias == null && fields == null)
            commentaires =  CommentaireCRUD.read();
        else if(criterias!= null && fields==null)
            commentaires = CommentaireCRUD.read(criterias);
        else if(criterias== null && fields!=null)
            commentaires = CommentaireCRUD.read(fields);
        else
            commentaires = CommentaireCRUD.read(criterias, fields);

        return commentaires;
    }

    public boolean exists(int id) throws Exception{
        Commentaire commentaire = read(id);
        if(commentaire!=null)
            return true;
        return false;
    }
}
