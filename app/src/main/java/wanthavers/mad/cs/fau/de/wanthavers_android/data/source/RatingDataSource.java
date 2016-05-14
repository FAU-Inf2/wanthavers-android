package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;

/**
 * Created by Nico on 14.05.2016.
 */
public interface RatingDataSource {

    interface CreateRating {

        void onRatingCreated(Rating rating);

        void onCreateFailed();

    }

    interface GetRating {

        void onRatingLoaded(Rating rating);

        void onDataNotAvailable();

    }

    interface GetAllRatingsForUser {

        void onAllRatingsForUserLoaded(List<Rating> ratings);

        void onDataNotAvailable();

    }

    interface UpdateRating {

        void onRatingUpdated(Rating rating);

        void onUpdateFailed();

    }

    interface DeleteRating {

        void onRatingDeleted();

        void onDeleteFailed();

    }

    interface GetAverageRatingForUser {

        void onAverageRatingForUserLoaded(Rating rating);

        void onDataNotAvailable();

    }

    void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRating callback);

    void getRating(@NonNull long userId, @NonNull long ratingId, @NonNull GetRating callback);

    void getAllRatingsForUser(@NonNull long userId, @NonNull GetAllRatingsForUser callback);

    void updateRating(@NonNull long userId, @NonNull long ratingId, @NonNull Rating rating, @NonNull UpdateRating callback);

    void deleteRating(@NonNull long userId, @NonNull long ratingId, @NonNull DeleteRating callback);

    void getAverageRatingForUser(@NonNull long userId, @NonNull GetAverageRatingForUser callback);

}
