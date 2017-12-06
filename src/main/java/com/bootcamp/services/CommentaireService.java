package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.entities.Commentaire;
import org.springframework.stereotype.Component;

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
    
    public List<Commentaire> getByEntity(int entityId, EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CommentaireCRUD.read(criterias);
    }
}
