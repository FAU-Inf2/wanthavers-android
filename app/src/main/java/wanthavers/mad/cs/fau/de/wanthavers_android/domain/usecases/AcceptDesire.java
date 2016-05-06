package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class AcceptDesire extends UseCase<AcceptDesire.RequestValues, AcceptDesire.ResponseValue> {

    private final TasksRepository mTasksRepository;


    public AcceptDesire(@NonNull TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        String acceptedDesire = values.getCompletedTask();
        mTasksRepository.acceptDesire(acceptedDesire);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final String mCompletedTask;

        public RequestValues(@NonNull String completedTask) {
            mCompletedTask = checkNotNull(completedTask, "completedTask cannot be null!");
        }

        public String getCompletedTask() {
            return mCompletedTask;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }

}
