package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;

public class RatingClient {
    private static RatingClient INSTANCE;

    private RatingResource ratingEndpoint;

    private RatingClient() {
        //TODO: get API-URL from shared preferences or something like that
        final String API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";
        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
        ratingEndpoint = WebResourceFactory.newResource(RatingResource.class, target);
    }

    public static RatingClient getInstance() {
        if(INSTANCE == null){
            INSTANCE = new RatingClient();
        }
        return INSTANCE;
    }

    public List<Rating> getAllRatings(@PathParam("user-id") long l) {
        return ratingEndpoint.getAllRatings(l);
    }

    public Rating createRating(@PathParam("user-id") long l, Rating rating) {
        return ratingEndpoint.createRating(l, rating);
    }

    public Rating get(@PathParam("user-id") long l, @PathParam("id") long l1) {
        return ratingEndpoint.get(l, l1);
    }

    public Rating updateRating(@PathParam("user-id") long l, @PathParam("id") long l1, Rating rating) {
        return ratingEndpoint.updateRating(l, l1, rating);
    }

    public void deleteRating(@PathParam("user-id") long l, @PathParam("id") long l1) {
        ratingEndpoint.deleteRating(l, l1);
    }

    public Rating avgRating(@PathParam("user-id") long l) {
        return ratingEndpoint.avgRating(l);
    }

    public void createDummies(@PathParam("user-id") long l) {
        throw new UnsupportedOperationException();
    }
}
