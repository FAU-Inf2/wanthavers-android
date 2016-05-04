package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;

    //add Repository

    @NonNull
    private String mDesireId;

    public DesireDetailPresenter(@NonNull String desireId, @NonNull DesireDetailContract.View view){
        mDesireDetailView = view;
        mDesireId = desireId;

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
