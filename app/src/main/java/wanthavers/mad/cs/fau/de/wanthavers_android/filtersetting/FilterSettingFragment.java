package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.categorylist.CategoryListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.locationlist.LocationListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilterSettingFragment extends Fragment implements FilterSettingContract.View {
    private FilterSettingContract.Presenter mPresenter;
    private FiltersettingFragBinding mFilterSettingFragBinding;

    public FilterSettingFragment() {
        //Requires empty public constructor
    }

    public static FilterSettingFragment newInstance() {
        return new FilterSettingFragment();
    }

    @Override
    public void setPresenter(@NonNull FilterSettingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mFilterSettingFragBinding = FiltersettingFragBinding.inflate(inflater, container, false);

        mFilterSettingFragBinding.setPresenter(mPresenter);

        //Set up radius seekbar
        SeekBar seekBar = (SeekBar) mFilterSettingFragBinding.radiusSeekbar;
        final WantHaversTextView radiusStatus = (WantHaversTextView) mFilterSettingFragBinding.filterRadiusStatus;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Integer mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress + 1;
                String curRadius = mProgress.toString();
                radiusStatus.setText(curRadius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String curRadius = mProgress.toString();
                radiusStatus.setText(curRadius);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable("filterCategory") != null) {
                showCategory((Category) savedInstanceState.getSerializable("filterCategory"));
            }
            if (savedInstanceState.getSerializable("filterLocation") != null) {
                setLocation((Location) savedInstanceState.getSerializable("filterLocation"));
            }
            if (savedInstanceState.getSerializable("filterRadius") != null) {
                Integer locationRadius = ((Integer) savedInstanceState.getSerializable("filterRadius") + 1);
                seekBar.setProgress(locationRadius - 1);
                radiusStatus.setText(locationRadius.toString());
            }
        }

        setLocation(WantHaversApplication.getLocation(getContext()));

        mPresenter.start();
        loadCurFilterSettings(mFilterSettingFragBinding.getCategory(), mFilterSettingFragBinding.getLocation());

        return mFilterSettingFragBinding.getRoot();
    }

    public void loadCurFilterSettings(Category category, Location location) {

        //get views
        RatingBar minRatingBar = mFilterSettingFragBinding.filterSettingRatingBar;
        EditText minPriceView = mFilterSettingFragBinding.minPriceInput;
        EditText maxPriceView = mFilterSettingFragBinding.maxPriceInput;
        SeekBar radiusView = mFilterSettingFragBinding.radiusSeekbar;

        //get values
        DesireFilter curFilter = WantHaversApplication.getDesireFilter(getContext());

        Long categoryId = curFilter.getCategory();
        Double maxPrice = curFilter.getPrice_max();
        Float minimalRating = curFilter.getRating_min();
        Double minimalPrice = curFilter.getPrice_min();
        Location filterLocation = curFilter.getLocation();
        Double radius = curFilter.getRadius();

        //set values
        if (categoryId != null && category == null) {
            mPresenter.getCategory(categoryId);
        }

        if (minimalPrice != null) {
            minPriceView.setText(Integer.toString(minimalPrice.intValue()));
        }

        if (maxPrice != null) {
            maxPriceView.setText(Integer.toString(maxPrice.intValue()));
        }

        if (minimalRating != null) {
            minRatingBar.setRating(minimalRating);
        }

        if (filterLocation != null && location == null) {
            setLocation(filterLocation);
        }

        if (radius != null) {
            radiusView.setProgress(radius.intValue() - 1);
        }
    }

    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCategorySelection() {
        Intent intent = new Intent(getContext(), CategoryListActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void openLocationList(Location filterLocation) {
        Intent intent = new Intent(getContext(), LocationListActivity.class);
        intent.putExtra("filterlocation", filterLocation);
        intent.putExtra("calledAct", "1"); //for distinguishing which activity started the map
        startActivityForResult(intent, 1);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGetCategoriesError() {
        showMessage(getString(R.string.get_categories_error));
    }

    @Override
    public void showFilterChangeSuccess() {
        showMessage(getString(R.string.filter_change_success));
    }

    @Override
    public void showWrongPricesSet() {
        showMessage(getString(R.string.wrong_price_set));
    }

    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        //case: category-backpress
        if (data == null) {
            return;
        }

        //case: category set
        if(data.hasExtra("selectedCategory")) {
            Category category = (Category) data.getExtras().getSerializable("selectedCategory");
            showCategory(category);
            return;
        }

        //case: location
        Location location = (Location) data.getExtras().getSerializable("selectedLocation");
        if (location != null) {
            setLocation(location);
        }
    }

    @Override
    public void setLocation(Location location) {
        mFilterSettingFragBinding.setLocation(location);
        System.out.println("reached set loaction");
        WantHaversApplication.setLocation(location, getActivity().getApplicationContext());
        showLocationInView();
    }

    public void showLocationInView() {
        if (mFilterSettingFragBinding.getLocation().getDescription() != null) {
            mFilterSettingFragBinding.selectedCustomLocationName
                    .setText(mFilterSettingFragBinding.getLocation().getDescription());
            mFilterSettingFragBinding.selectedCustomLocationName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCategory(Category category) {

        mFilterSettingFragBinding.setCategory(category);

        Media media = category.getImage();

        if (media != null) {
            final ImageView profileView = mFilterSettingFragBinding.selectedImageCategory;
            Picasso.with(mFilterSettingFragBinding.getRoot().getContext()).load(media.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mFilterSettingFragBinding.selectedImageCategory;
            profileView.setImageResource(R.drawable.no_pic);
        }

        mFilterSettingFragBinding.selectedImageCategory.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.selectedCategoryName.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.noCategorySelected.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("filterCategory", mFilterSettingFragBinding.getCategory());
        state.putSerializable("filterLocation", mFilterSettingFragBinding.getLocation());
        state.putSerializable("filterRadius", mFilterSettingFragBinding.radiusSeekbar.getProgress());
    }
}