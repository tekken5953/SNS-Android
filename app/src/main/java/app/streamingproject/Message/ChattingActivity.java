package app.streamingproject.Message;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.streamingproject.OuterClass.ChattingViewTypeClass;
import app.streamingproject.R;

public class ChattingActivity extends AppCompatActivity {

    TextView user_name;
    ImageView back;
    String get_user_name = null;

    private ArrayList<ChattingItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_activitiy);

        user_name = findViewById(R.id.chatting_user_name_tv);
        back = findViewById(R.id.chatting_back_iv);

        get_user_name = getIntent().getExtras().getString("name");

        Log.e("chatting", getIntent().getExtras().getString("name") + "");

        user_name.setText(get_user_name); // 상대 이름 설정

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.initializeData();

        RecyclerView recyclerView = findViewById(R.id.chatting_recycler);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new ChattingAdapter(dataList));  // Adapter 등록
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void initializeData() {
        dataList = new ArrayList<>();

        dataList.add(new ChattingItem(get_user_name, "08-02 20:42", "ㅎㅇㄹ", ChattingViewTypeClass.ViewType.LEFT_CONTENT));
        dataList.add(new ChattingItem("나", "08-02 20:45", "ㅎㅇㅎㅇ", ChattingViewTypeClass.ViewType.RIGHT_CONTENT));
        dataList.add(new ChattingItem("나", "08-02 20:45", "ㅂ2ㅂ2", ChattingViewTypeClass.ViewType.RIGHT_CONTENT));
        dataList.add(new ChattingItem(get_user_name, "08-02 20:47", "ㅂㅇㅂㅇ", ChattingViewTypeClass.ViewType.LEFT_CONTENT));

    }
}