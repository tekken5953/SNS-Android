package app.streamingproject.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import app.streamingproject.OuterClass.ChattingViewTypeClass;
import app.streamingproject.R;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChattingItem> mData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChattingAdapter(ArrayList<ChattingItem> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == ChattingViewTypeClass.ViewType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.chat_receiver_item, parent, false);
            return new LeftViewHolder(view);
        } else if (viewType == ChattingViewTypeClass.ViewType.RIGHT_CONTENT) {
            view = inflater.inflate(R.layout.chat_send_item, parent, false);
            return new RightViewHolder(view);
        } else {
            return null;
        }
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
    public void onBindViewHolder(RecyclerView.@NotNull ViewHolder holder, int position) {

        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).content_left.setText(mData.get(position).getContent());
            ((LeftViewHolder) holder).time_stamp_left.setText(mData.get(position).getTime_stamp());
        } else if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).content_right.setText(mData.get(position).getContent());
            ((RightViewHolder) holder).time_stamp_right.setText(mData.get(position).getTime_stamp());
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }

    public static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView content_left;
        TextView time_stamp_left;

        LeftViewHolder(View itemView) {
            super(itemView);

            content_left = itemView.findViewById(R.id.chat_receiver_text);
            time_stamp_left = itemView.findViewById(R.id.chat_receiver_time);
        }
    }

    public static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView content_right;
        TextView time_stamp_right;

        RightViewHolder(View itemView) {
            super(itemView);

            content_right = itemView.findViewById(R.id.chat_send_text);
            time_stamp_right = itemView.findViewById(R.id.chat_send_time);

        }
    }
}
