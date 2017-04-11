package fixtures;

import org.json.simple.JSONArray;
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

    public String generateJSONForLoginNegative() {
        JSONObject credentials = new JSONObject();
        credentials.put("username", "st123");
        credentials.put("password", "123");
        return credentials.toJSONString();
    }

    public String generateJSONForSampleIssue() {
        JSONObject issue = new JSONObject();

        JSONObject fields = new JSONObject();
        fields.put("summary", "api_rest_test_lr10 " + getCurrentDateTimeString());
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

    public String generateJSONForSearchFilter(String issueKey) {
        JSONObject credentials = new JSONObject();
        JSONArray fields = new JSONArray();
        fields.add("summary");
        fields.add("key");

        //credentials.put("jql", "id = " + issueId);
        credentials.put("jql", "key = " + issueKey);
        credentials.put("startAt", "0");
        credentials.put("maxResults", "200");
        credentials.put("fields", fields);

        //{"jql":"project = QAAUT","maxResults":"15","startAt":"0"}

        System.out.println("fixture for Search issue: " + credentials.toString());
        return credentials.toJSONString();
    }

    public String generateJSONForSearchFilter_onQueryText(String textQuery) {
        JSONObject credentials = new JSONObject();
        JSONArray fields = new JSONArray();
        fields.add("summary");
        fields.add("key");

        //credentials.put("jql", "id = " + issueId);
        credentials.put("jql", textQuery);
        //credentials.put("jql", "key = " + issueKey);
        credentials.put("startAt", "0");
        credentials.put("maxResults", "200");
        credentials.put("fields", fields);

        //key = QAAUT-1017 AND project = QAAutomation2  ORDER BY key DESC   - поиск задачи по ключу issue QAAUT-1017
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY key ASC
        //summary ~ "\\[rest_test\\]" AND project = QAAutomation2 AND assignee = currentuser() ORDER BY updated DESC - поиск всех задач в которых summary включает подстроку rest_test
        //{"jql":"project = QAAUT","maxResults":"15","startAt":"0"}

        System.out.println("fixture for Search issue: " + credentials.toString());
        return credentials.toJSONString();
    }

    public String generateJSONForAddComment() {
        JSONObject addCommentJSON = new JSONObject();
        addCommentJSON.put("body", "This is a comment added for testing " + getCurrentDateTimeString());
        return addCommentJSON.toJSONString();
    }

    public String getCurrentDateTimeString() {
        // для возможности последующего просмотра командой history
        Date d = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        //FileManager.executedOperations.put(formatDate.format(d), FileManager.currentCommand);
        return formatDate.format(d);
    }

    public String generateJSONForSetTypeIssue(String typeIssueId){
        JSONObject credentials =new JSONObject();
        JSONObject fields= new JSONObject();
        JSONObject issuetype = new JSONObject();
        fields.put("issuetype", issuetype);
        issuetype.put("id", typeIssueId);
        credentials.put("fields", fields);
        return credentials.toJSONString();
    }
}
