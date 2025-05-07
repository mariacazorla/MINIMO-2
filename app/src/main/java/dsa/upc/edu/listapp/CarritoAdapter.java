package dsa.upc.edu.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dsa.upc.edu.listapp.store.Producto;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.VH> {
    private List<Producto> data = new ArrayList<>();

    public interface OnRemoveClick {
        void onRemove(Producto p, int pos);
    }
    private OnRemoveClick removeListener;

    public void setOnRemoveClickListener(OnRemoveClick l) {
        this.removeListener = l;
    }

    public void setData(List<Producto> d) {
        data = d;
        notifyDataSetChanged();
    }
    public List<Producto> getData() {
        return data;
    }
    public void remove(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_carrito, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Producto p = data.get(position);
        holder.tvName.setText(p.getNombre());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "%.2f€", p.getPrecio()));
        holder.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onRemove(p, position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnRemove;
        VH(View v){
            super(v);
            tvName    = v.findViewById(R.id.tvCartProductName);
            tvPrice   = v.findViewById(R.id.tvCartProductPrice);
            btnRemove = v.findViewById(R.id.btnRemove);
        }
    }
}

