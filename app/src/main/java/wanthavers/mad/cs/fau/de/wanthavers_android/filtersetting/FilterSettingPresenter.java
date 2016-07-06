package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetCategory;

public class FilterSettingPresenter implements FilterSettingContract.Presenter {

    private final FilterSettingContract.View mFilterSettingView;
    private final UseCaseHandler mUseCaseHandler;
    private final FilterSettingActivity mActivity;
    private final GetCategory mGetCategory;

    public FilterSettingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull FilterSettingContract.View filterSettingView,
                                  @NonNull FilterSettingActivity filterSettingActivity, @NonNull GetCategory getCategory) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mFilterSettingView = checkNotNull(filterSettingView, "filtersetting view cannot be null");
        mActivity = checkNotNull(filterSettingActivity, "filtersetting activtiy cannot be null");
        mGetCategory = checkNotNull(getCategory);

        mFilterSettingView.setPresenter(this);
    }
    @Override
    public void start() {
        /*loadCurFilterSettings();*/
    }


    @Override
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

    @Override
    public void resetFilter(){
        DesireFilter desireFilter = new DesireFilter();
        desireFilter.setRadius(WantHaversApplication.getDefaultRadius());
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
        SeekBar radiusView = (SeekBar) mActivity.findViewById(R.id.radius_seekbar);

        //Category
        if (category != null) {
            desireFilter.setCategory(category.getId());
        }

        //Min_Price
        Double minPrice = null;
        if (!minPriceView.getText().toString().equals("")) {
            minPrice = Double.valueOf(minPriceView.getText().toString());
            desireFilter.setPrice_min(minPrice);
        }

        //Max_Price
        Double maxPrice = null;
        if (!maxPriceView.getText().toString().equals("")) {
            maxPrice = Double.valueOf(maxPriceView.getText().toString());
            desireFilter.setPrice_max(maxPrice);
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            mFilterSettingView.showWrongPricesSet();
            return;
        }

        //Min_Rating
        float minRating = minRatingBar.getRating();
        if(minRating != 0.0) {
            desireFilter.setRating_min(minRating);
        }

        //Location
        if (location != null) {
            desireFilter.setLon(location.getLon());
            desireFilter.setLat(location.getLat());
            desireFilter.setFullAddress(location.getFullAddress());
            desireFilter.setCityName(location.getCityName());
            desireFilter.setDescription(location.getDescription());
        }

        //Radius
        System.out.println(radiusView.getProgress() + 1.0);
        desireFilter.setRadius(radiusView.getProgress() + 1.0);

        setFilter(desireFilter);

        mFilterSettingView.showFilterChangeSuccess();

        mFilterSettingView.showDesireList();
    }

    @Override
    public void setFilter(DesireFilter desireFilter){
        WantHaversApplication.setDesireFilter(desireFilter, mActivity.getApplicationContext());
    }

    @Override
    public void openCategorySelection() {
        mFilterSettingView.showCategorySelection();
    }

    @Override
    public void changeLocation(Location filterLocation) {
        mFilterSettingView.openLocationList(filterLocation);
    }
}
