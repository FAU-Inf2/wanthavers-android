package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.TasksDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetDesire extends UseCase<GetDesire.RequestValues, GetDesire.ResponseValue> {

    private final TasksRepository mTasksRepository;


    public GetDesire(@NonNull TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mTasksRepository.getDesire(values.getDesireId(), new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Desire desire) {
                ResponseValue responseValue = new ResponseValue(desire);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final String mDesireId;

        public RequestValues(@NonNull String desireId) {
            mDesireId = checkNotNull(desireId, "desireId cannot be null!");
        }

        public String getDesireId() {
            return mDesireId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Desire mDesire;

        public ResponseValue(@NonNull Desire task) {
            mDesire = checkNotNull(task, "task cannot be null!");
        }

        public Desire getDesire() {
            return mDesire;
        }
    }

}
