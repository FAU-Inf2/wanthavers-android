package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireCreateContract {
    interface View extends BaseView<Presenter> {

        //void showMedia(Media m);
        void showMedia(Desire d);


        void showNextDesireCreateStep(String[] input);
    }




    interface Presenter extends BasePresenter {
       // Desire getEnteredText(Desire desire, String s1, String s2);

        void createNextDesireCreateStep(String[] input);


        void setDesire(Desire desire);

        void setImage(File file);

        //Media getMedia();
    }
}
