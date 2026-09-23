package com.techportfolio.app;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {

    private VideoView videoView;

    public VideoFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_video,
                container,
                false
        );

        videoView = view.findViewById(R.id.videoView);

        configurarVideo();

        return view;
    }

    private void configurarVideo() {

        Uri videoUri = Uri.parse(
                "android.resource://" +
                        requireContext().getPackageName() +
                        "/" +
                        R.raw.video_presentacion
        );

        videoView.setVideoURI(videoUri);

        MediaController mediaController =
                new MediaController(requireContext());

        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
    }
}