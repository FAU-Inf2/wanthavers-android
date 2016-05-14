package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.RatingDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 14.05.2016.
 */
public class RatingLocalDataSource implements RatingDataSource {
    private static RatingLocalDataSource INSTANCE;

    private Context context;

    private RatingLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        this.context = context;
    }

    public static RatingLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RatingLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRating callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onCreateFailed();
    }

    @Override
    public void getRating(@NonNull long userId, @NonNull long ratingId, @NonNull GetRating callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUser callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }

    @Override
    public void updateRating(@NonNull long userId, @NonNull long ratingId, @NonNull Rating rating, @NonNull UpdateRating callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onUpdateFailed();
    }

    @Override
    public void deleteRating(@NonNull long userId, @NonNull long ratingId, @NonNull DeleteRating callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDeleteFailed();
    }

    @Override
    public void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUser callback) {
        //TODO: alter this method when we decide to store ratings locally
        callback.onDataNotAvailable();
    }
}
