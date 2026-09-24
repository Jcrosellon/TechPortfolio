package com.techportfolio.app;

import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class FotosFragment extends Fragment {

    private TextView tvDescripcionFoto;
    private android.graphics.Bitmap[] imagenes;
    private ImageButton proyectoSeleccionado;

    private final int[] coloresTarjetas = {
            Color.parseColor("#D7E8FC"),
            Color.parseColor("#E6DDFB"),
            Color.parseColor("#DDF2E9"),
            Color.parseColor("#FBE3E3"),
            Color.parseColor("#FCEED5"),
            Color.parseColor("#DDF1F7")
    };

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
        int[] recursosImagenes = {
                R.raw.proyecto_01,
                R.raw.proyecto_02,
                R.raw.proyecto_03,
                R.raw.proyecto_04,
                R.raw.proyecto_05,
                R.raw.proyecto_06
        };
        imagenes = new android.graphics.Bitmap[recursosImagenes.length];

        for (int i = 0; i < proyectos.length; i++) {
            final int posicion = i;
            proyectos[i].setImageTintList(null);
            proyectos[i].setScaleType(ImageButton.ScaleType.CENTER_CROP);
            imagenes[i] = BitmapFactory.decodeResource(getResources(), recursosImagenes[i]);
            proyectos[i].setImageBitmap(imagenes[i]);
            aplicarFondoTarjeta(proyectos[i], coloresTarjetas[i], false);
            proyectos[i].setOnClickListener(v -> mostrarDetalle(
                    posicion,
                    descripciones[posicion]
            ));
        }

        return view;
    }

    private void mostrarDescripcion(String descripcion) {
        tvDescripcionFoto.setText(descripcion);
    }

    private void mostrarDetalle(int posicion, String descripcion) {
        seleccionarProyecto(posicion);
        mostrarDescripcion(descripcion);

        ImageView imagen = new ImageView(requireContext());
        imagen.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(240)
        ));
        imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imagen.setImageBitmap(imagenes[posicion]);

        TextView texto = new TextView(requireContext());
        texto.setText(descripcion);
        texto.setTextColor(requireContext().getColor(R.color.tp_text_secondary));
        texto.setTextSize(15);
        texto.setPadding(0, dpToPx(14), 0, 0);

        LinearLayout contenido = new LinearLayout(requireContext());
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setPadding(dpToPx(20), 0, dpToPx(20), 0);
        contenido.addView(imagen);
        contenido.addView(texto);

        new AlertDialog.Builder(requireContext())
                .setTitle(String.format("Proyecto %02d", posicion + 1))
                .setView(contenido)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void seleccionarProyecto(int posicion) {
        ImageButton[] proyectos = {
                requireView().findViewById(R.id.imgProyecto1),
                requireView().findViewById(R.id.imgProyecto2),
                requireView().findViewById(R.id.imgProyecto3),
                requireView().findViewById(R.id.imgProyecto4),
                requireView().findViewById(R.id.imgProyecto5),
                requireView().findViewById(R.id.imgProyecto6)
        };

        if (proyectoSeleccionado != null) {
            int anterior = -1;
            for (int i = 0; i < proyectos.length; i++) {
                if (proyectos[i] == proyectoSeleccionado) {
                    anterior = i;
                    break;
                }
            }
            if (anterior >= 0) {
                aplicarFondoTarjeta(proyectoSeleccionado, coloresTarjetas[anterior], false);
            }
        }

        proyectoSeleccionado = proyectos[posicion];
        aplicarFondoTarjeta(proyectoSeleccionado, coloresTarjetas[posicion], true);
    }

    private void aplicarFondoTarjeta(ImageButton boton, int color, boolean seleccionado) {
        GradientDrawable fondo = new GradientDrawable();
        fondo.setColor(color);
        fondo.setCornerRadius(dpToPx(12));
        if (seleccionado) {
            fondo.setStroke(dpToPx(2), Color.parseColor("#E53935"));
        }
        boton.setBackground(fondo);
        boton.setBackgroundTintList(null);
        boton.setElevation(seleccionado ? dpToPx(4) : 0);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}
