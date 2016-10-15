package fixtures;

import org.json.simple.JSONObject;

public class JiraJSONFixture {
    public String generateJSONForLogin() {
        JSONObject credentials = new JSONObject();
        credentials.put("username", "studinskyi");
        credentials.put("password", "dima_st");
        return credentials.toJSONString();
    }

    public String generateJSONForSampleIssue() {
        JSONObject issue = new JSONObject();

        JSONObject fields = new JSONObject();
        fields.put("summary", "rest_test");
        JSONObject project = new JSONObject();

        JSONObject issuetype = new JSONObject();
        JSONObject assignee = new JSONObject();
        JSONObject reporter = new JSONObject();

        issue.put("fields", fields);
        fields.put("project", project);
        fields.put("issuetype", issuetype);
        fields.put("assignee", assignee);
        fields.put("reporter", reporter);

        project.put("id", "10315");
        issuetype.put("id", "10004");
        assignee.put("name", "studinskyi");
        reporter.put("name", "studinskyi");

        return issue.toJSONString();
    }
}
