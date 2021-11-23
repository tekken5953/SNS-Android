package app.streamingproject.UserPage;

import static maes.tech.intentanim.CustomIntent.customType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import app.streamingproject.Login.LoginActivity;
import app.streamingproject.OuterClass.SharedPreferenceManager;
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

public class UserFragment extends Fragment {

    TextView user_name_tv, logout_tv, ask_tv, setTheme_tv, alarm_tv, change_pwd_tv;
    ImageView profile_iv, edit_profile_iv;
    final String lifecycle = "lifecycle";

    MyAPI mMyAPI;

    FirebaseStorage storage = FirebaseStorage.getInstance(); // 인스턴스 생성
    StorageReference storageRef = storage.getReference(); // 스토리지 참조

    String user_name_sp, user_pwd_sp, user_profile_sp;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(lifecycle, "User Fragment is onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(lifecycle, "User Fragment is onDetach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup user_container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, user_container, false);
        Log.d(lifecycle, "User Fragment is onCreateView");

        user_name_tv = view.findViewById(R.id.user_name_tv);
        profile_iv = view.findViewById(R.id.user_profile);
        edit_profile_iv = view.findViewById(R.id.user_edit_profile);
        logout_tv = view.findViewById(R.id.user_logout_tv);
        ask_tv = view.findViewById(R.id.user_ask_tv);
        setTheme_tv = view.findViewById(R.id.user_theme_tv);
        alarm_tv = view.findViewById(R.id.user_alarm_tv);
        change_pwd_tv = view.findViewById(R.id.user_change_pwd_tv);

        user_name_tv.setText(user_name_sp);

        //유저 정보 불러오기
        user_name_sp = SharedPreferenceManager.getString(view.getContext(), "name");

        // 유저 비밀번호 불러오기
        user_pwd_sp = SharedPreferenceManager.getString(view.getContext(), "pwd");

        //유저 프로필 정보 불러오기
        user_profile_sp = SharedPreferenceManager.getString(view.getContext(), "profile");

        settingProfile(user_name_sp);

