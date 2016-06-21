package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.view.View;

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

        void showMap(Location location);

        //Location getLocation();

        int getPriceClicked();

        void setPriceClicked(int clicked);

        String[] getRadiusArray();

        Category getSelectedCategory();

        void showNoLocationSetError();

        void showFilterChangeSuccess();

        void showLocationList();

        void closeLocationList();

        void showCreateLocationError();

        void showSavedLocations(List<Location> locationList);

        void showGetSavedLocationsError();

        void closeLocationNameDialog();

        String getNameInput();

        void showLocationInView();

        void showRadiusOption();

        void setLocation(Location location);

        void showUpdateLocationError();

        void showDeleteLocationError();

        void deleteLocationInView();

        String getCurLocationFilter();

    }

    interface Presenter extends BasePresenter {

        void setFilter(DesireFilter desireFilter);

        void setFilterWithInput(Location location);

        void resetFilter();

        void openMap(Location location);

        void openLocationList();

        void closeLocationList();

        void closeNameSelectionDialog();

        void finishLocationCreate(Location location);

        void finishLocationUpdate(Location location);

        void setLocation(Location location);

        void deleteLocation(final Location location);
    }
}
