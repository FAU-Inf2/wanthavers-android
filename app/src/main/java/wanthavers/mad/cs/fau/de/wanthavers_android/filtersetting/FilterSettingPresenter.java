package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.j256.ormlite.stmt.query.In;

import java.lang.reflect.Array;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSavedLocations;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

public class FilterSettingPresenter implements FilterSettingContract.Presenter {

    private final FilterSettingContract.View mFilterSettingView;
    private final UseCaseHandler mUseCaseHandler;
    private final FilterSettingActivity mActivity;
    private final GetSubcategories mGetSubcategories;
    private final CreateLocation mCreateLocation;
    private final GetSavedLocations mGetSavedLocations;

    public FilterSettingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull FilterSettingContract.View filterSettingView,
                                  @NonNull FilterSettingActivity filterSettingActivity,  @NonNull GetSubcategories getSubcategories,
                                  @NonNull CreateLocation createLocation, @NonNull GetSavedLocations getSavedLocations) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mFilterSettingView = checkNotNull(filterSettingView, "filtersetting view cannot be null");
        mActivity = checkNotNull(filterSettingActivity, "filtersetting activtiy cannot be null");
        mGetSubcategories = checkNotNull(getSubcategories);
        mCreateLocation = checkNotNull(createLocation);
        mGetSavedLocations = checkNotNull(getSavedLocations);

        mFilterSettingView.setPresenter(this);
    }
    @Override
    public void start() {
        loadCategories();
    }

    public void loadCategories() {

        GetSubcategories.RequestValues requestValues = new GetSubcategories.RequestValues(0, true);

        mUseCaseHandler.execute(mGetSubcategories, requestValues, new UseCase.UseCaseCallback<GetSubcategories.ResponseValue>() {

            @Override
            public void onSuccess(GetSubcategories.ResponseValue response) {
                List<Category> categories = response.getCategories();
                mFilterSettingView.showCategories(categories);
                //System.out.println("Server sent " + categories.size() + " categories");
            }

            @Override
            public void onError() {
                mFilterSettingView.showGetCategoriesError();
            }

        });
    }

    public void getSavedLocations() {

        GetSavedLocations.RequestValues requestValues = new GetSavedLocations.RequestValues();

        mUseCaseHandler.execute(mGetSavedLocations, requestValues, new UseCase.UseCaseCallback<GetSavedLocations.ResponseValue>() {
            @Override
            public void onSuccess(GetSavedLocations.ResponseValue response) {
                mFilterSettingView.showSavedLocations(response.getLocations());
            }

            @Override
            public void onError() {
                mFilterSettingView.showGetSavedLocationsError();
            }
        });

    }

    public void createLocation(Location location) {

        CreateLocation.RequestValues requestValues = new CreateLocation.RequestValues(location);

        mUseCaseHandler.execute(mCreateLocation, requestValues, new UseCase.UseCaseCallback<CreateLocation.ResponseValue>() {
            @Override
            public void onSuccess(CreateLocation.ResponseValue response) {
                setLocation(response.getLocation());
            }

            @Override
            public void onError() {
                mFilterSettingView.showCreateLocationError();
            }
        });

    }

    @Override
    public void finishLocationCreate(Location location) {
        String locationName = mFilterSettingView.getNameInput();
        if (locationName == null) {
            //show no empty string
            return;
        }
        location.setDescription(locationName);
        createLocation(location);
        mFilterSettingView.closeLocationNameDialog();
    }

    @Override
    public void resetFilter(){
        DesireFilter desireFilter = new DesireFilter();
        setFilter(desireFilter);
        mFilterSettingView.showDesireList();
    }


    @Override
    public void setFilterWithInput(){

        DesireFilter desireFilter = new DesireFilter();

        //get all Views
        RatingBar minRatingBar = (RatingBar)mActivity.findViewById(R.id.filterSettingRatingBar);
        EditText minRewardView = (EditText) mActivity.findViewById(R.id.filter_min_reward);
        Spinner radiusView = (Spinner) mActivity.findViewById(R.id.spinner_radius);

        //Category
        Category selectedCategory = mFilterSettingView.getSelectedCategory();
        if (selectedCategory == null) {
            return;
        } else if (selectedCategory.getName().equals("all")) {
            //nothing to do
        } else {
            desireFilter.setCategory(selectedCategory.getId());
            System.out.println(desireFilter.getCategory());
        }

        //Price
        int priceClicked = mFilterSettingView.getPriceClicked();
        //TODO: max price not hard coded!!!
        switch (priceClicked) {
            case 1:
                desireFilter.setPrice_max(10.0);
                break;
            case 2:
                desireFilter.setPrice_max(50.0);
                break;
            case 3:
                desireFilter.setPrice_max(100.0);
                break;
            default:
                break;
        }
        System.out.println(desireFilter.getPrice_max());

        //Min_Reward
        if (!minRewardView.getText().toString().equals("")) {
            Double minReward = Double.valueOf(minRewardView.getText().toString());
            desireFilter.setReward_min(minReward);
        }
        System.out.println(desireFilter.getReward_min());

        //Min_Rating
        float minRating = minRatingBar.getRating();
        if(minRating != 0.0) {
            desireFilter.setRating_min(minRating);
        }
        System.out.println(desireFilter.getRating_min());

        //Location
        /*Location location = mFilterSettingView.getLocation();
        if (location == null) {
            return;
        }
        desireFilter.setLon(location.getLon());
        desireFilter.setLat(location.getLat());
        System.out.println(desireFilter.getLat());
        System.out.println(desireFilter.getLon());
        System.out.println(location.getFullAddress());*/

        //Radius
        //TODO: differ between miles and kilometres?
        //now only km
        String radius = (String) radiusView.getSelectedItem();
        String[] array = mFilterSettingView.getRadiusArray();
        if (radius.equals(array[0])) {
            desireFilter.setRadius(1.0);
        } else if (radius.equals(array[1])) {
            desireFilter.setRadius(2.0);
        } else if (radius.equals(array[2])) {
            desireFilter.setRadius(5.0);
        } else if (radius.equals(array[3])) {
            desireFilter.setRadius(10.0);
        } else {
            //no restriction
        }
        System.out.println(desireFilter.getRadius());

        return;

        /*setFilter(desireFilter);

        mFilterSettingView.showFilterChangeSuccess();

        //mFilterSettingView.showDesireList();*/
    }

    @Override
    public void setFilter(DesireFilter desireFilter){
        WantHaversApplication.setCurDesireFilter(desireFilter);
    }

    @Override
    public void openMap() {
        mFilterSettingView.showMap();
    }

    @Override
    public void openLocationList() {
        mFilterSettingView.showLocationList();
        //TODO: loading start
        getSavedLocations();
        //TODO: loading end

    }

    @Override
    public void closeLocationList() {
        mFilterSettingView.closeLocationList();
    }

    @Override
    public void closeNameSelectionDialog() {
        mFilterSettingView.closeLocationNameDialog();
    }

    @Override
    public void setLocation(Location location) {
        mFilterSettingView.setLocation(location);
        mFilterSettingView.showLocationInView();
        mFilterSettingView.showRadiusOption();
    }
}
