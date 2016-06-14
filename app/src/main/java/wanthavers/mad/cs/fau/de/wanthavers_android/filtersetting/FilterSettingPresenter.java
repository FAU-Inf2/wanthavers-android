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
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;

public class FilterSettingPresenter implements FilterSettingContract.Presenter {

    private final FilterSettingContract.View mFilterSettingView;
    private final UseCaseHandler mUseCaseHandler;
    private final FilterSettingActivity mActivity;
    private final GetSubcategories mGetSubcategories;

    public FilterSettingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull FilterSettingContract.View filterSettingView,
                                  @NonNull FilterSettingActivity filterSettingActivity,  @NonNull GetSubcategories getSubcategories) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mFilterSettingView = checkNotNull(filterSettingView, "filtersetting view cannot be null");
        mActivity = checkNotNull(filterSettingActivity, "filtersetting activtiy cannot be null");
        mGetSubcategories = checkNotNull(getSubcategories);

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
        InstantAutoComplete autoComplete = (InstantAutoComplete) mActivity.findViewById(R.id.spinner_category);
        RatingBar minRatingBar = (RatingBar)mActivity.findViewById(R.id.filterSettingRatingBar);
        EditText minRewardView = (EditText) mActivity.findViewById(R.id.filter_min_reward);
        Spinner radiusView = (Spinner) mActivity.findViewById(R.id.spinner_radius);

        //Category
        /*Category selectedCategory = autoComplete.
        if (selectedCategory != null) {
            desireFilter.setCategory(selectedCategory.getId());
            System.out.println("Found Category");
        }*/
        /*int pos = autoComplete.getListSelection();
        Category category = (Category) autoComplete.getAdapter().getItem(pos);
        System.out.println(category.getName());*/

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
        desireFilter.setRating_min(minRating);
        System.out.println(desireFilter.getRating_min());

        //Location
        //TODO: make local list with names
        Location location = mFilterSettingView.getLocation();
        if (location == null) {
            return;
        }
        desireFilter.setLon(location.getLon());
        desireFilter.setLat(location.getLat());

        //Radius
        //TODO: differ between miles and kilometres
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

        setFilter(desireFilter);

        mFilterSettingView.showFilterChangeSuccess();

        mFilterSettingView.showDesireList();
    }

    @Override
    public void setFilter(DesireFilter desireFilter){
        WantHaversApplication.setCurDesireFilter(desireFilter);
    }

    @Override
    public void openMap() {
        mFilterSettingView.showMap();
    }
}
