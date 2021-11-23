package app.streamingproject.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import app.streamingproject.OuterClass.ProgressDialogClass;
import app.streamingproject.OuterClass.ToastMsg;
import app.streamingproject.OuterClass.UpKeyboardClass;
import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.UserItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class SignUpActivity extends AppCompatActivity {

    private final String retrofit = "retrofit";

    EditText et_name, et_phone, et_pwd, et_repwd;
    ToastMsg toastMsg = new ToastMsg();
    Context context;
    UpKeyboardClass upKeyboard = new UpKeyboardClass();  // 키보드 올리기

    MyAPI mMyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        et_name = findViewById(R.id.signup_name_et);
        et_phone = findViewById(R.id.signup_phone_et);
        et_pwd = findViewById(R.id.signup_password_et);
        et_repwd = findViewById(R.id.signup_repeat_password_et);

        initMyAPI();

    }

    public void press_complete_sign_up(View view) {

        context = getApplicationContext();

        // 이미 존재하는 유저인지 검색
        Call<List<UserItem>> get_all_user = mMyAPI.get_user();
        get_all_user.enqueue(new Callback<List<UserItem>>() {
            @Override
            public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {
                for (UserItem item : response.body()) {
                    if (item.getName().equals(et_name.getText().toString())) {
                        toastMsg.toastMsg_short("이미 존재하는 유저입니다.", context);
                        et_name.setText("");
                        upKeyboard.keyboardUp(et_name, context);
                        break;
                    } else {    // 아닐경우 그 다음 필터 진행
                        if (et_name.getText().toString().equals("")) {
                            toastMsg.toastMsg_short("이름을 입력해 주세요!", context);
                            upKeyboard.keyboardUp(et_name, context);
                        } else if (et_phone.getText().toString().equals("")) {
                            toastMsg.toastMsg_short("핸드폰 번호를 입력해 주세요!", context);
                            upKeyboard.keyboardUp(et_phone, context);
                        } else if (et_pwd.getText().toString().equals("")) {
                            toastMsg.toastMsg_short("비밀번호를 입력해 주세요!", context);
                            upKeyboard.keyboardUp(et_pwd, context);
                        } else if (et_repwd.getText().toString().equals("")) {
                            toastMsg.toastMsg_short("비밀번호 확인을 입력해 주세요!", context);
                            upKeyboard.keyboardUp(et_repwd, context);
                        } else if (et_name.getText().toString().contains(" ")) {
                            toastMsg.toastMsg_short("이름은 공백없이 입력해 주세요!", context);
                            upKeyboard.keyboardUp(et_name, context);
                        } else if (!et_pwd.getText().toString().equals(et_repwd.getText().toString())) {
                            toastMsg.toastMsg_short("비밀번호가 일치하지 않습니다!", context);
                            upKeyboard.keyboardUp(et_pwd, context);
                        } else if (et_pwd.getText().toString().length() < 6) {
                            toastMsg.toastMsg_short("비밀번호는 6자리 이상이여야합니다!", context);
                            et_pwd.setText("");
                            et_repwd.setText("");
                            upKeyboard.keyboardUp(et_pwd, context);
                        } else if (!et_phone.getText().toString().equals("") && !et_phone.getText().toString().startsWith("010")) {
                            toastMsg.toastMsg_short("핸드폰 번호는 010으로 시작해야 합니다.", context);
                            upKeyboard.keyboardUp(et_phone, context);
                        } else if (et_name.getText().toString().length() != 3) {
                            toastMsg.toastMsg_short("이름은 띄어쓰기 없이 3글자를 입력해주세요!", context);
                            upKeyboard.keyboardUp(et_name, context);
                        } else {
                            ProgressDialogClass progressDialogClass = new ProgressDialogClass(SignUpActivity.this);
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
                                    UserItem item = new UserItem();
                                    item.setName(et_name.getText().toString());
                                    item.setPhone(et_phone.getText().toString());
                                    item.setPwd(et_pwd.getText().toString());
                                    item.setRepeat_pwd(et_repwd.getText().toString());

                                    Call<UserItem> post_user = mMyAPI.post_user(item);
                                    post_user.enqueue(new Callback<UserItem>() {
                                        @Override
                                        public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                                            if (response.isSuccessful()) {

                                                progressDialogClass.dismiss();

                                                toastMsg.toastMsg_short("가입이 완료되었습니다!\n해당 정보로 로그인해주세요", context);

                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                customType(SignUpActivity.this, "up-to-bottom"); // 화면 위로 올라가는 애니메이션
                                                finish();

                                            }

                                            Log.d(retrofit, "Status Code : " + response.code());

                                        }

                                        @Override
                                        public void onFailure(Call<UserItem> call, Throwable t) {
                                            progressDialogClass.dismiss();
                                            Log.e("retrofit", "Fail msg : " + t.getMessage());
                                        }
                                    });
                                }
                            }, 1500);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserItem>> call, Throwable t) {
                Log.e(retrofit, "Occurring Problem by server");
                Log.e(retrofit, "Message is : " + t.getMessage());
                Log.e(retrofit, "Cause is : " + t.getCause());
            }
        });
    }

    public void press_back(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        customType(SignUpActivity.this, "up-to-bottom"); // 화면 위로 올라가는 애니메이션
        finish();
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