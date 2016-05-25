package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireCreateContract {
    interface View extends BaseView<Presenter> {


        void showNextDesireCreateStep(String[] input);
    }




    interface Presenter extends BasePresenter {
       // Desire getEnteredText(Desire desire, String s1, String s2);

        void createNextDesireCreateStep(String[] input);


        void setDesire(Desire desire);
    }
}
