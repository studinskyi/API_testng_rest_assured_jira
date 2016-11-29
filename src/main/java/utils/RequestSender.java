package utils;

import apis.ApiUrls;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import fixtures.JiraJSONFixture;

import static com.jayway.restassured.RestAssured.given;

public class RequestSender {

    public String JSESSIONID = null;
    public final static ContentType CONTENT_TYPE = ContentType.JSON;
    public RequestSpecification requestSpecification = null;
    public Response response = null;

    public RequestSender() {
        //RestAssured.baseURI = "https://forapitest.atlassian.net";
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    }

    public void authenticate() {
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String credentials = jiraJSONFixture.generateJSONForLogin();

        createRequest(credentials)
                .post(ApiUrls.LOGIN.getUri());

        this.JSESSIONID = this.response.then().extract().path("session.value");
    }

    public void authenticateNegative() {
        JiraJSONFixture jiraJSONFixture = new JiraJSONFixture();
        String credentials = jiraJSONFixture.generateJSONForLoginNegative();

        createRequest(credentials)
                .post(ApiUrls.LOGIN.getUri());

        //this.JSESSIONID = this.response.then().extract().path("session.value");
    }

    public RequestSender createRequest(String body) {
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                //.addHeader("Cookie", "JSESSIONID=" + RequestSender.JSESSIONID)
                .addHeader("Cookie", "JSESSIONID=" + this.JSESSIONID)
                .addBody(body);
        return this;
    }

    public RequestSender createRequestSpecification() {
        // обращение к объекту RestAssured, чтобы можно было начать заполнять параметры запроса
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
        this.response = requestSpecification.post(uri);
        return this;
    }

    public RequestSender get(String uri) {
        this.response = requestSpecification.get(uri);
        return this;
    }

    public RequestSender put(String uri) {
        response = requestSpecification.put(uri);
        return this;
    }

    public RequestSender delete(String uri) {
        this.response = requestSpecification.delete(uri);
        return this;
    }

    public String extractResponseByPath(String path){
        return this.response.then().extract().path(path).toString();
    }

    public String extractTextResponseAsString(){
        return response.then().extract().asString();
    }
}
