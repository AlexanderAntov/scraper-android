package antov.scraper;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import antov.scraper.Models.NewsDataObject;

public class RecyclerViewAdapter extends RecyclerView
        .Adapter<RecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<NewsDataObject> mDataset;
    private static ClickListener clickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView newsImage;
        TextView headline;
        TextView shortInfo;
        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);

            newsImage = (ImageView) itemView.findViewById(R.id.news_image);
            headline = (TextView) itemView.findViewById(R.id.headline);
            shortInfo = (TextView) itemView.findViewById(R.id.shortInfo);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecyclerViewAdapter(ArrayList<NewsDataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Bitmap image = mDataset.get(position).getmImage();
        if (image != null) {
            holder.newsImage.setVisibility(View.VISIBLE);
            holder.newsImage.setImageBitmap(image);
        } else {
            holder.newsImage.setVisibility(View.GONE);
        }
        holder.headline.setText(mDataset.get(position).getmHeadline());
        holder.shortInfo.setText(mDataset.get(position).getmShortInfo());
        holder.dateTime.setText(mDataset.get(position).getmDateTime());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public NewsDataObject getDataObject(int index) {
        return mDataset.get(index);
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);
    }
}