package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link DesireDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesireDetailFragment extends Fragment implements DesireDetailContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESIRE_ID = "DESIRE_ID";

    private DesiredetailFragBinding mViewDataBinding;
    private DesireDetailContract.Presenter mPresenter;

    //maybe unnecessary
    private DesireLogic mDesireLogic;


    public static DesireDetailFragment newInstance(long desireId){
        Bundle arguments = new Bundle();
        arguments.putLong(DESIRE_ID, desireId);
        DesireDetailFragment fragment = new DesireDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPresenter(@NonNull DesireDetailContract.Presenter presenter){
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.desiredetail_frag, container, false);

        mViewDataBinding = DesiredetailFragBinding.bind(view);

        mViewDataBinding.setDesirelogic(mDesireLogic);

        setHasOptionsMenu(true);

        setRetainInstance(true);

        // Set up floating action button


        /*
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskId = getArguments().getString(ARGUMENT_TASK_ID);
                Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                startActivityForResult(intent, REQUEST_EDIT_TASK);
            }
        });
        */

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void showDesire(Desire desire) {
        mViewDataBinding.setDesire(desire);
    }


    public void setDesireLogic(DesireLogic desireLogic){mDesireLogic = desireLogic;}


    /*
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DesireDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DesireDetailFragment.
     */
    // TODO: Rename and change types and number of parameters

    /*
    public static DesireDetailFragment newInstance(String param1, String param2) {
        DesireDetailFragment fragment = new DesireDetailFragment();
        Bundle args = new Bundle();
        args.putString(DESIRE_ID, param1);
        args.putString(DESIRE_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(DESIRE_ID);
            mParam2 = getArguments().getString(DESIRE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.desiredetail_frag, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
