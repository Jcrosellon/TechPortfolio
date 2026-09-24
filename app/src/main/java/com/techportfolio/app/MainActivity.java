package com.techportfolio.app;

import android.os.Bundle;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

public class MainActivity extends AppCompatActivity {

    private static final String MENU_EXPANDED_STATE = "menu_expanded";
    private static final float EXPANDED_MENU_WEIGHT = 0.9f;
    private static final float EXPANDED_CONTENT_WEIGHT = 2.3f;
    private static final int LANDSCAPE_MENU_WIDTH_DP = 168;

    private FragmentContainerView menuContainer;
    private FragmentContainerView contentContainer;
    private View sidebarDivider;
    private ImageButton toggleMenuButton;
    private LinearLayout topBar;
    private TextView tvAndroid;
    private ObjectAnimator androidBlinkAnimator;
    private boolean menuExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            menuExpanded = savedInstanceState.getBoolean(MENU_EXPANDED_STATE, true);
        }

        setContentView(R.layout.activity_main);

        menuContainer = findViewById(R.id.menuFragmentContainer);
        contentContainer = findViewById(R.id.contentFragmentContainer);
        sidebarDivider = findViewById(R.id.sidebarDivider);
        toggleMenuButton = findViewById(R.id.btnToggleMenu);
        topBar = findViewById(R.id.topBar);
        tvAndroid = findViewById(R.id.tvAndroid);
        configurarTextoAndroid();
        toggleMenuButton.setOnClickListener(v -> toggleMenu());
        menuContainer.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                  oldLeft, oldTop, oldRight, oldBottom) ->
                positionToggleButton()
        );
        setMenuExpanded(menuExpanded);
        aplicarLayoutOrientacion();

        if (savedInstanceState == null) {

            // Cargar menú izquierdo
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.menuFragmentContainer,
                            new MenuFragment()
                    )
                    .commit();

            // Cargar Perfil por defecto
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.contentFragmentContainer,
                            new PerfilFragment(),
                            "perfil"
                    )
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MENU_EXPANDED_STATE, menuExpanded);
        super.onSaveInstanceState(outState);
    }

    public boolean isMenuExpanded() {
        return menuExpanded;
    }

    public void toggleMenu() {
        setMenuExpanded(!menuExpanded);
    }

    public void setVideoFullscreen(boolean fullscreen) {
        topBar.setVisibility(fullscreen ? android.view.View.GONE : android.view.View.VISIBLE);
        menuContainer.setVisibility(fullscreen ? android.view.View.GONE : android.view.View.VISIBLE);
        toggleMenuButton.setVisibility(fullscreen ? android.view.View.GONE : android.view.View.VISIBLE);

        if (!fullscreen) {
            setMenuExpanded(menuExpanded);
        }
    }

    public void setMenuExpanded(boolean expanded) {
        menuExpanded = expanded;

        LinearLayout.LayoutParams menuParams =
                (LinearLayout.LayoutParams) menuContainer.getLayoutParams();
        LinearLayout.LayoutParams contentParams =
                (LinearLayout.LayoutParams) contentContainer.getLayoutParams();

        boolean horizontal = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (expanded && horizontal) {
            menuParams.width = dpToPx(LANDSCAPE_MENU_WIDTH_DP);
            menuParams.weight = 0;
            contentParams.width = 0;
            contentParams.weight = 1;
        } else if (expanded) {
            menuParams.width = 0;
            menuParams.weight = EXPANDED_MENU_WEIGHT;
            contentParams.width = 0;
            contentParams.weight = EXPANDED_CONTENT_WEIGHT;
        } else {
            menuParams.width = dpToPx(72);
            menuParams.weight = 0;
            contentParams.width = 0;
            contentParams.weight = 1;
        }

        menuContainer.setLayoutParams(menuParams);
        contentContainer.setLayoutParams(contentParams);
        toggleMenuButton.setImageResource(
                expanded ? android.R.drawable.ic_media_previous : android.R.drawable.ic_media_next
        );
        toggleMenuButton.setContentDescription(expanded ? "Cerrar menú" : "Abrir menú");
        menuContainer.post(this::positionToggleButton);

        Fragment menuFragment = getSupportFragmentManager()
                .findFragmentById(R.id.menuFragmentContainer);
        if (menuFragment instanceof MenuFragment) {
            ((MenuFragment) menuFragment).setMenuExpanded(expanded);
        }
    }

    private void positionToggleButton() {
        if (toggleMenuButton == null || menuContainer.getWidth() == 0) {
            return;
        }

        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) toggleMenuButton.getLayoutParams();
        params.leftMargin = menuContainer.getRight() - dpToPx(20);
        params.topMargin = dpToPx(20);
        toggleMenuButton.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        aplicarLayoutOrientacion();
    }

    private void aplicarLayoutOrientacion() {
        boolean horizontal = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        topBar.setVisibility(View.VISIBLE);
        menuContainer.setVisibility(View.VISIBLE);
        toggleMenuButton.setVisibility(View.VISIBLE);
        sidebarDivider.setVisibility(View.VISIBLE);
        setMenuExpanded(menuExpanded);
    }

    private void configurarTextoAndroid() {
        androidBlinkAnimator = ObjectAnimator.ofFloat(tvAndroid, View.ALPHA, 1f, 0.35f, 1f);
        androidBlinkAnimator.setDuration(900);
        androidBlinkAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        androidBlinkAnimator.start();
        tvAndroid.setOnClickListener(v -> mostrarIconoAndroid());
    }

    private void mostrarIconoAndroid() {
        FrameLayout contentRoot = findViewById(android.R.id.content);
        ImageView icono = new ImageView(this);
        icono.setImageResource(R.drawable.android);
        icono.setBackgroundColor(Color.TRANSPARENT);
        icono.setContentDescription("Icono Android animado");

        int size = dpToPx(96);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.TOP | Gravity.START;
        contentRoot.addView(icono, params);

        contentRoot.post(() -> {
            icono.setTranslationX(-size);
            icono.setTranslationY(dpToPx(90));

            ObjectAnimator moveX = ObjectAnimator.ofFloat(
                    icono,
                    View.TRANSLATION_X,
                    -size,
                    contentRoot.getWidth() + size
            );
            ObjectAnimator moveY = ObjectAnimator.ofFloat(
                    icono,
                    View.TRANSLATION_Y,
                    dpToPx(90),
                    dpToPx(40),
                    dpToPx(140),
                    dpToPx(90)
            );
            ObjectAnimator rotate = ObjectAnimator.ofFloat(icono, View.ROTATION, 0f, 360f);

            AnimatorSet animation = new AnimatorSet();
            animation.playTogether(moveX, moveY, rotate);
            animation.setDuration(2600);
            animation.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    contentRoot.removeView(icono);
                }
            });
            animation.start();
        });
    }

    @Override
    protected void onDestroy() {
        if (androidBlinkAnimator != null) {
            androidBlinkAnimator.cancel();
        }
        super.onDestroy();
    }
}
