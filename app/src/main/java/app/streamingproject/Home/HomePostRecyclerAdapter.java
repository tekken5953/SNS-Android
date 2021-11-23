package app.streamingproject.Home;

import static java.sql.Types.NULL;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.streamingproject.R;
import app.streamingproject.Retrfofit.MyAPI;
import app.streamingproject.Retrfofit.NullOnEmptyConverterFactory;
import app.streamingproject.Retrfofit.PostItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePostRecyclerAdapter extends RecyclerView.Adapter<HomePostRecyclerAdapter.ViewHolder> {
    private ArrayList<HomePostRecyclerItem> mData;
    int i = 0;
    int heart_count_int = 0;
    int last_position = 0;
    boolean isLoading = false;
    String img_path;
    ImageView imageView;

    MyAPI mMyAPI;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    HomePostRecyclerAdapter(ArrayList<HomePostRecyclerItem> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public HomePostRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.home_post_recycler_item, parent, false);
        HomePostRecyclerAdapter.ViewHolder vh = new HomePostRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(HomePostRecyclerAdapter.ViewHolder holder, int position) {

        HomePostRecyclerItem item = mData.get(position);
        holder.name.setText(item.getName());
        holder.date.setText(item.getDate());
        holder.post_content.setText(item.getPost_content());
        holder.heart_count.setText(item.getHeart_count() + "");
        holder.comment_count.setText(item.getComment_count() + "");

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView heart;
        TextView name, date, post_content, heart_count, comment_count, more;

        ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position);
                        }
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView = itemView.findViewById(R.id.home_post_photo_iv);
            name = itemView.findViewById(R.id.home_post_item_name_tv);
            date = itemView.findViewById(R.id.home_post_item_date_tv);
            post_content = itemView.findViewById(R.id.home_post_item_content_tv);
            heart_count = itemView.findViewById(R.id.home_post_item_heart_count_tv);
            comment_count = itemView.findViewById(R.id.home_post_item_comment_count_tv);
            more = itemView.findViewById(R.id.home_post_item_more_tv);
            heart = itemView.findViewById(R.id.home_post_item_heart_iv);

            if (post_content.getLineCount() > 4) {
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (post_content.getMaxLines() == 4) {
                        post_content.setMaxLines(NULL);
                        more.setText("줄이기");
                    } else {
                        post_content.setMaxLines(4);
                        more.setText("더보기");
                    }
                }
            });

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = 1 - i;
                    if (i == 1) {
                        heart.setImageDrawable(ResourcesCompat.getDrawable(heart.getResources(), R.drawable.heart_fill, null));
                        heart_count_int = Integer.parseInt(heart_count.getText().toString()) + 1;
                    } else {
                        heart.setImageDrawable(ResourcesCompat.getDrawable(heart.getResources(), R.drawable.heart_empty, null));
                        heart_count_int = Integer.parseInt(heart_count.getText().toString()) - 1;
                    }
                    heart_count.setText(heart_count_int);
                    //TODO 서버에 바뀐 카운트 리퀘스트
                }
            });


        }
    }

    // 처음 아이템 로딩
    public void initPost(Context context) {
        initMyAPI(); // 서버에 연결
        mData.clear(); // 컨텐츠 중복 방지
        Call<List<PostItem>> get_post = mMyAPI.get_post();
        get_post.enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(@NotNull Call<List<PostItem>> call, @NotNull Response<List<PostItem>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().toString().equals("[]")) {
                        Log.e("post_item", "init post item count = " + response.body().size());
                        if (response.body().size() == 0) {
                            mData.clear();
                        } else if (response.body().size() <= 4) {
                            for (int i = response.body().size() - 1; i >= 0; i--) {
                                img_path = response.body().get(i).getPhoto();
                                Log.e("post_item", "current item = " + i+"");
                                Log.e("post_item", "path(initPost) = " + img_path);
                                downloadImg(context,img_path);

                                addItem(response.body().get(i).getName(),
                                        response.body().get(i).getDate()
                                                .substring(5, 7) + "월 " +
                                                response.body().get(i).getDate().substring(8, 10) + "일 " +
                                                response.body().get(i).getDate().substring(12, 16),
                                        response.body().get(i).getContent(),
                                        response.body().get(i).getHeart(),
                                        response.body().get(i).getComment());

                                last_position = 0;
                                notifyItemChanged(i);
                            }

                        } else {
                            for (int i = response.body().size() - 1; i >= response.body().size() - 5; i--) {
                                img_path = response.body().get(i).getPhoto();
                                downloadImg(context,img_path);
                                Log.e("post_item", "path(initPost) = " + img_path);
                                Log.e("post_item", "current item = " + i+"");

                                addItem(response.body().get(i).getName(),
                                        response.body().get(i).getDate()
                                                .substring(5, 7) + "월 " +
                                                response.body().get(i).getDate().substring(8, 10) + "일 " +
                                                response.body().get(i).getDate().substring(12, 16),
                                        response.body().get(i).getContent(),
                                        response.body().get(i).getHeart(),
                                        response.body().get(i).getComment());
                                last_position = response.body().size() - 5;
                                notifyItemChanged(i);
                            }

                        }

                    } else {
                        Log.e("retrofit", response.message() + "\n" + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.e("retrofit", t.getMessage() + "");
            }
        });
    }

    // 피드 리사이클러뷰 아이템 추가 세팅
    public void addItem(String name, String date, String post_content, Integer heart_count, Integer comment_count) {
        HomePostRecyclerItem item = new HomePostRecyclerItem(name, date, post_content, heart_count, comment_count);

        item.setName(name);
        item.setDate(date);
        item.setPost_content(post_content);
        item.setHeart_count(heart_count);
        item.setComment_count(comment_count);

        mData.add(item);
    }

    public void downloadImg(Context context, String img) {
        FirebaseStorage storage = FirebaseStorage.getInstance(); // 인스턴스 생성
        StorageReference storageRef = storage.getReference(); // 스토리지 참조
        storageRef.child("post/post_" + img + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(uri).placeholder(R.drawable.loading).override(380, 380).error(R.drawable.cancel_iv).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                //실패시
                imageView.setVisibility(View.GONE);
            }
        });
    }

    // 스크롤 맨 밑으로 내려가면 추가 아이템 로딩
    public void load_more(SpinKitView spinKitView, Context context) {

        spinKitView.setVisibility(View.VISIBLE);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initMyAPI(); // 서버에 연결
                Call<List<PostItem>> get_post = mMyAPI.get_post();
                get_post.enqueue(new Callback<List<PostItem>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<PostItem>> call, @NotNull Response<List<PostItem>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (last_position - 2 >= 0) {
                                for (int i = last_position; i > last_position - 2; i--) {
                                    img_path = response.body().get(i).getPhoto();
                                    Log.e("post_item", "current item = " + i+"");
                                    Log.e("post_item", "path(initPost) = " + img_path);
                                    downloadImg(context,img_path);
                                    addItem(response.body().get(i).getName(),
                                            response.body().get(i).getDate()
                                                    .substring(5, 7) + "월 " +
                                                    response.body().get(i).getDate().substring(8, 10) + "일 " +
                                                    response.body().get(i).getDate().substring(12, 16),
                                            response.body().get(i).getContent(),
                                            response.body().get(i).getHeart(),
                                            response.body().get(i).getComment());
                                    notifyItemChanged(i);
                                }
                                last_position -= 2;
                            } else if (last_position == 1) {
                                img_path = response.body().get(0).getPhoto();
                                Log.e("post_item", "current item = " + i+"");
                                Log.e("post_item", "path(initPost) = " + img_path);
                                downloadImg(context,img_path);
                                addItem(response.body().get(0).getName(),
                                        response.body().get(0).getDate()
                                                .substring(5, 7) + "월 " +
                                                response.body().get(0).getDate().substring(8, 10) + "일 " +
                                                response.body().get(0).getDate().substring(12, 16),
                                        response.body().get(0).getContent(),
                                        response.body().get(0).getHeart(),
                                        response.body().get(0).getComment());
                                last_position = 0;
                                notifyItemChanged(0);

                            } else if (last_position == 0) {
                                Toast.makeText(context, "마지막 피드입니다.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "알수없는 오류 발생2", Toast.LENGTH_SHORT).show();
                            Log.e("retrofit", response.message() + "\n" + response.code());
                        }

                        isLoading = false;
                        spinKitView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<List<PostItem>> call, Throwable t) {
                        Log.e("retrofit", t.getMessage() + "");
                        spinKitView.setVisibility(View.GONE);
                    }
                });
            }
        }, 1500);
    }

    public void getHeartCount(String id) {
        initMyAPI();
        Call<PostItem> get_heart_by_id = mMyAPI.get_post_by_id(id);
        get_heart_by_id.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                Log.d("retrofit", "heart : " + response.body().getHeart());
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.e("retrofit", "load heart error : " + t.getMessage());
            }
        });
    }

    public void updateHeartCount(String id, Integer heart) {
        initMyAPI();
        PostItem item = new PostItem();
        item.setHeart(heart);
        Call<PostItem> update_heart_by_id = mMyAPI.update_post_by_id(id, item);
        update_heart_by_id.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                Log.d("retrofit", "heart : " + response.body().getHeart());
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.e("retrofit", "load heart error : " + t.getMessage());
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

