package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.FlagReason;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailAcceptDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailDeleteDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailHaverCancelDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailReportPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailUnacceptHaverPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.OnSwipeTouchListener;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.rating.RatingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.userprofile.UserProfileActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;


public class DesireDetailFragment extends Fragment implements DesireDetailContract.View {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESIRE_ID = "DESIRE_ID";
    private DesiredetailFragBinding mDesireDetailFragBinding;
    private DesireDetailContract.Presenter mPresenter;
    private DesireDetailAdapter mListAdapter;
    private DesireLogic mDesireLogic;
    private RecyclerView mRecyclerView;
    private DesireDetailViewModel mDesireDetailViewModel;
    private DesireDetailActionHandler mDesireDetailActionHandler;
    private Dialog mReportDialog, mDeleteDesireDialog, mAcceptDesireDialog, mDeleteHaverDialog, mUnacceptHaverDialog;
    private DesiredetailReportPopupBinding mDesiredetailReportPopupBinding;
    private DesiredetailAcceptDesirePopupBinding mDesiredetailAcceptDesirePopupBinding;
    private DesiredetailHaverCancelDesirePopupBinding mDesiredetailHaverCancelDesirePopupBinding;
    private Menu mOptionsMenu;
    private boolean mLoadFinishedFlag;

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

        mDesireDetailFragBinding = DesiredetailFragBinding.inflate(inflater, container, false);

        mDesireDetailFragBinding.setHavers(mDesireDetailViewModel);

