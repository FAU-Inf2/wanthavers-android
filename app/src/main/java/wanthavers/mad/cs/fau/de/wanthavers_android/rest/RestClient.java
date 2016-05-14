package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public abstract class RestClient {
    protected final WebTarget target;

    //TODO: get API-URL from shared preferences or something like that
    protected final String api_url = "http://faui21f.informatik.uni-erlangen.de:9090/";

    protected RestClient() {
        target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(api_url);
    }

    public String getApiUrl() { return api_url; }
}
