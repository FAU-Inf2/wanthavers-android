package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.ChatListActivity;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirelistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.NavHeaderBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.DesireCreateActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.GpsLocationTrackerLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting.FilterSettingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.LoginActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.settings.SettingsActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListFragment extends Fragment implements  DesireListContract.View {


    private DesireListContract.Presenter mPresenter;
    private DesireListAdapter mListAdapter;
    private DesireListViewModel mDesireListViewModel;
    private DesireLogic mDesireLogic;
    private NavHeaderBinding mNavHeaderBinding;
    private int i = 0;
    private GpsLocationTrackerLogic mGpsLocationTracker;
    private LatLng mLatLng;

    public DesireListFragment(){
        //Requires empty public constructor
    }


    public static DesireListFragment newInstance(){ return new DesireListFragment();}

    @Override
    public void setPresenter(@NonNull DesireListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    public void setNavBinding(NavHeaderBinding navBinding){
        mNavHeaderBinding = navBinding;
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

        //check for Location Runtime Permissions
        isFineLocationPermissionGranted();

        //starting GPS LocationTracker for Users actual LatLng
        mGpsLocationTracker = new GpsLocationTrackerLogic(this.getActivity(), 49.589674d, 11.011961d); //LatLng Erlangen
        double lat = mGpsLocationTracker.getLatitude();
        double lon = mGpsLocationTracker.getLongitude();
        mLatLng = new LatLng(lat, lon);
        // TODO Oliver Lutz: set LocationFilter for DesireList

        //Set up desire view
        RecyclerView recyclerView = (RecyclerView) desirelistFragBinding.desiresList;

        //to improve performance set the layout size as fixed as it is fixed in our case
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.list_divider_mediumdark,1));

        //use Linear Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mListAdapter = new DesireListAdapter(new ArrayList<DesireItemViewModel>(0),mPresenter, mDesireListViewModel, mDesireLogic);

        recyclerView.setAdapter(mListAdapter);

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


        swipeRefreshLayout.setScrollUpChild(recyclerView);

        // dynamically load new desires on scroll down
        if (Build.VERSION.SDK_INT >= 23) {
            recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if(!v.canScrollVertically(1)){
                        mPresenter.loadDesires(true,false, true);
                    }
                }
            });
        } else {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(!recyclerView.canScrollVertically(1)){
                        mPresenter.loadDesires(true, false, true);
                    }
                }
            });
        }

        setHasOptionsMenu(true);

        View root = desirelistFragBinding.getRoot();
        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_chat:
                //dummy user - TODO get real user here
                User user = new User("otto","blub@blub.de");
                user.setId(1234);
                mPresenter.openChat(user);
            break;
            case R.id.menu_filter:
                mPresenter.openFilterSettings();
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if(WantHaversApplication.getNewMessages()) {
            inflater.inflate(R.menu.desire_list_menu_newmessages, menu);
        }else {
            inflater.inflate(R.menu.desire_list_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);


    }

    public void setViewModel(DesireListViewModel viewModel){mDesireListViewModel = viewModel;}

    public void setDesireLogic(DesireLogic desireLogic){mDesireLogic = desireLogic;}

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

    public void showDesires(List<DesireItemViewModel> desireModels){

        mListAdapter.replaceData(desireModels);
        mDesireListViewModel.setDesireListSize(desireModels.size());
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

    @Override
    public void showDesireDetailsUi(long desireId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.

        Intent intent = new Intent(getContext(), DesireDetailActivity.class);
        intent.putExtra(DesireDetailActivity.EXTRA_DESIRE_ID, desireId);
        startActivity(intent);
    }

    @Override
    public void showChatList(long userid){

        //TODO change back to going to chat overview

        Intent intent = new Intent(getContext(), ChatListActivity.class);
        intent.putExtra(ChatListActivity.USER_ID, userid);
        startActivity(intent);


    }

    public void setUser(User user){
        mNavHeaderBinding.setUser(user);
        Media m = user.getImage();


        if (m != null) {
            final ImageView profileView = mNavHeaderBinding.navHeaderUserImage;
            Picasso.with(mNavHeaderBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        }
    }


    @Override
    public void showMyDesires(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        intent.putExtra(DesireListActivity.EXTRA_FRAGMENT_ID, DesireListType.MY_DESIRES);
        startActivity(intent);
    }


    @Override
    public void showMyTransactions(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        intent.putExtra(DesireListActivity.EXTRA_FRAGMENT_ID, DesireListType.MY_TRANSACTIONS);
        startActivity(intent);
    }

    @Override
    public void showAllDesires(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        intent.putExtra(DesireListActivity.EXTRA_FRAGMENT_ID, DesireListType.ALL_DESIRES);
        startActivity(intent);
    }

    @Override
    public void showNewDesire() {
        Intent intent = new Intent(getContext(), DesireCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLogout() {
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext().getApplicationContext());
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USER_EMAIL, null);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_PASSWORD, null);
        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, -1);
        RestClient.triggerSetNewBasicAuth();


        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showSettings() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showFilterSettings(){
        Intent intent = new Intent(getContext(), FilterSettingActivity.class);
        startActivity(intent);
    }


    public boolean isFineLocationPermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }else { //permission is automatically granted because sdk<23
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_DENIED){
            showMessage(getString(R.string.declined_location_runtime_permission));
        }
    }

}
