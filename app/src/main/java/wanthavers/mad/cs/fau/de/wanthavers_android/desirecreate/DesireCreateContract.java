package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.FragmentManager;
import android.content.Context;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireCreateContract {
    interface View extends BaseView<Presenter> {
        String getEnteredText(View v);//TODO
    }


    interface Presenter extends BasePresenter {
        void createNewDesire(Desire desire);
        //TODO Rename!
        //void switchFragment(FragmentManager frag);

        //TODO
    }
}
