package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag;


import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import javax.ws.rs.PathParam;

import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.FlagResource;

public class FlagLocalDataSource implements FlagDataSource {
    private static FlagLocalDataSource INSTANCE;

    private Context context;

    private FlagLocalDataSource(@NonNull Context context) {
        this.context = context;
    }

    public static FlagLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FlagLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getDesireFlags(@NonNull long id, @NonNull GetDesireFlagsCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }

    @Override
    public void flagDesire(@NonNull long id, @NonNull DesireFlag desireFlag, @NonNull FlagDesire callback) {
        //TODO
        callback.onCreateFailed();
    }

    @Override
    public void unflagDesire(@NonNull long id, @NonNull long flagId, @NonNull UnflagDesire callback) {
        //TODO
        callback.onDeleteFailed();
    }
}
