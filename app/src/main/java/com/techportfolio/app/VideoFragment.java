package com.techportfolio.app;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class VideoFragment extends Fragment {

    private VideoView videoView;
    private ImageButton btnVideoPlay;
    private ImageButton btnVideoMiniPlay;
    private ImageButton btnVideoFullscreen;
    private ProgressBar videoProgress;
    private SeekBar videoSeekBar;
    private TextView tvVideoTime;
    private TextView tvVideoError;
    private View videoCenterControls;
    private boolean videoReady;
    private int videoDuration;
    private boolean userSeeking;
    private boolean videoFullscreen;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (videoReady && videoView != null) {
                actualizarBarraProgreso();
                progressHandler.postDelayed(this, 500);
            }
        }
    };

    public VideoFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = view.findViewById(R.id.videoView);
        btnVideoPlay = view.findViewById(R.id.btnVideoPlay);
        btnVideoMiniPlay = view.findViewById(R.id.btnVideoMiniPlay);
        btnVideoFullscreen = view.findViewById(R.id.btnVideoFullscreen);
        videoProgress = view.findViewById(R.id.videoProgress);
        videoSeekBar = view.findViewById(R.id.videoSeekBar);
        tvVideoTime = view.findViewById(R.id.tvVideoTime);
        tvVideoError = view.findViewById(R.id.tvVideoError);
        videoCenterControls = view.findViewById(R.id.videoCenterControls);

        btnVideoPlay.setOnClickListener(v -> alternarReproduccion());
        btnVideoMiniPlay.setOnClickListener(v -> alternarReproduccion());
        btnVideoFullscreen.setOnClickListener(v -> alternarPantallaCompleta());
        btnVideoFullscreen.setImageResource(obtenerIconoPantallaCompleta());

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && videoReady && videoDuration > 0) {
                    videoView.seekTo(videoDuration * progress / seekBar.getMax());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userSeeking = false;
            }
        });

        configurarVideo();
        return view;
    }

    private void configurarVideo() {
        Uri videoUri = Uri.parse(
                "android.resource://" + requireContext().getPackageName() +
                        "/" + R.raw.video_presentacion
        );

        videoView.setOnPreparedListener(mediaPlayer -> {
            videoReady = true;
            videoDuration = videoView.getDuration();
            videoProgress.setVisibility(View.GONE);
            videoSeekBar.setEnabled(true);
            tvVideoTime.setText(formatearTiempo(videoDuration));
            actualizarBotonReproduccion();
            progressHandler.post(progressUpdater);
        });
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.seekTo(0);
            actualizarBotonReproduccion();
            actualizarBarraProgreso();
        });
        videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            mostrarErrorVideo();
            return true;
        });
        videoView.setVideoURI(videoUri);
    }

    private void alternarReproduccion() {
        if (!videoReady) {
            return;
        }
        if (videoView.isPlaying()) {
            videoView.pause();
        } else {
            videoView.start();
        }
        actualizarBotonReproduccion();
    }

    private void alternarPantallaCompleta() {
        videoFullscreen = !videoFullscreen;
        ((MainActivity) requireActivity()).setVideoFullscreen(videoFullscreen);
        btnVideoFullscreen.setImageResource(obtenerIconoPantallaCompleta());
        btnVideoFullscreen.setContentDescription(
                videoFullscreen ? "Salir del modo ampliado" : "Ampliar reproductor"
        );
    }

    private int obtenerIconoPantallaCompleta() {
        int icono = getResources().getIdentifier(
                "ic_media_fullscreen",
                "drawable",
                "android"
        );
        return icono != 0 ? icono : android.R.drawable.ic_menu_zoom;
    }

    private void actualizarBotonReproduccion() {
        boolean reproduciendo = videoView != null && videoView.isPlaying();
        int icono = reproduciendo
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play;
        String descripcion = reproduciendo ? "Pausar video" : "Reproducir video";

        btnVideoPlay.setImageResource(icono);
        btnVideoMiniPlay.setImageResource(icono);
        btnVideoPlay.setContentDescription(descripcion);
        btnVideoMiniPlay.setContentDescription(descripcion);
        videoCenterControls.setVisibility(reproduciendo ? View.GONE : View.VISIBLE);
    }

    private void actualizarBarraProgreso() {
        if (!userSeeking && videoDuration > 0 && videoView != null) {
            videoSeekBar.setProgress(
                    videoView.getCurrentPosition() * videoSeekBar.getMax() / videoDuration
            );
        }
    }

    private void mostrarErrorVideo() {
        videoReady = false;
        videoProgress.setVisibility(View.GONE);
        btnVideoPlay.setVisibility(View.GONE);
        btnVideoMiniPlay.setVisibility(View.GONE);
        videoSeekBar.setVisibility(View.GONE);
        videoCenterControls.setVisibility(View.GONE);
        tvVideoError.setVisibility(View.VISIBLE);
    }

    private String formatearTiempo(int milliseconds) {
        int totalSeconds = Math.max(0, milliseconds / 1000);
        return String.format(
                Locale.getDefault(), "%02d:%02d", totalSeconds / 60, totalSeconds % 60
        );
    }

    @Override
    public void onDestroyView() {
        progressHandler.removeCallbacks(progressUpdater);
        if (videoView != null) {
            videoView.stopPlayback();
        }
        if (videoFullscreen && isAdded()) {
            ((MainActivity) requireActivity()).setVideoFullscreen(false);
        }
        videoReady = false;
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && videoView != null && videoView.isPlaying()) {
            videoView.pause();
            actualizarBotonReproduccion();
        }
    }
}
