package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion;

import android.content.Context;
import android.support.annotation.NonNull;

public class AppVersionLocalDataSource implements AppVersionDataSource {
    private static AppVersionLocalDataSource INSTANCE;

    private Context context;

    private AppVersionLocalDataSource(Context context) {
        this.context = context;
    }

    public static AppVersionLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppVersionLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAppVersion(@NonNull int versionCode, @NonNull int os, @NonNull String appId, @NonNull GetAppVersionCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }
}
