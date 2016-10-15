package utils;

import apis.ApiUrls;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import fixtures.JiraJSONFixture;

import static com.jayway.restassured.RestAssured.given;

public class RequestSender {

    public static String JSESSIONID = null;
    public final static ContentType CONTENT_TYPE = ContentType.JSON;
    public static RequestSpecification requestSpecification = null;
    public static Response response = null;

    public RequestSender() {
        RestAssured.baseURI = "https://forapitest.atlassian.net";
        //RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    }

    public void authenticate() {


        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String credentials = jiraJSONFixture.generateJSONForLogin();

        createRequest(credentials)
                .post(ApiUrls.LOGIN.getUri());

        this.JSESSIONID = response.then().extract().path("session.value");
    }

    public RequestSender createRequest(String body) {
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                .addHeader("Cookie", "JSESSIONID=" + RequestSender.JSESSIONID)
                .addBody(body);
        return this;
    }

    public RequestSender createRequestSpecification() {
        requestSpecification = given().
                when();
        return this;
    }

    // этот метод сможет добавлять столько угодно хедеров
    public RequestSender addHeader(String headerName, String headerValue) {
        requestSpecification.header(headerName, headerValue);
        return this;
    }

    public RequestSender addBody(String body) {
        requestSpecification.body(body);
        return this;
    }

    public RequestSender post(String uri) {
        response = requestSpecification.post(uri);
        return this;
    }

    public String extractResponseByPath(String path){
        return response.then().extract().path(path);
    }
}
