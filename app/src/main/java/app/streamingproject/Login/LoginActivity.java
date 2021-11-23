package app.streamingproject.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import app.streamingproject.Admin.AdminPageActivity;
import app.streamingproject.MainActivity;
import app.streamingproject.OuterClass.ProgressDialogClass;
import app.streamingproject.OuterClass.SharedPreferenceManager;
import app.streamingproject.OuterClass.ToastMsg;
import app.streamingproject.OuterClass.UpKeyboardClass;
import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.UserItem;
import app.streamingproject.Tutorial.Tutorial_Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity {

    private String show_pwd = "not showing";
    private static final String TAG = "show_pwd";
    private static final String retrofit = "retrofit";
    private static final String user_name = "name";
    private static final String user_profile = "profile";
    private static final String user_pwd = "pwd";

    MyAPI mMyAPI;

    EditText et_name, et_pwd;
    ImageView show_password;
    ToastMsg toastMsg = new ToastMsg(); // 커스텀 토스트 메시지 클래스
    UpKeyboardClass upkeyboard = new UpKeyboardClass(); // 키보드 업 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        et_name = findViewById(R.id.login_edit_id);
        et_pwd = findViewById(R.id.login_edit_pwd);
        show_password = findViewById(R.id.login_show_pwd_iv);

        Log.d(TAG, show_pwd);
        Log.d(TAG, et_pwd.getInputType() + "");

        // 비밀번호 보이기 클릭 시 이벤트 처리
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_pwd.equals("not showing")) {
                    show_password.setImageResource(R.drawable.show_pwd);
                    et_pwd.setInputType(144); // 비밀번호 숨기기
                    show_pwd = "showing";
                    Log.d(TAG, show_pwd);
                    Log.d(TAG, et_pwd.getInputType() + "");

                } else if (show_pwd.equals("showing")) {
                    show_password.setImageResource(R.drawable.noshow_pwd);
                    et_pwd.setInputType(129); // 비밀번호 보이기
                    show_pwd = "not showing";
                    Log.d(TAG, show_pwd);
                    Log.d(TAG, et_pwd.getInputType() + "");
                }
            }
        });
    }

    public void press_sign_up(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        customType(LoginActivity.this, "bottom-to-up"); // 화면 아래로 내려가는 애니메이션
        finish();
    }

    public void press_sign_in(View view) {

        // Initialization App Server
        initMyAPI();

        if (et_name.getText().toString().equals("")) {      // 이름 미입력 시
            toastMsg.toastMsg_short("이름을 입력해 주세요.", getApplicationContext());
            upkeyboard.keyboardUp(et_name, getApplicationContext());
        } else if (et_pwd.getText().toString().equals("")) {        // 비밀번호 미입력 시
            toastMsg.toastMsg_short("비밀번호를 입력해 주세요.", getApplicationContext());
            upkeyboard.keyboardUp(et_pwd, getApplicationContext());
        } else if (et_pwd.getText().toString().length() < 6) {
            toastMsg.toastMsg_short("비밀번호를 확인해 주세요.", getApplicationContext());
            upkeyboard.keyboardUp(et_pwd, getApplicationContext());
        } else {
            ProgressDialogClass progressDialogClass = new ProgressDialogClass(this);
            progressDialogClass.show();

            // 1.5초 딜레이
            Handler.createAsync(Looper.myLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    return false;
                }
            }).postDelayed(new Runnable() {
                @Override
                public void run() {

                    //아이디, 비밀번호 입력 후 서버에 사용자인지 아닌지 확인
                    Call<List<UserItem>> get_user = mMyAPI.get_login(et_name.getText().toString());
                    get_user.enqueue(new Callback<List<UserItem>>() {
                        @Override
                        public void onResponse(Call<List<UserItem>> call, @NotNull Response<List<UserItem>> response) {
                            Log.e(retrofit, response.body().toString());
                            if (Objects.requireNonNull(response.body()).toString().equals("[]")) {
                                toastMsg.toastMsg_short("해당 정보로 가입된 이력이 없습니다.", getApplicationContext());

                            } else if (!response.body().get(0).getPwd().equals(et_pwd.getText().toString())) {
                                toastMsg.toastMsg_short("비밀번호가 일치하지 않습니다.", getApplicationContext());
                            } else {
                                Log.d(retrofit, response.body().toString());
                                Log.d(retrofit, response.message() + "");
                                Log.d(retrofit, "이름 : " + response.body().get(0).getName());
                                Log.d(retrofit, "비밀번호 : " + response.body().get(0).getPwd());
                                Log.d(retrofit, "프로필 : " + response.body().get(0).getProfile());

                                // 어드민 페이지로 접속
                                if (et_name.getText().toString().equals("admin")) {
                                    Log.d(retrofit, "connecting to admin page");
                                    Intent intent = new Intent(LoginActivity.this, AdminPageActivity.class);
                                    startActivity(intent);
                                }
                                // 튜토리얼 페이지로 접속
                                else if (response.body().get(0).getProfile() == null) {
                                    Log.d(retrofit, "this user's profile is null");
                                    Log.d(retrofit, "connecting to tutorial");
                                    Intent intent = new Intent(LoginActivity.this, Tutorial_Activity.class);
                                    intent.putExtra("name", et_name.getText().toString() + "");
                                    intent.putExtra("login_time", System.currentTimeMillis() + "");
                                    customType(LoginActivity.this, "fadein-to-fadeout");
                                    startActivity(intent);
                                    SharedPreferenceManager.setString(LoginActivity.this,user_name,et_name.getText().toString()); // 유저 이름 저장
                                    SharedPreferenceManager.setString(LoginActivity.this,user_profile,response.body().get(0).getProfile()); // 유저 프로필 저장
                                    SharedPreferenceManager.setString(LoginActivity.this,user_pwd,et_pwd.getText().toString()); // 유저 비밀번호 저장
                                }
                                // 기존 유저 페이지로 접속
                                else {
                                    Log.d(retrofit, "connecting to user page");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", et_name.getText().toString() + "");
                                    intent.putExtra("login_time", System.currentTimeMillis() + "");
                                    intent.putExtra("profile", response.body().get(0).getProfile());
                                    customType(LoginActivity.this, "fadein-to-fadeout");
                                    startActivity(intent);
                                    SharedPreferenceManager.setString(LoginActivity.this,user_name,et_name.getText().toString()); // 유저 이름 저장
                                    SharedPreferenceManager.setString(LoginActivity.this,user_profile,response.body().get(0).getProfile()); // 유저 프로필 저장
                                    SharedPreferenceManager.setString(LoginActivity.this,user_pwd,et_pwd.getText().toString()); // 유저 비밀번호 저장
                                }
                                finish();
                            }
                            if (progressDialogClass.isShowing()) {
                                progressDialogClass.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserItem>> call, Throwable t) {
                            if (progressDialogClass.isShowing()) {
                                progressDialogClass.dismiss();
                            }
                            toastMsg.toastMsg_short("로그인에 실패했습니다.", getApplicationContext());
                            Log.e(retrofit, "Fail msg : " + t.getMessage());
                        }
                    });
                }
            }, 1500);
        }
    }

    public void press_missing_data(View view) {
        toastMsg.toastMsg_short("사용자 정보 찾기", getApplicationContext());
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