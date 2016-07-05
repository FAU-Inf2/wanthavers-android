package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirecreateFragBinding;

public class DesireCreateFragment extends Fragment implements DesireCreateContract.View {
    private DesirecreateFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private DesireCreateActionHandler mDesireCreateActionHandler;

    private EditText mDesireTitle;
    private EditText mDesireDescription;
    private TextView mDesireDescriptionCounter;

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

        mDesireCreateActionHandler = new DesireCreateActionHandler(mPresenter);
        mViewDataBinding.setActionHandler(mDesireCreateActionHandler);

        mViewDataBinding.getRoot().setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter, mDesireCreateActionHandler));

        mDesireTitle  = mViewDataBinding.createDesireTitle;
        mDesireDescription  =  mViewDataBinding.createDesireDescription;
        mDesireDescriptionCounter = mViewDataBinding.createDesireDescriptionCounter;

        CustomTextWatcher myWatcher = new CustomTextWatcher(mDesireTitle);
        CustomTextWatcher myWatcher2 = new CustomTextWatcher(mDesireDescription);
        mDesireDescriptionCounter.setText("0/300");
        mDesireDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft ){
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDesireDescriptionCounter.setText(s.toString().length() + "/300");
            }
        });

        mDesireTitle.addTextChangedListener(myWatcher);
        mDesireDescription.addTextChangedListener(myWatcher2);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showNextDesireCreateStep() {


        if(mDesireTitle.getText().toString().isEmpty() || mDesireDescription.getText().toString().isEmpty() ){
            showMessage( getString(R.string.createDesire_Empty_Text));
            return;
        }

        Intent intent = new Intent(getContext(), DesireCreateActivity2ndStep.class);
        intent.putExtra("desireTitle", mDesireTitle.getText().toString());
        intent.putExtra("desireDescription", mDesireDescription.getText().toString());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.create_desire_step_one, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next_step:
                mPresenter.createNextDesireCreateStep();
        }

        return true;
    }

    @Override
    public void showCategorySelection() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategory(Category category) {
        //no Category Selection in this Step
    }

    @Override
    public void showGetCategoriesError() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategories(List<Category> categories) {
        //no Category Selection in this Step
    }

}
