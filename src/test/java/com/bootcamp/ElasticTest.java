package com.bootcamp;

import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Commentaire;
import com.rintio.elastic.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class ElasticTest {
    private final Logger LOG = LoggerFactory.getLogger(ElasticTest.class);


    @Test
    public void createIndexCommentaire()throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Commentaire> commentaires = CommentaireCRUD.read();
        for (Commentaire commentaire : commentaires){
            elasticClient.creerIndexObjectNative("commentaires","commentaire",commentaire,commentaire.getId());
            LOG.info("Commentaire "+commentaire.getId()+" created");
        }
    }
}
