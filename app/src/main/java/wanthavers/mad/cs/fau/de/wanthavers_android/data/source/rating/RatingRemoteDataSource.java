package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RatingClient;

/**
 * Created by Nico on 14.05.2016.
 */
public class RatingRemoteDataSource implements RatingDataSource {
    private static RatingRemoteDataSource INSTANCE;

    private RatingClient ratingClient;

    // Prevent direct instantiation.
    private RatingRemoteDataSource(Context context) {
        ratingClient = RatingClient.getInstance(context);
    }

    public static RatingRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RatingRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRatingCallback callback) {
        try {
            Rating ret = ratingClient.createRating(userId, rating);
            callback.onRatingCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void getRating(@NonNull long userId, @NonNull long ratingId, @NonNull GetRatingCallback callback) {
        try {
            Rating ret = ratingClient.get(userId, ratingId);
            callback.onRatingLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUserCallback callback) {
        try {
            List<Rating> ret = ratingClient.getAllRatings(userId);
            callback.onAllRatingsForUserLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void updateRating(@NonNull long userId, @NonNull long ratingId, @NonNull Rating rating, @NonNull UpdateRatingCallback callback) {
        try {
            Rating ret = ratingClient.updateRating(userId, ratingId, rating);
            callback.onRatingUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteRating(@NonNull long userId, @NonNull long ratingId, @NonNull DeleteRatingCallback callback) {
        try {
            ratingClient.deleteRating(userId, ratingId);
            callback.onRatingDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

    @Override
    public void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUserCallback callback) {
        try {
            Rating ret = ratingClient.avgRating(userId);
            callback.onAverageRatingForUserLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }
}
