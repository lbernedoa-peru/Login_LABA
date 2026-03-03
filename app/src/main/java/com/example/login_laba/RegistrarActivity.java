package com.example.login_laba;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono;
    AutoCompleteTextView etCorreo, etEspecializacion;
    Button btnGuardar;
    DatabaseHelper db;

    String[] dominios = {
            "gmail.com",
            "hotmail.com",
            "outlook.com",
            "yahoo.com",
            "icloud.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        // 🔹 Base de datos
        db = new DatabaseHelper(this);

        // 🔹 Vincular con XML (TUS IDS)
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        etEspecializacion = findViewById(R.id.etEspecializacion);
        etCorreo = findViewById(R.id.etCorreo);
        btnGuardar = findViewById(R.id.btnGuardar);

        // 🔹 Dropdown Especialización
        String[] opciones = {
                "Derecho Penal",
                "Derecho Civil",
                "Derecho Laboral",
                "Derecho Familiar",
                "Derecho Empresarial",
                "Derecho Tributario",
                "Derecho Administrativo"
        };

        ArrayAdapter<String> adapterEspecialidad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                opciones
        );

        etEspecializacion.setAdapter(adapterEspecialidad);
        etEspecializacion.setOnClickListener(v -> etEspecializacion.showDropDown());

        // 🔹 Autocompletado correo
        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String texto = s.toString();

                if (!texto.contains("@")) return;

                String usuario = texto.substring(0, texto.indexOf("@"));

                ArrayList<String> sugerencias = new ArrayList<>();

                for (String dominio : dominios) {
                    sugerencias.add(usuario + "@" + dominio);
                }

                ArrayAdapter<String> adapterCorreo = new ArrayAdapter<>(
                        RegistrarActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        sugerencias
                );

                etCorreo.setAdapter(adapterCorreo);
                etCorreo.showDropDown();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 🔥 BOTÓN GUARDAR
        btnGuardar.setOnClickListener(v -> {

            String nombre = txtNombre.getText().toString().trim();
            String especializacion = etEspecializacion.getText().toString().trim();
            String telefono = txtTelefono.getText().toString().trim();
            String email = etCorreo.getText().toString().trim();

            if (nombre.isEmpty() || especializacion.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean insertado = db.insertarAbogado(nombre, especializacion, telefono, email);

            if (insertado) {
                Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();

                // Limpiar campos
                txtNombre.setText("");
                txtTelefono.setText("");
                etEspecializacion.setText("");
                etCorreo.setText("");
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}