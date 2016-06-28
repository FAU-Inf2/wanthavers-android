package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.AppVersion;

import static com.google.common.base.Preconditions.checkNotNull;

public class AppVersionRepository implements AppVersionDataSource {
    private static AppVersionRepository INSTANCE;

    private final AppVersionDataSource appVersionRemoteDataSource;

    private final AppVersionDataSource appVersionLocalDataSource;

    private AppVersionRepository(@NonNull AppVersionDataSource appVersionRemoteDataSource, @NonNull AppVersionDataSource appVersionLocalDataSource) {
        this.appVersionRemoteDataSource = checkNotNull(appVersionRemoteDataSource);
        this.appVersionLocalDataSource = checkNotNull(appVersionLocalDataSource);
    }

    public static AppVersionRepository getInstance(@NonNull AppVersionDataSource appVersionRemoteDataSource, @NonNull AppVersionDataSource appVersionLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AppVersionRepository(appVersionRemoteDataSource, appVersionLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getAppVersion(@NonNull final int versionCode, @NonNull final int os, @NonNull final String appId, @NonNull final GetAppVersionCallback callback) {
        checkNotNull(versionCode);
        checkNotNull(os);
        checkNotNull(appId);
        checkNotNull(callback);

        appVersionLocalDataSource.getAppVersion(versionCode, os, appId, new GetAppVersionCallback() {
            @Override
            public void onAppVersionLoaded(AppVersion appVersion) {
                callback.onAppVersionLoaded(appVersion);
            }

            @Override
            public void onDataNotAvailable() {
                appVersionRemoteDataSource.getAppVersion(versionCode, os, appId, new GetAppVersionCallback() {
                    @Override
                    public void onAppVersionLoaded(AppVersion appVersion) {
                        callback.onAppVersionLoaded(appVersion);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }
}
