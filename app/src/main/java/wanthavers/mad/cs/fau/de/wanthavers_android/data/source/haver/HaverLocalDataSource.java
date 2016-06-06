package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.DatabaseHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 15.05.2016.
 */
public class HaverLocalDataSource implements HaverDataSource {

    private static HaverLocalDataSource INSTANCE;

    private RuntimeExceptionDao<Haver, Long> haverDao;

    private HaverLocalDataSource(Context context) {
        checkNotNull(context);
        haverDao = DatabaseHelper.getInstance(context).getHaverRuntimeDao();
    }

    public static HaverLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HaverLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getHaver(@NonNull long desireId, @NonNull long haverId, @NonNull GetHaverCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllHaversForDesire(@NonNull long desireId, @NonNull GetAllHaversForDesireCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onDataNotAvailable();
    }

    @Override
    public void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull CreateHaverCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onCreateFailed();
    }

    @Override
    public void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onUpdateFailed();
    }

    @Override
    public void deleteHaver(@NonNull long desireId, @NonNull long haverId, @NonNull DeleteHaverCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onDeleteFailed();
    }

    @Override
    public void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onAcceptFailed();
    }

    @Override
    public void getAcceptedHaverForDesire(@NonNull long desireId, @NonNull GetAcceptedHaverForDesireCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onDataNotAvailable();
    }

    @Override
    public void updateHaverStatus(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull int status, @NonNull UpdateHaverStatusCallback callback) {
        //TODO: alter this method when we decide to store havers locally
        callback.onUpdateFailed();
    }
}
