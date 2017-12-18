package com.bootcamp.integration;

import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.Commentaire;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jayway.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <h2> The integration test for Commentaire controller</h2>
 * <p>
 * In this test class, the methods :
 * <ul>
 * <li>create a commentaire </li>
 * <li>get one commentaire by it's id</li>
 * <li>get all commentaire</li>
 * <li>And update a commentaire have been implemented</li>
 * </ul>
 * before getting started , make sure , the commentaire fonctionnel service is
 * deploy and running as well. you can also test it will the online ruuning
 * service As this test interact directly with the local database, make sure
 * that the specific database has been created and all it's tables. If you have
 * data in the table,make sure that before creating a data with it's id, do not
 * use an existing id.
 * </p>
 */
public class CommentaireControllerIntegrationTest {

    private static Logger logger = LogManager.getLogger(CommentaireControllerIntegrationTest.class);

    /**
     * The Base URI of commentaire fonctionnal service, it can be change with
     * the online URIof this service.
     */
    private String BASE_URI = "http://localhost:8083/commentaire";

    /**
     * The path of the Commentaire controller, according to this controller
     * implementation
     */
    private String COMMENTAIRE_PATH = "/commentaires";

    /**
     * This ID is initialize for create , getById, and update method, you have
     * to change it if you have a save data on this ID otherwise a error or
     * conflit will be note by your test.
     */
    private int commentaireId = 0;


    /* @BeforeTest
    public void count() throws Exception{
       int totalData = new CommentaireService().getCountCommentaires();
       commentaireId=totalData;
       logger.info( commentaireId );
   }*/
    /**
     * This method create a new commentaire with the given id
     *
     * @see Commentaire#id
     * <b>you have to chenge the name of the commentaire if this name already
     * exists in the database
     * @see Commentaire#getContenu() else, the commentaire will be created but not
     * wiht the given ID. and this will accure an error in the getById and
     * update method</b>
     * Note that this method will be the first to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 0, groups = {"CommentaireTest"})
    public void createCommentaire() throws Exception {
        String createURI = BASE_URI + COMMENTAIRE_PATH;
        Commentaire commentaire = getCommentaireById(1);
        commentaire.setId(commentaireId);
        commentaire.setContenu("commentaire test after the doc");
        Gson gson = new Gson();
        String commentaireData = gson.toJson(commentaire);
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(commentaireData)
                .expect()
                .when()
                .post(createURI);

        commentaireId = gson.fromJson(response.getBody().print(), Commentaire.class).getId();

        logger.debug(commentaireId);
        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * This method get a commentaire with the given id
     *
     * @see Commentaire#id
     * <b>
     * If the given ID doesn't exist it will log an error
     * </b>
     * Note that this method will be the second to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 1, groups = {"CommentaireTest"})
    public void getCommentaireById() throws Exception {

        String getCommentaireById = BASE_URI + COMMENTAIRE_PATH + "/" + commentaireId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getCommentaireById);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get the statistics of the given entity type
     * <b>
     * the comments must exist in the database
     * </b>
     * Note that this method will be the third to execute If every done , it
     * will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 2, groups = {"CommentaireTest"})
    public void statsCommentaire() throws Exception {
        String statsURI = BASE_URI + COMMENTAIRE_PATH + "/stats/PROJET";
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(statsURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the commentaires in the database If every done , it will return a
     * 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 3, groups = {"CommentaireTest"})
    public void getAllCommentaires() throws Exception {
        String getAllCommentaireURI = BASE_URI + COMMENTAIRE_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getAllCommentaireURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Delete a commentaire for the given ID will return a 200 httpStatus code
     * if OK
     *
     * @throws Exception
     */
    @Test(priority = 4, groups = {"CommentaireTest"})
    public void deleteCommentaire() throws Exception {

        String deleteCommentaireUI = BASE_URI + COMMENTAIRE_PATH + "/" + commentaireId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .delete(deleteCommentaireUI);

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the commentaires related to a specify entity in the database If
     * every done , it will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 5, groups = {"CommentaireTest"})
    public void getCommentairesByEntity() throws Exception {
        String getCommentairesByEntityURI = BASE_URI + COMMENTAIRE_PATH + "/PROJET/7";
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getCommentairesByEntityURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Convert a relative path file into a File Object type
     *
     * @param relativePath
     * @return File
     * @throws Exception
     */
    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    /**
     * Get on commentaire by a given ID from the List of commentaires
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Commentaire getCommentaireById(int id) throws Exception {
        List<Commentaire> commentaires = loadDataCommentaireFromJsonFile();
        Commentaire commentaire = commentaires.stream().filter(item -> item.getId() == id).findFirst().get();

        return commentaire;
    }

    /**
     * Convert a commentaires json data to a commentaire objet list this json
     * file is in resources
     *
     * @return a list of commentaire in this json file
     * @throws Exception
     */
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
