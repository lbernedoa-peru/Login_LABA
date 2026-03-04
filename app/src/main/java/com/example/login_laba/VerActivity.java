package com.example.login_laba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerActivity extends AppCompatActivity {

    TextView txtDni, txtNombre, txtEspecializacion, txtTelefono, etCorreo, txtBiografia;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtDni            = findViewById(R.id.txtDni);
        txtNombre         = findViewById(R.id.txtNombre);
        txtEspecializacion = findViewById(R.id.txtEspecializacion);
        txtTelefono       = findViewById(R.id.txtTelefono);
        etCorreo          = findViewById(R.id.etCorreo);
        txtBiografia      = findViewById(R.id.txtBiografia);
        btnVolver         = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> finish());

        int id = getIntent().getIntExtra("id", -1);

        if (id != -1) {
            cargarDatos(id);
        } else {
            Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDatos(int id) {
        DBHelper db = new DBHelper(this);
        Abogado abogado = db.obtenerAbogadoObjeto(id);

        if (abogado != null) {
            txtDni.setText(abogado.getDni());
            txtNombre.setText(abogado.getNombre());
            txtEspecializacion.setText(abogado.getEspecialidad());
            txtTelefono.setText(abogado.getTelefono());
            etCorreo.setText(abogado.getCorreo());
            txtBiografia.setText(abogado.getBiografia());
        } else {
            Toast.makeText(this, "No se encontró el abogado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}