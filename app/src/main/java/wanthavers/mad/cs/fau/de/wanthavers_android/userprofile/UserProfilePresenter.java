package wanthavers.mad.cs.fau.de.wanthavers_android.userprofile;

import android.content.Context;
import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListType;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfilePresenter implements UserProfileContract.Presenter {

    private UseCaseHandler mUseCaseHandler;
    private UserProfileContract.View mUserProfileView;
    private GetDesireList mGetFinishedDesireList;
    private GetDesireList mGetCanceledDesireList;
    private Context mContext;
    private long mUserId;

    public UserProfilePresenter(@NonNull UserProfileContract.View userProfileView, @NonNull UseCaseHandler useCaseHandler,
                                @NonNull GetDesireList getFinishedDesireList, @NonNull GetDesireList getCanceledDesireList,
                                @NonNull Context context) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mUserProfileView = checkNotNull(userProfileView, "user profile view cannont be null");
        mGetFinishedDesireList = checkNotNull(getFinishedDesireList);
        mGetCanceledDesireList = checkNotNull(getCanceledDesireList);
        mContext = checkNotNull(context, "context cannot be null");

        mUserProfileView.setPresenter(this);
    }

    @Override
    public void start() {
        getFinishedDesires();
        getCanceledDesires();
    }

    public void getFinishedDesires() {

        GetDesireList.RequestValues requestValues = new GetDesireList.RequestValues(DesireListType.FINISHED_DESIRES, mContext);
        requestValues.setUserId(mUserId);

        mUseCaseHandler.execute(mGetFinishedDesireList, requestValues, new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
            @Override
            public void onSuccess(GetDesireList.ResponseValue response) {
                mUserProfileView.showDesireHistory(response.getDesires());
                mUserProfileView.showFinishedDesireStatistics(response.getDesires().size());
            }

            @Override
            public void onError() {
                mUserProfileView.showGetFinishedDesiresError();
            }
        });
    }

    public void getCanceledDesires() {

        GetDesireList.RequestValues requestValues = new GetDesireList.RequestValues(DesireListType.CANCELED_DESIRES, mContext);
        requestValues.setUserId(mUserId);

        mUseCaseHandler.execute(mGetCanceledDesireList, requestValues, new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
            @Override
            public void onSuccess(GetDesireList.ResponseValue response) {
                mUserProfileView.showCanceledDesireStatistics(response.getDesires().size());
            }

            @Override
            public void onError() {
                mUserProfileView.showGetCanceledDesiresError();
            }
        });

    }

    @Override
    public void setUserId(long userId) {
        mUserId = userId;
    }

}
