package apis;

public enum ApiUrls {

    LOGIN("/rest/auth/1/session"),
    ISSUE("/rest/api/2/issue"),
    DELETE("/rest/api/2/issue");

    private String uri;

    ApiUrls(String url) {
        this.uri = url;
    }

    public String getUri() {
        return this.uri;
    }

    public String getUri(String suffix) {
        return this.uri + "/" + suffix;
    }

}
