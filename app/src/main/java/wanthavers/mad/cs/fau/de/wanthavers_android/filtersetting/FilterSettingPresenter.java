package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetCategory;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSavedLocations;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

public class FilterSettingPresenter implements FilterSettingContract.Presenter {

    private final FilterSettingContract.View mFilterSettingView;
    private final UseCaseHandler mUseCaseHandler;
    private final FilterSettingActivity mActivity;
    private final GetSubcategories mGetSubcategories;
    private final GetCategory mGetCategory;
    private final CreateLocation mCreateLocation;
    private final UpdateLocation mUpdateLocation;
    private final GetSavedLocations mGetSavedLocations;
    private final DeleteLocation mDeleteLocation;

    public FilterSettingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull FilterSettingContract.View filterSettingView,
                                  @NonNull FilterSettingActivity filterSettingActivity,  @NonNull GetSubcategories getSubcategories,
                                  @NonNull CreateLocation createLocation, @NonNull GetSavedLocations getSavedLocations,
                                  @NonNull UpdateLocation updateLocation, @NonNull DeleteLocation deleteLocation,
                                  @NonNull GetCategory getCategory) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mFilterSettingView = checkNotNull(filterSettingView, "filtersetting view cannot be null");
        mActivity = checkNotNull(filterSettingActivity, "filtersetting activtiy cannot be null");
        mGetSubcategories = checkNotNull(getSubcategories);
        mCreateLocation = checkNotNull(createLocation);
        mGetSavedLocations = checkNotNull(getSavedLocations);
        mUpdateLocation = checkNotNull(updateLocation);
        mDeleteLocation = checkNotNull(deleteLocation);
        mGetCategory = checkNotNull(getCategory);

        mFilterSettingView.setPresenter(this);
    }
    @Override
    public void start() {
        loadCurFilterSettings();
    }

    public void loadCurFilterSettings() {

        //get views
        RatingBar minRatingBar = (RatingBar) mActivity.findViewById(R.id.filter_Setting_Rating_Bar);
        EditText minPriceView = (EditText) mActivity.findViewById(R.id.min_price_input);
        EditText maxPriceView = (EditText) mActivity.findViewById(R.id.max_price_input);
        Spinner radiusView = (Spinner) mActivity.findViewById(R.id.spinner_radius);

        //get values
        DesireFilter  curFilter = WantHaversApplication.getCurDesireFilter(mActivity.getApplicationContext());

        Long categoryId = curFilter.getCategory();
        Double maxPrice = curFilter.getPrice_max();
        Float minimalRating = curFilter.getRating_min();
        Double minimalPrice = curFilter.getPrice_min();
        Location location = curFilter.getLocation();
        Double radius = curFilter.getRadius();

        //set values
        if (categoryId != null) {
            getCategory(categoryId);
        }

        if (minimalPrice != null) {
            minPriceView.setText(Double.toString(minimalPrice));
        }

        if (maxPrice != null) {
            maxPriceView.setText(Double.toString(maxPrice));
        }

        if (minimalRating != null) {
            minRatingBar.setRating(minimalRating);
        }

        if (location != null) {
            setLocation(location);
        }

        //TODO: Radius not hard coded
        if (radius != null) {
            mFilterSettingView.showRadiusOption();
            if (radius == 1.0) {
                radiusView.setSelection(0);
            } else if (radius == 1.0) {
                radiusView.setSelection(0);
            } else if (radius == 2.0) {
                radiusView.setSelection(0);
            } else if (radius == 5.0) {
                radiusView.setSelection(0);
            } else if (radius == 10.0) {
                radiusView.setSelection(0);
            }
        }
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

    public void getCategory(long categoryId) {
        GetCategory.RequestValues requestValues = new GetCategory.RequestValues(categoryId);

        mUseCaseHandler.execute(mGetCategory, requestValues, new UseCase.UseCaseCallback<GetCategory.ResponseValue>() {
            @Override
            public void onSuccess(GetCategory.ResponseValue response) {
                Category category = response.getCategory();
                setCurFilterCategory(category);
            }

            @Override
            public void onError() {
                mFilterSettingView.showGetCategoriesError();
            }
        });
    }

    public void setCurFilterCategory(Category category) {
        mFilterSettingView.showCategory(category);
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
                mFilterSettingView.closeLocationList();
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
                mFilterSettingView.closeLocationNameDialog();
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

    public void updateLocation(final Location location) {
        UpdateLocation.RequestValues requestValues = new UpdateLocation.RequestValues(location.getId(), location);

        mUseCaseHandler.execute(mUpdateLocation, requestValues, new UseCase.UseCaseCallback<UpdateLocation.ResponseValue>() {
            @Override
            public void onSuccess(UpdateLocation.ResponseValue response) {
                updateLocationInView(location);
                mFilterSettingView.showLocationList();
                getSavedLocations();
            }

            @Override
            public void onError() {
                mFilterSettingView.closeLocationNameDialog();
                mFilterSettingView.showUpdateLocationError();
            }
        });
    }

    @Override
    public void finishLocationUpdate(Location location) {
        String locationName = mFilterSettingView.getNameInput();
        if (locationName == null) {
            //show no empty string
            return;
        }
        location.setDescription(locationName);
        System.out.println("new location name " + location.getDescription());
        updateLocation(location);
        mFilterSettingView.closeLocationNameDialog();
    }

    public void updateLocationInView(Location location) {
        Location curLocationFilter = mFilterSettingView.getCurLocationFilter();
        if (curLocationFilter == null) {
            //no location set in filter
            return;
        }
        long curLocationId = curLocationFilter.getId();
        if (curLocationId == location.getId()) {
            mFilterSettingView.setLocation(location);
        }
    }

    public void deleteLocation(final Location location) {
        DeleteLocation.RequestValues requestValues = new DeleteLocation.RequestValues(location.getId());

        mUseCaseHandler.execute(mDeleteLocation, requestValues, new UseCase.UseCaseCallback<DeleteLocation.ResponseValue>() {
            @Override
            public void onSuccess(DeleteLocation.ResponseValue response) {
                getSavedLocations();
                resetLocation(location);
            }

            @Override
            public void onError() {
                mFilterSettingView.closeLocationList();
                mFilterSettingView.showDeleteLocationError();
            }
        });
    }

    public void resetLocation(Location location) {
        Location curLocationFilter = mFilterSettingView.getCurLocationFilter();
        if (curLocationFilter == null) {
            //no location set in filter
            return;
        }
        long curLocationId = curLocationFilter.getId();
        if (curLocationId == location.getId()) {
            mFilterSettingView.deleteLocationInView();
        }
    }

    @Override
    public void resetFilter(){
        DesireFilter desireFilter = new DesireFilter();
        setFilter(desireFilter);
        mFilterSettingView.showDesireList();
    }


    @Override
    public void setFilterWithInput(Category category, Location location){

        DesireFilter desireFilter = new DesireFilter();

        //get all Views
        RatingBar minRatingBar = (RatingBar)mActivity.findViewById(R.id.filter_Setting_Rating_Bar);
        EditText minPriceView = (EditText) mActivity.findViewById(R.id.min_price_input);
        EditText maxPriceView = (EditText) mActivity.findViewById(R.id.max_price_input);
        Spinner radiusView = (Spinner) mActivity.findViewById(R.id.spinner_radius);

        //Category
        if (category != null) {
            desireFilter.setCategory(category.getId());
        }

        //Min_Price
        if (!minPriceView.getText().toString().equals("")) {
            Double minPrice = Double.valueOf(minPriceView.getText().toString());
            desireFilter.setPrice_min(minPrice);
        }

        //Max_Price
        if (!maxPriceView.getText().toString().equals("")) {
            Double maxPrice = Double.valueOf(maxPriceView.getText().toString());
            desireFilter.setPrice_min(maxPrice);
        }

        //Min_Rating
        float minRating = minRatingBar.getRating();
        if(minRating != 0.0) {
            desireFilter.setRating_min(minRating);
        }
        System.out.println("minRating: " + desireFilter.getRating_min());

        //Location
        if (location != null) {
            desireFilter.setLon(location.getLon());
            desireFilter.setLat(location.getLat());
            desireFilter.setLocation(location);

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
        }

        System.out.println("Lat: " + desireFilter.getLat());
        System.out.println("Lon: " + desireFilter.getLon());
        System.out.println("Radius: " + desireFilter.getRadius());

        setFilter(desireFilter);

        mFilterSettingView.showFilterChangeSuccess();

        mFilterSettingView.showDesireList();
    }

    @Override
    public void setFilter(DesireFilter desireFilter){
        WantHaversApplication.setCurDesireFilter(desireFilter);
    }

    @Override
    public void openMap(Location location) {
        mFilterSettingView.showMap(location);
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

    @Override
    public void openCategorySelection() {
        mFilterSettingView.showCategorySelection();
    }
}
