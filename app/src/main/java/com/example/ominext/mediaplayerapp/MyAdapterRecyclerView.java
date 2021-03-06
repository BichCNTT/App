package com.example.ominext.mediaplayerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ominext on 7/25/2017.
 */

public class MyAdapterRecyclerView extends RecyclerView.Adapter<MyAdapterRecyclerView.RecyclerViewHolder> {
    private List<MyData> myDataList = new ArrayList<>();
    private LayoutInflater inflater;
    Context context;


    public MyAdapterRecyclerView(Context context, List<MyData> myDataList) {
        this.myDataList = myDataList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvName.setText(myDataList.get(position).getName());
        holder.tvTime.setText(myDataList.get(position).getTime());
        holder.title=myDataList.get(position).getName();

        holder.dataSource=myDataList.get(position).getUrl();
        holder.tvUrl.setText(holder.dataSource);
        holder.i=position;
//        holder.tvPosition.setText(position);
    }

    @Override
    public int getItemCount() {
       return myDataList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvUrl;
        String dataSource;
        String title;
        int i;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.name);
            tvTime=(TextView)itemView.findViewById(R.id.time);
            tvUrl=(TextView)itemView.findViewById(R.id.url);

            final Context context=itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)context).initMedia(dataSource,i);
                    ((MainActivity)context).title.setText(title);
                    ((MainActivity)context).tvTotalTime.setText("00:00");
                    ((MainActivity)context).tvCurrentTime.setText("00:00");
                }
            });
        }
    }
}