        //유저 프로필 수정하기
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            try {
                                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), uri));
                                Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                                Glide.with(view.getContext()).load(resizeBitmap)
                                        .error(ResourcesCompat.getDrawable(getResources(), R.drawable.noprofile, null))
                                        .override(100, 100)
                                        .into(profile_iv);

                                // 사진 파이어베이스 스토리지에 저장
                                String filename = "profile_" + user_name_sp + ".jpg";
                                StorageReference riverRef = storageRef.child("user/" + filename);

                                UploadTask uploadTask = riverRef.putFile(uri);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Log.e("retrofit", "이미지 업로드 실패" + e.getMessage());
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d("retrofit", "이미지 업로드 성공");
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
                                    }

                                    @Override
                                    public void onFailure(Call<UserItem> call, Throwable t) {
                                        Log.e("retrofit", "프로필 정보 저장 실패");
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // 비밀번호 변경하기 클릭 시 다이얼로그 생성
        change_pwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View view_pwd = LayoutInflater.from(getContext()).inflate(R.layout.change_pwd_diag, null, false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setView(view_pwd);

                final ImageView cancel = view_pwd.findViewById(R.id.change_pwd_cancel_iv);
                final Button submit_btn = view_pwd.findViewById(R.id.change_pwd_ok_btn);
                final TextInputLayout til1 = view_pwd.findViewById(R.id.change_pwd_textInputLayout1);
                final TextInputLayout til2 = view_pwd.findViewById(R.id.change_pwd_textInputLayout2);
                final TextInputLayout til3 = view_pwd.findViewById(R.id.change_pwd_textInputLayout3);
                final TextInputEditText tie1 = view_pwd.findViewById(R.id.change_pwd_textInputEditText1);
                final TextInputEditText tie2 = view_pwd.findViewById(R.id.change_pwd_textInputEditText2);
                final TextInputEditText tie3 = view_pwd.findViewById(R.id.change_pwd_textInputEditText3);

                tie1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() == 0) {
                            til1.setError("필수 입력사항입니다.");
                        } else if (s.length() < 6) {
                            til1.setError("6자 미만!");
                        } else if (s.length() > 12) {
                            til1.setError("12자 초과!");
                        } else if (s.length() < 12) {
                            til1.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                tie2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            til2.setError("필수 입력사항입니다.");
                        } else if (s.length() < 6) {
                            til2.setError("6자 미만!");
                        } else if (s.length() > 12) {
                            til2.setError("12자 초과!");
                        } else if (s.length() < 12) {
                            til2.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                tie3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            til3.setError("필수 입력사항입니다.");
                        } else if (s.length() < 6) {
                            til3.setError("6자 미만!");
                        } else if (s.length() > 12) {
                            til3.setError("12자 초과!");
                        } else if (s.length() < 12) {
                            til3.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                //서버에 바뀐 비밀번호 보내기
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpKeyboardClass upKeyboardClass = new UpKeyboardClass();
                        if (tie1.length() == 0 || tie2.length() == 0 || tie3.length() == 0 ||
                                tie1.length() < 6 || tie2.length() < 6 || tie3.length() < 6 ||
                                tie1.length() > 12 || tie2.length() > 12 || tie3.length() > 12) {
                            Toast.makeText(getContext(), "다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (!tie1.getText().toString().equals(user_pwd_sp)) {
                            Toast.makeText(getContext(), "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            upKeyboardClass.keyboardUp(tie1, view_pwd.getContext());
                        } else if (!tie2.getText().toString().equals(tie3.getText().toString())) {
                            Toast.makeText(getContext(), "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            upKeyboardClass.keyboardUp(tie3, view_pwd.getContext());
                        } else {
                            initMyAPI();
                            UserItem item = new UserItem();
                            item.setPwd(tie2.getText().toString());
                            item.setRepeat_pwd(tie3.getText().toString());
                            Call<UserItem> update_pwd_by_name = mMyAPI.update_pwd_by_name(user_name_sp, item);
                            update_pwd_by_name.enqueue(new Callback<UserItem>() {
                                @Override
                                public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                                    Toast.makeText(view_pwd.getContext(), "비밀번호가 정상적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("retrofit", "비밀번호 변경 성공");
                                }

                                @Override
                                public void onFailure(Call<UserItem> call, Throwable t) {
                                    Toast.makeText(view_pwd.getContext(), "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("retrofit", "비밀번호 변경 실패 : " + t.getMessage());
                                }
                            });
                            alertDialog.dismiss();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            customType(getActivity(), "fadein-to-fadeout");
                            startActivity(intent);
                            ToastMsg toastMsg = new ToastMsg();
                            toastMsg.toastMsg_short("다시 로그인 해주세요!", view_pwd.getContext());
                            requireActivity().finish();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        // 프로필 수정하기 버튼 클릭 시 갤러리에서 사진 불러오기
        edit_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        // 로그아웃 클릭
        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final AlertDialog alertDialog = builder.create();
                alertDialog.setMessage("정말 로그아웃 하시겠습니까?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", (dialog, which) -> {
                    alertDialog.dismiss();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    customType(getActivity(), "fadein-to-fadeout");
                    startActivity(intent);
                    requireActivity().finish();
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", ((dialog, which) -> {
                    alertDialog.dismiss();
                }) );
            }
        });

        //문의하기 클릭
        ask_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(getContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 테마 설정하기 클릭
        setTheme_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(getContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 알람 설정하기 클릭
        setTheme_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(getContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void settingProfile(String user_name) {
        storageRef.child("user/profile_" + user_name + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Log.d("retrofit", "uri(user_profiile) : " + uri);
                Glide.with(getActivity().getApplicationContext()).load(uri).placeholder(R.drawable.loading).into(profile_iv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                //실패시
                Glide.with(getActivity().getApplicationContext()).load(R.drawable.noprofile).placeholder(R.drawable.loading).into(profile_iv);
                Log.e("retrofit", e.getMessage() + "");
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