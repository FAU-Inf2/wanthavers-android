package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface FilterSettingContract {
    interface View extends BaseView<FilterSettingContract.Presenter> {

        void showDesireList();

        void showCategories(List<Category> categories);

        void showGetCategoriesError();

        void showMap();

        Location getLocation();

        int getPriceClicked();

        String[] getRadiusArray();

        Category getSelectedCategory();

        void showNoLocationSetError();

        void showFilterChangeSuccess();

    }

    interface Presenter extends BasePresenter {

        void setFilter(DesireFilter desireFilter);

        void setFilterWithInput();

        void resetFilter();

        void openMap();
    }
}
