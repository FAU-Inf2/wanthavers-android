package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.SettingsFragBinding;

public class SettingsActionHandler {

    private SettingsContract.Presenter mListener;
    private SettingsFragBinding mSettingsFragBinding;

    public SettingsActionHandler(SettingsContract.Presenter listener, SettingsFragBinding settingsFragBinding) {
        mListener = listener;
        mSettingsFragBinding = settingsFragBinding;
    }

    public void changeMailClicked(long userId) {
        String email = mSettingsFragBinding.userMail.getText().toString();
        mListener.getUserForMailUpdate(userId, email);
        //mListener.updateUserMail(user, email);
    }

    public void changeFilterClicked() {
        mListener.openFilterSettings();
    }

}
