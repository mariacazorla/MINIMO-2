package dsa.upc.edu.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dsa.upc.edu.listapp.store.Objeto;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.VH> {

    private static final String BASE_URL = "http://10.0.2.2:8080"; // Emulador: localhost

    private List<Objeto> data = new ArrayList<>();

    public void setData(List<Objeto> lista) {
        this.data = lista;
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

        // Setear nombre y precio
        holder.tvName.setText(o.getNombre());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "%.2f€", (double) o.getPrecio()));

        // Cargar imagen con Glide
        String imageUrl = o.getImageUrl(); // Asegúrate de que este método exista en Objeto
        if (imageUrl == null || imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.img) // Imagen por defecto
                    .into(holder.imgProduct);
        } else {
            String fullUrl = BASE_URL + imageUrl;
            Glide.with(holder.itemView.getContext())
                    .load(fullUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.img)
                    .into(holder.imgProduct);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvItemName);
            tvPrice = v.findViewById(R.id.tvItemPrice);
            imgProduct = v.findViewById(R.id.imgProduct); // Debe existir en row_inventario.xml
        }
    }
}

