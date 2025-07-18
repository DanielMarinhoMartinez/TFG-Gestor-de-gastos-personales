package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.GastosAdapter;
import com.example.myapplication.database.GastoDBHelper;
import com.example.myapplication.models.Gasto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalGastos, tvEmptyMessage, tvFiltroAplicado;
    private RecyclerView rvGastos;
    private FloatingActionButton fabAgregarGasto;
    private Button btnFiltrarMes, btnQuitarFiltro, btnFiltrarCategoria;
    private List<Gasto> listaGastos;
    private List<Gasto> listaGastosFiltrados = new ArrayList<>();
    private boolean estaFiltrado = false;
    private Calendar calendarioFiltro = Calendar.getInstance();
    private GastosAdapter adapter;
    private GastoDBHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalGastos = findViewById(R.id.tv_total_gastos);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        rvGastos = findViewById(R.id.rv_gastos);
        fabAgregarGasto = findViewById(R.id.fab_agregar_gasto);
        btnFiltrarMes = findViewById(R.id.btn_filtrar_mes);
        btnQuitarFiltro = findViewById(R.id.btn_quitar_filtro);
        btnFiltrarCategoria = findViewById(R.id.btn_filtrar_categoria);
        tvFiltroAplicado = findViewById(R.id.tv_filtro_aplicado);

        dbHelper = new GastoDBHelper(this);
        listaGastos = dbHelper.obtenerGastos();

        adapter = new GastosAdapter(listaGastos, position -> mostrarDialogoOpciones(position));
        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        rvGastos.setAdapter(adapter);

        actualizarVista();

        fabAgregarGasto.setOnClickListener(v -> mostrarDialogoAgregarGasto(null, -1));

        btnFiltrarMes.setOnClickListener(v -> {
            final Calendar hoy = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioFiltro.set(Calendar.YEAR, year);
                        calendarioFiltro.set(Calendar.MONTH, month);
                        calendarioFiltro.set(Calendar.DAY_OF_MONTH, 1);
                        filtrarPorMes(calendarioFiltro.getTime());
                    },
                    hoy.get(Calendar.YEAR),
                    hoy.get(Calendar.MONTH),
                    hoy.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setOnShowListener(dialog -> {
                Button positive = dpd.getButton(DatePickerDialog.BUTTON_POSITIVE);
                Button negative = dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE);
                if (positive != null)
                    positive.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
                if (negative != null)
                    negative.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
            });
            dpd.show();
        });

        btnFiltrarCategoria.setOnClickListener(v -> {
            final String[] categorias = {"Comida", "Transporte", "Ocio", "Salud", "Otros"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Selecciona una categoría");
            builder.setItems(categorias, (dialog, which) -> {
                String categoriaSeleccionada = categorias[which];
                filtrarPorCategoria(categoriaSeleccionada);
            });
            builder.show();
        });

        btnQuitarFiltro.setOnClickListener(v -> {
            estaFiltrado = false;
            adapter.actualizarDatos(listaGastos);
            tvFiltroAplicado.setVisibility(View.GONE);
            actualizarVista();
        });

        Button btnTema = findViewById(R.id.btn_toggle_tema);
        btnTema.setOnClickListener(v -> {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                btnTema.setText("Modo Oscuro");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                btnTema.setText("Modo Claro");
            }
        });
    }

    private void filtrarPorCategoria(String categoria) {
        listaGastosFiltrados.clear();
        for (Gasto gasto : listaGastos) {
            if (categoria.equalsIgnoreCase(gasto.getCategoria())) {
                listaGastosFiltrados.add(gasto);
            }
        }
        estaFiltrado = true;
        adapter.actualizarDatos(listaGastosFiltrados);
        tvFiltroAplicado.setText("Filtrado por categoría: " + categoria);
        tvFiltroAplicado.setVisibility(View.VISIBLE);
        actualizarVista();
    }

    private void mostrarDialogoAgregarGasto(final Gasto gastoExistente, final int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle(gastoExistente == null ? "Agregar Gasto" : "Editar Gasto");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_gasto, null);
        final EditText etDescripcion = viewInflated.findViewById(R.id.et_descripcion);
        final EditText etCantidad = viewInflated.findViewById(R.id.et_cantidad);
        final TextView tvFecha = viewInflated.findViewById(R.id.tv_fecha);
        final Spinner spinnerCategoria = viewInflated.findViewById(R.id.spinner_categoria);

        String[] categorias = {"Comida", "Transporte", "Ocio", "Salud", "Otros"};
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);

        final Calendar calendario = Calendar.getInstance();
        if (gastoExistente != null) {
            etDescripcion.setText(gastoExistente.getDescripcion());
            etCantidad.setText(String.valueOf(gastoExistente.getCantidad()));
            calendario.setTime(gastoExistente.getFecha());
            int categoriaIndex = Arrays.asList(categorias).indexOf(gastoExistente.getCategoria());
            if (categoriaIndex != -1) {
                spinnerCategoria.setSelection(categoriaIndex);
            }
        }

        tvFecha.setText(dateFormat.format(calendario.getTime()));
        tvFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, año, mes, día) -> {
                        calendario.set(Calendar.YEAR, año);
                        calendario.set(Calendar.MONTH, mes);
                        calendario.set(Calendar.DAY_OF_MONTH, día);
                        tvFecha.setText(dateFormat.format(calendario.getTime()));
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.setOnShowListener(dialog -> {
                Button positive = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
                Button negative = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
                if (positive != null)
                    positive.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
                if (negative != null)
                    negative.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
            });

            datePickerDialog.show();
        });

        builder.setView(viewInflated);

        builder.setPositiveButton(gastoExistente == null ? "Agregar" : "Guardar", (dialog, which) -> {
            String descripcion = etDescripcion.getText().toString().trim();
            String cantidadStr = etCantidad.getText().toString().trim();
            String categoria = spinnerCategoria.getSelectedItem() != null ? spinnerCategoria.getSelectedItem().toString() : "Otros";

            if (descripcion.isEmpty() || cantidadStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) return;

            Date fecha = calendario.getTime();

            if (gastoExistente == null) {
                Gasto nuevoGasto = new Gasto(descripcion, cantidad, fecha, categoria);
                if (dbHelper.agregarGasto(nuevoGasto)) {
                    listaGastos.clear();
                    listaGastos.addAll(dbHelper.obtenerGastos());
                }
            } else {
                gastoExistente.setDescripcion(descripcion);
                gastoExistente.setCantidad(cantidad);
                gastoExistente.setFecha(fecha);
                gastoExistente.setCategoria(categoria);
                listaGastos.set(posicion, gastoExistente);
            }

            adapter.actualizarDatos(estaFiltrado ? listaGastosFiltrados : listaGastos);
            actualizarVista();
            Snackbar.make(rvGastos, gastoExistente == null ? "Gasto añadido" : "Gasto actualizado", Snackbar.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (positive != null)
            positive.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
        if (negative != null)
            negative.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
    }

    private void mostrarDialogoOpciones(final int posicion) {
        final CharSequence[] opciones = {"Editar", "Eliminar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                mostrarDialogoAgregarGasto(listaGastos.get(posicion), posicion);
            } else if (which == 1) {
                AlertDialog confirmDialog = new AlertDialog.Builder(this)
                        .setTitle("¿Eliminar gasto?")
                        .setMessage("¿Estás seguro de que deseas eliminar este gasto?")
                        .setPositiveButton("Sí", (dialogInterface, i) -> {
                            Gasto gasto = listaGastos.get(posicion);
                            if (dbHelper.eliminarGasto(gasto.getId())) {
                                listaGastos.remove(posicion);
                                adapter.actualizarDatos(estaFiltrado ? listaGastosFiltrados : listaGastos);
                                actualizarVista();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();

                confirmDialog.setOnShowListener(dialogInterface -> {
                    Button positive = confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negative = confirmDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    if (positive != null)
                        positive.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
                    if (negative != null)
                        negative.setTextColor(ContextCompat.getColor(this, R.color.dialog_button_color));
                });

                confirmDialog.show();
            }
        });
        builder.show();
    }

    private void actualizarVista() {
        List<Gasto> fuente = estaFiltrado ? listaGastosFiltrados : listaGastos;
        if (fuente.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvGastos.setVisibility(View.GONE);
            tvTotalGastos.setText("Total Gastos: 0€");
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvGastos.setVisibility(View.VISIBLE);
            double total = 0;
            for (Gasto gasto : fuente) {
                total += gasto.getCantidad();
            }
            tvTotalGastos.setText((estaFiltrado ? "Total filtrado: " : "Total Gastos: ") + String.format("%.2f", total) + "€");
        }
    }

    private void filtrarPorMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int mes = cal.get(Calendar.MONTH);
        int año = cal.get(Calendar.YEAR);

        listaGastosFiltrados.clear();
        for (Gasto gasto : listaGastos) {
            Calendar gCal = Calendar.getInstance();
            gCal.setTime(gasto.getFecha());
            if (gCal.get(Calendar.MONTH) == mes && gCal.get(Calendar.YEAR) == año) {
                listaGastosFiltrados.add(gasto);
            }
        }

        estaFiltrado = true;
        adapter.actualizarDatos(listaGastosFiltrados);
        tvFiltroAplicado.setText("Filtrado por mes: " + (mes + 1) + "/" + año);
        tvFiltroAplicado.setVisibility(View.VISIBLE);
        actualizarVista();
    }
}
