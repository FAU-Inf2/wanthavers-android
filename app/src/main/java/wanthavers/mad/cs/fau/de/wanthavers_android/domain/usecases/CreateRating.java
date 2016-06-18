package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateRating extends UseCase<CreateRating.RequestValues, CreateRating.ResponseValue> {

    private final RatingRepository mRatingRepository;

    public CreateRating(@NonNull RatingRepository ratingRepository) {
        mRatingRepository = checkNotNull(ratingRepository, "rating repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mRatingRepository.createRating(values.getUserId(), values.getRating(),
                new RatingDataSource.CreateRatingCallback() {

                    @Override
                    public void onRatingCreated(Rating rating) {
                        ResponseValue responseValue = new ResponseValue(rating);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onCreateFailed() {

                    }
                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mUserId;
        private final Rating mRating;

        public RequestValues(long userId, Rating rating) {
            mUserId = userId;
            mRating = rating;
        }

        public long getUserId() {
            return mUserId;
        }

        public Rating getRating() {
            return mRating;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Rating mRating;

        public ResponseValue(Rating rating) {
            mRating = rating;
        }

        public Rating getCategory() {
            return mRating;
        }

    }

}
