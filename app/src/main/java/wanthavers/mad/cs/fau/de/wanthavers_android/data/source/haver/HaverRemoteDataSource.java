package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.HaverClient;

/**
 * Created by Nico on 15.05.2016.
 */
public class HaverRemoteDataSource implements HaverDataSource {

    private static HaverRemoteDataSource INSTANCE;

    private HaverClient haverClient;

    private HaverRemoteDataSource(Context context) {
        haverClient = HaverClient.getInstance(context);
    }

    public static HaverRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HaverRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getHaver(@NonNull long desireId, @NonNull long userId, @NonNull GetHaverCallback callback) {
        try {
            Haver haver = haverClient.get(desireId, userId);
            callback.onHaverLoaded(haver);
        } catch (WebApplicationException wae) {
            if(wae.getResponse().getStatus() == 404) {
                callback.onHaverLoaded(null);
                return;
            }
            callback.onDataNotAvailable();
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllHaversForDesire(@NonNull long desireId, @NonNull GetAllHaversForDesireCallback callback) {
        try {
            List<Haver> havers = haverClient.getAllHavers(desireId);
            callback.onAllHaversForDesireLoaded(havers);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull CreateHaverCallback callback) {
        try {
            Haver ret = haverClient.createHaver(desireId, haver);
            callback.onHaverCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback) {
        try {
            Haver ret = haverClient.updateHaver(desireId, haverId, haver);
            callback.onHaverUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback) {
        try {
            Haver ret = haverClient.acceptHaver(desireId, haverId, haver);
            callback.onAcceptHaverForDesire(ret);
        } catch (Throwable t) {
            callback.onAcceptFailed();
        }
    }

    @Override
    public void getAcceptedHaverForDesire(@NonNull long desireId, @NonNull GetAcceptedHaverForDesireCallback callback) {
        try {
            Haver ret = haverClient.getAcceptedHaver(desireId);
            callback.onHaverLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void updateHaverStatus(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull int status, @NonNull UpdateHaverStatusCallback callback) {
        try {
            Haver ret = haverClient.setHaverStatus(desireId, haverId, haver, status);
            callback.onStatusUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }
}
