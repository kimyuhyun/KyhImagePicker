package com.honglab.kyh_image_picker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.honglab.kyh_image_picker.R;
import com.honglab.kyh_image_picker.model.DataVO;


import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DataVO> list;
    private int width;
    private AdapterClickListener adapterClickListener;

    public GalleryAdapter(Context context, AdapterClickListener adapterClickListener, ArrayList<DataVO> list, int width) {
        this.context = context;
        this.adapterClickListener = adapterClickListener;
        this.list = list;
        this.width = width;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        DataVO item = list.get(position);

        try {
            Glide.with(context)
                    .load(item.getPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(itemViewHolder.iv_thumb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (item.isChoose()) {
            itemViewHolder.fl_selected.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.fl_selected.setVisibility(View.GONE);
        }

        if (item.isToogle()) {
            itemViewHolder.tv_count.setBackgroundResource(R.drawable.circle_accent);
            itemViewHolder.tv_count.setText("" + (item.getSeq() + 1));
        } else {
            itemViewHolder.tv_count.setBackgroundResource(R.drawable.circle_accent_border);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout root;
        ImageView iv_thumb;
        FrameLayout fl_selected;
        TextView tv_count;

        public ItemViewHolder(View view) {
            super(view);

            root = (RelativeLayout) view.findViewById(R.id.root);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            fl_selected = (FrameLayout) view.findViewById(R.id.fl_selected);
            tv_count = (TextView) view.findViewById(R.id.tv_count);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
            root.setLayoutParams(params);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    adapterClickListener.onClick(pos);
                }
            });
        }
    }

    public interface AdapterClickListener {
        void onClick(int pos);
    }
}