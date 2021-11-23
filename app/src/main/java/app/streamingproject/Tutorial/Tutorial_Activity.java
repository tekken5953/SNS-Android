package app.streamingproject.Tutorial;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import app.streamingproject.MainActivity;
import app.streamingproject.OuterClass.SharedPreferenceManager;
import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.UserItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Tutorial_Activity extends AppCompatActivity {

    ImageView user_layout, user_layout_arrow, title_iv;
    View include_home, include_new, include_chat, include_user, layout_home, menu_home, layout_new, new_photo, new_photo_not, new_content;
    com.rey.material.widget.TextView title;

    Integer count = 1;

    MyAPI mMyAPI;

    String user_name_sp;

    FirebaseStorage storage = FirebaseStorage.getInstance(); // 인스턴스 생성
    StorageReference storageRef = storage.getReference(); // 스토리지 참조

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        include_home = findViewById(R.id.tutorial1_include_home);
        include_new = findViewById(R.id.tutorial1_include_new);
        include_chat = findViewById(R.id.tutorial1_include_chat);
        include_user = findViewById(R.id.tutorial1_include_user);
        title = findViewById(R.id.tutorial1_text);
        user_layout = findViewById(R.id.tutorial_layout_user);
        user_layout_arrow = findViewById(R.id.tutorial_layout_user_arrow);
        layout_home = findViewById(R.id.tutorial_layout_home);
        menu_home = findViewById(R.id.tutorial_menu_home);
        layout_new = findViewById(R.id.tutorial_layout_new);
        new_photo = findViewById(R.id.tutorial_new_photo);
        new_photo_not = findViewById(R.id.tutorial_new_photo_not);
        new_content = findViewById(R.id.tutorial_new_content);

        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                    case MotionEvent.ACTION_MOVE : {
                        v.setScaleY(1.1f);
                        v.setScaleX(1.1f);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.setScaleY(1f);
                        v.setScaleX(1f);
                        break;
                    }
                }
                return false;
            }
        });

        include_home.setEnabled(false);
        include_new.setEnabled(false);
        include_chat.setEnabled(false);
        include_user.setEnabled(false);
        include_new.setVisibility(View.GONE);
        include_chat.setVisibility(View.GONE);
        include_user.setVisibility(View.GONE);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 1:
                        title.setText("이곳에서 게시글을 확인 할 수 있습니다");
                        layout_home.setVisibility(View.VISIBLE);
                        menu_home.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 2:
                        title.setText("이곳에서 게시글을 등록 할 수 있습니다");
                        include_home.setVisibility(View.GONE);
                        include_new.setVisibility(View.VISIBLE);
                        layout_home.setVisibility(View.GONE);
                        menu_home.setVisibility(View.GONE);
                        layout_new.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 3:
                        title.setText("이곳을 클릭하여 사진을 등록할 수 있습니다");
                        new_photo.setVisibility(View.VISIBLE);
                        layout_new.setVisibility(View.GONE);
                        count += 1;
                        break;
                    case 4:
                        title.setText("사진등록을 안하시려면 체크를 해제해주세요");
                        new_photo.setVisibility(View.GONE);
                        new_photo_not.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 5:
                        title.setText("사진과 함께 등록 될 메시지입니다");
                        new_photo_not.setVisibility(View.GONE);
                        new_content.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 6:
                        title.setText("유저와의 채팅이 추가될 공간입니다!\n지금은 준비중이에요\uD83D\uDE2D\uD83D\uDEE0\uD83D\uDC77");
                        new_content.setVisibility(View.GONE);
                        include_new.setVisibility(View.GONE);
                        include_chat.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 7:
                        title.setText("이곳에서 설정 및 프로필 정보 확인이 가능합니다");
                        include_chat.setVisibility(View.GONE);
                        include_user.setVisibility(View.VISIBLE);
                        count += 1;
                        break;
                    case 8:
                        title.setText("마지막으로 프로필 설정을 도와드리겠습니다");
                        count += 1;
                        break;
                    case 9:
                        title.setText("위의 아이콘을 클릭하여 프로필사진을 추가해주세요!");
                        user_layout.setVisibility(View.VISIBLE);
                        user_layout_arrow.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);
                        count += 1;
                        break;
                }
            }
        });

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            // 사진 파이어베이스 스토리지에 저장
                            String filename = "profile_" + user_name_sp + ".jpg";
                            StorageReference riverRef = storageRef.child("user/" + filename);

                            UploadTask uploadTask = riverRef.putFile(uri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.e("retrofit", "이미지 업로드 실패" + e.getMessage());
                                    Toast.makeText(Tutorial_Activity.this, "이미지 업로드 실패!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("retrofit", "이미지 업로드 성공");
                                    Toast.makeText(Tutorial_Activity.this, "이미지 업로드 성공!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //프로필 수정 정보 서버에 저장
                            initMyAPI();

                            UserItem item = new UserItem();
                            item.setProfile(filename);

                            Call<UserItem> update_profile = mMyAPI.update_profile_by_name(user_name_sp, item);
                            update_profile.enqueue(new Callback<UserItem>() {
                                @Override
                                public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                                    Log.d("retrofit", "프로필 정보 저장 완료");
                                    Intent intent = new Intent(Tutorial_Activity.this, MainActivity.class);
                                    intent.putExtra("name", user_name_sp);
                                    intent.putExtra("login_time", System.currentTimeMillis() + "");
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<UserItem> call, Throwable t) {
                                    Log.d("retrofit", "프로필 정보 저장 실패");
                                    Toast.makeText(Tutorial_Activity.this, "프로필 정보 저장 실패", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Tutorial_Activity.this, MainActivity.class);
                                    intent.putExtra("name", user_name_sp);
                                    intent.putExtra("login_time", System.currentTimeMillis() + "");
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                });

        //유저 이름 불러오기
        user_name_sp = SharedPreferenceManager.getString(Tutorial_Activity.this, "name");

        user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
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