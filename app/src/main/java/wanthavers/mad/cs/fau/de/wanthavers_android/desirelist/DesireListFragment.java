package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirelistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListFragment extends Fragment implements  DesireListContract.View {


    private DesireListContract.Presenter mPresenter;
    private DesireListAdapter mListAdapter;
    private DesireListViewModel mDesireListViewModel;

    public DesireListFragment(){
        //Requires empty public constructor
    }


    public static DesireListFragment newInstance(){ return new DesireListFragment();}

    @Override
    public void setPresenter(@NonNull DesireListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DesirelistFragBinding desirelistFragBinding = DesirelistFragBinding.inflate(inflater,container,false);

        desirelistFragBinding.setDesires(mDesireListViewModel);

        desirelistFragBinding.setPresenter(mPresenter);

        //Set up desire view
        ListView listView = desirelistFragBinding.desiresList;

        mListAdapter = new DesireListAdapter(new ArrayList<Desire>(0),mPresenter);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fabCreateNewDesire =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_desire);


        fabCreateNewDesire.setImageResource(R.drawable.ic_add);
        fabCreateNewDesire.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mPresenter.createNewDesire();
            }
        });

        // Set up progress indicator  TODO decide whether this is needed
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = desirelistFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);


        setHasOptionsMenu(true);

        View root = desirelistFragBinding.getRoot();
        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_chat:
                mPresenter.openChat();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setViewModel(DesireListViewModel viewModel){mDesireListViewModel = viewModel;}


    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    public void showDesires(List<Desire> desires){
        mListAdapter.replaceData(desires);
        mDesireListViewModel.setDesireListSize(desires.size());
    }

    @Override
    public void showLoadingDesiresError() {
        showMessage(getString(R.string.loading_desires_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @TargetApi(Build.VERSION_CODES.M)  //TODO should check whether this is ok
    @Override
    public void showDesireDetailsUi(long desireId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), DesireDetailActivity.class);
        intent.putExtra(DesireDetailActivity.EXTRA_TASK_ID, desireId);
        startActivity(intent);
    }
}
