package com.example.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayer.model.MyVideo;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    public interface VideoIsClicked{
       void onVideoClicked (MyVideo video,int index);
    }
    private VideoIsClicked videoIsClicked;
    private ArrayList<MyVideo> videos;
    private Context context;

    public VideoAdapter(VideoIsClicked videoIsClicked, ArrayList<MyVideo> videos, Context context) {
        this.videoIsClicked = videoIsClicked;
        this.videos = videos;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom,parent,false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.getTxtName().setText(videos.get(position).getName());
        holder.getTxtDuration().setText(String.format(context.getString(R.string.duration),
                formatDate(videos.get(position).getDuration())[0],
                formatDate(videos.get(position).getDuration())[1]));
        holder.getConstraintLayout().setOnClickListener((view)->{
            videoIsClicked.onVideoClicked(videos.get(position),position);
        });


    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    private int[] formatDate(int duration){
        int durationInSecs=duration/1000;
        int minutes=durationInSecs/60;
        int secs=duration%60;

        int[] dates= new int[2];
        dates[0]=minutes;
        dates[1]=secs;

        return dates;

    }
}
