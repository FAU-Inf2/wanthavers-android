package wanthavers.mad.cs.fau.de.wanthavers_android.settings;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.SettingsFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.FilterSettingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsFragment extends Fragment implements SettingsContract.View {
    private SettingsContract.Presenter mPresenter;
    private SettingsFragBinding mSettingsFragBinding;
    private SettingsActionHandler mSettingsActionHandler;
    private DesireLogic mDesireLogic;

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

        //View view = inflater.inflate(R.layout.settings_frag, container, false);
        mSettingsFragBinding = SettingsFragBinding.inflate(inflater, container, false);

        mPresenter.getUser(mDesireLogic.getLoggedInUserId());

        setHasOptionsMenu(true);

        mSettingsActionHandler = new SettingsActionHandler(mPresenter);
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

    @Override
    public void showFilterSettings() {
        Intent intent = new Intent(getContext(), FilterSettingActivity.class);
        startActivity(intent);
    }
}
