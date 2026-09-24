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

        View view = inflater.inflate(R.layout.fragment_fotos, container, false);
        tvDescripcionFoto = view.findViewById(R.id.tvDescripcionFoto);

        ImageButton[] proyectos = {
                view.findViewById(R.id.imgProyecto1),
                view.findViewById(R.id.imgProyecto2),
                view.findViewById(R.id.imgProyecto3),
                view.findViewById(R.id.imgProyecto4),
                view.findViewById(R.id.imgProyecto5),
                view.findViewById(R.id.imgProyecto6)
        };
        String[] descripciones = {
                "Proyecto de aplicación móvil desarrollado en Android Studio.",
                "Sistema de inventario diseñado para controlar productos, entradas y salidas.",
                "Interfaz web enfocada en la presentación de servicios y proyectos.",
                "Aplicación académica para gestionar información y tareas.",
                "Prototipo de solución tecnológica para procesos empresariales.",
                "Diseño de interfaz responsive para una experiencia sencilla."
        };

        for (int i = 0; i < proyectos.length; i++) {
            final int posicion = i;
            proyectos[i].setOnClickListener(v ->
                    mostrarDescripcion(descripciones[posicion])
            );
        }

        return view;
    }

    private void mostrarDescripcion(String descripcion) {
        tvDescripcionFoto.setText(descripcion);
    }
}
