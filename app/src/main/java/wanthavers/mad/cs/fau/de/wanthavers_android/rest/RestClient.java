package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class RestClient {
    protected final WebTarget target;

    protected Context context;

    //TODO: get API-URL from shared preferences or something like that
    public static final String API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";

    protected RestClient(@NonNull Context context) {
        checkNotNull(context);
        this.context = context;

        SharedPreferencesHelper sharedPreferences = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, context);
        long userId = sharedPreferences.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);
        String password = sharedPreferences.loadString(SharedPreferencesHelper.KEY_PASSWORD, "test");

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(String.valueOf(userId), password);
        target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
        target.register(feature);
    }
}
