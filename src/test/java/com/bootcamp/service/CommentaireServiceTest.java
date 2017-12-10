package com.bootcamp.service;

import com.bootcamp.application.Application;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.CommentaireCRUD;
import com.bootcamp.entities.*;
import com.bootcamp.services.CommentaireService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ibrahim on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = CommentaireService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(CommentaireCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class CommentaireServiceTest {
    private final Logger LOG = LoggerFactory.getLogger(CommentaireServiceTest.class);

    @InjectMocks
    private CommentaireService commentaireService;

    @Test
    public void getAllCommentaire() throws Exception {
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        PowerMockito.mockStatic(CommentaireCRUD.class);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.
                when(CommentaireCRUD.read()).thenReturn(commentaires);
        List<Commentaire> resultCommentaires = commentaireService.readAll(mockRequest);
        Assert.assertEquals(commentaires.size(), resultCommentaires.size());
        LOG.info(" get all commentaire test done");

    }

    @Test
    public void create() throws Exception{
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        Commentaire commentaire = commentaires.get(1);

        PowerMockito.mockStatic(CommentaireCRUD.class);
        Mockito.
                when(CommentaireCRUD.create(commentaire)).thenReturn(true);
    }

    @Test
    public void getByCriteria() throws Exception{
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", "PROJET"), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", 2), null));
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        List<Commentaire> commentaireList = getCommentaires("SECTEUR",2);

        PowerMockito.mockStatic(CommentaireCRUD.class);
        Mockito.
                when(CommentaireCRUD.read(criterias)).thenReturn(commentaireList);
        Gson gson = new Gson();
        for(Commentaire current:commentaireList){
            System.out.println(gson.toJson(current));
        }
       // commentaireList.forEach(System.out::print);
        //System.out.println(commentaireList);
    }

    @Test
    public void delete() throws Exception{
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        Commentaire commentaire = commentaires.get(1);

        PowerMockito.mockStatic(CommentaireCRUD.class);
        Mockito.
                when(CommentaireCRUD.delete(commentaire)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        Commentaire commentaire = commentaires.get(1);

        PowerMockito.mockStatic(CommentaireCRUD.class);
        Mockito.
                when(CommentaireCRUD.update(commentaire)).thenReturn(true);
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    public List<Projet> getProjectsFromJson() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "projets.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Projet>>() {
        }.getType();
        List<Projet> projets = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return projets;
    }

    public List<Secteur> loadDataSecteurFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "secteurs.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Secteur>>() {
        }.getType();
        List<Secteur> secteurs = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return secteurs;
    }

    public List<Commentaire> loadDataCommentaireFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "commentaires.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Commentaire>>() {
        }.getType();
        List<Commentaire> commentaires = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return commentaires;
    }

    private Secteur getSecteurById(int id) throws Exception {
        List<Secteur> secteurs = loadDataSecteurFromJsonFile();
        Secteur secteur = secteurs.stream().filter(item -> item.getId() == id).findFirst().get();

        return secteur;
    }

    private List<Commentaire> getCommentaires(String entityType,int entityId) throws Exception {
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        List<Commentaire> result = commentaires.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId).collect(Collectors.toList());

        return result;
    }

    public Axe getAxeById(int id) throws Exception {
        List<Axe> projets = loadDataAxeFromJsonFile();
        Axe projet = projets.stream().filter(item -> item.getId() == id).findFirst().get();

        return projet;
    }


    public List<Axe> loadDataAxeFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "projets.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Axe>>() {
        }.getType();
        List<Axe> projets = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        for (int i = 0; i < projets.size(); i++) {
            Axe projet = projets.get(i);
            List<Secteur> secteurs = new LinkedList();
            switch (i) {
                case 0:
                    secteurs.add(getSecteurById(8));
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    secteurs.add(getSecteurById(1));
                    secteurs.add(getSecteurById(2));
                    secteurs.add(getSecteurById(5));
                    secteurs.add(getSecteurById(9));
                    break;
                case 4:
                    secteurs.add(getSecteurById(3));
                    break;
                case 5:
                    secteurs.add(getSecteurById(8));
                    break;
                case 6:
                    secteurs.add(getSecteurById(6));
                    break;
            }
            projet.setSecteurs(secteurs);
        }

        return projets;
    }

    public List<Pilier> loadDataPilierFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "piliers.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Pilier>>() {
        }.getType();
        List<Pilier> piliers = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);
        for (int i = 0; i < piliers.size(); i++) {
            List<Axe> projets = new LinkedList();
            Pilier pilier = piliers.get(i);
            switch (i) {
                case 0:
                    projets.add(getAxeById(1));
                    projets.add(getAxeById(2));
                    break;
                case 1:
                    projets.add(getAxeById(3));
                    projets.add(getAxeById(4));
                    projets.add(getAxeById(5));
                    break;
                case 2:
                    projets.add(getAxeById(6));
                    projets.add(getAxeById(7));
                    break;
            }
            pilier.setAxes(projets);
        }

        return piliers;
    }


    public List<Projet> loadDataProjetFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "projets.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Projet>>() {
        }.getType();
        List<Projet> projets = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return projets;
    }

}