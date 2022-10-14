package com.milonsheikh.rxjavaandretrofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.milonsheikh.rxjavaandretrofit.R;
import com.milonsheikh.rxjavaandretrofit.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> dataList;
    private Context context;

    public PostAdapter(Context context,List<Post> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.tvUserId.setText("Uer ID: "+dataList.get(position).getUserId());
        holder.tvPostId.setText("Post ID: "+dataList.get(position).getId());
        holder.tvTitle.setText(dataList.get(position).getTitle());
        holder.tvBody.setText(String.valueOf(dataList.get(position).getBody()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView tvUserId,tvPostId, tvTitle, tvBody;

        PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            tvUserId = mView.findViewById(R.id.tv_user_id);
            tvPostId = mView.findViewById(R.id.tv_post_id);
            tvTitle = mView.findViewById(R.id.tv_title);
            tvBody = mView.findViewById(R.id.tv_body);
        }
    }

}
