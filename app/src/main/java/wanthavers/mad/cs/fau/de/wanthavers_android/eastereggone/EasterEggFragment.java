package wanthavers.mad.cs.fau.de.wanthavers_android.eastereggone;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.EastereggFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.WelcomeFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeContract;

public class EasterEggFragment extends Fragment implements EasterEggContract.View {
    private EastereggFragBinding mViewDataBinding;
    private EasterEggContract.Presenter mPresenter;
    private Uri image;
    private ProgressDialog mLoadingDialog;

    public EasterEggFragment(){
        //Requires empty public constructor
    }
    public static EasterEggFragment newInstance(){ return new EasterEggFragment();}

    @Override
    public void setPresenter(@NonNull EasterEggContract.Presenter presenter) {
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

        mViewDataBinding = EastereggFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

         return mViewDataBinding.getRoot();
    }

    public void setPresenter(EasterEggPresenter presenter){ mPresenter = presenter;}


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


}
