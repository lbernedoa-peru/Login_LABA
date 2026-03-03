package com.example.login_laba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity {

    ListView listaAbogados;
    EditText txtBuscar;
    Button btnBuscar, btnAnterior, btnSiguiente, btnLimpiar;
    RadioButton rbNombre, rbId, rbDni;
    TextView txtPagina;

    DBHelper db;

    ArrayList<Integer> listaIds = new ArrayList<>();
    ArrayList<String> lista = new ArrayList<>();
    ArrayAdapter<String> adapter;

    int paginaActual = 1;
    int registrosPorPagina = 5;
    int totalPaginas = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        db = new DBHelper(this);

        listaAbogados = findViewById(R.id.listaAbogados);
        txtBuscar = findViewById(R.id.txtBuscar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        txtPagina = findViewById(R.id.txtPagina);

        rbNombre = findViewById(R.id.rbNombre);
        rbId = findViewById(R.id.rbId);
        rbDni = findViewById(R.id.rbDni);

        cargarDatos("");

        btnBuscar.setOnClickListener(v -> {
            paginaActual = 1;
            cargarDatos(txtBuscar.getText().toString());
        });

        btnLimpiar.setOnClickListener(v -> {
            txtBuscar.setText("");
            rbNombre.setChecked(false);
            rbId.setChecked(false);
            rbDni.setChecked(false);

            paginaActual = 1;
            cargarDatos("");
        });

        btnAnterior.setOnClickListener(v -> {
            if (paginaActual > 1) {
                paginaActual--;
                cargarDatos(txtBuscar.getText().toString());
            }
        });

        btnSiguiente.setOnClickListener(v -> {
            if (paginaActual < totalPaginas) {
                paginaActual++;
                cargarDatos(txtBuscar.getText().toString());
            }
        });

        listaAbogados.setOnItemClickListener((parent, view, position, id) -> {

            int idAbogado = listaIds.get(position);
            String datos = lista.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Opciones")
                    .setMessage(datos)
                    .setPositiveButton("Ver", null)
                    .setNeutralButton("Editar", (d, i) -> {

                        Intent intent = new Intent(ListarActivity.this, EditarActivity.class);
                        intent.putExtra("id", idAbogado);
                        startActivity(intent);

                    })
                    .setNegativeButton("Eliminar", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos(txtBuscar.getText().toString());
    }

    private void cargarDatos(String textoBusqueda) {

        lista.clear();
        listaIds.clear();

        Cursor cursor;

        if (textoBusqueda.isEmpty()) {
            cursor = db.obtenerAbogados();
        } else {
            if (rbNombre.isChecked()) {
                cursor = db.buscarPorNombre(textoBusqueda);
            } else if (rbDni.isChecked()) {
                cursor = db.buscarPorDni(textoBusqueda);
            } else if (rbId.isChecked()) {
                cursor = db.buscarPorId(textoBusqueda);
            } else {
                cursor = db.obtenerAbogados();
            }
        }

        int totalRegistros = cursor.getCount();

        if (totalRegistros == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Resultado")
                    .setMessage("No se encontraron registros")
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

        totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);

        int inicio = (paginaActual - 1) * registrosPorPagina;
        int fin = inicio + registrosPorPagina;

        int contador = 0;

        if (cursor.moveToFirst()) {
            do {
                if (contador >= inicio && contador < fin) {

                    listaIds.add(cursor.getInt(0));

                    String datos =
                            "ID: " + cursor.getInt(0) + "\n" +
                                    "DNI: " + cursor.getString(1) + "\n" +
                                    "Nombre: " + cursor.getString(2) + "\n" +
                                    "Especialidad: " + cursor.getString(3);

                    lista.add(datos);
                }
                contador++;
            } while (cursor.moveToNext());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listaAbogados.setAdapter(adapter);

        txtPagina.setText("Página " + paginaActual + " de " + totalPaginas);
    }
}