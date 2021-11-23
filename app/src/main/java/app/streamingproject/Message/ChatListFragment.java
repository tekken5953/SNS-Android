package app.streamingproject.Message;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.material.widget.ImageView;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.UserItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatListFragment extends Fragment {

    ArrayList<ChatNewItem> newItems = new ArrayList<>();
    ArrayList<ChatListItem> listItems = new ArrayList<>();
    ChatNewAdapter newAdapter;
    ChatListAdapter listAdapter;
    RecyclerView.LayoutManager mLayoutManager, lLayoutManager;
    RecyclerView listRecycler;

    MyAPI mMyAPI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_list_fragment, container, false);

        // 채팅 리스트 리사이클러 뷰 구현
        listRecycler = v.findViewById(R.id.msg_list_recycler);
        listAdapter = new ChatListAdapter(listItems);
        lLayoutManager = new LinearLayoutManager(getContext());
        listRecycler.setLayoutManager(lLayoutManager);

        listRecycler.setAdapter(listAdapter);

        initMyAPI();

        //TODO 서버에서 리스폰스 받아서 아이템 추가
        listItems.clear(); // 아이템 중복 생성 방지
        list_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.profile1, null), "유저1", "ㅎㅇㅎㅇ", "07-27-09:44");
        list_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.profile2, null), "유저2", "ㅂㅇㅂㅇ", "07-31-12:27");
        list_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.profile3, null), "유저3", "ㅇㅇㅇㅇ", "08-02-03:11");
        list_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.profile4, null), "유저4", "ㅋㅋㅋㅋ", "08-04-19:58");
        list_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.profile5, null), "유저5", "ㅂㅂㅂㅂ", "08-04-21:35");
        listAdapter.notifyDataSetChanged();

        // 리스트 아이템 클릭 시 채팅화면 액티비티로 이동
        listAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("name", listItems.get(position).getUser_name());
                startActivity(intent);
            }
        });


        //새로운 채팅(fab icon) 클릭
        com.rey.material.widget.FloatingActionButton fab = v.findViewById(R.id.msg_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View view_new = LayoutInflater.from(getContext()).inflate(R.layout.chat_new_start_dialog, container, false);
                builder.setView(view_new);
                final ImageView back = view_new.findViewById(R.id.chat_start_back_iv);
                final TextView create = view_new.findViewById(R.id.chat_start_create_tv);
                final com.rey.material.widget.EditText search = view_new.findViewById(R.id.chat_new_search_et);
                final RecyclerView newRecycler = view_new.findViewById(R.id.chat_start_recycler);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                newItems.clear(); // 아이템 중복 생성 방지

                // 새로운 채팅유저 리사이클러뷰 선언
                mLayoutManager = new LinearLayoutManager(getContext());
                newRecycler.setLayoutManager(mLayoutManager);

                newAdapter = new ChatNewAdapter(newItems);
                newRecycler.setAdapter(newAdapter);
                Log.e("fab", newRecycler.getAdapter() + "");

                //새로운 채팅 전체 유저 불러오기
                Call<List<UserItem>> get_all_user = mMyAPI.get_user();
                get_all_user.enqueue(new Callback<List<UserItem>>() {
                    @Override
                    public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {
                        assert response.body() != null;
                        for (UserItem item : response.body()) {
                            //TODO 이미지파일 불러오기
                            new_addItem(ResourcesCompat.getDrawable(getResources(), R.drawable.noprofile, null),
                                    item.getName());
                        }

                        newAdapter.notifyDataSetChanged();
                        Log.e("fab", "총 유저수 = " + newAdapter.getItemCount() + "명");
                    }

                    @Override
                    public void onFailure(Call<List<UserItem>> call, Throwable t) {

                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 채팅방 오픈
                        Toast.makeText(getContext(), "채팅방 오픈", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        return v;
    }

    // 새로운 채팅유저 리사이클러 뷰 아이템 추가 클래스
    public void new_addItem(Drawable profile, String name) {
        ChatNewItem item = new ChatNewItem(profile, name);
        item.setProfile(profile);
        item.setName(name);
        newItems.add(item);
    }

    // 채팅 리스트 리사이클러 뷰 아이템 추가 클래스
    public void list_addItem(Drawable profile, String name, String chat_content, String time_stamp) {
        ChatListItem item = new ChatListItem(profile, name, chat_content, time_stamp);
        item.setProfile(profile);
        item.setUser_name(name);
        item.setChat_content(chat_content);
        item.setTime_stamp(time_stamp);
        listItems.add(item);
    }

    private void initMyAPI() {
//        final String URL = "http://10.0.2.2:3333/"; //AVD
//        final String URL = "http://localhost:3333/"; //Phone
        final String URL = "http://52.14.240.99/"; //AWS
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
}
