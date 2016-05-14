package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;

public class RatingClient extends RestClient {
    private static RatingClient INSTANCE;

    private RatingResource ratingEndpoint;

    private RatingClient() {
        super();
        ratingEndpoint = WebResourceFactory.newResource(RatingResource.class, target);
    }

    public static RatingClient getInstance() {
        if(INSTANCE == null){
            INSTANCE = new RatingClient();
        }
        return INSTANCE;
    }

    public List<Rating> getAllRatings(long userId) {
        return ratingEndpoint.getAllRatings(userId);
    }

    public Rating createRating(long userId, Rating rating) {
        return ratingEndpoint.createRating(userId, rating);
    }

    public Rating get(long userId, long ratingId) {
        return ratingEndpoint.get(userId, ratingId);
    }

    public Rating updateRating(long userId, long ratingId, Rating rating) {
        return ratingEndpoint.updateRating(userId, ratingId, rating);
    }

    public void deleteRating(long userId, long ratingId) {
        ratingEndpoint.deleteRating(userId, ratingId);
    }

    public Rating avgRating(long userId) {
        return ratingEndpoint.avgRating(userId);
    }

}
