package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver;

import android.support.annotation.NonNull;

import java.util.List;

import javax.ws.rs.NameBinding;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;

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

    interface UnacceptHaverForDesireCallback {

        void onUnacceptHaverForDesire(Haver haver);

        void onUnacceptFailed();

    }

    interface GetAcceptedHaverForDesireCallback {

        void onHaverLoaded(Haver haver);

        void onDataNotAvailable();

    }

    interface UpdateHaverStatusCallback {

        void onStatusUpdated(Haver haver);

        void onUpdateFailed();

    }

    interface UpdateRequestedPriceCallback {

        void onRequestedPriceUpdated(Haver haver);

        void onUpdateFailed();

    }

    void getHaver(@NonNull long desireId, @NonNull long userId, @NonNull GetHaverCallback callback);

    void getAllHaversForDesire(@NonNull long desireId, @NonNull GetAllHaversForDesireCallback callback);

    void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull CreateHaverCallback callback);

    void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback);

    void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback);

    void unacceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UnacceptHaverForDesireCallback callback);

    void getAcceptedHaverForDesire(@NonNull long desireId, @NonNull GetAcceptedHaverForDesireCallback callback);

    void updateHaverStatus(@NonNull long desireId, @NonNull long userId, @NonNull int status, @NonNull UpdateHaverStatusCallback callback);

    void updateRequestedPrice(@NonNull long desireId, @NonNull long userId, @NonNull double requestedPrice, @NonNull UpdateRequestedPriceCallback callback);

    void deleteHaver(@NonNull long desireId, @NonNull long userId, @NonNull DeleteHaverCallback callback);
}
