package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private final AcceptDesire ucAcceptDesire;
    private final GetDesire ucGetDesire;
    private final UseCaseHandler useCaseHandler;
    //add Repository

    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(@NonNull UseCaseHandler ucHandler, @NonNull long desireId, @NonNull DesireDetailContract.View view,
                                 @NonNull AcceptDesire acceptDesire, @NonNull GetDesire getDesire){

        useCaseHandler = ucHandler;
        mDesireDetailView = view;
        mDesireId = desireId;



        ucAcceptDesire = acceptDesire;
        ucGetDesire = getDesire;

        mDesireDetailView.setPresenter(this);
        //TODO add repo
    }


    @Override
    public void start(){ getDesire(); }

    @Override
    public void getDesire(){

        //add repo, for now just basic desire
        useCaseHandler.execute(ucGetDesire, new GetDesire.RequestValues(0),
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


        //Desire desire = new Desire("00000", "Kitzinger beer", "What i need is some kitzinger, pronto!!!");
        //mDesireDetailView.showDesire(desire);

    }
}
