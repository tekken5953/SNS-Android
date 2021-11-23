package app.streamingproject.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.streamingproject.R;

public class ChatNewAdapter extends RecyclerView.Adapter<ChatNewAdapter.ViewHolder> {
    private ArrayList<ChatNewItem> mData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChatNewAdapter(ArrayList<ChatNewItem> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ChatNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.chat_new_recycler_item, parent, false);
        ChatNewAdapter.ViewHolder vh = new ChatNewAdapter.ViewHolder(view);

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
    public void onBindViewHolder(ChatNewAdapter.ViewHolder holder, int position) {

        ChatNewItem item = mData.get(position);

        holder.profile.setImageDrawable(item.getProfile());
        holder.name.setText(item.getName());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        CheckBox check;

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
            profile = itemView.findViewById(R.id.chat_new_recycler_item_iv);
            name = itemView.findViewById(R.id.chat_new_recycler_item_name_tv);
            check = itemView.findViewById(R.id.chat_new_recycler_item_CheckBox);

        }
    }
}

