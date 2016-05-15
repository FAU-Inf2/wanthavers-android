package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import javax.ws.rs.NameBinding;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;

/**
 * Created by Nico on 15.05.2016.
 */
public interface HaverDataSource {

    interface GetHaverCallback {

        void onHaverLoaded(Haver haver);

        void onDataNotAvailable();

    }

    interface GetAllHaversForDesireCallback {

        void onAllHaversForDesireLoaded(List<Haver> havers);

        void onDataNotAvailable();

    }

    interface CreateHaverCallback {

        void onHaverCreated(Haver haver);

        void onCreateFailed();

    }

    interface UpdateHaverCallback {

        void onHaverUpdated(Haver haver);

        void onUpdateFailed();

    }

    interface DeleteHaverCallback {

        void onHaverDeleted();

        void onDeleteFailed();

    }

    interface AcceptHaverForDesireCallback {

        void onAcceptHaverForDesire(Haver haver);

        void onAcceptFailed();

    }

    void getHaver(@NonNull long desireId, @NonNull long haverId, @NonNull GetHaverCallback callback);

    void getAllHaversForDesire(@NonNull long desireId, @NonNull GetAllHaversForDesireCallback callback);

    void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull User user, @NonNull CreateHaverCallback callback);

    void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback);

    void deleteHaver(@NonNull long desireId, @NonNull long haverId, @NonNull DeleteHaverCallback callback);

    void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback);

}
