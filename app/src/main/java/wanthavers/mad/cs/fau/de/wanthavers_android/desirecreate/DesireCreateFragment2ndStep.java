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
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate2ndFragBinding;

public class DesireCreateFragment2ndStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate2ndFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;

    public DesireCreateFragment2ndStep(){
        //Requires empty public constructor
    }
    public static DesireCreateFragment2ndStep newInstance(){ return new DesireCreateFragment2ndStep();}

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
        View view = inflater.inflate(R.layout.desirecreate2nd_frag, container, false);
        mViewDataBinding = Desirecreate2ndFragBinding.bind(view);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        //TODO View for Fragment
        final EditText desirePrice   = (EditText) view.findViewById(R.id.create_desire_price);
        final EditText desireReward   = (EditText) view.findViewById(R.id.create_desire_reward);

        Button button = (Button) view.findViewById(R.id.button_2nd_step);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //getActivity().finish();

                if (desirePrice.getText().toString().isEmpty() || desireReward.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), R.string.createDesire_Empty_Text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String[] input = {desirePrice.getText().toString(), desireReward.getText().toString()};
                    mPresenter.createNextDesireCreateStep(input);
                    //Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);
                    //startActivity(intent);
                }

                       }
        });

        return view;
    }


    @Override
    public void showNextDesireCreateStep(String[] input) {
        String title = getActivity().getIntent().getExtras().getString("desireTitle");
        String description = getActivity().getIntent().getExtras().getString("desireDescription");
        Intent intent = new Intent(getContext(), DesireCreateActivity3rdStep.class);
        intent.putExtra("desireTitle", title);
        intent.putExtra("desireDescription", description);
        intent.putExtra("desirePrice", input[0]);
        intent.putExtra("desireReward", input[1]);
        startActivity(intent);
        //getActivity().finish();
    }

}
