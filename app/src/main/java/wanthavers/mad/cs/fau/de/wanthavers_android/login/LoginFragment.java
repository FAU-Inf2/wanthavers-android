package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.content.Context;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginFragBinding mViewDataBinding;
    private LoginContract.Presenter mPresenter;


    public LoginFragment(){
        //Requires empty public constructor
    }
    public static LoginFragment newInstance(){ return new LoginFragment();}

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
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
        View view = inflater.inflate(R.layout.login_frag, container, false);
        mViewDataBinding = LoginFragBinding.bind(view);

        //setHasOptionsMenu(true);
        //setRetainInstance(true);


        Button user1 = (Button) view.findViewById(R.id.button_user1);
        Button user2 = (Button) view.findViewById(R.id.button_user2);
        Button user3 = (Button) view.findViewById(R.id.button_user3);
        user1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                mPresenter.createDesireList();
            }
        });

        user2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                mPresenter.createDesireList();

            }
        });

        user3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                mPresenter.createDesireList();

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
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

}
