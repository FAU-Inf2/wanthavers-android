package wanthavers.mad.cs.fau.de.wanthavers_android.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.DesireCreateActivity3rdStep;
import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.FilterSettingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.settings.SettingsActivity;

public class MapActivity extends Activity implements MapWrapperLayout.OnDragListener {

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
    private LocationManager mLocationManager;
    private GpsLocationTracker mGpsLocationTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_act);
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mGpsLocationTracker = new GpsLocationTracker(MapActivity.this, 49.573759d, 11.027389d);
        if(!mGpsLocationTracker.isGpsEnabled()){
           showAlert();
        }

        initializeUI();


        Button b = (Button) findViewById(R.id.button_select_location);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MapAdresse", mLocationTextView.getText().toString());
                LatLng centerLatLng = googleMap.getProjection().fromScreenLocation(new Point(
                        centerX, centerY));
                setLocation(mLocationTextView.getText().toString(), centerLatLng.latitude, centerLatLng.longitude);
            }
        });


    }

    private void initializeUI() {
        mLocationTextView = (TextView) findViewById(R.id.location_text_view);
        mMarkerParentView = findViewById(R.id.marker_view_incl);
        mMarkerImageView = (ImageView) findViewById(R.id.marker_icon_view);
        double latitude = mGpsLocationTracker.getLatitude();
        double longitude = mGpsLocationTracker.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        updateLocation(latLng);
        try {
            // Loading map
            initializeMap(latLng);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        imageParentWidth = mMarkerParentView.getWidth();
        imageParentHeight = mMarkerParentView.getHeight();
        imageHeight = mMarkerImageView.getHeight();

        centerX = imageParentWidth / 2;
        centerY = (imageParentHeight / 2);
    }

    private void initializeMap(LatLng latLng) {
        if (googleMap == null) {
            mMapFragment = ((MyMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));
            mMapFragment.setOnDragListener(MapActivity.this);
            googleMap = mMapFragment.getMap();

            //users location according to GPS or Network
            //or if permissions are not granted computer science tower uni erlangen
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));


            // check if map is created successfully or not
            if (googleMap == null) {
                showMessage(getString(R.string.maps_error));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDrag(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Projection projection = (googleMap != null && googleMap.getProjection() != null) ? googleMap.getProjection()
                    : null;
            //
            if (projection != null) {
                LatLng centerLatLng = projection.fromScreenLocation(new Point(
                        centerX, centerY));
                updateLocation(centerLatLng);
            }
        }
    }

    private void updateLocation(LatLng centerLatLng) {
        if (centerLatLng != null) {
            Geocoder geocoder = new Geocoder(MapActivity.this,
                    Locale.getDefault());

            List<Address> addresses = new ArrayList<Address>();
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

    public void showMessage(String message) {
        Snackbar.make(mMarkerParentView , message, Snackbar.LENGTH_LONG).show();
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


    public void setLocation(String location, double lat, double lng){

        if(getIntent().getExtras().getString("desireTitle").compareTo("") == 0) {
            Intent intent = new Intent();

            intent.putExtra("desireLocation", location);
            intent.putExtra("desireLocationLat", Double.toString(lat));
            intent.putExtra("desireLocationLng", Double.toString(lng));

            Log.d("1", location);

            setResult(1, intent);
            finish();

        }else{
            String title = getIntent().getExtras().getString("desireTitle");
            String description = getIntent().getExtras().getString("desireDescription");
            String price = getIntent().getExtras().getString("desirePrice");
            String reward = getIntent().getExtras().getString("desireReward");
            String currency = getIntent().getExtras().getString("desireCurrency");
            Uri image = getIntent().getExtras().getParcelable("desireImage");

            Intent intent = new Intent(this, DesireCreateActivity3rdStep.class);

            intent.putExtra("desireTitle", title);
            intent.putExtra("desireDescription", description);
            intent.putExtra("desirePrice", price);
            intent.putExtra("desireReward", reward);
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
            Intent intent = new Intent();
            intent.putExtra("desireLocation", "");
            setResult(1, intent);
            super.onBackPressed();
        }
}