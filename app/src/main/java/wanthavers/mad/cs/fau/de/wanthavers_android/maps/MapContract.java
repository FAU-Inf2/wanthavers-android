package wanthavers.mad.cs.fau.de.wanthavers_android.maps;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface MapContract {
    interface View extends BaseView<Presenter> {
        void moveToCurrentGpsPosition();

        void showFinishDesireCreate();

        void editAddress();

        boolean forDesireDetail();

        boolean forFilterSettings();
    }




    interface Presenter extends BasePresenter {

        void createFinishDesireCreate();

        void createMoveToCurrentGpsPosition();

        void createEditAddress();



    }
}
