package wanthavers.mad.cs.fau.de.wanthavers_android.eastereggone;

import java.io.File;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface EasterEggContract {
    interface View extends BaseView<Presenter> {

        void showDesireList();

        void showMessage(String message);

    }




    interface Presenter extends BasePresenter {

        void openDesireList();
    }
}
