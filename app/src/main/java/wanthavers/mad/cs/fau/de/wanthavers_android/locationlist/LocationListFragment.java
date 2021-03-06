package wanthavers.mad.cs.fau.de.wanthavers_android.locationlist;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LocationlistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LocationlistPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.OnSwipeTouchListener;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationListFragment extends Fragment implements LocationListContract.View {

    private LocationListAdapter mLocationListAdapter;
    private LocationListActionHandler mLocationListActionHandler;
    private LocationListContract.Presenter mPresenter;
    private LocationlistFragBinding mLocationListFragBinding;
    private Dialog mSetCustomLocationName;
    private LocationlistPopupBinding mLocationlistPopupBinding;
    private int mCalledAct;
    private static boolean mFirstCalled;

    public LocationListFragment() {
        //requires empty public constructor
    }

    public static LocationListFragment newInstance() {
        return new LocationListFragment();
    }

    @Override
    public void setPresenter(@NonNull LocationListContract.Presenter presenter) {
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

        mLocationListFragBinding = LocationlistFragBinding.inflate(inflater, container, false);

        mLocationListActionHandler = new LocationListActionHandler(mPresenter);

        mLocationListFragBinding.setActionHandler(mLocationListActionHandler);

        Location curFilterLocation = (Location) getActivity().getIntent().getExtras().getSerializable("filterlocation");
        mLocationListFragBinding.setLocation(curFilterLocation);
        Location defaultLocation = WantHaversApplication.getLocation(getContext());
        mLocationListFragBinding.setDefaultLocation(defaultLocation);

        //use Linear Layout Manager
        RecyclerView recyclerView = mLocationListFragBinding.locationList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.list_divider_mediumdark,1));

        //set up location list
        mLocationListAdapter = new LocationListAdapter(new ArrayList<Location>(0), mLocationListActionHandler);
        recyclerView.setAdapter(mLocationListAdapter);

        mCalledAct = Integer.parseInt(getActivity().getIntent().getStringExtra("calledAct"));
        if (mCalledAct == 0) {
            layoutChangesForDesireCreate(recyclerView);
        } else if (mCalledAct == 4) {
            mLocationListFragBinding.buttonCancelLocationChoice.setVisibility(View.GONE);
            mLocationListFragBinding.fabAddLocation.setY(mLocationListFragBinding.fabAddLocation.getY()+100);
        }

        mFirstCalled = true;

        return mLocationListFragBinding.getRoot();
    }

    public void showSavedLocations(List<Location> locationList) {
        mLocationListAdapter.replaceData(locationList);
        if (locationList.size() == 0) {
            if (mFirstCalled) {
                mFirstCalled = false;
                //showMap(null);
            }
            mLocationListFragBinding.locationListEmpty.setVisibility(View.VISIBLE);
        } else {
            mLocationListFragBinding.locationListEmpty.setVisibility(View.GONE);
        }
    }

    private void layoutChangesForDesireCreate(RecyclerView recyclerView){
        getActivity().overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter, mLocationListActionHandler));

        Button cancelLocationChoice = mLocationListFragBinding.buttonCancelLocationChoice;
        ViewGroup.LayoutParams params = cancelLocationChoice.getLayoutParams();
        params.height = 0;
        cancelLocationChoice.setLayoutParams(params);
        cancelLocationChoice.setVisibility(View.INVISIBLE);
    }

    @Override
    public void closeLocationList(Location location) {

        if (mCalledAct == 1) {

            Intent intent = new Intent();
            intent.putExtra("selectedLocation", location);
            getActivity().setResult(1, intent);
            getActivity().finish();

        } else if(mCalledAct == 0){

            Intent intent = new Intent();
            intent.putExtra("locationObject", location);

            intent.putExtra("calledAct", String.valueOf(mCalledAct));

            getActivity().setResult(0, intent);
            getActivity().finish();

        } else if(mCalledAct == 4) {

        }
    }

    @Override
    public void showMap(Location location) {

            Intent intent = new Intent(getContext(), MapActivity.class);
            intent.putExtra("location", location);
            //for distinguishing which activity started the map
            intent.putExtra("calledAct", getActivity().getIntent().getExtras().getString("calledAct"));

            startActivityForResult(intent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if(data == null){
            return;
        }

        if(data.hasExtra("locationObject") || !(data.getStringExtra("desireLocation").equals(""))) {

            if (resultCode == 1) {
                //get values
                String locationAddress = data.getStringExtra("desireLocation");
                Double lat = Double.parseDouble(data.getStringExtra("desireLocationLat"));
                Double lon = Double.parseDouble(data.getStringExtra("desireLocationLng"));
                String locationName = data.getStringExtra("desireLocationName");
                String locationId = data.getStringExtra("desireLocationId");
                String cityName = data.getStringExtra("desireLocationCity");

                long userId = new DesireLogic(getContext()).getLoggedInUserId();

                //Create Location object
                Location location = new Location();
                if (locationAddress != null) {
                    location.setFullAddress(locationAddress);
                }
                if (lat != null) {
                    location.setLat(lat);
                }
                if (lon != null) {
                    location.setLon(lon);
                }
                location.setUserId(userId);
                if (locationName != null) {
                    location.setDescription(locationName);
                }
                if (cityName != null) {
                    location.setCityName(cityName);
                }


                if (!locationId.equals("")) {
                    location.setId(Long.parseLong(locationId));
                }

                setCustomLocationName(location);

            }else if(resultCode == 0){

                Location location = (Location) data.getSerializableExtra("locationObject");
                Intent intent = new Intent();
                intent.putExtra("locationObject", location);
                getActivity().setResult(0, intent);
                getActivity().finish();
            }
        }
    }

    public void setCustomLocationName(Location location) {

        mSetCustomLocationName = new Dialog(getContext());

        mLocationlistPopupBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.locationlist_popup, null, false);
        mSetCustomLocationName.setContentView(mLocationlistPopupBinding.getRoot());
        mLocationlistPopupBinding.setLocation(location);
        mLocationlistPopupBinding.setActionHandler(mLocationListActionHandler);

        if (!location.getDescription().equals("")) {
            mLocationlistPopupBinding.buttonFrameSubmitNameChoice.setVisibility(View.GONE);
            mLocationlistPopupBinding.buttonFrameUpdateNameChoice.setVisibility(View.VISIBLE);
            mLocationlistPopupBinding.setCustomLocationName.setText(location.getDescription());
        }

        mSetCustomLocationName.show();
    }

    @Override
    public Location getFilterLocation() {
        return mLocationListFragBinding.getLocation();
    }

    @Override
    public void setFilterLocation(Location location) {
        mLocationListFragBinding.setLocation(location);
    }

    @Override
    public String getNameInput() {
        String nameInput = mLocationlistPopupBinding.setCustomLocationName.getText().toString();
        if (nameInput.equals("")) {
            mLocationlistPopupBinding.setCustomLocationName.setHint(R.string.location_name_not_set);
            return null;
        }
        return nameInput;
    }

    @Override
    public void closeLocationNameDialog() {
        mSetCustomLocationName.dismiss();
    }


    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
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

}
