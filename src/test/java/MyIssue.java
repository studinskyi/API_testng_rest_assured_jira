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
    String issueId = "";
    IssueAPI issueAPI = null;

    //Response response;

    List<String> stringList = null;
    JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();

    @Test(groups = {"Issue", "Search", "Comment"})
    //@BeforeTest(groups = {"Issue", "Search", "Comment"})
    public void login() {
        issueAPI = new IssueAPI();
        issueAPI.loginAPI();
        sessionID = issueAPI.getRequestSender().extractResponseByPath("session.value");
        assertTrue(issueAPI.getRequestSender().response.getStatusCode() == 200);
        //        assertTrue(response.path("session") != null);
        assertNotNull(sessionID);

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("Log in - thread id: " + Thread.currentThread().getId());
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

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void createIssuePositive_statusCode201() {
        //String issueId = null;

        // подготовка JSON текста тела запроса body
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String bodyIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(bodyIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);

        // проверка ответа от сервера после создания задачи
        Response responseСreate = issueAPI.getRequestSender().response;
        //keyIssue = responseСreate.getBody().jsonPath().get("key");
        Assert.assertEquals(responseСreate.getStatusCode(), 201);
        //assertEquals(responseСreate.statusCode(), 201);
        AssertJUnit.assertTrue(responseСreate.contentType().contains(ContentType.JSON.toString()));
        assertTrue(responseСreate.getBody().jsonPath().get("key").toString().contains("QAAUT-"));
        assertTrue(responseСreate.getBody().jsonPath().get("self").toString().contains("http://soft.it-hillel.com.ua:8080"));

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("createIssuePositive_statusCode201 - thread id: " + Thread.currentThread().getId());
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

    @Test(dependsOnMethods = {"login"})
    public void deleteIssueOnly_statusCode204() {
        //keyIssue = "QAAUT-1056";
        System.out.println("for delet keyIssue = " + keyIssue);

        // подготовка JSON текста тела запроса body
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String bodyIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(bodyIssue, keyIssue);

        // проверка ответа от сервера после создания задачи
        Response responseDelete = issueAPI.getRequestSender().response;
        assertEquals(responseDelete.statusCode(), 204);
        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("deleteIssueOnly_statusCode204 - thread id: " + Thread.currentThread().getId());
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


    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void deleteIssuePositive_statusCode204() {
        //        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        //        System.out.println(keyIssue + " key Issue before test");
        createIssuePositive_statusCode201();
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " key Issue deleted test");

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("deleteIssuePositive_statusCode204 - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void getIssue_statusCode200() {
        // создание Issue
        createIssuePositive_statusCode201();

        // получение Issue по ключу задачи keyIssue
        issueAPI.getIssue(issueId);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        AssertJUnit.assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
        System.out.println("get issue keyIssue = " + keyIssue);

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("getIssue_statusCode200 - thread id: " + Thread.currentThread().getId());
    }


    @Test(groups = {"Search"}, dependsOnMethods = {"login"})
    public void searchIssue_statusCode200() {
        //        // подготовка JSON текста тела запроса body
        //        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        //        String bodyIssue = jiraJSONFixture.generateJSONForSampleIssue();
        //
        //        // создание задачи через выполнение метода объекта issueAPI
        //        issueAPI.createIssue(bodyIssue);
        //        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        //        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        //        System.out.println("new issueId = " + issueId);
        //        System.out.println("new keyIssue = " + keyIssue);

        // создание Issue
        createIssuePositive_statusCode201();

        // поиск Issue по значению ключа задачи keyIssue через строку запроса "jql:id = " + issueId
        System.out.println("looking for issue for the keyIssue : " + keyIssue);
        String  Jira_searchFixture = jiraJSONFixture.generateJSONForSearchFilter(keyIssue);
        issueAPI.search(Jira_searchFixture);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("searchIssue_statusCode200 - thread id: " + Thread.currentThread().getId());
    }


    @Test
    public void taskToSubTask() {
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
    public void addVote() {
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
    public void getVote() {
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
    public void remoteVote() {
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
    public void addComment201() {
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
    public void addComment400() {
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
    public void getComment200() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        get("/rest/api/2/issue/" + keyIssue + "/comment");


        stringList = from(response.asString()).getList("comments.id");

        System.out.println(stringList.get(stringList.size() - 1));

        System.out.println(response.asString());
        System.out.println(response.getBody().jsonPath().get("comments.body").toString());
        System.out.println(response.getBody().jsonPath().get("comments.id").toString());
        assertTrue(response.getStatusCode() == 200);
    }

    @Test
    public void getComment404() {
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
    public void deleteComment204() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";

        keyIssue = "QA-1148";
        getComment200();

        Response response =
                given().
                        contentType("application/json").
                        cookie("JSESSIONID=" + sessionID).
                        delete("/rest/api/2/issue/" + keyIssue + "/comment/" + stringList.get(stringList.size() - 1));


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
