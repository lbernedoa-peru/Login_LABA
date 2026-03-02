package com.example.login_laba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button registrar_abogado;
    Button listaAbogados;
    Button editar;

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

        registrar_abogado = findViewById(R.id.registrar);
        listaAbogados = findViewById(R.id.listaAbogados);
        editar = findViewById(R.id.editar);

        registrar_abogado.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });

        listaAbogados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListarActivity.class);
            startActivity(intent);
        });

        editar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditarActivity.class);
            startActivity(intent);
        });
    }
}