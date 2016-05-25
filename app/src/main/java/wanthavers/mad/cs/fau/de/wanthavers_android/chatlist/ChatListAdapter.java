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
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {


    private List<Chat> mChatList;
    private List<User> mUserList;
    private List<Desire> mDesireList;

    private ChatListContract.Presenter mUserActionsListener;
    private ChatListViewModel mChatListViewModel;
    private UseCaseHandler mUseCaseHandler;

    public ChatListAdapter(List<Chat> chats, List<User> userList, List<Desire> desireList, ChatListContract.Presenter itemListener,
                           ChatListViewModel chatListViewModel) {
        setList(chats, userList, desireList);
        mUserActionsListener = itemListener;
        mChatListViewModel = chatListViewModel;
        mUseCaseHandler = UseCaseHandler.getInstance();
    }

    public void replaceData(List<Chat> chatList, List<User> userList, List<Desire> desireList) {
        setList(chatList, userList, desireList);
    }

    private void setList(List<Chat> chatList, List<User> userList, List<Desire> desireList) {
        mChatList = chatList;
        mUserList = userList;
        mDesireList = desireList;
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


    public User getUserForItem(int i){
        return mUserList.get(i);
    }

    public Desire getDesireForItem(int i){
        return mDesireList.get(i);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Chat chat = getItem(i);
        User user = getUserForItem(i);
        Desire desire = getDesireForItem(i);


        ChatItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = ChatItemBinding.inflate(inflater, viewGroup, false);

        } else {
            binding = DataBindingUtil.getBinding(view);
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view

        //binding.setChats(mChatListViewModel);

        ChatListItemActionHandler itemActionHandler = new ChatListItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);
        binding.setChat(chat);
        binding.setUser(user);
        binding.setDesire(desire);

        Media m = user.getImage();

        if (m != null) {
            final ImageView profileView = (ImageView) binding.ivProfileOther;
            Picasso.with(viewGroup.getContext()).load(m.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
            //chatItemViewModels.get(curChat).setProfilePic(profileView);
        }


        //chatItemViewModels.add(new ChatItemViewModel());
        //binding.setChatItemViewModel(chatItemViewModels.get(i));

        // get User and bind to view

        //executeUserUseCase(otherUserId, i);

        //getDesire and bind to view
        //executeDesireUseCase(chat.getDesireId(), i);

        binding.executePendingBindings();
        return binding.getRoot();
    }





    /*
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
                        /
                    }

                });
    }
    */

    /*
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
    */
}
