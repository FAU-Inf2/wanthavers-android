package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingLocationPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingLocationSetnameBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DividerItemDecoration;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilterSettingFragment extends Fragment implements FilterSettingContract.View {
    private FilterSettingContract.Presenter mPresenter;
    private FiltersettingFragBinding mFilterSettingFragBinding;
    private CategoryAdapter mCategoryListAdapter;
    private FilterSettingActionHandler mFilterSettingActionHandler;
    private LocationAdapter mLocationListAdapter;
    private Dialog mLocationList, mSetCustomLocationName;
    private FiltersettingLocationSetnameBinding mFiltersettingLocationSetnameBinding;

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

        mFilterSettingActionHandler = new FilterSettingActionHandler(mPresenter, mFilterSettingFragBinding);
        mFilterSettingFragBinding.setActionHandler(mFilterSettingActionHandler);

        //Set up autocompletetextview
        mCategoryListAdapter = new CategoryAdapter(getContext(), R.layout.category_item, new ArrayList<Category>(0), mFilterSettingActionHandler);
        final InstantAutoComplete autoCompleteTextView = (InstantAutoComplete) mFilterSettingFragBinding.spinnerCategory;
        //autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setShowAlways(true);

        /*autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selected = (Category) parent.getItemAtPosition(position);
                mCategoryListAdapter.setSelected(selected);
            }
        });*/
        autoCompleteTextView.setAdapter(mCategoryListAdapter);

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
    public void showLocationList() {
        mLocationList = new Dialog(getContext());

        FiltersettingLocationPopupBinding mFiltersettingLocationPopupBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.filtersetting_location_popup, null, false);
        mLocationList.setContentView(mFiltersettingLocationPopupBinding.getRoot());

        mFiltersettingLocationPopupBinding.setActionHandler(mFilterSettingActionHandler);

        //use Linear Layout Manager
        RecyclerView recyclerView = mFiltersettingLocationPopupBinding.locationList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.list_divider_mediumdark,1));

        //set up location list
        mLocationListAdapter = new LocationAdapter(new ArrayList<Location>(0), mFilterSettingActionHandler);
        recyclerView.setAdapter(mLocationListAdapter);

        mFiltersettingLocationPopupBinding.fabAddLocation.bringToFront();

        mLocationList.show();
        Window window = mLocationList.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1000);
    }

    @Override
    public void closeLocationList() {
        mLocationList.dismiss();
    }

    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMap(Location location) {
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("desireTitle", "");
        intent.putExtra("location", location);
        startActivityForResult(intent, 1);
    }

    /*@Override
    public Location getLocation() {
        Location ret = new Location();
        ret.setLat(mLat);
        ret.setLon(mLon);
        ret.setFullAddress( mLocation);

        return ret;

    }*/

    @Override
    public String[] getRadiusArray() {
        return getResources().getStringArray(R.array.radius);
    }

    @Override
    public void showCategories(List<Category> categories) {
        mCategoryListAdapter.replaceData(categories);
        mCategoryListAdapter.getFilter().filter(null);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGetCategoriesError() {
        showMessage(getString(R.string.get_categories_error));
    }

    @Override
    public void showNoLocationSetError() {
        showMessage(getString(R.string.get_no_location_set_error));
    }

    @Override
    public void showFilterChangeSuccess() {
        showMessage(getString(R.string.filter_change_success));
    }

    @Override
    public void showCreateLocationError() {
        showMessage(getString(R.string.create_location_error));
    }

    @Override
    public void showUpdateLocationError() {
        showMessage(getString(R.string.update_location_error));
    }

    @Override
    public void showGetSavedLocationsError() {
        showMessage(getString(R.string.get_saved_locations_error));
    }

    @Override
    public void showDeleteLocationError() {
        showMessage(getString(R.string.delete_location_error));
    }

    @Override
    public int getPriceClicked() {
        return mFilterSettingActionHandler.getPriceClicked();
    }

    @Override
    public void setPriceClicked(int clicked) {
        mFilterSettingActionHandler.buttonChangeColor(clicked);
    }

    @Override
    public Category getSelectedCategory() {
        InstantAutoComplete instantAutoComplete = mFilterSettingFragBinding.spinnerCategory;
        String input = instantAutoComplete.getText().toString();
        if (input.compareTo("") == 0) {
            Category category = new Category();
            category.setName("all");
            return category;
        }

        for (int i = 0; i < mCategoryListAdapter.getCount(); i++) {
            String tmp = mCategoryListAdapter.getItem(i).getName();
            if (input.compareTo(tmp) == 0) {
                return mCategoryListAdapter.getItem(i);
            }
        }

        showMessage(getString(R.string.no_category_match));

        return null;
    }

    @Override
    public void showSavedLocations(List<Location> locationList) {
        mLocationListAdapter.replaceData(locationList);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(!data.getExtras().getString("desireLocation").equals("")) { //checks if backbutton is pressed

            //get values
            String locationAddress = data.getExtras().getString("desireLocation");
            double lat = Double.parseDouble(data.getExtras().getString("desireLocationLat"));
            double lon = Double.parseDouble(data.getExtras().getString("desireLocationLng"));
            String locationName = data.getExtras().getString("desireLocationName");
            String locationId = data.getExtras().getString("desireLocationId");

            long userId = new DesireLogic(getContext()).getLoggedInUserId();

            //Create Location object
            Location location = new Location();
            location.setFullAddress(locationAddress);
            location.setLat(lat);
            location.setLon(lon);
            location.setUserId(userId);
            location.setDescription(locationName);
            if (locationId != "") {
                System.out.println("Reached");
                location.setId(Long.parseLong(locationId));
                System.out.println("Reached2");
            }

            //set custom location name dialog
            closeLocationList();
            setCustomLocationName(location);
        }
    }

    public void setCustomLocationName(Location location) {

        mSetCustomLocationName = new Dialog(getContext());

        mFiltersettingLocationSetnameBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.filtersetting_location_setname, null, false);
        mSetCustomLocationName.setContentView(mFiltersettingLocationSetnameBinding.getRoot());
        mFiltersettingLocationSetnameBinding.setLocation(location);
        mFiltersettingLocationSetnameBinding.setActionHandler(mFilterSettingActionHandler);

        if (!location.getDescription().equals("")) {
            mFiltersettingLocationSetnameBinding.buttonFrameSubmitNameChoice.setVisibility(View.GONE);
            mFiltersettingLocationSetnameBinding.buttonFrameUpdateNameChoice.setVisibility(View.VISIBLE);
            mFiltersettingLocationSetnameBinding.setCustomLocationName.setText(location.getDescription());
        }

        mSetCustomLocationName.show();
    }

    @Override
    public String getNameInput() {
        String nameInput = mFiltersettingLocationSetnameBinding.setCustomLocationName.getText().toString();
        if (nameInput.equals("")) {
            mFiltersettingLocationSetnameBinding.setCustomLocationName.setHint(R.string.location_name_not_set);
            return null;
        }
        return nameInput;
    }

    @Override
    public void closeLocationNameDialog() {
        mSetCustomLocationName.dismiss();
    }

    @Override
    public void setLocation(Location location) {
        mFilterSettingFragBinding.setLocation(location);
    }

    @Override
    public void showLocationInView() {
        mFilterSettingFragBinding.noLocationSelected.setVisibility(View.GONE);
        mFilterSettingFragBinding.selectedCustomLocationName.setVisibility(View.VISIBLE);
        mFilterSettingFragBinding.selectedLocationString.setVisibility(View.VISIBLE);
    }

    @Override
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
    public String getCurLocationFilter() {
        return mFilterSettingFragBinding.selectedCustomLocationName.getText().toString();
    }
}