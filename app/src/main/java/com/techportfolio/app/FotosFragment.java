package com.techportfolio.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FotosFragment extends Fragment {

    private ImageButton imgProyecto1;
    private ImageButton imgProyecto2;
    private ImageButton imgProyecto3;

    private TextView tvDescripcionFoto;

    public FotosFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_fotos,
                container,
                false
        );

        imgProyecto1 = view.findViewById(R.id.imgProyecto1);
        imgProyecto2 = view.findViewById(R.id.imgProyecto2);
        imgProyecto3 = view.findViewById(R.id.imgProyecto3);

        tvDescripcionFoto = view.findViewById(R.id.tvDescripcionFoto);

        imgProyecto1.setOnClickListener(v ->
                mostrarDescripcion(
                        "Proyecto 1: Aplicación móvil desarrollada en Android Studio para gestionar información profesional."
                )
        );

        imgProyecto2.setOnClickListener(v ->
                mostrarDescripcion(
                        "Proyecto 2: Sistema de inventario diseñado para controlar productos, entradas y salidas."
                )
        );

        imgProyecto3.setOnClickListener(v ->
                mostrarDescripcion(
                        "Proyecto 3: Desarrollo de una interfaz web enfocada en la presentación de servicios y proyectos."
                )
        );

        return view;
    }

    private void mostrarDescripcion(String descripcion) {
        tvDescripcionFoto.setText(descripcion);
    }
}