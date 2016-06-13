package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
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
        RatingBar minRatingBar = (RatingBar)mActivity.findViewById(R.id.filter_setting_RatingBar);
        EditText minRewardView = (EditText) mActivity.findViewById(R.id.filter_min_reward);

        //get all Values
        float minRating = minRatingBar.getRating();
        Double minReward = Double.valueOf(minRewardView.getText().toString());

        //set all filter values

        desireFilter.setRating_min(minRating);
        desireFilter.setReward_min(minReward);

        setFilter(desireFilter);

        mFilterSettingView.showDesireList();
    }

    @Override
    public void setFilter(DesireFilter desireFilter){
        WantHaversApplication.setCurDesireFilter(desireFilter);
    }
}
