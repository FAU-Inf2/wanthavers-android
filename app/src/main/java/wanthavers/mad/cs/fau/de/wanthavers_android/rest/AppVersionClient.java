package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.fau.cs.mad.wanthavers.common.AppVersion;
import de.fau.cs.mad.wanthavers.common.rest.api.AppVersionResource;

public class AppVersionClient extends RestClient {
    private static AppVersionClient INSTANCE;

    private AppVersionResource appVersionEndpoint;

    private AppVersionClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        appVersionEndpoint = null;
        appVersionEndpoint = WebResourceFactory.newResource(AppVersionResource.class, target);
    }

    public static AppVersionClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppVersionClient(context);
        }
        return INSTANCE;
    }

    public AppVersion getAppVersion(int versionCode, int os, String appId) {
        return appVersionEndpoint.get(versionCode, os, appId);
    }
}
