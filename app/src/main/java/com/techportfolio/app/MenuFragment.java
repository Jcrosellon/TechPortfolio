package com.techportfolio.app;

import android.os.Bundle;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

public class MenuFragment extends Fragment {

    private Button btnPerfil;
    private Button btnFotos;
    private Button btnVideo;
    private Button btnWeb;
    private Button btnBotones;
    private TextView tvMenuTitle;
    private Button botonSeleccionado;
    private ImageView[] iconosVisibles;

    private final int[] iconosMenu = {
            android.R.drawable.ic_menu_myplaces,
            android.R.drawable.ic_menu_gallery,
            android.R.drawable.ic_media_play,
            android.R.drawable.ic_menu_view,
            android.R.drawable.ic_menu_manage
    };

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
        tvMenuTitle = view.findViewById(R.id.tvMenuTitle);
        iconosVisibles = new ImageView[]{
                view.findViewById(R.id.iconPerfil),
                view.findViewById(R.id.iconFotos),
                view.findViewById(R.id.iconVideo),
                view.findViewById(R.id.iconWeb),
                view.findViewById(R.id.iconBotones)
        };

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
        setMenuExpanded(((MainActivity) requireActivity()).isMenuExpanded());

        return view;
    }

    public void setMenuExpanded(boolean expanded) {
        if (tvMenuTitle == null) {
            return;
        }

        tvMenuTitle.setVisibility(expanded ? View.VISIBLE : View.INVISIBLE);

        Button[] botones = {
                btnPerfil,
                btnFotos,
                btnVideo,
                btnWeb,
                btnBotones
        };
        String[] nombres = {
                "Perfil",
                "Fotos",
                "Video",
                "Web",
                "Botones"
        };

        for (int i = 0; i < botones.length; i++) {
            botones[i].setText(expanded ? nombres[i] : "");
            botones[i].setGravity(expanded ? Gravity.CENTER_VERTICAL : Gravity.CENTER);
            botones[i].setPadding(expanded ? dpToPx(12) : 0, 0, 0, 0);
            iconosVisibles[i].setVisibility(expanded ? View.GONE : View.VISIBLE);

            if (expanded) {
                Drawable icono = ContextCompat.getDrawable(requireContext(), iconosMenu[i]);
                botones[i].setCompoundDrawablesWithIntrinsicBounds(icono, null, null, null);
            } else {
                botones[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            botones[i].setCompoundDrawablePadding(expanded ? dpToPx(8) : 0);
        }

        actualizarColorIconos();
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

        this.botonSeleccionado = botonSeleccionado;

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

        actualizarColorIconos();
    }

    private void actualizarColorIconos() {
        if (btnPerfil == null) {
            return;
        }

        Button[] botones = {
                btnPerfil,
                btnFotos,
                btnVideo,
                btnWeb,
                btnBotones
        };

        for (int i = 0; i < botones.length; i++) {
            Button boton = botones[i];
            int color = boton == botonSeleccionado
                    ? R.color.tp_blue
                    : R.color.tp_text_primary;
            ColorStateList tint = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), color)
            );
            ImageViewCompat.setImageTintList(iconosVisibles[i], tint);
            Drawable icono = boton.getCompoundDrawables()[0];
            if (icono != null) {
                DrawableCompat.setTintList(
                        DrawableCompat.wrap(icono),
                        tint
                );
            }
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}
