package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
//import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;
import de.fau.cs.mad.wanthavers.common.Desire;
public interface DesireDetailContract {


    interface View extends BaseView<Presenter> {

        void showDesire(Desire desire);

    }



    interface Presenter extends BasePresenter {

        void getDesire();


    }
}
