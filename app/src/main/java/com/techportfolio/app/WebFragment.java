package com.techportfolio.app;

import android.os.Bundle;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WebFragment extends Fragment {

    private EditText etUrl;
    private ImageButton btnBuscarWeb;
    private FrameLayout webViewContainer;
    private LinearLayout historialContainer;
    private ProgressBar webProgress;
    private WebView webView;
    private final ArrayList<String> historial = new ArrayList<>();

    public WebFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_web,
                container,
                false
        );

        etUrl = view.findViewById(R.id.etUrl);
        btnBuscarWeb = view.findViewById(R.id.btnBuscarWeb);
        webViewContainer = view.findViewById(R.id.webViewContainer);
        historialContainer = view.findViewById(R.id.historialContainer);
        webProgress = view.findViewById(R.id.webProgress);
        ajustarContenidoHorizontal(view);

        btnBuscarWeb.setOnClickListener(v -> cargarPagina());

        return view;
    }

    private void ajustarContenidoHorizontal(View view) {
        boolean horizontal = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (!horizontal) {
            return;
        }

        TextView titulo = view.findViewById(R.id.webTitle);
        TextView subtitulo = view.findViewById(R.id.webSubtitle);
        TextView etiqueta = view.findViewById(R.id.webAddressLabel);
        HorizontalScrollView historialView = view.findViewById(R.id.webHistoryScroll);
        LinearLayout controles = view.findViewById(R.id.webControls);
        LinearLayout contenido = view.findViewById(R.id.webContentCard);

        titulo.setTextSize(24);
        subtitulo.setTextSize(14);
        etiqueta.setVisibility(View.GONE);
        historialView.getLayoutParams().height = dpToPx(34);
        historialView.setLayoutParams(historialView.getLayoutParams());
        historialView.setVisibility(View.GONE);

        controles.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        LinearLayout.LayoutParams controlesParams =
                (LinearLayout.LayoutParams) controles.getLayoutParams();
        controlesParams.topMargin = dpToPx(6);
        controles.setLayoutParams(controlesParams);

        LinearLayout.LayoutParams contenidoParams =
                (LinearLayout.LayoutParams) contenido.getLayoutParams();
        contenidoParams.topMargin = dpToPx(8);
        contenido.setLayoutParams(contenidoParams);
        contenido.setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));
    }

    private void configurarWebView() {

        // Mantiene la navegación dentro de la aplicación
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                webProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webProgress.setVisibility(View.GONE);
            }
        });

        // Algunas páginas modernas necesitan JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);
    }

    private void prepararWebView() {
        if (webView != null) {
            return;
        }

        webView = new WebView(requireContext());
        webView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        webViewContainer.addView(webView);
        webProgress.bringToFront();
        configurarWebView();
    }

    private void cargarPagina() {

        String url = etUrl.getText().toString().trim();

        if (url.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Ingrese una dirección web",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!url.startsWith("http://") &&
                !url.startsWith("https://")) {

            url = "https://" + url;
        }

        prepararWebView();
        agregarAlHistorial(url);
        webView.loadUrl(url);
    }

    private void agregarAlHistorial(String url) {
        historial.remove(url);
        historial.add(0, url);
        while (historial.size() > 4) {
            historial.remove(historial.size() - 1);
        }

        historialContainer.removeAllViews();
        for (String elemento : historial) {
            Button boton = new Button(requireContext());
            boton.setText(obtenerEtiquetaHistorial(elemento));
            boton.setAllCaps(false);
            boton.setTextSize(12);
            boton.setContentDescription(elemento);
            boton.setTextColor(ContextCompat.getColor(requireContext(), R.color.tp_blue));
            boton.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.tp_blue_light)
            ));
            boton.setMinHeight(0);
            boton.setMinWidth(0);
            boton.setPadding(dpToPx(12), 0, dpToPx(12), 0);
            boton.setOnClickListener(v -> {
                etUrl.setText(elemento);
                etUrl.setSelection(etUrl.length());
                cargarPagina();
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(60),
                    dpToPx(40)
            );
            params.setMarginEnd(dpToPx(8));
            historialContainer.addView(boton, params);
        }
    }

    private String obtenerEtiquetaHistorial(String url) {
        String host = Uri.parse(url).getHost();
        if (host == null || host.isEmpty()) {
            return url.substring(0, Math.min(2, url.length())).toUpperCase();
        }

        host = host.replace("www.", "");
        String[] partes = host.split("\\.");
        String nombre = partes[0];
        if (nombre.equals("youtube")) {
            return "YT";
        }
        if (nombre.equals("google")) {
            return "GO";
        }
        if (nombre.equals("github")) {
            return "GH";
        }
        if (nombre.length() == 1) {
            return nombre.toUpperCase();
        }
        return nombre.substring(0, 2).toUpperCase();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (webView != null) {
            if (hidden) {
                webView.onPause();
                webView.stopLoading();
            } else {
                webView.onResume();
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }
}
