package com.example.networkingcaching.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.networkingcaching.R;
import com.example.networkingcaching.model.ApiResponse;
import com.example.networkingcaching.model.access.BookPremium;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 05-02-2020
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.CustomViewHolder> {

    private List<BookPremium> dataList;
    private Context context;

    public BookAdapter(Context context, List<BookPremium> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView id1Title;
        TextView id2Title;
        TextView title;
        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            id1Title = mView.findViewById(R.id.id);
            id2Title = mView.findViewById(R.id.book_id);
            title = mView.findViewById(R.id.title);
        }
    }

    @NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.book_main, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.id1Title.setText(String.valueOf(dataList.get(position).getId()));
        holder.id2Title.setText(String.valueOf(dataList.get(position).getBookId()));
        holder.title.setText(dataList.get(position).getTitle());
/*
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);
*/

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
