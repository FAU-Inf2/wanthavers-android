package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.AppVersion;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.AppVersionClient;

import static com.google.common.base.Preconditions.checkNotNull;

public class AppVersionRemoteDataSource implements AppVersionDataSource {
    private static AppVersionRemoteDataSource INSTANCE;

    private AppVersionClient appVersionClient;

    private AppVersionRemoteDataSource(Context context) {
        appVersionClient = AppVersionClient.getInstance(context);
    }

    public static AppVersionRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppVersionRemoteDataSource(checkNotNull(context));
        }
        return INSTANCE;
    }

    @Override
    public void getAppVersion(@NonNull int versionCode, @NonNull int os, @NonNull String appId, @NonNull GetAppVersionCallback callback) {
        try {
            AppVersion ret = appVersionClient.getAppVersion(versionCode, os, appId);
            callback.onAppVersionLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }
}
