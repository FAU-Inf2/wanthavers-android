package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirecreateFragBinding;

public class DesireCreateFragment extends Fragment implements DesireCreateContract.View {
    private DesirecreateFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;

    public DesireCreateFragment(){
        //Requires empty public constructor
    }
    public static DesireCreateFragment newInstance(){ return new DesireCreateFragment();}

    @Override
    public void setPresenter(@NonNull DesireCreateContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }
    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mViewDataBinding = DesirecreateFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

        mViewDataBinding.getRoot().setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter));

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {
        final EditText desireTitle   = mViewDataBinding.createDesireTitle;
        final EditText desireDescription   =  mViewDataBinding.createDesireDescription;

        if(desireTitle.getText().toString().isEmpty() || desireDescription.getText().toString().isEmpty() ){
            showMessage( getString(R.string.createDesire_Empty_Text));
            return;
        }
        Intent intent = new Intent(getContext(), DesireCreateActivity2ndStep.class);
        intent.putExtra("desireTitle", desireTitle.getText().toString());
        intent.putExtra("desireDescription", desireDescription.getText().toString());
        startActivity(intent);
    }



    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMedia(Desire m){
        //no Medias in this Step
    }

}
