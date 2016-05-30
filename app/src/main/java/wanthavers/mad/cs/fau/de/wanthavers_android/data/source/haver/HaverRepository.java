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
    public void createHaver(@NonNull long desireId, @NonNull Haver haver, @NonNull final CreateHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverRemoteDataSource.createHaver(desireId, haver, new CreateHaverCallback() {
            @Override
            public void onHaverCreated(Haver haver) {
                callback.onHaverCreated(haver);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void updateHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull final UpdateHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverRemoteDataSource.updateHaver(desireId, haverId, haver, new UpdateHaverCallback() {
            @Override
            public void onHaverUpdated(Haver haver) {
                callback.onHaverUpdated(haver);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void deleteHaver(@NonNull long desireId, @NonNull long haverId, @NonNull final DeleteHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(callback);

        haverRemoteDataSource.deleteHaver(desireId, haverId, new DeleteHaverCallback() {
            @Override
            public void onHaverDeleted() {
                callback.onHaverDeleted();
            }

            @Override
            public void onDeleteFailed() {
                callback.onDeleteFailed();
            }
        });
    }

    @Override
    public void acceptHaver(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver, @NonNull final AcceptHaverForDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(haverId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverRemoteDataSource.acceptHaver(desireId, haverId, haver, new AcceptHaverForDesireCallback() {
            @Override
            public void onAcceptHaverForDesire(Haver haver) {
                callback.onAcceptHaverForDesire(haver);
            }

            @Override
            public void onAcceptFailed() {
                callback.onAcceptFailed();
            }
        });
    }
}