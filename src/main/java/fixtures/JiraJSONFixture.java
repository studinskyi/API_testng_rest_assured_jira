package fixtures;

import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        fields.put("summary", "api_rest_test_lr10 " + getCurrenDateTimeString());
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
        issuetype.put("id", "10004"); // 10004 - Bug (баг) , 10003 - Task (задача), 10002 -  Story (история)
        assignee.put("name", "studinskyi");
        reporter.put("name", "studinskyi");

        return issue.toJSONString();
    }

    public String getCurrenDateTimeString() {
        // для возможности последующего просмотра командой history
        Date d = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        //FileManager.executedOperations.put(formatDate.format(d), FileManager.currentCommand);
        return formatDate.format(d);
    }
}
