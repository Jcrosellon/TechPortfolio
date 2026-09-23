package com.techportfolio.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BotonesFragment extends Fragment {

    private Button btnMensaje;
    private Button btnCambiarTexto;
    private Button btnAumentar;
    private Button btnReiniciar;

    private TextView tvResultado;
    private TextView tvContador;

    private int contador = 0;

    public BotonesFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_botones,
                container,
                false
        );

        // Vincular variables con los identificadores XML
        btnMensaje = view.findViewById(R.id.btnMensaje);
        btnCambiarTexto = view.findViewById(R.id.btnCambiarTexto);
        btnAumentar = view.findViewById(R.id.btnAumentar);
        btnReiniciar = view.findViewById(R.id.btnReiniciar);

        tvResultado = view.findViewById(R.id.tvResultado);
        tvContador = view.findViewById(R.id.tvContador);

        // Eventos
        btnMensaje.setOnClickListener(v -> mostrarMensaje());

        btnCambiarTexto.setOnClickListener(v -> cambiarTexto());

        btnAumentar.setOnClickListener(v -> aumentarContador());

        btnReiniciar.setOnClickListener(v -> reiniciarContador());

        return view;
    }

    private void mostrarMensaje() {

        Toast.makeText(
                requireContext(),
                "¡Bienvenido a TechPortfolio!",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void cambiarTexto() {

        tvResultado.setText(
                "El texto fue modificado correctamente."
        );
    }

    private void aumentarContador() {

        contador++;

        tvContador.setText(
                "Contador: " + contador
        );
    }

    private void reiniciarContador() {

        contador = 0;

        tvContador.setText(
                "Contador: 0"
        );

        tvResultado.setText(
                "Interactúa con los botones."
        );
    }
}