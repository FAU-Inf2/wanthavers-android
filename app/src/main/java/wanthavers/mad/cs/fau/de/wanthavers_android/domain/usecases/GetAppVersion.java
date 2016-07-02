package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.AppVersion;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion.AppVersionDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion.AppVersionRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetAppVersion extends UseCase<GetAppVersion.RequestValues, GetAppVersion.ResponseValue> {
    private final AppVersionRepository mAppVersionRepository;

    public GetAppVersion(@NonNull AppVersionRepository appVersionRepository) {
        mAppVersionRepository = checkNotNull(appVersionRepository, "desireRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mAppVersionRepository.getAppVersion(values.getVersionCode(), values.getOS(), values.getAppId(), new AppVersionDataSource.GetAppVersionCallback() {
            @Override
            public void onAppVersionLoaded(AppVersion appVersion) {
                ResponseValue responseValue = new ResponseValue(appVersion);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final int mVersionCode;
        private final int mOS;
        private final String mAppId;

        public RequestValues(@NonNull int versionCode, @NonNull int os, @NonNull String appId) {
            mVersionCode = checkNotNull(versionCode, "versionCode must'nt be null");
            mOS = checkNotNull(os, "os must'nt be null");
            mAppId = checkNotNull(appId, "appId must'nt be null");
        }

        public int getVersionCode() {
            return mVersionCode;
        }

        public int getOS() {
            return mOS;
        }

        public String getAppId() {
            return mAppId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private AppVersion mAppVersion;

        public ResponseValue(@NonNull AppVersion appVersion) {
            mAppVersion = checkNotNull(appVersion, "appVersion must'nt be null!");
        }

        public AppVersion getAppVersion() {
            return mAppVersion;
        }
    }
}
