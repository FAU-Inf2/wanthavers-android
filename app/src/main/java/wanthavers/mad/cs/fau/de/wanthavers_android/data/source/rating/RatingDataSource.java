package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating;

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

    interface GetAverageRatingForUserCallback {

        void onAverageRatingForUserLoaded(Rating rating);

        void onDataNotAvailable();

    }

    void createRating(@NonNull long rateeId, @NonNull long desireId, @NonNull float stars, @NonNull String comment, @NonNull CreateRatingCallback callback);

    void getRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull GetRatingCallback callback);

    void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUserCallback callback);

    void updateRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull float stars, @NonNull String comment, @NonNull UpdateRatingCallback callback);

    void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUserCallback callback);

}
