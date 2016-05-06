package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private final AcceptDesire ucAcceptDesire;
    //add Repository

    @NonNull
    private String mDesireId;

    public DesireDetailPresenter(@NonNull String desireId, @NonNull DesireDetailContract.View view,
                                 @NonNull AcceptDesire acceptDesire, @NonNull GetDesire getDesire){
        mDesireDetailView = view;
        mDesireId = desireId;

        ucAcceptDesire = acceptDesire;


        mDesireDetailView.setPresenter(this);
        //TODO add repo
    }


    @Override
    public void start(){ getDesire(); }

    @Override
    public void getDesire(){

        //add repo, for now just basic desire

        Desire desire = new Desire("00000", "Kitzinger beer", "What i need is some kitzinger, pronto!!!");
        mDesireDetailView.showDesire(desire);

    }
}
