package com.techportfolio.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
}