        //Set up havers view
        mRecyclerView = mDesireDetailFragBinding.haverList;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            //show full list without scrolling
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);

        setHasOptionsMenu(true);

        //set up action handler
        mDesireDetailActionHandler = new DesireDetailActionHandler(mPresenter);
        mDesireDetailFragBinding.setActionHandler(mDesireDetailActionHandler);

        mListAdapter = new DesireDetailAdapter(new ArrayList<Haver>(0), mPresenter, mDesireDetailActionHandler, mDesireLogic);
        mRecyclerView.setAdapter(mListAdapter);

        //ScrollView
        ScrollView scrollView = mDesireDetailFragBinding.desireDetailMainScreen;
        scrollView.setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter));

        return mDesireDetailFragBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mLoadFinishedFlag = false;
        inflater.inflate(R.menu.desire_detail_menu, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){

        if(mLoadFinishedFlag && getActivity() != null) {
            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getActivity().getApplicationContext());
            long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);

            Desire curDesire = mDesireDetailFragBinding.getDesire();

            MenuItem menReportDesire = menu.findItem(R.id.menu_report_desire);
            MenuItem menDeleteDesire = menu.findItem(R.id.menu_delete_desire);

            if (mDesireDetailFragBinding.getDesire() != null && mDesireDetailFragBinding.getDesire().getCreator().getId() == loggedInUser) {

                if(curDesire.getStatus() == DesireStatus.STATUS_OPEN) {
                    menDeleteDesire.setVisible(true);
                }

                if(curDesire.getStatus() == DesireStatus.STATUS_IN_PROGRESS ){
                    menDeleteDesire.setVisible(true);
                }

            } else {
                menReportDesire.setVisible(true);
            }

        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_report_desire:
                mPresenter.openReportPopup();
                break;
            case R.id.menu_delete_desire:
                showDeleteDesirePopup();
                break;
        }
        return true;
    }

    public void setViewModel(DesireDetailViewModel viewModel) {mDesireDetailViewModel = viewModel;}

    /**
     *
     * Process incoming data & modify current view
     *
     */

    @Override
    public void showDesire(Desire desire, Haver acceptedHaver) {

        if (acceptedHaver != null) {
            mDesireDetailFragBinding.noHavers.setVisibility(View.GONE);
        }

        mListAdapter.setDesire(desire);
        mDesireDetailFragBinding.setDesire(desire);
        mDesireDetailFragBinding.setDesirelogic(mDesireLogic);
        mDesireDetailFragBinding.setHaver(acceptedHaver);

        //show desire image
        Media mediaDesire = desire.getImage();
        CircularImageView desireImageView = mDesireDetailFragBinding.desireDetailItemImage;
        showImage(mediaDesire, desireImageView);

        if (desire.getStatus() == DesireStatus.STATUS_OPEN) {
            //nothing to do; logic in presenter
        } else if (desire.getStatus() == DesireStatus.STATUS_IN_PROGRESS) {
            showDesireInProgress();
        } else if (desire.getStatus() == DesireStatus.STATUS_DONE) {
            showDesireDone();
        } else {
            showDesireNeutral();
        }

    }

    @Override
    public void showDesireOpen(boolean isUnacceptedHaver) {
        Desire desire = mDesireDetailFragBinding.getDesire();
        boolean isCreator = mDesireLogic.isDesireCreator(desire.getCreator().getId());
        boolean isBiddingAllowed = desire.isBiddingAllowed();

        if(isAdded()) {
            showStatus(getString(R.string.haver_status_open));
        }

        if (isCreator) {
            mDesireDetailFragBinding.finishDesire.setVisibility(View.GONE);
            mPresenter.loadHavers(false);
        } else if (isUnacceptedHaver) {
            showWanter();
            showStatus(getString(R.string.haver_status_waiting));
            if (isBiddingAllowed) {
                showBidderView(true);
            }
            mDesireDetailFragBinding.acceptDesire.setVisibility(View.GONE);
            mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);
            endLoadingProgress();
        } else {
            showWanter();
            if (isBiddingAllowed) {
                showBidderView(false);
            }
            mDesireDetailFragBinding.acceptDesire.setVisibility(View.VISIBLE);
            mDesireDetailFragBinding.cancelDesire.setVisibility(View.GONE);
            endLoadingProgress();
        }
    }

    private void showDesireInProgress() {
        Desire desire = mDesireDetailFragBinding.getDesire();
        boolean isCreator = mDesireLogic.isDesireCreator(desire.getCreator().getId());
        Haver acceptedHaver = mDesireDetailFragBinding.getHaver();
        boolean isAcceptedHaver = (mDesireLogic.getLoggedInUserId() == acceptedHaver.getUser().getId());

        if (isCreator) {
            if(isAdded()) {
                showStatus(getString(R.string.haver_status_in_progress));
            }

            mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.VISIBLE);
            Media mediaHaver = acceptedHaver.getUser().getImage();
            CircularImageView imageAcceptedHaverView = mDesireDetailFragBinding.imageAcceptedHaver;
            showImage(mediaHaver, imageAcceptedHaverView);

            mDesireDetailFragBinding.finishDesire.setVisibility(View.VISIBLE);
        } else if (isAcceptedHaver){
            showWanter();

            mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);

            if(isAdded()) {
                showStatus(getString(R.string.haver_status_accepted));
            }
        } else {
            showWanter();
            if(isAdded()) {
                showStatus(getString(R.string.haver_status_rejected));
            }
        }
        endLoadingProgress();
    }

    private void showDesireDone() {
        Desire desire = mDesireDetailFragBinding.getDesire();
        boolean isCreator = mDesireLogic.isDesireCreator(desire.getCreator().getId());
        Haver acceptedHaver = mDesireDetailFragBinding.getHaver();
        boolean isAcceptedHaver = (mDesireLogic.getLoggedInUserId() == acceptedHaver.getUser().getId());

        //show status
        if(isAdded()) {
            showStatus(getString(R.string.haver_status_done));
        }

        if (isCreator) {
            mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.VISIBLE);

            Media mediaHaver = acceptedHaver.getUser().getImage();
            CircularImageView imageAcceptedHaverView = mDesireDetailFragBinding.imageAcceptedHaver;
            showImage(mediaHaver, imageAcceptedHaverView);

            hideFinishDesire();
        } else if (isAcceptedHaver){
            showWanter();
        } else {
            showWanter();
            if(isAdded()) {
                showStatus(getString(R.string.haver_status_rejected));
            }
        }
        endLoadingProgress();
    }

    private void showDesireNeutral() {
        Desire desire = mDesireDetailFragBinding.getDesire();
        boolean isCreator = mDesireLogic.isDesireCreator(desire.getCreator().getId());
        Haver acceptedHaver = mDesireDetailFragBinding.getHaver();
        boolean isAcceptedHaver = (mDesireLogic.getLoggedInUserId() == acceptedHaver.getUser().getId());

        //show status
        if(isAdded() && desire.getStatus() == DesireStatus.STATUS_EXPIRED) {
            showStatus(getString(R.string.haver_status_expired));
        } else if(isAdded() && desire.getStatus() == DesireStatus.STATUS_DELETED) {
            showStatus(getString(R.string.haver_status_deleted));
        }

        if (isCreator) {
            mDesireDetailFragBinding.desireDetailHaverCard.setVisibility(View.GONE);
        } else if (isAcceptedHaver){
            showWanter();
        } else {
            showWanter();
            if(isAdded()) {
                showStatus(getString(R.string.haver_status_rejected));
            }
        }
        endLoadingProgress();
    }

    private void showStatus(String status) {
        mDesireDetailFragBinding.desireStatus.setText(status);
    }

    private void showWanter() {
        Media mediaWanter = mDesireDetailFragBinding.getDesire().getCreator().getImage();
        CircularImageView wanterImageView = mDesireDetailFragBinding.imageWanter;
        showImage(mediaWanter, wanterImageView);
    }

    private void showBidderView(boolean active) {
        if (active) {
            if (isAdded()) {
                mDesireDetailFragBinding.desireDetailPriceTitle.setText(getString(R.string.accepted_desire_bid_title));
            }
        } else {
            if (isAdded()) {
                mDesireDetailFragBinding.desireDetailPriceTitle.setText(getString(R.string.create_desire_price_header));
                mDesireDetailFragBinding.setBidder(null);
            }
        }
    }

    private void showImage(Media media, @NonNull ImageView view) {
        checkNotNull(view);
        if (media != null) {
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(media.getLowRes()).into(view);
            //Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            view.setImageResource(R.drawable.no_pic);
        }
    }

    @Override
    public void showHavers(List<Haver> havers) {
        mListAdapter.replaceData(havers);
        mDesireDetailViewModel.setWanterListSize(havers.size());
        mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.GONE);
        mDesireDetailFragBinding.haverList.setVisibility(View.VISIBLE);
        endLoadingProgress();
    }

    //may be modified
    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        /*final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });*/
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void endLoadingProgress() {
        mDesireDetailFragBinding.desireDetailLoadingScreen.setVisibility(View.GONE);
        mDesireDetailFragBinding.desireDetailMainScreen.setVisibility(View.VISIBLE);
        mLoadFinishedFlag = true;
        onPrepareOptionsMenu(mOptionsMenu);
    }

    private void hideFinishDesire() {
        mDesireDetailFragBinding.finishDesire.setVisibility(View.GONE);
        MenuItem menDeleteDesire = mOptionsMenu.findItem(R.id.menu_delete_desire);
        menDeleteDesire.setVisible(false);
    }

    /**
     *
     * Getter & Setter
     *
     */

    public void setDesireLogic(DesireLogic desireLogic){mDesireLogic = desireLogic;}

    @Override
    public void setBidder(Haver bidder) {
        mDesireDetailFragBinding.setBidder(bidder);
    }

    @Override
    public double getBidInput() {
        EditText bidInputView = mDesiredetailAcceptDesirePopupBinding.acceptDesireBidInput;
        double bid = -1;
        if (!bidInputView.getText().toString().equals("")) {
            bid = Double.valueOf(bidInputView.getText().toString());
        }
        return bid;
    }

    @Override
    public Haver getAcceptedHaver() {
        return mDesireDetailFragBinding.getHaver();
    }

    @Override
    public DesireFlag getReport() {
        //get values
        String comment = mDesiredetailReportPopupBinding.reportComment.getText().toString();
        FlagReason flagReason = null;
        String flagString = (String) mDesiredetailReportPopupBinding.reportReasonSpinner.getSelectedItem();
        String[] flags = getResources().getStringArray(R.array.report_reasons);

        if (flagString.equals(flags[0])) {
            flagReason = FlagReason.INAPPROPRIATE;
        } else  if (flagString.equals(flags[1])) {
            flagReason = FlagReason.IMPOSSIBLE;
        } else  if (flagString.equals(flags[2])) {
            flagReason = FlagReason.SPAM;
        }

        DesireFlag desireFlag = new DesireFlag();
        if (!comment.equals("")) {
            desireFlag.setComment(comment);
        }
        desireFlag.setFlagReason(flagReason);

        return desireFlag;
    }

    /**
     *
     * Leave current view
     *
     */

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void showChatDetailsUi(Chat chat) {
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra(ChatDetailActivity.EXTRA_CHAT_ID, chat.getObjectId());
        intent.putExtra("ChatOjbect", chat);
        startActivity(intent);
    }

    @Override
    public void showMap(Location location){
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("calledAct", "2"); //for distinguishing which activity started the map
        startActivity(intent);

    }

    @Override
    public void showRating(long desireId) {
        Intent intent = new Intent(getContext(), RatingActivity.class);
        intent.putExtra(RatingActivity.EXTRA_DESIRE_ID, desireId);
        startActivity(intent);
    }

    @Override
    public void showUserProfile(User user) {
        Intent intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     *
     * Popup issues
     *
     */

    @Override
    public void showAcceptDesirePopup(boolean initialCall) {
        mAcceptDesireDialog = new Dialog(getContext());

        mDesiredetailAcceptDesirePopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_accept_desire_popup, null, false);
        mAcceptDesireDialog.setContentView(mDesiredetailAcceptDesirePopupBinding.getRoot());

        mDesiredetailAcceptDesirePopupBinding.setActionHandler(mDesireDetailActionHandler);
        mDesiredetailAcceptDesirePopupBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mDesiredetailAcceptDesirePopupBinding.setDesirelogic(mDesireLogic);

        if (!initialCall) {
            if (isAdded()) {
                mDesiredetailAcceptDesirePopupBinding.acceptDesireTitle.setText(getString(R.string.desire_modify_bid));
            }
            mDesiredetailAcceptDesirePopupBinding.buttonSubmitBid.setVisibility(View.GONE);
            mDesiredetailAcceptDesirePopupBinding.buttonSubmitModifiedBid.setVisibility(View.VISIBLE);
            mDesiredetailAcceptDesirePopupBinding.acceptDesireSubtitle.setVisibility(View.GONE);
            mDesiredetailAcceptDesirePopupBinding.acceptDesireBidInput.setHint(String.valueOf( (int) mDesireDetailFragBinding.getBidder().getRequestedPrice()));
        }

        mAcceptDesireDialog.show();
    }

    @Override
    public void closeAcceptDesirePopup() {
        mAcceptDesireDialog.dismiss();
    }

    private void showDeleteDesirePopup() {

        mDeleteDesireDialog = new Dialog(getContext());

        DesiredetailDeleteDesirePopupBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_delete_desire_popup, null, false);
        mDeleteDesireDialog.setContentView(mBinding.getRoot());

        mBinding.setActionHandler(mDesireDetailActionHandler);
        mBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mBinding.setDesirelogic(mDesireLogic);

        mDeleteDesireDialog.show();
    }

    @Override
    public void closeDeleteDesirePopup() {
        mDeleteDesireDialog.dismiss();
    }

    @Override
    public void showDeleteHaverPopup() {
        mDeleteHaverDialog = new Dialog(getContext());

        mDesiredetailHaverCancelDesirePopupBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.desiredetail_haver_cancel_desire_popup, null, false
        );
        mDeleteHaverDialog.setContentView(mDesiredetailHaverCancelDesirePopupBinding.getRoot());

        mDesiredetailHaverCancelDesirePopupBinding.setActionHandler(mDesireDetailActionHandler);
        mDesiredetailHaverCancelDesirePopupBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mDesiredetailHaverCancelDesirePopupBinding.setDesirelogic(mDesireLogic);
        mDesiredetailHaverCancelDesirePopupBinding.setHaver(mDesireDetailFragBinding.getHaver());

        mDeleteHaverDialog.show();
    }

    @Override
    public void closeDeleteHaverPopup() {
        mDeleteHaverDialog.dismiss();
    }

    @Override
    public void showReportPopup() {
        mReportDialog = new Dialog(getContext());

        mDesiredetailReportPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_report_popup, null, false);
        mReportDialog.setContentView(mDesiredetailReportPopupBinding.getRoot());

        mDesiredetailReportPopupBinding.setActionHandler(mDesireDetailActionHandler);

        //Set up flag reason spinner
        Spinner mFlagReasonSpinner = mDesiredetailReportPopupBinding.reportReasonSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.report_reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFlagReasonSpinner.setAdapter(adapter);

        mReportDialog.show();
    }

    @Override
    public void closeReportPopup() {
        mReportDialog.dismiss();
    }

    @Override
    public void showUnacceptHaverPopup() {
        mUnacceptHaverDialog = new Dialog(getContext());

        DesiredetailUnacceptHaverPopupBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_unaccept_haver_popup, null, false);
        mUnacceptHaverDialog.setContentView(mBinding.getRoot());

        mBinding.setActionHandler(mDesireDetailActionHandler);
        mBinding.setHaver(mDesireDetailFragBinding.getHaver());

        mUnacceptHaverDialog.show();
    }

    @Override
    public void closeUnacceptHaverPopup() {
        mUnacceptHaverDialog.dismiss();
    }

    /**
     *
     * Snackbar messages
     *
     */

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAcceptHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.accepting_haver_error));
        }
    }

    @Override
    public void showDeleteDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.delete_desire_error));
        }
    }

    @Override
    public void showDeleteHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.desire_detail_delete_haver_error));
        }
    }

    @Override
    public void showFlagDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.flag_desire_error));
        }
    }

    @Override
    public void showGetChatForDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.get_chat_for_desire_error));
        }
    }

    @Override
    public void showLoadingDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.loading_desire_error));
        }
    }

    @Override
    public void showLoadingHaversError() {

        if (isAdded()) {
            showMessage(getString(R.string.loading_havers_error));
        }
    }

    @Override
    public void showSetHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.setting_haver_error));
        }
    }

    @Override
    public void showUpdateDesireStatusError() {
        if (isAdded()) {
            showMessage(getString(R.string.closing_transaction_error));
        }
    }

    @Override
    public void showUpdateRequestedPriceError() {
        if (isAdded()) {
            showMessage(getString(R.string.update_requested_price_error));
        }
    }

    @Override
    public void showTransactionSuccessMessage(){
        if (isAdded()) {
            showMessage(getString(R.string.closing_transaction_success));
        }
    }

    @Override
    public void showUnacceptHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.unaccept_haver_error));
        }
    }
}
