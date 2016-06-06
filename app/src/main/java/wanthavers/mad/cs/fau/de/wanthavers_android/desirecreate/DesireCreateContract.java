package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireCreateContract {
    interface View extends BaseView<Presenter> {

        void showMedia(Desire d);

        void showMessage(String message);

        void showNextDesireCreateStep();

        boolean isStoragePermissionGranted();

        void selectImageForDesire();
    }




    interface Presenter extends BasePresenter {

        void createNextDesireCreateStep();

        void setDesire(Desire desire);

        void setImage(File file, Desire desire);

        void selectImageFromDevice();

    }
}
