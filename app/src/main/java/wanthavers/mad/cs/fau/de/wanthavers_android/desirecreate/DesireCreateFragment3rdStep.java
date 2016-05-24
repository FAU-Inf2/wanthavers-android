package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.Desirecreate3rdFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class DesireCreateFragment3rdStep extends Fragment implements DesireCreateContract.View {
    private Desirecreate3rdFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private final Desire desire = new Desire();

    public DesireCreateFragment3rdStep(){
        //Requires empty public constructor
    }
    public static DesireCreateFragment3rdStep newInstance(){ return new DesireCreateFragment3rdStep();}

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
        View view = inflater.inflate(R.layout.desirecreate3rd_frag, container, false);
        mViewDataBinding = Desirecreate3rdFragBinding.bind(view);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        final EditText desireDropzone   = (EditText) view.findViewById(R.id.create_desire_dropzone);
        //TODO View for Fragment
        Button button = (Button) view.findViewById(R.id.button_3rd_step);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if (desireDropzone.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), R.string.createDesire_Empty_Text, Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    String title = getActivity().getIntent().getExtras().getString("desireTitle");
                    String description = getActivity().getIntent().getExtras().getString("desireDescription");
                    String price = getActivity().getIntent().getExtras().getString("desirePrice");
                    String reward = getActivity().getIntent().getExtras().getString("desireReward");
                    Uri image =  getActivity().getIntent().getExtras().getParcelable("desireImage");

                    setDataForDesire(title, description, Integer.parseInt(price), Integer.parseInt(reward), desireDropzone.getText().toString(), image);
                    //sendDesireToServer(desire);

                    mPresenter.createNextDesireCreateStep(null);
                }
            }
        });

        return view;
    }


    @Override
    public void showNextDesireCreateStep(String[] s) {
        Log.d("DesireTitle:",desire.getTitle());
        Log.d("DesireDesciption:",desire.getDescription());
        Log.d("DesirePrice:",Double.toString(desire.getPrice()));
        Log.d("DesireReward:",Double.toString(desire.getReward()));
        Log.d("DesireDropzone:",desire.getDropzone_string());
        Log.d("DesireColor:",Integer.toString(desire.getColorIndex()));

        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
        //getActivity().finish();

    }

    public void setDataForDesire(String title, String description, int price, int reward, String dropzone, Uri image){
        desire.setTitle(title);
        desire.setDescription(description);
        desire.setPrice(price);
        desire.setReward(reward);
        desire.setDropzone_string(dropzone);

        int colorNumber = (int) (Math.random() * 4);
        desire.setColorIndex(colorNumber);
        desire.setCreation_time(new Date());


        if(image != null) {
            Log.d("Image", image.getPath());
            File file = new File(image.getPath());
            Log.d("Image", file.getPath());

            //desire.setImage(); // TODO
        }

        //will be set by the server
        desire.setCreator(null);
        desire.setId(0);
    }


    public void sendDesireToServer(Desire desire){
        //TODO
    }


}
