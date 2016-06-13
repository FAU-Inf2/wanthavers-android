package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag;


import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.DesireFlag;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.FlagClient;

public class FlagRemoteDataSource implements FlagDataSource{
    private static FlagRemoteDataSource INSTANCE;

    private FlagClient flagClient;

    private FlagRemoteDataSource(@NonNull Context context) {
        flagClient = FlagClient.getInstance(context);
    }

    public static FlagRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FlagRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getDesireFlags(@NonNull long id, @NonNull GetDesireFlagsCallback callback) {
        try {
            List<DesireFlag> ret = flagClient.getDesireFlags(id);
            callback.onDesireFlagsLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void flagDesire(@NonNull long id, @NonNull DesireFlag desireFlag, @NonNull FlagDesire callback) {
        try {
            DesireFlag ret = flagClient.flagDesire(id, desireFlag);
            callback.onDesireFlagCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void unflagDesire(@NonNull long id, @NonNull long flagId, @NonNull UnflagDesire callback) {
        try {
            flagClient.unflagDesire(id, flagId);
            callback.onDesireFlagDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

}
