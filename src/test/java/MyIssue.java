import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import fixtures.JiraJSONFixture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.IssueAPI;
import utils.RequestSender;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public class MyIssue {

    private String sessionID = "";
    private String keyIssue = "";
    private String issueId = "";
    private IssueAPI issueAPI = null;

    //Response response;
    public JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();

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

    //@Test(groups = {"Issue"})
    @Test(enabled = false)
    public void loginNegative() {
        issueAPI = new IssueAPI();
        issueAPI.loginAPI_Negative();
        assertTrue(issueAPI.getRequestSender().response.getStatusCode() != 200);
    }

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void createIssuePositive_statusCode201() {
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
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

        // удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("deleteIssue - thread id: " + Thread.currentThread().getId());

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
    public void deleteIssuesOnly() {
        //keyIssue = "QAAUT-1513";
        List<String> issuesToDelete = getIssuesToDelete();
        for (String elem : issuesToDelete) {
            //System.out.println(elem);
            keyIssue = elem;
            System.out.println("for delet keyIssue = " + keyIssue);

            // подготовка JSON текста тела запроса body для удаления issue
            String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
            // удаление задачи через выполнение метода объекта issueAPI
            issueAPI.deleteIssue(body_deleteIssue, keyIssue);

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
        }
    }

    @Test(dependsOnMethods = {"login"})
    public void deleteIssues_forSubstringSummary() {
        String substringSummary = "rest_test"; // summary ~ "\\[rest_test\\]" AND project = QAAutomation2
        List<String> issuesToDelete = getIssuesKeys_forSubstringSummary(substringSummary);
        for (String elem : issuesToDelete) {
            //System.out.println(elem);
            keyIssue = elem;
            System.out.println("for delet keyIssue = " + keyIssue);

            // подготовка JSON текста тела запроса body для удаления issue
            String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
            // удаление задачи через выполнение метода объекта issueAPI
            issueAPI.deleteIssue(body_deleteIssue, keyIssue);

            //            // проверка ответа от сервера после создания задачи
            //            Response responseDelete = issueAPI.getRequestSender().response;
            //            assertEquals(responseDelete.statusCode(), 204);
            //            AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));

            // ожидание после выполнения
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public List<String> getIssuesToDelete() {
        List<String> issuesToDelete = new ArrayList<String>();
        String fullPathToFile = "IssuesToDelete.properties";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fullPathToFile));
            String s = reader.readLine();
            while (s != null) {
                issuesToDelete.add(s);
                //            int num = Integer.parseInt(s);
                //            if (num % 2 == 0)
                //                list.add(num);
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        try {
        //            listOfIssuesToDelete = Files.lines(Paths.get(file)).collect(Collectors.toList());
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        for(int i = 0; i < issuesToDelete.size(); i++){
        //            System.out.println(issuesToDelete.get(i));
        //        }
        return issuesToDelete;
    }

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void deleteIssuePositive_statusCode204() {
        // создание Issue
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        JiraJSONFixture Jira_createFixture = new JiraJSONFixture();
        String bodyIssue = Jira_createFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(bodyIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
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
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // получение Issue по ключу задачи keyIssue
        issueAPI.getIssue(issueId);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        AssertJUnit.assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
        System.out.println("get issue keyIssue = " + keyIssue);

        //проверяем, какой поток
        System.out.println("getIssue_statusCode200 - thread id: " + Thread.currentThread().getId());

        // удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"Search"}, dependsOnMethods = {"login"})
    public void searchIssue_statusCode200() {
        // создание Issue
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // поиск Issue по значению ключа задачи keyIssue через строку запроса "jql:id = " + issueId
        System.out.println("looking for issue for the keyIssue : " + keyIssue);
        //JiraJSONFixture Jira_searchFixture = new JiraJSONFixture();
        String testQuery = "key = " + keyIssue + " AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key DESC";
        String body_searchIssue = jiraJSONFixture.generateJSONForSearchFilter_onQueryText(testQuery);
        issueAPI.search(body_searchIssue);
        //System.out.println("Response total results count: " + issueAPI.getRequestSender().extractResponseByPath("total"));
        //String strRespBody = issueAPI.getRequestSender().extractResponseByPath("issues").toString();
        String strRespBody = issueAPI.getRequestSender().response.getBody().asString();
        System.out.println("Response text response.getBody():");
        System.out.println(strRespBody);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        //проверяем, какой поток
        System.out.println("searchIssue_statusCode200 - thread id: " + Thread.currentThread().getId());

        // удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"Search"}, dependsOnMethods = {"login"})
    public void filterIssues_example() {

        // пример фильтрации через строку запроса "jql:id = " + issueId
        //String testQuery = "summary ~ \"\\\\[rest_test\\\\]\" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key ASC";
        //String testQuery = "summary ~ \"\\\\[rest_test\\\\]\" AND project = QAAutomation2 ORDER BY key ASC";
        //String testQuery = "key = QAAUT-1228 AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key DESC";
        //String testQuery = "key = QAAUT-1228 AND project = QAAutomation2 ORDER BY key ASC";
        //key = QAAUT-1017 AND project = QAAutomation2  ORDER BY key DESC   - поиск задачи по ключу issue QAAUT-1017
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key ASC
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY updated DESC - поиск всех задач в которых summary включает подстроку rest_test
        String testQuery = "project = QAAutomation2 ORDER BY key ASC";
        String body_searchIssue = jiraJSONFixture.generateJSONForSearchFilter_onQueryText(testQuery);
        //String body_searchIssue = jiraJSONFixture.generateJSONForSearchFilter(keyIssue);
        issueAPI.search(body_searchIssue);
        String strRespBody = issueAPI.getRequestSender().response.getBody().asString();
        System.out.println("Response total results count: " + issueAPI.getRequestSender().extractResponseByPath("total"));
        System.out.println("Response text response.getBody():");
        System.out.println(strRespBody);
        System.out.println("Response text issueAPI.getRequestSender().extractTextResponseAsString():");
        System.out.println(issueAPI.getRequestSender().extractTextResponseAsString());
        //        System.out.println("Array of issues in response:");
        //        System.out.println(issueAPI.getRequestSender().extractResponseByPath("issues"));
        //System.out.println("Response text get(\"/apps\").asString():");
        //System.out.println(given().when().get("/apps").asString());

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        int numbIssue = 0;
        try {
            jsonObject = (JSONObject) parser.parse(strRespBody);

            JSONArray issuesArr = (JSONArray) jsonObject.get("issues");
            for (Object elArr : issuesArr) {
                //System.out.println(elArr.toString());
                jsonObject = (JSONObject) parser.parse(elArr.toString());
                keyIssue = (String) jsonObject.get("key");
                numbIssue++;
                System.out.println("key Issue " + numbIssue + " = " + keyIssue);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // assertTrue(responseСreate.getBody().jsonPath().get("key").toString().contains("QAAUT-"));
        // assertTrue(responseСreate.getBody().jsonPath().get("self").toString().contains("http://soft.it-hillel.com.ua:8080"));

        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
    }

    public List<String> getIssuesKeys_forSubstringSummary(String substringSummary) {
        // получение списка ключей issue фильтрацией через строку запроса "jql:id = " + issueId
        List<String> issuesForSubstrSummary = new ArrayList<String>();
        //String substringSummary = "rest_test";
        //String testQuery = "summary ~ \"\\\\[rest_test\\\\]\" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key ASC";
        //String testQuery = "summary ~ \"\\\\[rest_test\\\\]\" AND project = QAAutomation2 ORDER BY key ASC";
        //String testQuery = "key = QAAUT-1228 AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key DESC";
        //String testQuery = "key = QAAUT-1228 AND project = QAAutomation2 ORDER BY key ASC";
        //key = QAAUT-1017 AND project = QAAutomation2  ORDER BY key DESC   - поиск задачи по ключу issue QAAUT-1017
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key ASC
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY updated DESC - поиск всех задач в которых summary включает подстроку rest_test
        String testQuery = "summary ~ \"\\\\[" + substringSummary + "\\\\]\" AND project = QAAutomation2 ORDER BY key DESC";
        String body_searchIssue = jiraJSONFixture.generateJSONForSearchFilter_onQueryText(testQuery);
        //String body_searchIssue = jiraJSONFixture.generateJSONForSearchFilter(keyIssue);
        issueAPI.search(body_searchIssue);
        String strRespBody = issueAPI.getRequestSender().response.getBody().asString();
        System.out.println("Response total results count: " + issueAPI.getRequestSender().extractResponseByPath("total"));
        System.out.println("Response text response.getBody():");
        System.out.println(strRespBody);
        System.out.println("Response text issueAPI.getRequestSender().extractTextResponseAsString():");
        System.out.println(issueAPI.getRequestSender().extractTextResponseAsString());
        //        System.out.println("Array of issues in response:");
        //        System.out.println(issueAPI.getRequestSender().extractResponseByPath("issues"));
        //System.out.println("Response text get(\"/apps\").asString():");
        //System.out.println(given().when().get("/apps").asString());

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        int numbIssue = 0;
        try {
            jsonObject = (JSONObject) parser.parse(strRespBody);

            JSONArray issuesArr = (JSONArray) jsonObject.get("issues");
            for (Object elArr : issuesArr) {
                //System.out.println(elArr.toString());
                jsonObject = (JSONObject) parser.parse(elArr.toString());
                keyIssue = (String) jsonObject.get("key");
                issuesForSubstrSummary.add(keyIssue);
                numbIssue++;
                System.out.println("key Issue " + numbIssue + " = " + keyIssue);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(issueAPI.getRequestSender().response.statusCode(), 200);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));

        return issuesForSubstrSummary;
    }


    @Test(groups = {"Comment"}, dependsOnMethods = {"login"})
    public void addComment_statusCode201() {
        // 1. создание Issue
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. добавление комментария в Issue
        String Jira_commentFixture = jiraJSONFixture.generateJSONForAddComment();
        issueAPI.addComment(Jira_commentFixture, keyIssue);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 201);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("addComment_statusCode201 - thread id: " + Thread.currentThread().getId());

        // 3. удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"Comment"}, dependsOnMethods = {"login"})
    public void deleteComment_statusCode204() {
        // 1. создание Issue
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. добавление комментария в Issue
        String body_addCommentIssue = jiraJSONFixture.generateJSONForAddComment();
        issueAPI.addComment(body_addCommentIssue, keyIssue);
        String idComment = issueAPI.getRequestSender().extractResponseByPath("id");
        System.out.println("id new comment in created issue = " + idComment);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3. удаление комментария в Issue
        issueAPI.deleteComment(keyIssue, idComment);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 204);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //проверяем, какой поток
        System.out.println("deleteComment_statusCode204 - thread id: " + Thread.currentThread().getId());

        // 4. удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"Issue"}, dependsOnMethods = {"login"})
    public void changeTypeIssue_statusCode204() {
        // 1. создание Issue
        //createIssuePositive_statusCode201();
        // подготовка JSON текста тела запроса body для создания issue
        String body_createIssue = jiraJSONFixture.generateJSONForSampleIssue();

        // создание задачи через выполнение метода объекта issueAPI
        issueAPI.createIssue(body_createIssue);
        issueId = issueAPI.getRequestSender().extractResponseByPath("id");
        keyIssue = issueAPI.getRequestSender().extractResponseByPath("key");
        System.out.println("new issueId = " + issueId);
        System.out.println("new keyIssue = " + keyIssue);
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. измение типа issue
        String Jira_SetTypeIssueFixture = jiraJSONFixture.generateJSONForSetTypeIssue("10003");
        issueAPI.changeTypeIssue(Jira_SetTypeIssueFixture, keyIssue);
        assertEquals(issueAPI.getRequestSender().response.statusCode(), 204);
        assertTrue(issueAPI.getRequestSender().response.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // проверяем, какой поток
        System.out.println("changeTypeIssue_statusCode204 - thread id: " + Thread.currentThread().getId());

        // 3. удаление Issue
        //deleteIssueOnly_statusCode204();
        System.out.println("for delet keyIssue = " + keyIssue);
        // подготовка JSON текста тела запроса body для удаления issue
        String body_deleteIssue = jiraJSONFixture.generateJSONForSampleIssue();
        // удаление задачи через выполнение метода объекта issueAPI
        issueAPI.deleteIssue(body_deleteIssue, keyIssue);
        //        // проверка ответа от сервера после создания задачи
        //        Response responseDelete = issueAPI.getRequestSender().response;
        //        assertEquals(responseDelete.statusCode(), 204);
        //        AssertJUnit.assertTrue(responseDelete.contentType().contains(ContentType.JSON.toString()));
        // ожидание после выполнения
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
