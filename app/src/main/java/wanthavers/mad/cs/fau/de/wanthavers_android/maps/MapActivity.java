package wanthavers.mad.cs.fau.de.wanthavers_android.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.widget.Toast;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.DesireCreateActivity3rdStep;
import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.FilterSettingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.settings.SettingsActivity;
import de.fau.cs.mad.wanthavers.common.Location;
import android.databinding.DataBindingUtil;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.MapActBinding;

public class MapActivity extends Activity implements MapWrapperLayout.OnDragListener, OnMapReadyCallback, MapContract.View{



    // Google Map
    private GoogleMap googleMap;
    private MyMapFragment mMapFragment;

    private View mMarkerParentView;
    private ImageView mMarkerImageView;

    private int imageParentWidth = -1;
    private int imageParentHeight = -1;
    private int imageHeight = -1;
    private int centerX = -1;
    private int centerY = -1;

    private TextView mLocationTextView;
    private TextView mLocationTextHeaderView;
    private LocationManager mLocationManager;
    private GpsLocationTracker mGpsLocationTracker;
    private Location mSettingsLocation = null;
    private LatLng mlatLng;

    private MapContract.Presenter mMapPresenter;;
    private MapActBinding mViewDataBinding;
    private MapActionHandler mMapActionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras().getString("calledAct").equals("0")){
            //Swipe animations only in CreateDesire
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }

        MapActBinding binding =
                DataBindingUtil.setContentView(this, R.layout.map_act);
        mMapPresenter = new MapPresenter(MapActivity.this);
        binding.setPresenter(mMapPresenter);

        mMapActionHandler = new MapActionHandler(mMapPresenter);
        binding.setActionHandler(mMapActionHandler);


        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mGpsLocationTracker = new GpsLocationTracker(MapActivity.this, 49.573759d, 11.027389d); // computer science tower uni erlangen


        if (!forDesireDetail()) {
            //dont ask for GPS in DesireDetail
            if (!mGpsLocationTracker.isGpsEnabled()) {
                showAlert();
            }

        }


        mLocationTextView = (TextView) findViewById(R.id.location_text_view);
        mLocationTextHeaderView = (TextView) findViewById(R.id.location_text_view_header);
        mMarkerParentView = findViewById(R.id.marker_view_incl);
        mMarkerImageView = (ImageView) findViewById(R.id.marker_icon_view);


        initializeUI();

    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mMapPresenter = presenter;
    }

    private void initializeUI() {
        double latitude;
        double longitude;
        //Location settingsLocation = new Location();
        mSettingsLocation = (Location) getIntent().getExtras().getSerializable("location");

        if (mSettingsLocation != null) {
            //Location from Settings
            latitude = mSettingsLocation.getLat();
            longitude = mSettingsLocation.getLon();

        } else {
            //Location from GPS, Network
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();

        }

        mlatLng = new LatLng(latitude, longitude);
        updateLocation(mlatLng);
        try {
            // Loading map
            initializeMap();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        if (mMarkerParentView!= null && mMarkerImageView!=null) {
            imageParentWidth = mMarkerParentView.getWidth();
            imageParentHeight = mMarkerParentView.getHeight();
            imageHeight = mMarkerImageView.getHeight();

            centerX = imageParentWidth / 2;
            centerY = (imageParentHeight / 2);
        }
    }

    private void initializeMap() {
        if (googleMap == null) {
            mMapFragment = ((MyMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));
            mMapFragment.setOnDragListener(MapActivity.this);
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        //users location according to GPS or Network
        //or if permissions are not granted computer science tower uni erlangen
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlatLng, 15.0f));


        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //showing yourself on the map, if GPS is enabled
            googleMap.setMyLocationEnabled(true);

        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        //googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);

        if (forDesireDetail()) {

            //remove MarkerImage
            mMarkerImageView.setImageBitmap(null);

            //edit LocationTextHeader
            mLocationTextHeaderView.setText(getString(R.string.location_title));

            //change Button Text
            Button b = (Button) findViewById(R.id.button_select_location);
            b.setText(getString(R.string.alternative_Location_Button));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(mlatLng).title(getString(R.string.dropzone_marker));
            googleMap.addMarker(marker);

        }

        // check if map is created successfully or not
        if (googleMap == null) {
            showMessage(getString(R.string.maps_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapPresenter.start();
    }

    @Override
    public void onDrag(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Projection projection = (googleMap != null) ? googleMap.getProjection()
                    : null;
            //
            if (projection != null) {
                LatLng centerLatLng = projection.fromScreenLocation(new Point(
                        centerX, centerY));
                //no changes for DesireDetail
                if (!forDesireDetail()) {
                    updateLocation(centerLatLng);
                }
            }
        }
    }

    private void updateLocation(LatLng centerLatLng) {
        if (centerLatLng != null) {
            Geocoder geocoder = new Geocoder(MapActivity.this,
                    Locale.getDefault());

            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(centerLatLng.latitude,
                        centerLatLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {

                String addressIndex0 = (addresses.get(0).getAddressLine(0) != null) ? addresses
                        .get(0).getAddressLine(0) : null;
                String addressIndex1 = (addresses.get(0).getAddressLine(1) != null) ? addresses
                        .get(0).getAddressLine(1) : null;
                String addressIndex2 = (addresses.get(0).getAddressLine(2) != null) ? addresses
                        .get(0).getAddressLine(2) : null;
                String addressIndex3 = (addresses.get(0).getAddressLine(3) != null) ? addresses
                        .get(0).getAddressLine(3) : null;

                String completeAddress = addressIndex0 + "," + addressIndex1;

                if (addressIndex2 != null) {
                    completeAddress += "," + addressIndex2;
                }
                if (addressIndex3 != null) {
                    completeAddress += "," + addressIndex3;
                }
                if (completeAddress != null) {
                    mLocationTextView.setText(completeAddress);
                }
            }
        }
    }

    private void updateAdress(String loc){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng latLng = new LatLng(0,0);

        try {
            address = coder.getFromLocationName(loc,5);
            if (address==null) {
                return;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());


        } catch (IOException e) {
            e.printStackTrace();
        }

        updateLocation(latLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    public void editAddress(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.set_location));
        builder.setIcon(android.R.drawable.ic_menu_mylocation);
        //builder.setMessage("");

        // EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(mLocationTextView.getText());
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.Set_location_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String address = input.getText().toString();
                updateAdress(address);

                return;
            }
        });

        builder.setNegativeButton(getString(R.string.Set_location_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;}
        });

        builder.show();

    }

    public void moveToCurrentGpsPosition(){
        mGpsLocationTracker = new GpsLocationTracker(MapActivity.this, mlatLng.latitude, mlatLng.longitude);
        LatLng tmp = new LatLng(mGpsLocationTracker.getLatitude(),mGpsLocationTracker.getLongitude());
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp , 17.0f));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tmp, googleMap.getCameraPosition().zoom));

        updateLocation(mlatLng);
    }



    public void showMessage(String message) {
        if ( mMarkerParentView != null) {
            Snackbar.make(mMarkerParentView, message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showAlert() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.enable_gps))
                .setMessage(getString(R.string.enable_gps_text))
                .setPositiveButton(getString(R.string.enable_gps_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(getString(R.string.enable_gps_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    public void showFinishDesireCreate(){
        LatLng centerLatLng = googleMap.getProjection().fromScreenLocation(new Point(
                centerX, centerY));
        setLocation(mLocationTextView.getText().toString(), centerLatLng.latitude, centerLatLng.longitude);
    }

    private void setLocation(String location, double lat, double lng) {

        if (forFilterSettings()) {
            Intent intent = new Intent();

            intent.putExtra("desireLocation", location);
            intent.putExtra("desireLocationLat", Double.toString(lat));
            intent.putExtra("desireLocationLng", Double.toString(lng));
            if (mSettingsLocation != null) {
                intent.putExtra("desireLocationName", mSettingsLocation.getDescription());
                intent.putExtra("desireLocationId", Long.toString(mSettingsLocation.getId()));

            } else {
                intent.putExtra("desireLocationName", "");
                intent.putExtra("desireLocationId", "");

            }

            setResult(1, intent);
            finish();

        } else if (forDesireDetail()){
            finish();

        }else{ // DesireCreate
            String title = getIntent().getExtras().getString("desireTitle");
            String description = getIntent().getExtras().getString("desireDescription");
            String price = getIntent().getExtras().getString("desirePrice");
            //String reward = getIntent().getExtras().getString("desireReward");
            String currency = getIntent().getExtras().getString("desireCurrency");
            Uri image = getIntent().getExtras().getParcelable("desireImage");

            Intent intent = new Intent(this, DesireCreateActivity3rdStep.class);

            intent.putExtra("desireTitle", title);
            intent.putExtra("desireDescription", description);
            intent.putExtra("desirePrice", price);
            //intent.putExtra("desireReward", reward);
            intent.putExtra("desireCurrency", currency);
            intent.putExtra("desireImage", image);
            intent.putExtra("desireLocation", location);
            intent.putExtra("desireLocationLat", Double.toString(lat));
            intent.putExtra("desireLocationLng", Double.toString(lng));
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed(){

        if(forFilterSettings()) {
            Intent intent = new Intent();
            intent.putExtra("desireLocation", "");
            setResult(1, intent);
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }

    public boolean forDesireDetail(){
        return getIntent().getExtras().getString("calledAct").equals("2");
    }

    public boolean forFilterSettings() {
        return getIntent().getExtras().getString("calledAct").equals("1");
    }

}