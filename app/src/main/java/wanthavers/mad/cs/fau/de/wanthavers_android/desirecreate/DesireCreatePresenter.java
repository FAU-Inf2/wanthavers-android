package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;

public class DesireCreatePresenter implements DesireCreateContract.Presenter {
    private final DesireCreateContract.View mDesireCreateView;
    private final UseCaseHandler useCaseHandler;

    public DesireCreatePresenter(@NonNull UseCaseHandler ucHandler, @NonNull DesireCreateContract.View view) {

        useCaseHandler = ucHandler;
        mDesireCreateView = view;

        mDesireCreateView.setPresenter(this);

    }

    public void start() { //TODO;
    }


    /*@Override
    public Desire getEnteredText(Desire desire, String s1, String s2){
        return new Desire();
    }*/

    @Override
    public void createNextDesireCreateStep(String[] input) {
        mDesireCreateView.showNextDesireCreateStep(input);
    }





}

