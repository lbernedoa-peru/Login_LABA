package com.example.login_laba;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class EditarActivity extends AppCompatActivity {

    EditText txtDni, txtNombre, txtTelefono, etCorreo, txtBiografia;
    AutoCompleteTextView etEspecializacion;
    Button btnActualizar;

    DBHelper db;
    int idAbogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        db = new DBHelper(this);

        txtDni = findViewById(R.id.txtDni);
        txtNombre = findViewById(R.id.txtNombre);
        etEspecializacion = findViewById(R.id.etEspecializacion);
        txtTelefono = findViewById(R.id.txtTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        txtBiografia = findViewById(R.id.txtBiografia);
        btnActualizar = findViewById(R.id.btnActualizar);

        idAbogado = getIntent().getIntExtra("id", -1);

        if (idAbogado == -1) {
            Toast.makeText(this, "Error al cargar el abogado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatos();

        btnActualizar.setOnClickListener(v -> confirmarActualizacion());
    }

    private void cargarDatos() {

        Cursor cursor = db.obtenerAbogadoPorId(idAbogado);

        if (cursor != null && cursor.moveToFirst()) {

            txtDni.setText(cursor.getString(1));
            txtNombre.setText(cursor.getString(2));
            etEspecializacion.setText(cursor.getString(3));
            txtTelefono.setText(cursor.getString(4));
            etCorreo.setText(cursor.getString(5));
            txtBiografia.setText(cursor.getString(6));
        }
    }

    private void confirmarActualizacion() {

        new AlertDialog.Builder(this)
                .setTitle("Confirmar actualización")
                .setMessage("¿Deseas actualizar los datos?")
                .setPositiveButton("Sí", (d, i) -> actualizarDatos())
                .setNegativeButton("No", null)
                .show();
    }

    private void actualizarDatos() {

        String dni = txtDni.getText().toString().trim();
        String nombre = txtNombre.getText().toString().trim();
        String especializacion = etEspecializacion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String biografia = txtBiografia.getText().toString().trim();

        // VALIDACIONES
        if (dni.isEmpty() || nombre.isEmpty() || especializacion.isEmpty() ||
                telefono.isEmpty() || correo.isEmpty() || biografia.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Campos obligatorios")
                    .setMessage("Todos los campos deben completarse")
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }


        if (dni.length() != 8) {
            txtDni.setError("El DNI debe tener 8 dígitos");
            txtDni.requestFocus();
            return;
        }

        if (telefono.length() != 9) {
            txtTelefono.setError("El teléfono debe tener 9 dígitos");
            txtTelefono.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Correo no válido");
            etCorreo.requestFocus();
            return;
        }
        if (db.existeDni(dni, idAbogado)) {
            txtDni.setError("Este DNI ya está registrado");
            return;
        }

        // CONFIRMAR ACTUALIZACIÓN
        new AlertDialog.Builder(this)
                .setTitle("Confirmar actualización")
                .setMessage("¿Deseas actualizar los datos?")
                .setPositiveButton("Sí", (d, i) -> {

                    boolean resultado = db.actualizarAbogado(
                            idAbogado,
                            dni,
                            nombre,
                            especializacion,
                            telefono,
                            correo,
                            biografia
                    );

                    if (resultado) {
                        new AlertDialog.Builder(this)
                                .setTitle("Éxito")
                                .setMessage("Datos actualizados correctamente")
                                .setPositiveButton("Aceptar", (dialog, which) -> finish())
                                .show();
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}