package com.example.mediaplayer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayer.model.MyVideo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements VideoAdapter.VideoIsClicked {
    @Override
    public void onVideoClicked(MyVideo video,int position) {
        videoAdapterIsClicked.videoIsClicked(video,position);
    }

    public interface VideoAdapterIsClicked{
        void videoIsClicked(MyVideo video,int position);
    }

    private static  ArrayList<MyVideo> videos;
    private  VideoAdapterIsClicked videoAdapterIsClicked;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;



    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(ArrayList<MyVideo> myVideos) {
        videos=myVideos;
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof VideoAdapterIsClicked){
            videoAdapterIsClicked= (VideoAdapterIsClicked) context;
            adapter=new VideoAdapter(this,videos,context);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}