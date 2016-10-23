import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import fixtures.JiraJSONFixture;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.IssueAPI;
import utils.RequestSender;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MyIssue {

    String sessionID = "";
    String keyIssue = "";
    IssueAPI issueAPI = null;

    Response response;

    List<String> stringList = null;
    JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();

    @BeforeTest
    //@Test
    public void login(){
        //RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        //String loginBody = jiraJSONFixture.generateJSONForLogin();
        //RequestSender requestSender = new RequestSender();
        //requestSender.authenticate();
        issueAPI = new IssueAPI();
        issueAPI.loginAPI();
        sessionID = issueAPI.getRequestSender().extractResponseByPath("session.value");
        //        assertTrue(response.getStatusCode() == 200);
        //        assertTrue(response.path("session") != null);
        assertNotNull(sessionID);
    }
    //    public void login_old(){
    //        //RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    //        //String loginBody = jiraJSONFixture.generateJSONForLogin();
    //        RequestSender requestSender = new RequestSender();
    //        requestSender.authenticate();
    //
    //        sessionID = requestSender.extractResponseByPath("session.value");
    //        //        assertTrue(response.getStatusCode() == 200);
    //        //        assertTrue(response.path("session") != null);
    //        //assertNotNull(response.path("session"));
    //        assertNotNull(sessionID);
    //    }

    @Test
    public void createIssuePositive_statusCode201(){
        String issueId = null;

        // подготовка JSON текста тела запроса body
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String bodyIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(bodyIssue);

        // проверка ответа от сервера после создания задачи
        Response responseСreate = issueAPI.getRequestSender().response;
        assertEquals(responseСreate.statusCode(), 201);
        AssertJUnit.assertTrue(responseСreate.contentType().contains(ContentType.JSON.toString()));

        keyIssue = responseСreate.getBody().jsonPath().get("key");

        // проверка ответа от сервера
        Assert.assertEquals(responseСreate.getStatusCode(),201);
        //assertEquals(responseСreate.statusCode(), 201);
        AssertJUnit.assertTrue(responseСreate.contentType().contains(ContentType.JSON.toString()));
        assertTrue(responseСreate.getBody().jsonPath().get("key").toString().contains("QAAUT-"));
        assertTrue(responseСreate.getBody().jsonPath().get("self").toString().contains("http://soft.it-hillel.com.ua:8080"));
    }
    //    public void createIssuePositive_statusCode201_old(){
    //        //RestAssured.baseURI = "https://forapitest.atlassian.net";
    //        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    //        String body1 = jiraJSONFixture.generateJSONForSampleIssue();
    //
    //        Response response = given().
    //                contentType("application/json").
    //                cookie("JSESSIONID=" + sessionID).
    //                body(body1).
    //                when().
    //                post("/rest/api/2/issue");
    //
    //        keyIssue = response.getBody().jsonPath().get("key");
    //
    //        Assert.assertEquals(response.getStatusCode(),201);
    //        //assertTrue(response.getStatusCode() == 201);
    //        //        assertTrue(response.getBody().jsonPath().get("key").toString().contains("TES-"));
    //        //        assertTrue(response.getBody().jsonPath().get("self").toString().contains("https://forapitest.atlassian.net"));
    //        assertTrue(response.getBody().jsonPath().get("key").toString().contains("QAAUT-"));
    //        assertTrue(response.getBody().jsonPath().get("self").toString().contains("http://soft.it-hillel.com.ua:8080"));
    //    }


    @Test
    public void deleteIssueOnly_statusCode204() {
        keyIssue = "QAAUT-1019";
        System.out.println("keyIssue = " + keyIssue);

        // подготовка JSON текста тела запроса body
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String bodyIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(bodyIssue,keyIssue);

        // проверка ответа от сервера после создания задачи
        Response responseDelete = issueAPI.getRequestSender().response;
        assertEquals(responseDelete.statusCode(), 204);
        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));

        //        given().
        //                contentType("application/json").
        //                cookie("JSESSIONID=" + sessionID).
        //                when().
        //                delete("/rest/api/2/issue/" + keyIssue).
        //                then().
        //                statusCode(204);
    }
    //    public void deleteIssueOnly_statusCode204_old() {
    //        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    //        //        System.out.println(keyIssue + " first");
    //        //        createIssuePositive_statusCode201();
    //        //        System.out.println(keyIssue + " second");
    //
    //        //keyIssue = "QAAUT-672";
    //        //keyIssue = keyLocal;
    //
    //        System.out.println("key=" + keyIssue);
    //        given().
    //                contentType("application/json").
    //                cookie("JSESSIONID=" + sessionID).
    //                when().
    //                delete("/rest/api/2/issue/" + keyIssue).
    //                then().
    //                statusCode(204);
    //    }


    @Test
    public void deleteIssuePositive_status204() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        System.out.println(keyIssue + " key Issue before test");

        createIssuePositive_statusCode201();
        System.out.println(keyIssue + " key Issue created test");
        //        //System.out.println("key=" + keyIssue);

        deleteIssueOnly_statusCode204();

        //        given().
        //                contentType("application/json").
        //                cookie("JSESSIONID=" + sessionID).
        //                when().
        ////                delete("/rest/api/2/issue/QAAUT-145").
        //        delete("/rest/api/2/issue/" + keyIssue).
        //                then().
        //                statusCode(204);
    }

    @Test
    public void createIssueNegative400(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        String body1 = " {\n" +
                "\n" +
                " \"fields\": {\n" +
                "  \"project\": {\n" +
                "   \"id\": \"103151\"\n" +
                "  },\n" +
                "  \"summary\": \"rest_test\",\n" +
                "  \"issuetype\": {\n" +
                "   \"id\": \"10004\"\n" +
                "  },\n" +
                "  \"assignee\": {\n" +
                "   \"name\": \"r.polunov111\"\n" +
                "  },\n" +
                "  \"reporter\": {\n" +
                "   \"name\": \"r.polunov\"\n" +
                "  }\n" +
                " }\n" +
                "}";

        given().
                contentType("application/json").
                cookie("JSESSIONID="+sessionID).
                body(body1).
                when().
                post("/rest/api/2/issue").
                then().
                statusCode(400);
    }


    @Test
    public void deleteIssuePositive204() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        System.out.println(keyIssue + " first");

        createIssuePositive_statusCode201();

        System.out.println(keyIssue + " second");

        System.out.println("key=" + keyIssue);
        given().
                contentType("application/json").
                cookie("JSESSIONID=" + sessionID).
                when().
