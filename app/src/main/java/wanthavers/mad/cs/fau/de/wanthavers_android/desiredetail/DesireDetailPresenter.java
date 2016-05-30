package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private final AcceptDesire mAcceptDesire;
    private boolean mFirstLoad = true;
    private final GetDesire mGetDesire;
    private final GetHaverList mGetHaverList;
    private final UseCaseHandler mUseCaseHandler;

    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull long desireId, @NonNull DesireDetailContract.View desireDetailView,
                                 @NonNull AcceptDesire acceptDesire, @NonNull GetDesire getDesire, @NonNull GetHaverList getHaverList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mDesireDetailView = checkNotNull(desireDetailView, "desiredetail view cannot be null");
        mDesireId = desireId;
        mGetHaverList = checkNotNull(getHaverList);
        mAcceptDesire = checkNotNull(acceptDesire);
        mGetDesire = checkNotNull(getDesire);

        mDesireDetailView.setPresenter(this);
    }


    @Override
    public void start(){
        getDesire();
        loadHavers(false);
    }

    @Override
    public void getDesire(){

        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(mDesireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {
                        Desire desire = response.getDesire();
                        mDesireDetailView.showDesire(desire);
                    }

                    @Override
                    public void onError() {
                        //error handling
                    }
                });

    }

    @Override
    public void loadHavers(boolean forceUpdate) {
        loadHavers(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadHavers(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mDesireDetailView.setLoadingIndicator(true);
        }

        GetHaverList.RequestValues requestValues = new GetHaverList.RequestValues();

        mUseCaseHandler.execute(mGetHaverList, requestValues, new UseCase.UseCaseCallback<GetHaverList.ResponseValue>() {
            @Override
            public void onSuccess(GetHaverList.ResponseValue response) {
                List<Haver> havers = response.getHavers();
                if (!mDesireDetailView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mDesireDetailView.setLoadingIndicator(false);
                }

                processHavers(havers);

            }

            @Override
            public void onError() {
                if (!mDesireDetailView.isActive()) {
                    return;
                }
                mDesireDetailView.showLoadingHaversError();
            }
        });
    }

    private void processHavers(List<Haver> havers) {
        if (havers.isEmpty()) {
            //TODO no havers yet
        } else {
            mDesireDetailView.showHavers(havers);
        }
    }
}
