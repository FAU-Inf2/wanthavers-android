package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.WelcomeFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;

public class WelcomeFragment extends Fragment implements WelcomeContract.View {
    private WelcomeFragBinding mViewDataBinding;
    private WelcomeContract.Presenter mPresenter;
    private Uri image;
    private ProgressDialog mLoadingDialog;
    private final int MAX_IMAGE_SIZE = 1200;

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
            image = imageLogic.scaleDown(image, MAX_IMAGE_SIZE, orientation);
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

}
