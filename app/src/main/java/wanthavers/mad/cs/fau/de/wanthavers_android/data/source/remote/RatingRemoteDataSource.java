package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.RatingDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RatingClient;

/**
 * Created by Nico on 14.05.2016.
 */
public class RatingRemoteDataSource implements RatingDataSource {
    private static RatingRemoteDataSource INSTANCE;

    private RatingClient ratingEndpoint = RatingClient.getInstance();

    // Prevent direct instantiation.
    private RatingRemoteDataSource() {
    }

    public static RatingRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RatingRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRating callback) {
        try {
            Rating ret = ratingEndpoint.createRating(userId, rating);
            callback.onRatingCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void getRating(@NonNull long userId, @NonNull long ratingId, @NonNull GetRating callback) {
        try {
            Rating ret = ratingEndpoint.get(userId, ratingId);
            callback.onRatingLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUser callback) {
        try {
            List<Rating> ret = ratingEndpoint.getAllRatings(userId);
            callback.onAllRatingsForUserLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void updateRating(@NonNull long userId, @NonNull long ratingId, @NonNull Rating rating, @NonNull UpdateRating callback) {
        try {
            Rating ret = ratingEndpoint.updateRating(userId, ratingId, rating);
            callback.onRatingUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteRating(@NonNull long userId, @NonNull long ratingId, @NonNull DeleteRating callback) {
        try {
            ratingEndpoint.deleteRating(userId, ratingId);
            callback.onRatingDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

    @Override
    public void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUser callback) {
        try {
            Rating ret = ratingEndpoint.avgRating(userId);
            callback.onAverageRatingForUserLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }
}
