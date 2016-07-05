package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GpsLocationTrackerLogic extends Service implements LocationListener {

    private Context mContext;
    private Location mLocation;
    private double mLatitude;// = 49.573840d;
    private double mLongitude;// =  11.027730d;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 1;
    private static final long MIN_TIME_FOR_UPDATE = 1;
    private LocationManager mLocationManager;


    public GpsLocationTrackerLogic(Context context, double startLat, double startLng) {
        mContext = context;
        mLatitude = startLat;
        mLongitude = startLng;
        getLocation();
        removeLocationListener();
    }


    private Location getLocation() {

        try {

            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


            if (Build.VERSION.SDK_INT >= 23) {
                if (isGpsEnabled() && mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    //check if GPS Signal is found
                    if (mLocation != null && mLocation.getLatitude()!=mLatitude && mLocation.getLongitude()!=mLongitude) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }


                }
                if (isNetworkEnabled() && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }

                }

            } else {
                if (isGpsEnabled()) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    //check if GPS Signal is found
                    if (mLocation != null && mLocation.getLatitude()!=mLatitude && mLocation.getLongitude()!=mLongitude) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        return mLocation;
                    }


                }
                if (isNetworkEnabled()) {

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
        if(mLocation!= null) {
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
        }
        return mLocation;
    }

    public boolean isGpsEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private boolean isNetworkEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnected());
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


    public de.fau.cs.mad.wanthavers.common.Location getGpsLocation() {
        de.fau.cs.mad.wanthavers.common.Location location = new  de.fau.cs.mad.wanthavers.common.Location();
        location.setLat(mLatitude);
        location.setLon(mLongitude);
        List<String> locationFullAddress = getAddressStrings(new LatLng(mLatitude, mLongitude));
        if (locationFullAddress == null || locationFullAddress.isEmpty()){
            return null;
        }
        if (locationFullAddress.get(0) == null ||
                locationFullAddress.get(1) == null){
            return null;
        }

        location.setFullAddress(locationFullAddress.get(0));
        location.setCityName(locationFullAddress.get(1));

        return location;
    }


    private List<String> getAddressStrings(LatLng loc) {
        List<String> AddressList = new ArrayList<>();
        String completeAddress;

        if (loc != null) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1);

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

                completeAddress = addressIndex0 + "," + addressIndex1;

                if (addressIndex2 != null) {
                    completeAddress += "," + addressIndex2;
                }
                if (addressIndex3 != null) {
                    completeAddress += "," + addressIndex3;
                }
                if (completeAddress != null) {
                    AddressList.add(0, completeAddress);

                    String city = null;
                    if (addresses.get(0).getLocality() != null)  {
                        city=addresses.get(0).getLocality();
                    }
                    else if (addresses.get(0).getSubAdminArea() != null)  {
                        city=addresses.get(0).getSubAdminArea();
                    }
                    AddressList.add(1, city);
                }
            }
        }

        return AddressList;

    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
        if(location!=null) {
            mLocation = location;
          //getLocation();
        }
    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (extras != null) {
          Location loc = (Location) extras.get(android.location.LocationManager.KEY_LOCATION_CHANGED);
          mLocation = loc;
          //getLocation();
         }
    }

    private void removeLocationListener() {
        if (mLocationManager != null) {
            //stop searching for a GPS Signal after leaving MapActivity
            if (Build.VERSION.SDK_INT >= 23) {
                if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.removeUpdates(this);
                    mLocationManager = null;
                    return;

                }
            }

            mLocationManager.removeUpdates(this);
            mLocationManager = null;

        }
    }

}
