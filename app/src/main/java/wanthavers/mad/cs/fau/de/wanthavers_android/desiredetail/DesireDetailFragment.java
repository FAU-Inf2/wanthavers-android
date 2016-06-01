package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESIRE_ID = "DESIRE_ID";
    private DesiredetailFragBinding mDesireDetailFragBinding;
    private DesireDetailContract.Presenter mPresenter;
    private DesireDetailAdapter mListAdapter;
    private DesireLogic mDesireLogic;
    private RecyclerView mRecyclerView;
    private DesireDetailViewModel mDesireDetailViewModel;

    public DesireDetailFragment() {
        //Requires empty public constructor
    }

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
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.desiredetail_frag, container, false);

        //DesiredetailFragBinding desiredetailFragBinding = DesiredetailFragBinding.inflate(inflater, container, false);

        mDesireDetailFragBinding = DesiredetailFragBinding.inflate(inflater, container, false);

        //mViewDataBinding = DesiredetailFragBinding.bind(view);

        mDesireDetailFragBinding.setDesirelogic(mDesireLogic);

        mDesireDetailFragBinding.setHavers(mDesireDetailViewModel);



        //setRetainInstance(true);

        //Set up havers view
        mRecyclerView = mDesireDetailFragBinding.haverList;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mListAdapter = new DesireDetailAdapter(new ArrayList<Haver>(0), mPresenter);
        mRecyclerView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);
        /*
        // Set up progress indicator  TODO decide whether this is needed
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mDesireDetailFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);*/


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

        return mDesireDetailFragBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setViewModel(DesireDetailViewModel viewModel) {mDesireDetailViewModel = viewModel;}

    public void showDesire(Desire desire) {
        mDesireDetailFragBinding.setDesire(desire);

        //show desire image
        Media mediaDesire = desire.getImage();
        if (mediaDesire != null) {
            final ImageView profileView = mDesireDetailFragBinding.desireDetailItemImage;
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mDesireDetailFragBinding.desireDetailItemImage;
            profileView.setImageResource(R.drawable.no_pic);
        }

        //show wanter image
        User creator = desire.getCreator();

        if (!mDesireLogic.isDesireCreator(creator.getID())) {
            Media mediaWanter = creator.getImage();
            if (mediaWanter != null) {
                final ImageView profileView = mDesireDetailFragBinding.imageWanter;
                Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
            } else{
                //else case is neccessary as the image is otherwise overwritten on scroll
                final ImageView profileView = mDesireDetailFragBinding.imageWanter;
                profileView.setImageResource(R.drawable.no_pic);
            }
        }
    }

    public void showHavers(List<Haver> havers) {
        mListAdapter.replaceData(havers);
        mDesireDetailViewModel.setWanterListSize(havers.size());
    }

    @Override
    public void showLoadingHaversError() {
        showMessage(getString(R.string.loading_desires_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public void setDesireLogic(DesireLogic desireLogic){mDesireLogic = desireLogic;}

    //may be modified
    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
