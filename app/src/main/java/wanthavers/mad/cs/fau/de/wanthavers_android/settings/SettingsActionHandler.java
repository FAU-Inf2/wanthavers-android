package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

public class SettingsActionHandler {

    private SettingsContract.Presenter mListener;

    public SettingsActionHandler(SettingsContract.Presenter listener) {
        mListener = listener;
    }

    public void changeFilterClicked() {
        mListener.openFilterSettings();
    }

}
