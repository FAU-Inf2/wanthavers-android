package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.FragmentManager;
import android.content.Context;

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
    }
}
