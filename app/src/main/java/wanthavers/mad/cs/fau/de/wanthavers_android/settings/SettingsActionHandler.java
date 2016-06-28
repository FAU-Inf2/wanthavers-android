package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.SettingsFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;

public class SettingsActionHandler {

    private SettingsContract.Presenter mListener;
    private SettingsFragBinding mSettingsFragBinding;
    private SelectImageLogic mSelectImageLogic;

    public SettingsActionHandler(SettingsContract.Presenter listener, SettingsFragBinding settingsFragBinding,
                                 SelectImageLogic selectImageLogic) {
        mListener = listener;
        mSettingsFragBinding = settingsFragBinding;
        mSelectImageLogic = selectImageLogic;
    }

    public void changeNameClicked(User user) {
        if (user == null) {
            return;
        }
        String name = mSettingsFragBinding.userName.getText().toString();
        user.setName(name);
        mListener.upDateUser(user);
    }

    public void changeMailClicked(User user) {
        if (user == null) {
            return;
        }
        String email = mSettingsFragBinding.userMail.getText().toString();
        user.setEmail(email);
        mListener.upDateUserMail(user);
    }

    public void changePhotoClicked(User user) {
        if(mSelectImageLogic.isStoragePermissionGranted()){
            mSelectImageLogic.selectImageForDesire();
        }
    }

    public void changeFilterClicked() {
        mListener.openFilterSettings();
    }

    public SelectImageLogic getSelectImageLogic() {
        return  mSelectImageLogic;
    }

    public void resetPassword(String userMail) {
        mListener.sendPWResetToken(userMail);
    }

}
