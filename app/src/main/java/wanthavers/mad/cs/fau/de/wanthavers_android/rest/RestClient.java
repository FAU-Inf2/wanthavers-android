package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class RestClient {
    protected final WebTarget target;

    protected final String API_URL;

    protected RestClient() {
        //TODO: get API-URL from shared preferences or something like that
        API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";
        target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
    }

    public String getApiUrl() { return API_URL; }
}
