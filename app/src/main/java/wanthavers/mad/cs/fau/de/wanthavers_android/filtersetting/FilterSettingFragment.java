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
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.categorylist.CategoryListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.locationlist.LocationListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

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
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mFilterSettingFragBinding = FiltersettingFragBinding.inflate(inflater, container, false);

        mFilterSettingFragBinding.setPresenter(mPresenter);

        //Set up radius spinner
        Spinner spinner = (Spinner) mFilterSettingFragBinding.spinnerRadius;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int defaultLocationId = adapter.getPosition("5km");
        spinner.setSelection(defaultLocationId);

        return mFilterSettingFragBinding.getRoot();
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
        startActivityForResult(intent, 1);
    }

    @Override
    public String[] getRadiusArray() {
        return getResources().getStringArray(R.array.radius);
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
        } else {
            deleteLocationInView();
        }
    }

    @Override
    public void setLocation(Location location) {
        mFilterSettingFragBinding.setLocation(location);
        showLocationInView();
        showRadiusOption();
    }

    public void showLocationInView() {
        mFilterSettingFragBinding.noLocationSelected.setVisibility(View.GONE);
        mFilterSettingFragBinding.selectedCustomLocationName.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.selectedLocationString.setVisibility(View.VISIBLE);
    }

    public void deleteLocationInView() {
        mFilterSettingFragBinding.noLocationSelected.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.selectedCustomLocationName.setVisibility(View.GONE);
        mFilterSettingFragBinding.selectedLocationString.setVisibility(View.GONE);
        mFilterSettingFragBinding.selectRadius.setVisibility(View.GONE);
    }

    @Override
    public void showRadiusOption() {
        mFilterSettingFragBinding.selectRadius.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCategory(Category category) {

        mFilterSettingFragBinding.setCategory(category);

        Media media = category.getImage();

        if (media != null) {
            final ImageView profileView = mFilterSettingFragBinding.selectedImageCategory;
            Picasso.with(mFilterSettingFragBinding.getRoot().getContext()).load(media.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mFilterSettingFragBinding.selectedImageCategory;
            profileView.setImageResource(R.drawable.no_pic);
        }

        mFilterSettingFragBinding.selectedImageCategory.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.selectedCategoryName.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.noCategorySelected.setVisibility(View.GONE);
    }
}