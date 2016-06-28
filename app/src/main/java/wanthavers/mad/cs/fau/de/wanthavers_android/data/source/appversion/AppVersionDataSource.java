package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.AppVersion;

public interface AppVersionDataSource {

    interface GetAppVersionCallback {

        void onAppVersionLoaded(AppVersion appVersion);

        void onDataNotAvailable();

    }

    void getAppVersion(@NonNull int versionCode, @NonNull int os, @NonNull String appId, @NonNull GetAppVersionCallback callback);

}
