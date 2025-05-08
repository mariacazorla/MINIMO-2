package dsa.upc.edu.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dsa.upc.edu.listapp.store.Objeto;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.VH> {
    private List<Objeto> data = new ArrayList<>();

    public void setData(List<Objeto> lista) {
        data = lista;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_inventario, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Objeto o = data.get(position);
        holder.tvName.setText(o.getNombre());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "%d€", o.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        VH(View v) {
            super(v);
            tvName  = v.findViewById(R.id.tvItemName);
            tvPrice = v.findViewById(R.id.tvItemPrice);
        }
    }
}

