package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag;


import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.DesireFlag;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlagRepository implements FlagDataSource {

    private static FlagRepository INSTANCE;

    private final FlagDataSource flagRemoteDataSource;

    private final FlagDataSource flagLocalDataSource;

    private FlagRepository(@NonNull FlagDataSource flagRemoteDataSource, @NonNull FlagDataSource flagLocalDataSource) {
        this.flagRemoteDataSource = checkNotNull(flagRemoteDataSource);
        this.flagLocalDataSource = checkNotNull(flagLocalDataSource);
    }

    public static FlagRepository getInstance(@NonNull FlagDataSource flagRemoteDataSource, @NonNull FlagDataSource flagLocalDataSource) {
        if (INSTANCE == null)
            INSTANCE = new FlagRepository(flagRemoteDataSource, flagLocalDataSource);

        return INSTANCE;
    }

    @Override
    public void getDesireFlags(@NonNull final long id, @NonNull final GetDesireFlagsCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);

        flagLocalDataSource.getDesireFlags(id, new GetDesireFlagsCallback() {
            @Override
            public void onDesireFlagsLoaded(List<DesireFlag> flags) {
                callback.onDesireFlagsLoaded(flags);
            }

            @Override
            public void onDataNotAvailable() {
                flagRemoteDataSource.getDesireFlags(id, new GetDesireFlagsCallback() {
                    @Override
                    public void onDesireFlagsLoaded(List<DesireFlag> flags) {
                        callback.onDesireFlagsLoaded(flags);
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
    public void flagDesire(@NonNull final long id, @NonNull final DesireFlag desireFlag, @NonNull final FlagDesire callback) {
        checkNotNull(id);
        checkNotNull(callback);

        flagLocalDataSource.flagDesire(id, desireFlag, new FlagDesire() {
            @Override
            public void onDesireFlagCreated(DesireFlag flag) {
                callback.onDesireFlagCreated(flag);
            }

            @Override
            public void onCreateFailed() {
                flagRemoteDataSource.flagDesire(id, desireFlag, new FlagDesire() {
                    @Override
                    public void onDesireFlagCreated(DesireFlag flag) {
                        callback.onDesireFlagCreated(flag);
                    }

                    @Override
                    public void onCreateFailed() {
                        callback.onCreateFailed();
                    }
                });
            }
        });
    }

    @Override
    public void unflagDesire(@NonNull final long id, @NonNull final long flagId, @NonNull final UnflagDesire callback) {
        checkNotNull(id);
        checkNotNull(callback);

        flagLocalDataSource.unflagDesire(id, flagId, new UnflagDesire() {
            @Override
            public void onDesireFlagDeleted() {
                callback.onDesireFlagDeleted();
            }

            @Override
            public void onDeleteFailed() {
                flagRemoteDataSource.unflagDesire(id, flagId, new UnflagDesire() {
                    @Override
                    public void onDesireFlagDeleted() {
                        callback.onDesireFlagDeleted();
                    }

                    @Override
                    public void onDeleteFailed() {
                        callback.onDeleteFailed();
                    }
                });
            }
        });
    }
}
