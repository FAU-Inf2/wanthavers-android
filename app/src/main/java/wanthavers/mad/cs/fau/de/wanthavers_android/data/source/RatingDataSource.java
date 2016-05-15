package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;

/**
 * Created by Nico on 14.05.2016.
 */
public interface RatingDataSource {

    interface CreateRatingCallback {

        void onRatingCreated(Rating rating);

        void onCreateFailed();

    }

    interface GetRatingCallback {

        void onRatingLoaded(Rating rating);

        void onDataNotAvailable();

    }

    interface GetAllRatingsForUserCallback {

        void onAllRatingsForUserLoaded(List<Rating> ratings);

        void onDataNotAvailable();

    }

    interface UpdateRatingCallback {

        void onRatingUpdated(Rating rating);

        void onUpdateFailed();

    }

    interface DeleteRatingCallback {

        void onRatingDeleted();

        void onDeleteFailed();

    }

    interface GetAverageRatingForUserCallback {

        void onAverageRatingForUserLoaded(Rating rating);

        void onDataNotAvailable();

    }

    void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRatingCallback callback);

    void getRating(@NonNull long userId, @NonNull long ratingId, @NonNull GetRatingCallback callback);

    void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUserCallback callback);

    void updateRating(@NonNull long userId, @NonNull long ratingId, @NonNull Rating rating, @NonNull UpdateRatingCallback callback);

    void deleteRating(@NonNull long userId, @NonNull long ratingId, @NonNull DeleteRatingCallback callback);

    void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUserCallback callback);

}
