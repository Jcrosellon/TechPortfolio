package com.techportfolio.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment {

    private EditText etUrl;
    private Button btnAbrirWeb;
    private WebView webView;

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
        btnAbrirWeb = view.findViewById(R.id.btnAbrirWeb);
        webView = view.findViewById(R.id.webView);

        configurarWebView();

        btnAbrirWeb.setOnClickListener(v -> cargarPagina());

        return view;
    }

    private void configurarWebView() {

        // Mantiene la navegación dentro de la aplicación
        webView.setWebViewClient(new WebViewClient());

        // Algunas páginas modernas necesitan JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);
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

        webView.loadUrl(url);
    }
}