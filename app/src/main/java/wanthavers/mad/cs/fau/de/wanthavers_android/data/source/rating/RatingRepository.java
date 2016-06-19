package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 14.05.2016.
 */
public class RatingRepository implements RatingDataSource {
    private static RatingRepository INSTANCE = null;

    private final RatingDataSource ratingRemoteDataSource;

    private final RatingDataSource ratingLocalDataSource;

    private RatingRepository(@NonNull RatingDataSource ratingRemoteDataSource, @NonNull RatingDataSource ratingLocalDataSource) {
        this.ratingLocalDataSource = checkNotNull(ratingLocalDataSource);
        this.ratingRemoteDataSource = checkNotNull(ratingRemoteDataSource);
    }

    public static RatingRepository getInstance(RatingDataSource ratingRemoteDataSource, RatingDataSource ratingLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RatingRepository(ratingRemoteDataSource, ratingLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void createRating(@NonNull long rateeId, @NonNull long desireId, @NonNull float stars, @NonNull String comment, @NonNull final CreateRatingCallback callback) {
        checkNotNull(rateeId);
        checkNotNull(desireId);
        checkNotNull(stars);
        checkNotNull(comment);
        checkNotNull(callback);

        ratingRemoteDataSource.createRating(rateeId, desireId, stars, comment, new CreateRatingCallback() {
            @Override
            public void onRatingCreated(Rating rating) {
                callback.onRatingCreated(rating);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void getRating(@NonNull final long rateeId, @NonNull final long ratingId, @NonNull final GetRatingCallback callback) {
        checkNotNull(rateeId);
        checkNotNull(ratingId);
        checkNotNull(callback);

        ratingLocalDataSource.getRating(rateeId, ratingId, new GetRatingCallback() {
            @Override
            public void onRatingLoaded(Rating rating) {
                callback.onRatingLoaded(rating);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getRating(rateeId, ratingId, new GetRatingCallback() {
                    @Override
                    public void onRatingLoaded(Rating rating) {
                        callback.onRatingLoaded(rating);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getAllRatingsForUser(@NonNull final long userId, @NonNull final GetAllRatingsForUserCallback callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        ratingLocalDataSource.getAllRatingsForUser(userId, new GetAllRatingsForUserCallback() {
            @Override
            public void onAllRatingsForUserLoaded(List<Rating> ratings) {
                callback.onAllRatingsForUserLoaded(ratings);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getAllRatingsForUser(userId, new GetAllRatingsForUserCallback() {
                    @Override
                    public void onAllRatingsForUserLoaded(List<Rating> ratings) {
                        callback.onAllRatingsForUserLoaded(ratings);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void updateRating(@NonNull long rateeId, @NonNull long ratingId, @NonNull float stars, @NonNull String comment, @NonNull final UpdateRatingCallback callback) {
        checkNotNull(rateeId);
        checkNotNull(ratingId);
        checkNotNull(stars);
        checkNotNull(comment);
        checkNotNull(callback);

        ratingRemoteDataSource.updateRating(rateeId, ratingId, stars, comment, new UpdateRatingCallback() {
            @Override
            public void onRatingUpdated(Rating rating) {
                callback.onRatingUpdated(rating);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void getAverageRatingForUser(@NonNull final long userId, @NonNull final GetAverageRatingForUserCallback callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        ratingLocalDataSource.getAverageRatingForUser(userId, new GetAverageRatingForUserCallback() {
            @Override
            public void onAverageRatingForUserLoaded(Rating rating) {
                callback.onAverageRatingForUserLoaded(rating);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getAverageRatingForUser(userId, new GetAverageRatingForUserCallback() {
                    @Override
                    public void onAverageRatingForUserLoaded(Rating rating) {
                        callback.onAverageRatingForUserLoaded(rating);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }
}
