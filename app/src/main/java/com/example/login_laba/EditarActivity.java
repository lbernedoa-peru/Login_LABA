package com.example.login_laba;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditarActivity extends AppCompatActivity {

    EditText txtDni, txtNombre, txtTelefono, etCorreo, txtBiografia;
    AutoCompleteTextView etEspecializacion;
    Button btnActualizar, btnSeleccionarImagen;
    ImageView imgAbogado;

    DBHelper db;
    int idAbogado;

    private Uri imagenUri = null;
    private byte[] imagenActual = null; // imagen que ya tenía el abogado
    private ActivityResultLauncher<Intent> seleccionarImagenLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        db = new DBHelper(this);

        txtDni              = findViewById(R.id.txtDni);
        txtNombre           = findViewById(R.id.txtNombre);
        etEspecializacion   = findViewById(R.id.etEspecializacion);
        txtTelefono         = findViewById(R.id.txtTelefono);
        etCorreo            = findViewById(R.id.etCorreo);
        txtBiografia        = findViewById(R.id.txtBiografia);
        btnActualizar       = findViewById(R.id.btnActualizar);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        imgAbogado          = findViewById(R.id.imgAbogado);

        // BLOQUEAR ESPACIOS EN CORREO
        etCorreo.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    if (source.toString().contains(" ")) return "";
                    return null;
                }
        });

        // LAUNCHER IMAGEN
        seleccionarImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imagenUri = result.getData().getData();
                        imgAbogado.setImageURI(imagenUri);
                    }
                }
        );

        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            seleccionarImagenLauncher.launch(intent);
        });

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

            // Cargar imagen existente
            imagenActual = cursor.getBlob(7);
            if (imagenActual != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenActual, 0, imagenActual.length);
                imgAbogado.setImageBitmap(bitmap);
            }

            cursor.close();
        }
    }

    private byte[] imagenABytes() {
        // Si seleccionó nueva imagen la convertimos
        if (imagenUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                return stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Si no seleccionó nueva imagen, conservamos la anterior
        return imagenActual;
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

        String dni             = txtDni.getText().toString().trim();
        String nombre          = txtNombre.getText().toString().trim();
        String especializacion = etEspecializacion.getText().toString().trim();
        String telefono        = txtTelefono.getText().toString().trim();
        String correo          = etCorreo.getText().toString().trim();
        String biografia       = txtBiografia.getText().toString().trim();

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

        // ACTUALIZAR DIRECTO SIN SEGUNDA ALERTA
        boolean resultado = db.actualizarAbogado(
                idAbogado,
                dni,
                nombre,
                especializacion,
                telefono,
                correo,
                biografia,
                imagenABytes()
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
    }
}