package utils;

import apis.ApiUrls;

public class IssueAPI {

    private RequestSender requestSender = new RequestSender();

    public RequestSender getRequestSender() {
        return requestSender;
    }

    public void loginAPI(){
        requestSender.authenticate();
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

    public void getIssue(String issueId){
        requestSender
                //.createRequest(body)
                .get(ApiUrls.ISSUE.getUri(issueId));
        //        RequestSender requestSender = new RequestSender();
        //        requestSender.createEmptyRequest().get(ApiUrls.ISSUE.getUri(issue));
        //        return requestSender;
    }

    public void addComment(String issueId, String body) {
        requestSender
                .createRequest(body)
                .post(ApiUrls.ISSUE.getUri() + "/" + issueId + "/comment");

    }

}
