package utils;

import apis.ApiUrls;
import com.jayway.restassured.response.Response;

public class IssueAPI {

    private RequestSender requestSender = new RequestSender();

    public RequestSender getRequestSender() {
        return requestSender;
    }

    public void loginAPI() {
        requestSender.authenticate();
    }

    public void loginAPI_Negative() {
        requestSender.authenticateNegative();
    }

    public void createIssue(String body) {
        requestSender
                .createRequest(body)
                .post(ApiUrls.ISSUE.getUri());

    }

    public void deleteIssue(String body, String keyIssue) {
        requestSender
                .createRequest(body)
                .delete(ApiUrls.DELETE.getUri(keyIssue));
    }

    public void getIssue(String issueId) {
        requestSender
                //.createRequest(body)
                .get(ApiUrls.ISSUE.getUri(issueId));
    }

    public void search(String body) {
        requestSender
                .createRequest(body)
                .get(ApiUrls.SEARCH.getUri());
    }

    public void addComment(String body, String issueIdOrKey) {
        requestSender
                .createRequest(body)
                .post(ApiUrls.ISSUE.getUri(issueIdOrKey + "/comment"));
                //.post(ApiUrls.ISSUE.getUri() + "/" + issueId + "/comment");

    }

    public void deleteComment(String issueIdOrKey, String idComment){
        requestSender
                .createRequest("")
                .delete(ApiUrls.ISSUE.getUri(issueIdOrKey + "/comment/" + idComment));
                //.delete(ApiUrls.ISSUE.getUri() + "/" + issueIdOrKey + "/comment");
    }

    public void changeTypeIssue(String body, String issueId){
        requestSender
                .createRequest(body)
                .put(ApiUrls.ISSUE.getUri(issueId));
    }
}
