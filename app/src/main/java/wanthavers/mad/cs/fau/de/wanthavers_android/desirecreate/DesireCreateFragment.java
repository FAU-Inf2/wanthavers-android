package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.desirecreate_frag, container, false);
        mViewDataBinding = DesirecreateFragBinding.bind(view);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        final EditText desireTitle   = (EditText) view.findViewById(R.id.create_desire_Title);
        final EditText desireDescription   = (EditText) view.findViewById(R.id.create_desire_description);



        Button button = (Button) view.findViewById(R.id.button_1st_step);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                if (desireTitle.getText().toString().isEmpty() || desireDescription.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), R.string.createDesire_Empty_Text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String[] input = {desireTitle.getText().toString(), desireDescription.getText().toString()};
                    mPresenter.createNextDesireCreateStep(input);
                }
            }
        });

        return view;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/


    @Override
    public void showNextDesireCreateStep(String input[]) {
        Intent intent = new Intent(getContext(), DesireCreateActivity2ndStep.class);
        intent.putExtra("desireTitle", input[0]);
        intent.putExtra("desireDescription", input[1]);
        startActivity(intent);
        //getActivity().finish();
    }

}
