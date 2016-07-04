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
    public void getHaver(@NonNull final long desireId, @NonNull final long userId, @NonNull final GetHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(userId);
        checkNotNull(callback);

        haverLocalDataSource.getHaver(desireId, userId, new GetHaverCallback() {
            @Override
            public void onHaverLoaded(Haver haver) {
                callback.onHaverLoaded(haver);
            }

            @Override
            public void onDataNotAvailable() {
                haverRemoteDataSource.getHaver(desireId, userId, new GetHaverCallback() {
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
    public void updateHaver(@NonNull long desireId, @NonNull long userId, @NonNull Haver haver, @NonNull final UpdateHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(userId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverRemoteDataSource.updateHaver(desireId, userId, haver, new UpdateHaverCallback() {
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
    public void acceptHaver(@NonNull long desireId, @NonNull long userId, @NonNull Haver haver, @NonNull final AcceptHaverForDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(userId);
        checkNotNull(haver);
        checkNotNull(callback);

        haverRemoteDataSource.acceptHaver(desireId, userId, haver, new AcceptHaverForDesireCallback() {
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

    @Override
    public void getAcceptedHaverForDesire(@NonNull long desireId, @NonNull final GetAcceptedHaverForDesireCallback callback) {
        checkNotNull(desireId);
        checkNotNull(callback);

        haverRemoteDataSource.getAcceptedHaverForDesire(desireId, new GetAcceptedHaverForDesireCallback() {
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

    @Override
    public void updateHaverStatus(@NonNull long desireId, @NonNull long userId, @NonNull Haver haver, @NonNull int status, @NonNull final UpdateHaverStatusCallback callback) {
        checkNotNull(desireId);
        checkNotNull(userId);
        checkNotNull(haver);
        checkNotNull(status);
        checkNotNull(callback);

        haverRemoteDataSource.updateHaverStatus(desireId, userId, haver, status, new UpdateHaverStatusCallback() {
            @Override
            public void onStatusUpdated(Haver haver) {
                callback.onStatusUpdated(haver);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void deleteHaver(@NonNull long desireId, @NonNull long userId, @NonNull final DeleteHaverCallback callback) {
        checkNotNull(desireId);
        checkNotNull(userId);
        checkNotNull(callback);

        haverRemoteDataSource.deleteHaver(desireId, userId, new DeleteHaverCallback() {
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
}
