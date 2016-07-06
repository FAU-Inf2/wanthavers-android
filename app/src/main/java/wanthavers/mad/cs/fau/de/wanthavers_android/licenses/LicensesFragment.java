package wanthavers.mad.cs.fau.de.wanthavers_android.licenses;

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

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LicensesFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.WelcomeFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;

public class LicensesFragment extends Fragment{


    public LicensesFragment(){
        //Requires empty public constructor
    }
    public static LicensesFragment newInstance(){ return new LicensesFragment();}


    @Override
    public void onResume()  {
        super.onResume();
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LicensesFragBinding licensesFragBinding = LicensesFragBinding.inflate(inflater, container, false);

         return licensesFragBinding.getRoot();
    }







}
