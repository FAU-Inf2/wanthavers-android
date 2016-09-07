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

    public void changeUserDataClicked(User user) {
        if (user == null) {
            return;
        }

        String firstName = mSettingsFragBinding.userFirstName.getText().toString();
        String lastName = mSettingsFragBinding.userLastName.getText().toString();
        String email = mSettingsFragBinding.userMail.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            mListener.showEmptyUserDataError();
            return;
        }

        user.setName(firstName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        mListener.upDateUserIncludingMail(user);
    }

    public void manageLocationsClicked() {
        mListener.openLocationList();
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
