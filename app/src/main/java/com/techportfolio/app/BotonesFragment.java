package com.techportfolio.app;

import android.os.Bundle;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BotonesFragment extends Fragment {

    private Button btnMensaje;
    private Button btnCambiarTexto;
    private Button btnCambiarImagen;
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
        btnCambiarImagen = view.findViewById(R.id.btnCambiarImagen);
        btnAumentar = view.findViewById(R.id.btnAumentar);
        btnReiniciar = view.findViewById(R.id.btnReiniciar);

        tvResultado = view.findViewById(R.id.tvResultado);
        tvContador = view.findViewById(R.id.tvContador);

        ajustarLayoutOrientacion(view);

        // Eventos
        btnMensaje.setOnClickListener(v -> mostrarMensaje());

        btnCambiarTexto.setOnClickListener(v -> cambiarTexto());

        btnCambiarImagen.setOnClickListener(v -> cambiarImagen());

        btnAumentar.setOnClickListener(v -> aumentarContador());

        btnReiniciar.setOnClickListener(v -> reiniciarContador());

        return view;
    }

    private void ajustarLayoutOrientacion(View view) {
        boolean horizontal = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        LinearLayout mainCard = view.findViewById(R.id.botonesMainCard);
        LinearLayout acciones = view.findViewById(R.id.accionesColumn);
        LinearLayout contadorColumn = view.findViewById(R.id.contadorColumn);
        TextView titulo = view.findViewById(R.id.tvBotonesTitulo);
        TextView subtitulo = view.findViewById(R.id.tvBotonesSubtitulo);

        titulo.setTextSize(horizontal ? 28 : 22);
        subtitulo.setTextSize(horizontal ? 16 : 13);

        if (horizontal) {
            mainCard.setOrientation(LinearLayout.HORIZONTAL);
            configurarColumna(acciones, true, false);
            configurarColumna(contadorColumn, true, false);
        } else {
            mainCard.setOrientation(LinearLayout.VERTICAL);
            configurarColumna(acciones, false, false);
            configurarColumna(contadorColumn, false, true);
        }
    }

    private void configurarColumna(
            LinearLayout columna,
            boolean horizontal,
            boolean separacionSuperior) {
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) columna.getLayoutParams();
        params.width = horizontal ? 0 : LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.weight = horizontal ? 1 : 0;
        params.topMargin = separacionSuperior ? dpToPx(18) : 0;
        params.setMarginStart(0);
        params.setMarginEnd(0);
        boolean contador = columna.getId() == R.id.contadorColumn;
        columna.setPadding(
                horizontal && contador ? dpToPx(12) : 0,
                0,
                horizontal && !contador ? dpToPx(12) : 0,
                0
        );
        columna.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
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

    private void cambiarImagen() {
        tvResultado.setText("La imagen fue modificada correctamente.");
    }

    private void aumentarContador() {

        contador++;

        tvContador.setText(
                String.valueOf(contador)
        );
    }

    private void reiniciarContador() {

        contador = 0;

        tvContador.setText(
                "0"
        );

        tvResultado.setText(
                "Interactúa con los botones."
        );
    }
}
