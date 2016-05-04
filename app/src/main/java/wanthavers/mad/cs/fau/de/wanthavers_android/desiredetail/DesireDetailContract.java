package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import wanthavers.mad.cs.fau.de.wanthavers_android.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.BaseView;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;

public interface DesireDetailContract {


    interface View extends BaseView<Presenter> {

        void showDesire(Desire desire);

    }



    interface Presenter extends BasePresenter {

        void getDesire();


    }
}
