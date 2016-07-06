package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.Map;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.WelcomeFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.GpsLocationTrackerLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;

public class WelcomeFragment extends Fragment implements WelcomeContract.View {
    private WelcomeFragBinding mViewDataBinding;
    private WelcomeContract.Presenter mPresenter;
    private Uri image;
    private ProgressDialog mLoadingDialog;

    public WelcomeFragment(){
        //Requires empty public constructor
    }
    public static WelcomeFragment newInstance(){ return new WelcomeFragment();}

    @Override
    public void setPresenter(@NonNull WelcomeContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }
    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //check for Location Runtime Permissions
        isFineLocationPermissionGranted();

        mViewDataBinding = WelcomeFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

         return mViewDataBinding.getRoot();
    }



    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("calledAct", "3");
        startActivityForResult(intent, 30);


       // Intent intent = new Intent(getContext(), DesireListActivity.class);
        //startActivity(intent);
    }


    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && data!= null){

            Intent intent = new Intent(getContext(), DesireListActivity.class);
            String fullAddress = data.getExtras().getString("desireLocation");
            Double lat = Double.parseDouble(data.getStringExtra("desireLocationLat"));
            Double lon = Double.parseDouble(data.getStringExtra("desireLocationLng"));
            String cityName = data.getExtras().getString("desireLocationCity");

            Location location = new Location();
            if (fullAddress != null) {
                location.setFullAddress(fullAddress);
            }
            if (lat != null) {
                location.setLat(lat);
            }
            if (lon != null) {
                location.setLon(lon);
            }
            if (cityName != null) {
                location.setCityName(cityName);
            }
            WantHaversApplication.setLocation(location, getContext());
            startActivity(intent);
        }

        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            showLoadingProgress();

            image = data.getData();
            ImageView imageView = (ImageView) getView().findViewById(R.id.image_camera);
            imageView.setImageURI(image);

            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};

            Cursor cur = getContext().getContentResolver().query(image, orientationColumn, null, null, null);
            int orientation = -1;
            if (cur != null && cur.moveToFirst()) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }

            //resizing high resolution images
            SelectImageLogic imageLogic = new SelectImageLogic(getContext());
            image = imageLogic.scaleDown(image, imageLogic.getMaxImageSize(), orientation);
            imageView.setImageURI(image);

            File file = new File(PathHelper.getRealPathFromURI(this.getContext().getApplicationContext(), image));
            mPresenter.setImage(file);

        }

    }

    public void showLoadingProgress() {
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setTitle(getString(R.string.changeProfilePic_loadingProgress_title));
        mLoadingDialog.setMessage(getString(R.string.createDesire_loadingProgress_message));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();
    }

    public void endLoadingProgress() {
        mLoadingDialog.cancel();
    }

    private boolean isGpsEnabled() {
        LocationManager locManager= (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public boolean isFineLocationPermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        }else { //permission is automatically granted because sdk<23
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_DENIED && requestCode ==2){
            showMessage(getString(R.string.declined_location_runtime_permission));
        }

        if(grantResults[0]== PackageManager.PERMISSION_GRANTED && requestCode == 1){
            SelectImageLogic imageLogic = new SelectImageLogic(getActivity());
            imageLogic.selectImageForDesire();
        }else if (grantResults[0]== PackageManager.PERMISSION_DENIED && requestCode == 1){
            showMessage(getString(R.string.declined_memory_runtime_permission));
        }
    }

}
