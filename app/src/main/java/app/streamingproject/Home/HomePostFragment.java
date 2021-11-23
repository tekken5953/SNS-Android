package app.streamingproject.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import app.streamingproject.R;

public class HomePostFragment extends Fragment {

    RecyclerView mRecyclerView = null;
    HomePostRecyclerAdapter adapter;
    ArrayList<HomePostRecyclerItem> mList = new ArrayList<>();
    LinearLayoutManager manager;

    SwipeRefreshLayout swipeRefreshLayout;

    boolean isLoading = false;

    SpinKitView progress;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 리사이클러뷰에 Adapter 객체 지정.
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_post_fragment, container, false);
        adapter = new HomePostRecyclerAdapter(mList);

        mRecyclerView = view.findViewById(R.id.home_post_post_recycler);
        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        adapter.initPost(view.getContext());

        progress = view.findViewById(R.id.home_post_progress);

        if (mList.size() >= 5) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!isLoading) {
                        if (manager != null && manager.findLastCompletelyVisibleItemPosition() == mList.size() - 1) {
                            adapter.load_more(progress, view.getContext());
                            isLoading = true;
                        }
                    }
                }
            });
        }

        // 스와이프 리프래쉬
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_layout);
        swipeLayout(view.getContext());

        return view;
    }

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static HomePostFragment newInstance() {
        return new HomePostFragment();
    }

    private void swipeLayout(Context context) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.initPost(context);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }
}