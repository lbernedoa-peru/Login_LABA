package com.example.login_laba;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    CardView cardRegistrar;
    CardView cardLista;
    CardView cardEditar;
    CardView cardEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cardRegistrar = findViewById(R.id.cardRegistrar);
        cardLista = findViewById(R.id.cardLista);
        cardEditar = findViewById(R.id.cardEditar);
        cardEliminar = findViewById(R.id.cardEliminar);

        cardRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });

        cardLista.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListarActivity.class);
            startActivity(intent);
        });

        cardEditar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditarActivity.class);
            startActivity(intent);
        });

        cardEliminar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditarActivity.class);
            startActivity(intent);
        });
    }
}