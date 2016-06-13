package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.DesireFlag;

public interface FlagDataSource {

    interface GetDesireFlagsCallback {
        void onDesireFlagsLoaded(List<DesireFlag> flags);

        void onDataNotAvailable();
    }

    interface FlagDesire {
        void onDesireFlagCreated(DesireFlag flag);

        void onCreateFailed();
    }

    interface UnflagDesire {
        void onDesireFlagDeleted();

        void onDeleteFailed();
    }

    void getDesireFlags(@NonNull long id, @NonNull GetDesireFlagsCallback callback);

    void flagDesire(@NonNull long id, @NonNull DesireFlag desireFlag, @NonNull FlagDesire callback);

    void unflagDesire(@NonNull long id, @NonNull long flagId, @NonNull UnflagDesire callback);

}
