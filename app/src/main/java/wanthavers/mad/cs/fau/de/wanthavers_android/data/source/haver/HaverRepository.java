package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 15.05.2016.
 */
public class HaverRepository implements HaverDataSource {

    private static HaverRepository INSTANCE = null;

    private final HaverDataSource haverRemoteDataSource;

    private final HaverDataSource haverLocalDataSource;

    private HaverRepository(@NonNull HaverDataSource haverRemoteDataSource, @NonNull HaverDataSource haverLocalDataSource) {
        this.haverRemoteDataSource = checkNotNull(haverRemoteDataSource);
        this.haverLocalDataSource = checkNotNull(haverLocalDataSource);
    }

    public static HaverRepository getInstance(HaverDataSource haverRemoteDataSource, HaverDataSource haverLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HaverRepository(haverRemoteDataSource, haverLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getHaver(@NonNull final long desireId, @NonNull final long haverId, @NonNull final GetHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(callback);

        haverLocalDataSource.getHaver(desireId, haverId, new GetHaverCallback() {
            @Override
            public void onHaverLoaded(Haver haver) {
                callback.onHaverLoaded(haver);
            }

            @Override
            public void onDataNotAvailable() {
                haverRemoteDataSource.getHaver(desireId, haverId, new GetHaverCallback() {
                    @Override
                    public void onHaverLoaded(Haver haver) {
                        callback.onHaverLoaded(haver);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getAllHaversForDesire(@NonNull final long desireId, @NonNull final GetAllHaversForDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(callback);

        haverLocalDataSource.getAllHaversForDesire(desireId, new GetAllHaversForDesireCallback() {
            @Override
            public void onAllHaversForDesireLoaded(List<Haver> havers) {
                callback.onAllHaversForDesireLoaded(havers);
            }

            @Override
            public void onDataNotAvailable() {
                haverRemoteDataSource.getAllHaversForDesire(desireId, new GetAllHaversForDesireCallback() {
                    @Override
                    public void onAllHaversForDesireLoaded(List<Haver> havers) {
                        callback.onAllHaversForDesireLoaded(havers);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull CreateHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverLocalDataSource.createHaver(desireId, haver, callback);
        haverRemoteDataSource.createHaver(desireId, haver, callback);
    }

    @Override
    public void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull UpdateHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverLocalDataSource.updateHaver(desireId, haverId, haver, callback);
        haverRemoteDataSource.updateHaver(desireId, haverId, haver, callback);
    }

    @Override
    public void deleteHaver(@NonNull long desireId, @NonNull long haverId, @NonNull DeleteHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(callback);

        haverLocalDataSource.deleteHaver(desireId, haverId, callback);
        haverRemoteDataSource.deleteHaver(desireId, haverId, callback);
    }

    @Override
    public void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull AcceptHaverForDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverLocalDataSource.acceptHaver(desireId, haverId, haver, callback);
        haverRemoteDataSource.acceptHaver(desireId, haverId, haver, callback);
    }
}
