package app.streamingproject.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.streamingproject.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private ArrayList<ChatListItem> mData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChatListAdapter(ArrayList<ChatListItem> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.chat_list_item, parent, false);
        ChatListAdapter.ViewHolder vh = new ChatListAdapter.ViewHolder(view);

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
    public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {

        ChatListItem item = mData.get(position);

        holder.profile.setImageDrawable(item.getProfile());
        holder.user_name.setText(item.getUser_name());
        holder.chat_content.setText(item.getChat_content());
        holder.time_stamp.setText(item.getTime_stamp());

    }

//    // 데이터 필터 검색 Filterable implement ---------------------------------
//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }
//
//    private Filter exampleFilter = new Filter() {
//        //Automatic on background thread
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<ChatListItem> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(mData);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (ChatListItem item : mData) {
//                    // filter 대상 setting
//                    if (item.getUser_name().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        //Automatic on UI thread
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mData.clear();
//            mData.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView user_name, chat_content, time_stamp;

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
            profile = itemView.findViewById(R.id.msg_item_iv);
            user_name = itemView.findViewById(R.id.msg_item_name);
            chat_content = itemView.findViewById(R.id.msg_item_content);
            time_stamp = itemView.findViewById(R.id.msg_item_time);

        }
    }
}

