package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.WelcomeFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.LoginContract;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;

public class WelcomeFragment extends Fragment implements WelcomeContract.View {
    private WelcomeFragBinding mViewDataBinding;
    private WelcomeContract.Presenter mPresenter;
    private Uri image;

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


        mViewDataBinding = WelcomeFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

         return mViewDataBinding.getRoot();
    }



    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }


    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            image = data.getData();
            ImageView imageView = (ImageView) getView().findViewById(R.id.image_camera);
            imageView.setImageURI(image);

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(image.getEncodedPath());

                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        imageView.setRotation(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        imageView.setRotation(180);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(PathHelper.getRealPathFromURI(this.getContext().getApplicationContext(), image));
            mPresenter.setImage(file);

        }

    }
}
