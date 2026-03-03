package com.example.login_laba;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono, txtDni, txtBiografia;
    AutoCompleteTextView etCorreo, etEspecializacion;
    Button btnGuardar;
    DBHelper db;

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

        db = new DBHelper(this);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtDni = findViewById(R.id.txtDni);
        txtBiografia = findViewById(R.id.txtBiografia);
        etEspecializacion = findViewById(R.id.etEspecializacion);
        etCorreo = findViewById(R.id.etCorreo);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Lista especialización
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

        // Autocompletar correo
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

        // BOTÓN GUARDAR
        btnGuardar.setOnClickListener(v -> validarYGuardar());
    }

    private void validarYGuardar() {

        String dni = txtDni.getText().toString().trim();
        String nombre = txtNombre.getText().toString().trim();
        String especializacion = etEspecializacion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String email = etCorreo.getText().toString().trim();
        String biografia = txtBiografia.getText().toString().trim();

        // Validar campos vacíos
        if (dni.isEmpty() || nombre.isEmpty() || especializacion.isEmpty()
                || telefono.isEmpty() || email.isEmpty() || biografia.isEmpty()) {

            mostrarAlerta("Campos incompletos", "Debe completar todos los campos.");
            return;
        }

        // Validar DNI
        if (dni.length() != 8) {
            mostrarAlerta("DNI inválido", "El DNI debe tener exactamente 8 dígitos.");
            return;
        }

        // Validar Teléfono
        if (telefono.length() != 9) {
            mostrarAlerta("Teléfono inválido", "El teléfono debe tener exactamente 9 dígitos.");
            return;
        }

        // Validar nombre
        if (nombre.length() < 3) {
            mostrarAlerta("Nombre inválido", "El nombre debe tener al menos 3 letras.");
            return;
        }

        // Validar correo
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarAlerta("Correo inválido", "Ingrese un correo electrónico válido.");
            return;
        }

        // Validar biografía
        if (biografia.length() < 10) {
            mostrarAlerta("Biografía muy corta", "La biografía debe tener mínimo 10 caracteres.");
            return;
        }

        boolean insertado = db.insertarAbogado(
                dni,
                nombre,
                especializacion,
                telefono,
                email,
                biografia
        );

        if (insertado) {

            new AlertDialog.Builder(this)
                    .setTitle("Registro exitoso")
                    .setMessage("El abogado fue registrado correctamente.")
                    .setCancelable(false)
                    .setPositiveButton("Ir al inicio", (dialog, which) -> {

                        Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    })
                    .show();

        } else {
            mostrarAlerta("Error", "No se pudo registrar el abogado.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}