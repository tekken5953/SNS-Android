package app.streamingproject;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import app.streamingproject.Home.HomePostFragment;
import app.streamingproject.OuterClass.ProgressDialogClass;
import app.streamingproject.OuterClass.ToastMsg;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.PostItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostNewFragment extends Fragment {

    private static final String retrofit = "retrofit";

    MyAPI mMyAPI;
    ToastMsg toastMsg = new ToastMsg();

    Uri parse_uri = null;

    String post_id, filename;

    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = new MainActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_new_fragment, container, false);

        TextView add_photo = view.findViewById(R.id.post_new_add_picture);
        com.rey.material.widget.Button upload = view.findViewById(R.id.post_new_upload_btn);
        TextView view_name = view.findViewById(R.id.post_new_name_tv);
        TextView view_date = view.findViewById(R.id.post_new_date_tv);
        TextView view_content = view.findViewById(R.id.post_new_content_tv);
        ImageView write_content = view.findViewById(R.id.post_new_add_content);
        ImageView view_photo = view.findViewById(R.id.post_new_picture);
        CheckBox checkBox = view.findViewById(R.id.post_new_photo_cb);
        FrameLayout frameLayout = view.findViewById(R.id.post_new_frame);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    frameLayout.setVisibility(View.VISIBLE);
                } else {
                    frameLayout.setVisibility(View.GONE);
                    view_photo.setImageDrawable(null);
                }
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd_hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        view_name.setText(getActivity().getIntent().getExtras().getString("name")); // 사용자 이름 불러오기
        view_date.setText(simpleDateFormat.format(calendar.getTime())); // 업로드 날짜 불러오기

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            Bitmap bitmap = null;
                            try {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), uri));
                                Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 380, 380, false);
                                view_photo.setVisibility(View.VISIBLE);
                                Glide.with(view.getContext()).load(resizeBitmap)
                                        .error(ResourcesCompat.getDrawable(getResources(), R.drawable.noprofile, null))
                                        .override(380, 380)
                                        .into(view_photo);

                                parse_uri = uri;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // 이미지 추가하기 버튼 클릭 시 갤러리에서 사진 불러오기
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        //내용 작성 버튼 클릭 시 다이얼로그 생성
        write_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.post_new_content_dialog, null, false);
                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                final EditText et_content = view1.findViewById(R.id.post_new_content_dialog_et);
                final com.rey.material.widget.Button btn_create = view1.findViewById(R.id.post_new_content_dialog_ok);
                final com.rey.material.widget.Button btn_cancel = view1.findViewById(R.id.post_new_content_dialog_cancel);
                alertDialog.setCanceledOnTouchOutside(true);

                et_content.setText(view_content.getText().toString()); // 현재 내용 가져와서 수정

                // 내용 작성 다이얼로그 기능 구현
                btn_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        view_content.setText(et_content.getText().toString());
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        // 업로드 버튼 클릭 시 서버에 리퀘스트
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parse_uri != null) {
                    //파이어베이스 스토리지에 이미지 업로드
                    FirebaseStorage storage = FirebaseStorage.getInstance(); //Firebase Storage Settings
                    StorageReference storageRef = storage.getReference();

                    post_id = view_date.getText().toString() + "_" + view_name.getText().toString();
                    filename = "post_" + post_id + ".jpg";
                    StorageReference riverRef = storageRef.child("post/" + filename);

                    UploadTask uploadTask = riverRef.putFile(parse_uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d(retrofit, "이미지 업로드 실패" + e.getMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(retrofit, "이미지 업로드 성공");
                        }
                    });
                } else {
                    post_id = "null";
                }

                initMyAPI(); // 서버에 연결

                ProgressDialogClass progressDialogClass =
                        new ProgressDialogClass(getActivity());

                PostItem item = new PostItem();

                //서버 전송
                item.setName(view_name.getText().toString());
                item.setDate(view_date.getText().toString());
                item.setContent(view_content.getText().toString());
                item.setPhoto(post_id);
                item.setHeart(0);
                item.setComment(0);

                Log.d(retrofit, view_content.getText().toString());
                Log.d(retrofit, "parse_uri : " + parse_uri);

                progressDialogClass.show();

                Call<PostItem> post_post = mMyAPI.post_post(item);
                post_post.enqueue(new Callback<PostItem>() {
                    @Override
                    public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                        if (response.code() == 200) {
                            progressDialogClass.dismiss();
                            Log.d(retrofit, response.code() + "");
                            toastMsg.toastMsg_long("게시물을 등록하였습니다!", getContext());

                        }
                    }

                    @Override
                    public void onFailure(Call<PostItem> call, Throwable t) {
                        progressDialogClass.dismiss();
                        toastMsg.toastMsg_long("게시물 등록에 실패했습니다.", getContext());
                        Log.e(retrofit, t.getMessage());
                    }
                });
            }
        });

        return view;
    }

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static PostNewFragment newInstance() {
        return new PostNewFragment();
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