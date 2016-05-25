package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

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
    private final AcceptDesire ucAcceptDesire;
    private boolean mFirstLoad = true;
    private final GetDesire ucGetDesire;
    private final GetHaverList mGetHaverList;
    private final UseCaseHandler useCaseHandler;
    //add Repository

    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(@NonNull UseCaseHandler ucHandler, @NonNull long desireId, @NonNull DesireDetailContract.View view,
                                 @NonNull AcceptDesire acceptDesire, @NonNull GetDesire getDesire, @NonNull GetHaverList getHaverList){

        useCaseHandler = ucHandler;
        mDesireDetailView = view;
        mDesireId = desireId;
        mGetHaverList = getHaverList;



        ucAcceptDesire = acceptDesire;
        ucGetDesire = getDesire;

        mDesireDetailView.setPresenter(this);
        //TODO add repo
    }


    @Override
    public void start(){
        getDesire();
        loadHavers(false);
    }

    @Override
    public void getDesire(){

        //add repo, for now just basic desire
        useCaseHandler.execute(ucGetDesire, new GetDesire.RequestValues(mDesireId),
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

        useCaseHandler.execute(mGetHaverList, requestValues, new UseCase.UseCaseCallback<GetHaverList.ResponseValue>() {
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
