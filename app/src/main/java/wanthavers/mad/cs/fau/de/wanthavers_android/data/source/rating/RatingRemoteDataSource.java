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
    public void createRating(@NonNull long rateeId, @NonNull long desireId, @NonNull float stars, @NonNull String comment, @NonNull CreateRatingCallback callback) {
        try {
            Rating ret = ratingClient.createRating(rateeId, desireId, stars, comment);
            callback.onRatingCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void getRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull GetRatingCallback callback) {
        try {
            Rating ret = ratingClient.get(rateeId, ratingId);
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
    public void updateRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull float stars, @NonNull String comment, @NonNull UpdateRatingCallback callback) {
        try {
            Rating ret = ratingClient.updateRating(rateeId, ratingId, stars, comment);
            callback.onRatingUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
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
