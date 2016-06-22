package wanthavers.mad.cs.fau.de.wanthavers_android.maps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.ContextThemeWrapper;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;


public class GpsLocationTracker extends Service implements LocationListener {

    private Context mContext;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 5;
    private static final long MIN_TIME_FOR_UPDATE = 0;
    private LocationManager mLocationManager;


    public GpsLocationTracker(Context context, double startLat, double startLng) {
        mContext = context;
        mLatitude = startLat;
        mLongitude = startLng;
        getLocation();
    }


    public Location getLocation() {

        try {

            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


            if (Build.VERSION.SDK_INT >= 23) {
                if (isGpsEnabled() && mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }


                }else if (isNetworkEnabled() && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }

                }

            }else{
                if (isGpsEnabled()) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }


                }else if (isNetworkEnabled()) {

                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }
                }

            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        return mLocation;
    }

    public boolean isGpsEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private boolean isNetworkEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
        getLocation();
    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}