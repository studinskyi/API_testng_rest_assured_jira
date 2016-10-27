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
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("Log in - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = {"Issue"})
    //@Test(enabled = false)
    public void loginNegative() {
        issueAPI = new IssueAPI();
        issueAPI.loginAPI_Negative();
        assertTrue(issueAPI.getRequestSender().response.getStatusCode() != 200);
    }

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
            Thread.sleep(200);
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
        //keyIssue = "QAAUT-1156";
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
            Thread.sleep(200);
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
        // создание Issue
        createIssuePositive_statusCode201();

        // удаление Issue
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " key Issue deleted test");

        // ожидание после выполнения
        try {
            Thread.sleep(200);
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
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("getIssue_statusCode200 - thread id: " + Thread.currentThread().getId());
    }


    @Test(groups = {"Search"}, dependsOnMethods = {"login"})
    public void searchIssue_statusCode200() {
        // создание Issue
        createIssuePositive_statusCode201();

        // поиск Issue по значению ключа задачи keyIssue через строку запроса "jql:id = " + issueId
        System.out.println("looking for issue for the keyIssue : " + keyIssue);
        String Jira_searchFixture = jiraJSONFixture.generateJSONForSearchFilter(keyIssue);
        issueAPI.search(Jira_searchFixture);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("searchIssue_statusCode200 - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = {"Comment"}, dependsOnMethods = {"login"})
    public void addComment_statusCode201() {
        // создание Issue
        createIssuePositive_statusCode201();

        // добавление комментария в Issue
        String Jira_commentFixture = jiraJSONFixture.generateJSONForAddComment();
        issueAPI.addComment(Jira_commentFixture, keyIssue);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 201);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("addComment_statusCode201 - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = {"Comment"}, dependsOnMethods = {"login"})
    public void deleteComment_statusCode204() {
        // создание Issue
        createIssuePositive_statusCode201();

        // добавление комментария в Issue
        String Jira_commentFixture = jiraJSONFixture.generateJSONForAddComment();
        issueAPI.addComment(Jira_commentFixture, keyIssue);
        String idComment = issueAPI.getRequestSender().extractResponseByPath("id");
        System.out.println("id new comment in created issue = " + idComment);

        // удаление комментария в Issue
        issueAPI.deleteComment(keyIssue, idComment);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 204);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("deleteComment_statusCode204 - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void changeTypeIssue_statusCode204() {
        // создание Issue
        createIssuePositive_statusCode201();

        //измение типа issue
        String Jira_SetTypeIssueFixture = jiraJSONFixture.generateJSONForSetTypeIssue("10003");
        issueAPI.changeTypeIssue(Jira_SetTypeIssueFixture, keyIssue);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 204);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        // удаление задачи
        deleteIssueOnly_statusCode204();
        System.out.println(keyIssue + " issue was deleted");

        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("changeTypeIssue_statusCode204 - thread id: " + Thread.currentThread().getId());
    }
    //    @Test
    //    public void changeTypeIssue_old() {
    //
    //        String keyIssue = "QAAUT-202";
    //        //String IdIssue = "13306";
    //        //login();
    //        System.out.println("session ID = " + sessionId);
    //
    //        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080/";
    //        String issue_type = "10004";
    //
    //        given()
    //                .contentType("application/json")
    //                .cookie("JSESSIONID=" + sessionId)
    //                .body("{\"fields\": \t{\"issuetype\": {\"id\": \"" + issue_type + "\"}}}")
    //                .put("/rest/api/2/issue/" + keyIssue)
    //                .then()
    //                .assertThat()
    //                .statusCode(204);
    //        //        statusCode(200).body("session.value");
    //
    //        System.out.println("key new issue = " + keyIssue);
    //        //System.out.println(given().when().get("/apps").asString());
    //
    //    }

}
