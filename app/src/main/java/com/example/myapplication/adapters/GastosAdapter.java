package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.Gasto;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.GastoViewHolder> {
    private List<Gasto> listaGastos;
    private OnItemLongClickListener longClickListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public GastosAdapter(List<Gasto> listaGastos, OnItemLongClickListener longClickListener) {
        this.listaGastos = listaGastos;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = listaGastos.get(position);
        holder.tvDescripcion.setText(gasto.getDescripcion());
        holder.tvCantidad.setText(String.format("%.2f€", gasto.getCantidad()));
        holder.tvFecha.setText(dateFormat.format(gasto.getFecha()));
        holder.tvCategoria.setText(gasto.getCategoria());

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvCantidad, tvFecha, tvCategoria;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tv_nombre_gasto);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad_gasto);
            tvFecha = itemView.findViewById(R.id.tv_fecha_gasto);
            tvCategoria = itemView.findViewById(R.id.tv_categoria_gasto);
        }
    }


    public void actualizarDatos(List<Gasto> nuevosGastos) {
        this.listaGastos = nuevosGastos;
        notifyDataSetChanged();
    }
}
