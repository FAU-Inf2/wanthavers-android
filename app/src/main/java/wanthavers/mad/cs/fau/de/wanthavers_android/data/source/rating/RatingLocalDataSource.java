package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.DatabaseHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 14.05.2016.
 */
public class RatingLocalDataSource implements RatingDataSource {
    private static RatingLocalDataSource INSTANCE;

    private RuntimeExceptionDao<Rating, Long> ratingDao;

    private RatingLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        ratingDao = DatabaseHelper.getInstance(context).getRatingRuntimeDao();
    }

    public static RatingLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RatingLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createRating(@NonNull long rateeId, @NonNull long desireId, @NonNull float stars, @NonNull String comment, @NonNull CreateRatingCallback callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onCreateFailed();
    }

    @Override
    public void getRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull GetRatingCallback callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUserCallback callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }

    @Override
    public void updateRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull float stars, @NonNull String comment, @NonNull UpdateRatingCallback callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onUpdateFailed();
    }

    @Override
    public void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUserCallback callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }
}
