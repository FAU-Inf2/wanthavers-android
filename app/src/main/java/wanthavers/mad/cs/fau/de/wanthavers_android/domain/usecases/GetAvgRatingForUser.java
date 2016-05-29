package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetAvgRatingForUser extends UseCase<GetAvgRatingForUser.RequestValues, GetAvgRatingForUser.ResponseValue> {

    private final RatingRepository mRatingRepository;


    public GetAvgRatingForUser(@NonNull RatingRepository ratingRepository) {
        mRatingRepository = checkNotNull(ratingRepository, "rating Repository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mRatingRepository.getAverageRatingForUser(values.getUserId(), new RatingDataSource.GetAverageRatingForUserCallback() {

            @Override
            public void onAverageRatingForUserLoaded(Rating rating) {
                ResponseValue responseValue = new ResponseValue(rating);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mUserId;

        public RequestValues(@NonNull long userId) {
            mUserId = checkNotNull(userId, "userId cannot be null!");
        }

        public long getUserId() {
            return mUserId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Rating mRating;

        public ResponseValue(@NonNull Rating rating) {
            mRating = checkNotNull(rating, "rating cannot be null!");
        }

        public Rating getRating() {
            return mRating;
        }
    }

}
