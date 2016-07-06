package wanthavers.mad.cs.fau.de.wanthavers_android.settings;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.SettingsFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.FilterSettingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.PathHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsFragment extends Fragment implements SettingsContract.View {
    private SettingsContract.Presenter mPresenter;
    private SettingsFragBinding mSettingsFragBinding;
    private SettingsActionHandler mSettingsActionHandler;
    private DesireLogic mDesireLogic;
    private SelectImageLogic mSelectImageLogic;
    private int REQUEST_CAMERA = 0;
    private int REQUEST_GALLERY = 1;
    private ProgressDialog mLoadingDialog;

    public SettingsFragment() {
        //Requires empty public constructor
    }

    public static SettingsFragment newInstance() {return new SettingsFragment();}

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
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

        mSettingsFragBinding = SettingsFragBinding.inflate(inflater, container, false);

        mPresenter.getUser(mDesireLogic.getLoggedInUserId());

        setHasOptionsMenu(true);

        mSettingsActionHandler = new SettingsActionHandler(mPresenter, mSettingsFragBinding, mSelectImageLogic);
        mSettingsFragBinding.setActionHandler(mSettingsActionHandler);

        return mSettingsFragBinding.getRoot();
    }

    public void setUser(User user){
        mSettingsFragBinding.setUser(user);
        Media m = user.getImage();

        if (m != null) {
            final ImageView profileView = mSettingsFragBinding.profilePicture;
            Picasso.with(mSettingsFragBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mSettingsFragBinding.profilePicture;
            profileView.setImageResource(R.drawable.no_pic);
        }
    }

    @Override
    public void showSettingsScreen() {
        mSettingsFragBinding.settingsLoadingScreen.setVisibility(View.GONE);
        mSettingsFragBinding.settingsMainScreen.setVisibility(View.VISIBLE);
    }

    public void setDesireLogic(DesireLogic desireLogic) {

        mDesireLogic = desireLogic;
    }

    public void setSelectImageLogic(SelectImageLogic selectImageLogic) {
        mSelectImageLogic = selectImageLogic;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            mSettingsActionHandler.getSelectImageLogic().selectImageForDesire();
        }else if (grantResults[0]== PackageManager.PERMISSION_DENIED){
            showMessage(getString(R.string.declined_memory_runtime_permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri image = null;
        ImageView imageView = mSettingsFragBinding.profilePicture;
        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            showLoadingProgress();

            if(requestCode == REQUEST_GALLERY) {
                image = galleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                image = cameraResult(data);
            }
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
            long loggedInUserId = mDesireLogic.getLoggedInUserId();
            mPresenter.getUserForImageUpdate(loggedInUserId, file);
        }


    }

    private Uri galleryResult(Intent data){
        Uri image = data.getData();
        return image;
    }

    private Uri cameraResult(Intent data){
        //showLoadingProgress();
        //SelectImageLogic imageLogic = mSettingsActionHandler.getSelectImageLogic();
        //Uri image = imageLogic.getImageFromCamera(data);
        Uri image = data.getData();
        return image;
    }

    @Override
    public void showFilterSettings() {
        Intent intent = new Intent(getContext(), FilterSettingActivity.class);
        startActivity(intent);
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
        showUpdateUserSuccess();
        mLoadingDialog.cancel();
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public void showGetUserError() {
        showMessage(getString(R.string.get_user_error));
    }

    public void showUpdateUserSuccess() {
        showMessage(getString(R.string.update_user_success));
    }

    public void showUpdateUserError() {
        showMessage(getString(R.string.update_user_error));
    }

    @Override
    public void showResetPasswordSuccess() {
        showMessage(getString(R.string.password_reset_success));
    }

    @Override
    public void showResetPasswordError() {
        showMessage(getString(R.string.password_reset_error));
    }
}
