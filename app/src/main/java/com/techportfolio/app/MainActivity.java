package com.techportfolio.app;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

public class MainActivity extends AppCompatActivity {

    private static final String MENU_EXPANDED_STATE = "menu_expanded";
    private static final float EXPANDED_MENU_WEIGHT = 0.9f;
    private static final float EXPANDED_CONTENT_WEIGHT = 2.3f;

    private FragmentContainerView menuContainer;
    private FragmentContainerView contentContainer;
    private ImageButton toggleMenuButton;
    private LinearLayout topBar;
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
        toggleMenuButton = findViewById(R.id.btnToggleMenu);
        topBar = findViewById(R.id.topBar);
        toggleMenuButton.setOnClickListener(v -> toggleMenu());
        menuContainer.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                  oldLeft, oldTop, oldRight, oldBottom) ->
                positionToggleButton()
        );
        setMenuExpanded(menuExpanded);

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
                            new PerfilFragment()
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

        if (expanded) {
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
}
