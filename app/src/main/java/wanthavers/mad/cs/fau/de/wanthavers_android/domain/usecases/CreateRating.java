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

        mRatingRepository.createRating(values.getUserId(), values.getDesireId(), values.getStars(), values.getComment(),
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
        private final long mDesireId;
        private final float mStars;
        private final String mComment;

        public RequestValues(long userId, long desireId, float stars, String comment) {
            mUserId = userId;
            mDesireId = desireId;
            mStars = stars;
            mComment = comment;
        }

        public long getUserId() {
            return mUserId;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public float getStars() {
            return mStars;
        }

        public String getComment() {
            return mComment;
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
