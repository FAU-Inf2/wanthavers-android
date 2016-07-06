package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.ExceptionMapper;

import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class RestClient {
    protected WebTarget target;

    protected Context context;

    protected static ArrayList<RestClient> clients = new ArrayList<>();

    //TODO: get API-URL from shared preferences or something like that
    public static final String API_URL = "https://faui21f.informatik.uni-erlangen.de:8080/";

    protected RestClient(@NonNull Context context) {
        checkNotNull(context);
        this.context = context;

        buildNew();

        clients.add(this);
    }

    protected void buildNew() {
        target = null;
        SharedPreferencesHelper sharedPreferences = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, context);
        String email = sharedPreferences.loadString(SharedPreferencesHelper.KEY_USER_EMAIL, "");
        String password = sharedPreferences.loadString(SharedPreferencesHelper.KEY_PASSWORD, "");

        HttpAuthenticationFeature basicAuthFeature = HttpAuthenticationFeature.basic(email, password);
        SSLContext ctx = null;
        try{
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());
        }catch(Exception e){}

        Client client = ClientBuilder.newBuilder()
                .hostnameVerifier(new TrustAllHostNameVerifier())
                .sslContext(ctx)
                .build();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        target = client.register(JacksonJsonProvider.class).register(basicAuthFeature).target(API_URL);
        buildNewEndpoint();
    }

    public static void triggerSetNewBasicAuth() {
        for(RestClient client : clients) {
            client.buildNew();
        }
    }

    protected abstract void buildNewEndpoint();

    TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };

    public static class TrustAllHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}
