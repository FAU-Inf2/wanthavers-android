package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.HaverClient;

/**
 * Created by Nico on 15.05.2016.
 */
public class HaverRemoteDataSource implements HaverDataSource {

    private static HaverRemoteDataSource INSTANCE;

    private HaverClient haverEndpoint = HaverClient.getInstance();

    private HaverRemoteDataSource() { }

    public static HaverRemoteDataSource getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new HaverRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void getHaver(@NonNull long desireId, @NonNull long haverId, @NonNull GetHaverCallback callback) {
        try {
            Haver haver = haverEndpoint.get(desireId, haverId);
            callback.onHaverLoaded(haver);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllHaversForDesire(@NonNull long desireId, @NonNull GetAllHaversForDesireCallback callback) {
        try {
            List<Haver> havers = haverEndpoint.getAllHavers(desireId);
            callback.onAllHaversForDesireLoaded(havers);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull User user, @NonNull CreateHaverCallback callback) {
        try {
            Haver ret = haverEndpoint.createHaver(desireId, haver, user);
            callback.onHaverCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback) {
        try {
            Haver ret = haverEndpoint.updateHaver(desireId, haverId, haver);
            callback.onHaverUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteHaver(@NonNull long desireId, @NonNull long haverId, @NonNull DeleteHaverCallback callback) {
        try {
            haverEndpoint.deleteHaver(desireId, haverId);
            callback.onHaverDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

    @Override
    public void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback) {
        try {
            Haver ret = haverEndpoint.acceptHaver(desireId, haverId, haver);
            callback.onAcceptHaverForDesire(ret);
        } catch (Throwable t) {
            callback.onAcceptFailed();
        }
    }
}
