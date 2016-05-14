package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

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
    public void createRating(@NonNull long userId, @NonNull Rating rating, @NonNull CreateRating callback) {
        checkNotNull(userId);
        checkNotNull(rating);
        checkNotNull(callback);

        ratingRemoteDataSource.createRating(userId, rating, callback);
        ratingLocalDataSource.createRating(userId, rating, callback);
    }

    @Override
    public void getRating(@NonNull final long userId, @NonNull final long ratingId, @NonNull final GetRating callback) {
        checkNotNull(userId);
        checkNotNull(ratingId);
        checkNotNull(callback);

        ratingLocalDataSource.getRating(userId, ratingId, new GetRating() {
            @Override
            public void onRatingLoaded(Rating rating) {
                callback.onRatingLoaded(rating);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getRating(userId, ratingId, new GetRating() {
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
    public void getAllRatingsForUser(@NonNull final long userId, @NonNull final GetAllRatingsForUser callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        ratingLocalDataSource.getAllRatingsForUser(userId, new GetAllRatingsForUser() {
            @Override
            public void onAllRatingsForUserLoaded(List<Rating> ratings) {
                callback.onAllRatingsForUserLoaded(ratings);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getAllRatingsForUser(userId, new GetAllRatingsForUser() {
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
    public void updateRating(@NonNull final long userId, @NonNull final long ratingId, @NonNull final Rating rating, @NonNull final UpdateRating callback) {
        checkNotNull(userId);
        checkNotNull(ratingId);
        checkNotNull(rating);
        checkNotNull(callback);

        ratingLocalDataSource.updateRating(userId, ratingId, rating, new UpdateRating() {
            @Override
            public void onRatingUpdated(Rating rating) {
                callback.onRatingUpdated(rating);
            }

            @Override
            public void onUpdateFailed() {
                ratingRemoteDataSource.updateRating(userId, ratingId, rating, new UpdateRating() {
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
        });
    }

    @Override
    public void deleteRating(@NonNull final long userId, @NonNull final long ratingId, @NonNull final DeleteRating callback) {
        checkNotNull(userId);
        checkNotNull(ratingId);
        checkNotNull(callback);

        ratingLocalDataSource.deleteRating(userId, ratingId, new DeleteRating() {
            @Override
            public void onRatingDeleted() {
                callback.onRatingDeleted();
            }

            @Override
            public void onDeleteFailed() {
                ratingRemoteDataSource.deleteRating(userId, ratingId, new DeleteRating() {
                    @Override
                    public void onRatingDeleted() {
                        callback.onRatingDeleted();
                    }

                    @Override
                    public void onDeleteFailed() {
                        callback.onDeleteFailed();
                    }
                });
            }
        });
    }

    @Override
    public void getAverageRatingForUser(@NonNull final long userId, @NonNull final GetAverageRatingForUser callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        ratingLocalDataSource.getAverageRatingForUser(userId, new GetAverageRatingForUser() {
            @Override
            public void onAverageRatingForUserLoaded(Rating rating) {
                callback.onAverageRatingForUserLoaded(rating);
            }

            @Override
            public void onDataNotAvailable() {
                ratingRemoteDataSource.getAverageRatingForUser(userId, new GetAverageRatingForUser() {
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