//                delete("/rest/api/2/issue/QAAUT-145").
        delete("/rest/api/2/issue/" + keyIssue).
                then().
                statusCode(204);
    }


    @Test
    public void deleteGroupIssuePositive204(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

            FilterIssue();

        System.out.println(stringList);
        for (String s : stringList) {
            System.out.println("key=" +  s);
            given().
                    contentType("application/json").
                    cookie("JSESSIONID=" + sessionID).
                    when().
//                delete("/rest/api/2/issue/QAAUT-145").
        delete("/rest/api/2/issue/"+s).
                    then().
                    statusCode(204);
        }
        }


    @Test
    public void deleteIssueNegative401(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        given().
                contentType("application/json").
                cookie("JSESSIONID=" + "1").
                when().
                delete("/rest/api/2/issue/").
                then().
                statusCode(401);
    }

    @Test
    public void deleteIssueNegative404(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        given().
                contentType("application/json").
                cookie("JSESSIONID=" + sessionID).
                when().
                delete("/rest/api/2/issue/QAAUT-145").
                then().
                statusCode(404);
    }



    @Test
    public void FilterIssue(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        Response response = given().
                contentType("application/json").
                cookie("JSESSIONID=" + sessionID).
                body("{\n" +
                        "    \"jql\": \"project = QAAUT and reporter = r.polunov\",\n" +
                        "    \"startAt\": 0,\n" +
                        "    \"maxResults\": 3,\n" +
                        "    \"fields\": [\n" +
                        "        \"key\" \n" +
                        "    ]\n" +
                        "}").
                post("/rest/api/2/search");

        String rez = response.asString();

        System.out.println(rez);


//        List<String> stringList = response.path("key");
        stringList = from(rez).getList("issues.key");

//        Person[] persons = given().when().get("person/").as(Person[].class);

        for (String s2 : stringList) {
            System.out.println(s2);
        }
    }


    @Test
    public void taskToSubTask(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        Response response =
        given().
                contentType("application/json").
                cookie("JSESSIONID=" + sessionID).
                body("{\"fields\":\n" +
                        "   {\"parent\":{\"id\":\"QAAUT-133\"},\n" +
                        "   \"project\":{\"id\":\"10315\"},\n" +
                        "   \"issuetype\":{\"id\":\"10003\"}}\n" +
                        "}").
                put("/rest/api/2/issue/QAAUT-255");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 204);

    }


    @Test
    public void addVote(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-138";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        post("/rest/api/2/issue/" + keyIssue + "/votes");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 204);

    }


    @Test
    public void getVote(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-138";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        get("/rest/api/2/issue/" + keyIssue + "/votes");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 200);
        assertTrue(response.getBody().jsonPath().get("voters.displayName").toString().equals("[r.polunov]"));
    }

    @Test
    public void remoteVote(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-138";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        delete("/rest/api/2/issue/" + keyIssue + "/votes");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 204);
    }

    @Test
    public void addComment201(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        body("{\n" +
                                "    \"body\": \"new comment via API\"\n" +
                                "}").
                        post("/rest/api/2/issue/" + keyIssue + "/comment");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 201);
    }

    @Test
    public void addComment400(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        body("{\n" +
                                "    \"body1\": \"new comment via API\"\n" +
                                "}").
                        post("/rest/api/2/issue/" + keyIssue + "/comment");

        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 400);
    }

    @Test
    public void getComment200(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        get("/rest/api/2/issue/" + keyIssue + "/comment");



        stringList = from(response.asString()).getList("comments.id");

        System.out.println(stringList.get(stringList.size()-1));

        System.out.println(response.asString());
        System.out.println(response.getBody().jsonPath().get("comments.body").toString());
        System.out.println(response.getBody().jsonPath().get("comments.id").toString());
        assertTrue(response.getStatusCode() == 200);
    }

    @Test
    public void getComment404(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-9999999";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        get("/rest/api/2/issue/" + keyIssue + "/comment");


        System.out.println(response.asString());
        assertTrue(response.getStatusCode() == 404);
        assertTrue(response.getBody().jsonPath().get("errorMessages").toString().contains("Issue Does Not Exist"));
    }

    @Test
    public void deleteComment204(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";
        getComment200();

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        delete("/rest/api/2/issue/" + keyIssue + "/comment/" + stringList.get(stringList.size()-1));


//        System.out.println(response.getBody().jsonPath().get("comments.body").toString());
        assertTrue(response.getStatusCode() == 204);
    }

//    @Test
//    public void taskToSubTask2(){
//        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
//
//        Response response =
//        given().
//                contentType("application/json").
//                cookie("JSESSIONID=" + sessionID).
//                body("{\"fields\":\n" +
//                        "   {\"parent\":{\"id\":\"QAAUT-133\"},\n" +
//                        "   \"project\":{\"id\":\"10315\"},\n" +
//                        "   \"issuetype\":{\"id\":\"10002\"}}\n" +
//                        "}").
//                get("/rest/api/2/issue/QAAUT-256/editmeta");
//
//        System.out.println(response.asString());
//
//    }
}
