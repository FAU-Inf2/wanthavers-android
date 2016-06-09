package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import de.fau.cs.mad.wanthavers.common.DesireFilter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface FilterSettingContract {
    interface View extends BaseView<FilterSettingContract.Presenter> {
        //TODO

        void showDesireList();
    }

    interface Presenter extends BasePresenter {
        //TODO
        void setFilter(DesireFilter desireFilter);

        void setFilterWithInput();

        void resetFilter();
    }
}
