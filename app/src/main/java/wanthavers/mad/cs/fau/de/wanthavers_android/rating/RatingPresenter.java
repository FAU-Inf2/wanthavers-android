package wanthavers.mad.cs.fau.de.wanthavers_android.rating;

import android.support.annotation.NonNull;
import android.widget.RatingBar;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateRating;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAcceptedHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;

import static com.google.common.base.Preconditions.checkNotNull;

public class RatingPresenter implements RatingContract.Presenter {

    private final RatingContract.View mRatingView;
    private final UseCaseHandler mUseCaseHandler;
    private final DesireLogic mDesireLogic;
    private final RatingActivity mActivity;

    //Use Cases
    private final GetDesire mGetDesire;
    private final GetAcceptedHaver mGetAcceptedHaver;
    private final CreateRating mCreateRating;

    @NonNull
    private long mDesireId;

    public RatingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull RatingContract.View ratingView,
                           @NonNull GetDesire getDesire, @NonNull GetAcceptedHaver getAcceptedHaver,
                           @NonNull CreateRating createRating, @NonNull long desireId, @NonNull DesireLogic desireLogic,
                           @NonNull RatingActivity ratingActivity) {
        mRatingView = checkNotNull(ratingView, "rating view cannot be null");
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mDesireLogic = checkNotNull(desireLogic);
        mActivity = checkNotNull(ratingActivity, "rating activity cannot be null");

        mGetDesire = checkNotNull(getDesire);
        mGetAcceptedHaver = checkNotNull(getAcceptedHaver);
        mCreateRating = checkNotNull(createRating);

        mDesireId = desireId;
    }

    @Override
    public void start() {
        getDesire();
        getAcceptedHaver();
    }

    public void getDesire(){

        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(mDesireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {
                        Desire desire = response.getDesire();
                        mRatingView.showDesire(desire);
                    }

                    @Override
                    public void onError() {
                        mRatingView.showLoadingDesireError();
                    }
                });

    }

    @Override
    public void getAcceptedHaver() {

        GetAcceptedHaver.RequestValues requestValues = new GetAcceptedHaver.RequestValues(mDesireId);

        mUseCaseHandler.execute(mGetAcceptedHaver, requestValues,
                new UseCase.UseCaseCallback<GetAcceptedHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAcceptedHaver.ResponseValue response) {
                        Haver haver = response.getHaver();
                        mRatingView.showAcceptedHaver(haver);
                    }

                    @Override
                    public void onError() {
                        mRatingView.showLoadingHaverError();
                    }
                }
        );

    }

    public void finishRating(Desire desire, Haver haver) {
        long userId;
        if (mDesireLogic.isDesireCreator(desire.getCreator().getId())) {
            userId = haver.getUser().getId();
        } else {
            userId = desire.getCreator().getId();
        }
        RatingBar ratingBar = (RatingBar) mActivity.findViewById(R.id.rating_ratingbar);
        float rating_value = ratingBar.getRating();
        //TODO: comment
        String comment = "";
        createRating(userId, desire.getId(), rating_value, comment);

        //TODO: indicator if user has already rated

        //TODO: Julian open new screen
    }

    public void createRating(long userId, long desireId, float stars, String comment) {

        final CreateRating.RequestValues requestValues = new CreateRating.RequestValues(userId, desireId, stars, comment);

        mUseCaseHandler.execute(mCreateRating, requestValues,
                new UseCase.UseCaseCallback<CreateRating.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateRating.ResponseValue response) {
                        //nothing to do
                    }

                    @Override
                    public void onError() {
                        mRatingView.showCreateRatingError();
                    }
                }
        );
    }

}
