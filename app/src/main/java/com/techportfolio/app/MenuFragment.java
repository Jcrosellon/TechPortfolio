package com.techportfolio.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

public class MenuFragment extends Fragment {

    private Button btnPerfil;
    private Button btnFotos;
    private Button btnVideo;
    private Button btnWeb;
    private Button btnBotones;

    public MenuFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_menu,
                container,
                false
        );

        // Vincular botones con los IDs del XML
        btnPerfil = view.findViewById(R.id.btnPerfil);
        btnFotos = view.findViewById(R.id.btnFotos);
        btnVideo = view.findViewById(R.id.btnVideo);
        btnWeb = view.findViewById(R.id.btnWeb);
        btnBotones = view.findViewById(R.id.btnBotones);

        // Eventos
        btnPerfil.setOnClickListener(v -> {
            seleccionarBoton(btnPerfil);
            abrirPerfil();
        });

        btnFotos.setOnClickListener(v -> {
            seleccionarBoton(btnFotos);
            abrirFotos();
        });

        btnVideo.setOnClickListener(v -> {
            seleccionarBoton(btnVideo);
            abrirVideo();
        });

        btnWeb.setOnClickListener(v -> {
            seleccionarBoton(btnWeb);
            abrirWeb();
        });

        btnBotones.setOnClickListener(v -> {
            seleccionarBoton(btnBotones);
            abrirBotones();
        });

        seleccionarBoton(btnPerfil);

        return view;
    }

    private void abrirPerfil() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.contentFragmentContainer,
                        new PerfilFragment()
                )
                .commit();
    }

    private void abrirFotos() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.contentFragmentContainer,
                        new FotosFragment()
                )
                .commit();
    }

    private void abrirVideo() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.contentFragmentContainer,
                        new VideoFragment()
                )
                .commit();
    }

    private void abrirWeb() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.contentFragmentContainer,
                        new WebFragment()
                )
                .commit();
    }

    private void abrirBotones() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.contentFragmentContainer,
                        new BotonesFragment()
                )
                .commit();
    }

    private void seleccionarBoton(Button botonSeleccionado) {

        Button[] botones = {
                btnPerfil,
                btnFotos,
                btnVideo,
                btnWeb,
                btnBotones
        };

        for (Button boton : botones) {

            boton.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.white
                            )
                    )
            );

            boton.setTextColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.tp_text_primary
                    )
            );
        }

        botonSeleccionado.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(
                                requireContext(),
                                R.color.tp_blue_light
                        )
                )
        );

        botonSeleccionado.setTextColor(
                ContextCompat.getColor(
                        requireContext(),
                        R.color.tp_blue
                )
        );
    }
}