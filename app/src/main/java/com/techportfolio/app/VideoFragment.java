package com.techportfolio.app;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class VideoFragment extends Fragment {

    private TextureView videoView;
    private ImageButton btnVideoPlay;
    private ImageButton btnVideoMiniPlay;
    private ImageButton btnVideoFullscreen;
    private ProgressBar videoProgress;
    private SeekBar videoSeekBar;
    private TextView tvVideoTime;
    private TextView tvVideoError;
    private View videoCenterControls;
    private MediaPlayer mediaPlayer;
    private Surface videoSurface;
    private Uri videoUri;
    private boolean videoReady;
    private int videoDuration;
    private boolean userSeeking;
    private boolean videoFullscreen;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (videoReady && mediaPlayer != null) {
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

        View view = inflater.inflate(
                R.layout.fragment_video,
                container,
                false
        );

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
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && videoReady && videoDuration > 0) {
                    mediaPlayer.seekTo(videoDuration * progress / seekBar.getMax());
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

        videoUri = Uri.parse(
                "android.resource://" +
                        requireContext().getPackageName() +
                        "/" +
                        R.raw.video_presentacion
        );

        videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(
                    @NonNull SurfaceTexture surfaceTexture,
                    int width,
                    int height) {
                prepararVideo(surfaceTexture);
            }

            @Override
            public void onSurfaceTextureSizeChanged(
                    @NonNull SurfaceTexture surfaceTexture,
                    int width,
                    int height) {
                // La superficie sigue el tamaño del reproductor.
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                liberarMediaPlayer();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
                // El progreso se actualiza mediante progressUpdater.
            }
        });

        return view;
    }

    private void prepararVideo(SurfaceTexture surfaceTexture) {
        liberarMediaPlayer();
        videoSurface = new Surface(surfaceTexture);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(requireContext(), videoUri);
            mediaPlayer.setSurface(videoSurface);
            mediaPlayer.setOnPreparedListener(player -> {
                videoReady = true;
                videoDuration = player.getDuration();
                videoProgress.setVisibility(View.GONE);
                videoSeekBar.setEnabled(true);
                tvVideoTime.setText(formatearTiempo(videoDuration));
                actualizarBotonReproduccion();
                progressHandler.post(progressUpdater);
            });
            mediaPlayer.setOnCompletionListener(player -> {
                player.seekTo(0);
                actualizarBotonReproduccion();
                actualizarBarraProgreso();
            });
            mediaPlayer.setOnErrorListener((player, what, extra) -> {
                mostrarErrorVideo();
                return true;
            });
            mediaPlayer.prepareAsync();
        } catch (Exception exception) {
            mostrarErrorVideo();
        }
    }

    private void alternarReproduccion() {
        if (!videoReady || mediaPlayer == null) {
            return;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }

        actualizarBotonReproduccion();
    }

    private void alternarPantallaCompleta() {
        videoFullscreen = !videoFullscreen;
        ((MainActivity) requireActivity()).setVideoFullscreen(videoFullscreen);
        btnVideoFullscreen.setImageResource(
                videoFullscreen
                        ? android.R.drawable.ic_menu_close_clear_cancel
                        : android.R.drawable.ic_menu_zoom
        );
        btnVideoFullscreen.setContentDescription(
                videoFullscreen ? "Salir del modo ampliado" : "Ampliar reproductor"
        );
    }

    private void actualizarBotonReproduccion() {
        if (mediaPlayer == null) {
            return;
        }

        boolean reproduciendo = mediaPlayer.isPlaying();
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
        if (!userSeeking && videoDuration > 0 && mediaPlayer != null) {
            int position = mediaPlayer.getCurrentPosition();
            videoSeekBar.setProgress(position * videoSeekBar.getMax() / videoDuration);
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
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void liberarMediaPlayer() {
        progressHandler.removeCallbacks(progressUpdater);
        videoReady = false;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (videoSurface != null) {
            videoSurface.release();
            videoSurface = null;
        }
    }

    @Override
    public void onDestroyView() {
        if (videoFullscreen && isAdded()) {
            ((MainActivity) requireActivity()).setVideoFullscreen(false);
        }
        liberarMediaPlayer();
        super.onDestroyView();
    }
}
