package com.techportfolio.app;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    public PerfilFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_perfil,
                container,
                false
        );

        ImageView imgPerfil = view.findViewById(R.id.imgPerfil);
        imgPerfil.setImageResource(R.drawable.perfil);
        imgPerfil.setOnClickListener(v -> mostrarFotoGrande());

        return view;
    }

    private void mostrarFotoGrande() {
        ImageView imagen = new ImageView(requireContext());
        imagen.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(360)
        ));
        imagen.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imagen.setImageBitmap(BitmapFactory.decodeResource(
                getResources(),
                R.drawable.perfil
        ));

        new AlertDialog.Builder(requireContext())
                .setTitle("Foto de perfil")
                .setView(imagen)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}
