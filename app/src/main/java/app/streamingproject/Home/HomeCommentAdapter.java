package app.streamingproject.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.streamingproject.R;

public class HomeCommentAdapter extends RecyclerView.Adapter<HomeCommentAdapter.ViewHolder> {
    private ArrayList<HomeCommentItem> mData;

    HomeCommentAdapter(ArrayList<HomeCommentItem> list) {
        mData = list;
    }

    @NonNull
    @Override
    public HomeCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.home_comment_item, parent, false);
        HomeCommentAdapter.ViewHolder vh = new HomeCommentAdapter.ViewHolder(view);
        return vh;
    }

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCommentAdapter.ViewHolder holder, int position) {
        HomeCommentItem item = mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final @NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
