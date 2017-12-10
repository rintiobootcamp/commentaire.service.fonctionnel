package com.bootcamp.controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.utils.GsonUtils;

import com.bootcamp.entities.Commentaire;
import com.bootcamp.entities.Projet;
import com.bootcamp.entities.Secteur;
import com.bootcamp.services.CommentaireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 *
 * Created by Ibrahim on 12/5/17.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = CommentaireController.class, secure = false)
@ContextConfiguration(classes={Application.class})
public class CommentaireControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentaireService commentaireService;


    @Test
    public void getCommentaires() throws Exception{
        List<Commentaire> commentaires =  loadDataCommentaireFromJsonFile();
        System.out.println(commentaires.size());
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(commentaireService.readAll(Mockito.any(HttpServletRequest.class))).thenReturn(commentaires);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/commentaires")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }

    @Test
    public void getCommentairesByEntity() throws Exception{
        List<Commentaire> commentaires =  loadDataCommentaireFromJsonFile();
        EntityType pro = EntityType.SECTEUR;
        int id = 2;
        System.out.println(commentaires.size());
        List<Commentaire> resultcomm = getCommentairesByEnity("SECTEUR",id);
        Mockito.
        when(commentaireService.getByEntity(pro,id)).thenReturn(resultcomm);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/commentaires/{entityType}/{entityId}",pro,id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }

    @Test
    public void getCommentaireByIdForController() throws Exception{
        int id = 1;
        Commentaire commentaire = getCommentaireById(id);

        when(commentaireService.read(id)).thenReturn(commentaire);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/commentaires/{id}",id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get a commentaire by id in commentaire controller done *******************");

    }

    @Test
    public void CreateCommentaire() throws Exception{
        Commentaire commentaire = getCommentaireById(2);

        when(commentaireService.create(commentaire)).thenReturn(commentaire);

        RequestBuilder requestBuilder =
                post("/commentaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(commentaire));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for create commentaire in commentaire controller done *******************");

    }


    @Test
    public void deleteCommentaire() throws Exception{
        int id = 5;
        Commentaire commentaire = getCommentaireById(id);
        when(commentaireService.exists(id)).thenReturn(true);
        when(commentaireService.delete(id)).thenReturn(commentaire);

        RequestBuilder requestBuilder =
                delete("/commentaires/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for delete commentaire in commentaire controller done *******************");


    }
    private List<Commentaire> getCommentairesByEnity(String entityType,int entityId) throws Exception {
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        List<Commentaire> result = commentaires.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId).collect(Collectors.toList());

        return result;
    }

    private Commentaire getCommentaireById(int id) throws Exception {
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        Commentaire commentaire = commentaires.stream().filter(item -> item.getId() == id).findFirst().get();
        return commentaire;
    }

    public static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if(!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    public  List<Projet> getProjectsFromJson() throws Exception {
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

    private Projet getProjetById(int id) throws Exception {
        List<Projet> projets = loadDataProjetFromJsonFile();
        Projet projet = projets.stream().filter(item->item.getId()==id).findFirst().get();

        return projet;
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

    public List<Commentaire> loadDataCommentaireFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "commentaires.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Commentaire>>() {
        }.getType();
        List<Commentaire> commentaires = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return commentaires;
    }
}