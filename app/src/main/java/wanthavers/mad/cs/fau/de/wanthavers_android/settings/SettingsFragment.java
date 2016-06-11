package wanthavers.mad.cs.fau.de.wanthavers_android.settings;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
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
            Picasso.with(mSettingsFragBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mSettingsFragBinding.profilePicture;
            profileView.setImageResource(R.drawable.no_pic);
        }
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri image = data.getData();
            ImageView imageView = (ImageView) getView().findViewById(R.id.profile_picture);
            imageView.setImageURI(image);

            File file = new File(PathHelper.getRealPathFromURI(this.getContext().getApplicationContext(), image));

            long loggedInUserId = mDesireLogic.getLoggedInUserId();

            mPresenter.getUserForImageUpdate(loggedInUserId, file);
        }
    }

    @Override
    public void showFilterSettings() {
        Intent intent = new Intent(getContext(), FilterSettingActivity.class);
        startActivity(intent);
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
}
