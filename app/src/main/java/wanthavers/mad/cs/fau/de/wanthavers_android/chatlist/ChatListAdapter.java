package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {


    private List<Chat> mChatList;
    private List<ChatItemViewModel> chatItemViewModels;
    private final GetUser mGetUser;
    private final GetDesire mGetDesire;
    private User mUser;
    private long mLoggedInUserId;
    private ViewGroup mViewGroup;

    private ChatListContract.Presenter mUserActionsListener;
    private ChatListViewModel mChatListViewModel;
    private UseCaseHandler mUseCaseHandler;

    public ChatListAdapter(List<Chat> chats, ChatListContract.Presenter itemListener,
                           ChatListViewModel chatListViewModel,
                           @NonNull GetUser getUser, long loggedInUserId,
                           @NonNull GetDesire getDesire) {
        setList(chats);
        mUserActionsListener = itemListener;
        mChatListViewModel = chatListViewModel;
        mGetUser = getUser;
        mLoggedInUserId = loggedInUserId;
        mUseCaseHandler = UseCaseHandler.getInstance();
        mGetDesire = getDesire;
        chatItemViewModels = new ArrayList<>();
    }

    public void replaceData(List<Chat> chatList) {
        setList(chatList);
    }

    private void setList(List<Chat> chatList) {
        mChatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChatList != null ? mChatList.size() : 0;
    }

    @Override
    public Chat getItem(int i) {
        return mChatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Chat chat = getItem(i);
        ChatItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = ChatItemBinding.inflate(inflater, viewGroup, false);

        } else {
            binding = DataBindingUtil.getBinding(viewGroup);
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view
        ChatListItemActionHandler itemActionHandler = new ChatListItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);

        binding.setChats(mChatListViewModel);
        binding.setChat(chat);

        chatItemViewModels.add(new ChatItemViewModel());
        binding.setChatItemViewModel(chatItemViewModels.get(i));

        // get User and bind to view
        long otherUserId = getOtherUserId(chat.getUser1(),chat.getUser2());
        mViewGroup = viewGroup;
        executeUserUseCase(otherUserId, i);

        //getDesire and bind to view
        executeDesireUseCase(chat.getDesireId(), i);




        binding.executePendingBindings();
        return binding.getRoot();
    }


    private long getOtherUserId(long user1Id, long user2Id){

        if(user1Id == mLoggedInUserId){
            return user2Id;
        }

        return user1Id;
    }


    private void executeUserUseCase(long userId, final int curChat){



        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser,requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        mUser = response.getUser();
                        chatItemViewModels.get(curChat).setUser(mUser);
                        Media m = mUser.getImage();

                        if(m != null) {
                            final ImageView profileView = (ImageView) mViewGroup.findViewById(R.id.ivProfileOther);
                            Picasso.with(mViewGroup.getContext()).load(m.getLowRes()).into(profileView);
                            chatItemViewModels.get(curChat).setProfilePic(profileView);
                        }


                    }

                    @Override
                    public void onError() {
                        /* TODO think about something useful to do on error
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        mChatListView.showLoadingChatsError();
                        */
                    }

                });
    }


    public void executeDesireUseCase(long desireId, final int curChat){
        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(desireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {
                        Desire desire = response.getDesire();
                        chatItemViewModels.get(curChat).setDesire(desire);


                    }

                    @Override
                    public void onError() {
                        //error handling
                        Desire desire = new Desire();
                        desire.setTitle("AAAAAAHHHH");
                        chatItemViewModels.get(curChat).setDesire(desire);
                    }
                });
    }
}
