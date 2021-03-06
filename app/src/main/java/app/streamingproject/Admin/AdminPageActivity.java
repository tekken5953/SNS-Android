package app.streamingproject.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.streamingproject.Login.LoginActivity;
import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.PostItem;
import app.streamingproject.Retrfofit.UserItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminPageActivity extends AppCompatActivity {

    StringBuffer user_search_content_sb = new StringBuffer();
    StringBuffer user_delete_content_sb = new StringBuffer();
    StringBuffer post_search_content_sb = new StringBuffer();

    MyAPI mMyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page_acitvity);

        initMyAPI();

        com.rey.material.widget.Spinner spinner_subject = findViewById(R.id.admin_spinner_subject);
        com.rey.material.widget.Spinner spinner_do = findViewById(R.id.admin_spinner_do);
        Button ok = findViewById(R.id.admin_spinner_btn);
        TextView content = findViewById(R.id.admin_content_tv);
        TableRow tr = findViewById(R.id.admin_tr);
        EditText delete_et = findViewById(R.id.admin_delete_et);
        Button delete_search = findViewById(R.id.admin_delete_search_btn);
        Button delete_ok = findViewById(R.id.admin_delete_delete_btn);

        final ArrayList<String> list_subject = new ArrayList<>();
        final ArrayList<String> list_do = new ArrayList<>();
        final ArrayAdapter<String> adapter_subject = new ArrayAdapter<>(AdminPageActivity.this, android.R.layout.simple_list_item_1, list_subject);
        final ArrayAdapter<String> adapter_do = new ArrayAdapter<>(AdminPageActivity.this, android.R.layout.simple_list_item_1, list_do);

        // ????????? ????????? ??????
        list_subject.add("??????");
        list_subject.add("?????????");
        list_subject.add("??????");
        spinner_subject.setAdapter(adapter_subject);
        spinner_subject.setGravity(Gravity.CENTER);
        spinner_subject.setSelected(true);
        adapter_subject.setNotifyOnChange(true);


        spinner_do.setGravity(Gravity.CENTER);
        adapter_do.setNotifyOnChange(true);

        spinner_subject.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spinner_do.removeAllViews();
                        list_do.clear();
                        list_do.add("?????? ??????");
                        list_do.add("?????? ??????");
                        list_do.add("?????? ??????");
                        spinner_do.setAdapter(adapter_do);
                        break;
                    case 1:
                        spinner_do.removeAllViews();
                        list_do.clear();
                        list_do.add("????????? ??????");
                        list_do.add("????????? ????????? ??????");
                        list_do.add("????????? ???????????? ??????");
                        list_do.add("????????? ?????? ??????");
                        spinner_do.setAdapter(adapter_do);
                        break;
                    case 2:
                        spinner_do.removeAllViews();
                        list_do.clear();
                        list_do.add("?????? ?????? ??????");
                        spinner_do.setAdapter(adapter_do);
                        break;
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr.setVisibility(View.GONE);
                delete_ok.setVisibility(View.GONE);

                if (spinner_subject.getSelectedItem().equals("??????")) {

                    if (spinner_do.getSelectedItem().equals("?????? ??????")) {

                        // ?????? ??????
                        Call<List<UserItem>> get_all_user = mMyAPI.get_user();
                        get_all_user.enqueue(new Callback<List<UserItem>>() {
                            @Override
                            public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {
                                int  num = 1;

                                assert response.body() != null;
                                for (UserItem item : response.body()) {
                                    user_search_content_sb.append(num)
                                            .append("\n?????? : ").append(item.getName())
                                            .append("\n????????? : ").append(item.getId())
                                            .append("\n???????????? : ").append(item.getPhone())
                                            .append("\n\n");
                                    num++;
                                }
                                content.setText(response.headers().toString() + "\n" + user_search_content_sb);
                            }

                            @Override
                            public void onFailure(Call<List<UserItem>> call, Throwable t) {
                                Log.e("retrofit", "Error : " + t.getMessage());
                            }
                        });

                    } else if (spinner_do.getSelectedItem().equals("?????? ??????")) {
                        //TODO ????????????
                    }  else if (spinner_do.getSelectedItem().equals("?????? ??????(?????????)")) {
                        // ????????????
                        tr.setVisibility(View.VISIBLE);
                        delete_ok.setVisibility(View.GONE);
                        delete_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!delete_et.getText().toString().equals("")) {
                                    Call<List<UserItem>> get_user_by_name = mMyAPI.get_user_by_user_name(delete_et.getText().toString());
                                    get_user_by_name.enqueue(new Callback<List<UserItem>>() {
                                        @Override
                                        public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {
                                            delete_ok.setVisibility(View.VISIBLE);
                                            if (response.code() == 200) {

                                                assert response.body() != null;
                                                user_delete_content_sb
                                                        .append("\n?????? : ").append(response.body().stream().findFirst().get().getName())
                                                        .append("\n????????? : ").append(response.body().stream().findFirst().get().getId())
                                                        .append("\n???????????? : ").append(response.body().stream().findFirst().get().getPhone())
                                                        .append("\n\n");

                                                content.setText(user_delete_content_sb);

                                                delete_ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminPageActivity.this);
                                                        final AlertDialog alertDialog = builder.create();
                                                        alertDialog.setMessage("?????? ????????? ?????????????????????????");
                                                        alertDialog.setTitle("?????? ????????????");
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.setCanceledOnTouchOutside(false);
                                                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "??????", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Call<UserItem> delete_user_by_id = mMyAPI.delete_user_by_id(delete_et.getText().toString());
                                                                delete_user_by_id.enqueue(new Callback<UserItem>() {
                                                                    @Override
                                                                    public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                                                                        Toast.makeText(AdminPageActivity.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                                                        Log.d("retrofit", "?????? ?????? ??????");
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<UserItem> call, Throwable t) {
                                                                        Log.e("retrofit", "?????? ?????? ??????");
                                                                    }
                                                                });
                                                            }
                                                        });
                                                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "??????", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                alertDialog.dismiss();
                                                            }
                                                        });
                                                        alertDialog.show();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(AdminPageActivity.this, "?????? ?????? : " + response.code(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<UserItem>> call, Throwable t) {
                                            Toast.makeText(AdminPageActivity.this, "?????? ??????\n" + t.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } else {
                                    Toast.makeText(AdminPageActivity.this, "????????? ?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else if (spinner_subject.getSelectedItem().equals("?????????")) {

                    if (spinner_do.getSelectedItem().equals("????????? ??????")) {
                        Call<List<PostItem>> get_all_post = mMyAPI.get_post();
                        get_all_post.enqueue(new Callback<List<PostItem>>() {
                            @Override
                            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                                int num = 1;
                                for (PostItem item : response.body()) {
                                    if (!response.body().toString().equals("[]")) {
                                        post_search_content_sb.append(num)
                                                .append("\n?????? : ").append(item.getName())
                                                .append("\n?????? : ").append(item.getDate())
                                                .append("\n?????? : ").append(item.getContent())
                                                .append("\n??????(URI) : ").append(item.getPhoto())
                                                .append("\n?????? ??? : ").append(item.getHeart())
                                                .append("\n?????? ??? : ").append(item.getComment())
                                                .append("\n\n");
                                        num++;
                                        content.setText(post_search_content_sb);
                                    } else {
                                        content.setText("null");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                                content.setText(t.getMessage() + "");
                            }
                        });
                    } else if (spinner_do.getSelectedItem().equals("????????? ????????? ??????")) {
                        //TODO ????????? ????????? ??????
                    } else if (spinner_do.getSelectedItem().equals("????????? ???????????? ??????")) {
                        //TODO ????????? ???????????? ??????
                    } else if (spinner_do.getSelectedItem().equals("????????? ?????? ??????")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminPageActivity.this);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("??????");
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("?????? ???????????? ???????????????.\n ????????? ?????????????????????????");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ?????? ????????? ?????????
                                Call<PostItem> delete_all_post = mMyAPI.delete_all_post();
                                delete_all_post.enqueue(new Callback<PostItem>() {
                                    @Override
                                    public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(AdminPageActivity.this, "??????????????? ?????? ???????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PostItem> call, Throwable t) {
                                        Log.e("retrofit", t.getMessage());
                                        Toast.makeText(AdminPageActivity.this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }
                } else if (spinner_subject.getSelectedItem().equals("??????")) {
                    if (spinner_do.getSelectedItem().equals("?????? ?????? ??????")) {
                        content.setText(Objects.requireNonNull(mMyAPI.get_user().request().body()).toString());
                    }
                }
            }
        });
    }

    public void admin_press_cancel(View view) {
        Intent intent = new Intent(AdminPageActivity.this, LoginActivity.class);
        startActivity(intent);
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