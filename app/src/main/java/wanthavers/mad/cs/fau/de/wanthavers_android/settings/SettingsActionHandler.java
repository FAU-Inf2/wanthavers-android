package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.SettingsFragBinding;

public class SettingsActionHandler {

    private SettingsContract.Presenter mListener;
    private SettingsFragBinding mSettingsFragBinding;

    public SettingsActionHandler(SettingsContract.Presenter listener, SettingsFragBinding settingsFragBinding) {
        mListener = listener;
        mSettingsFragBinding = settingsFragBinding;
    }

    public void changeMailClicked(long userId) {
        String mail = mSettingsFragBinding.userMail.getText().toString();
        mListener.getUserForMailUpdate(userId, mail);
    }

    public void changeFilterClicked() {
        mListener.openFilterSettings();
    }

}
