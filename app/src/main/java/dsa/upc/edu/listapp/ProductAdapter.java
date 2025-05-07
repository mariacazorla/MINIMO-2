package dsa.upc.edu.listapp;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dsa.upc.edu.listapp.store.Producto;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    private List<Producto> data = new ArrayList<>();

    public interface OnBuyClickListener {
        void onBuyClick(Producto p);
    }

    private OnBuyClickListener buyClickListener;

    public void setOnBuyClickListener(OnBuyClickListener listener) {
        this.buyClickListener = listener;
    }

    public void setData(List<Producto> d) {
        data = d;
        notifyDataSetChanged();
    }

    public Producto getProductAt(int pos) {
        return data.get(pos);
    }

    public void remove(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int pos) {
        Producto p = data.get(pos);
        holder.tvName.setText(p.getNombre());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "%.2f€", p.getPrecio()));
        holder.btnBuy.setOnClickListener(v -> {
            if (buyClickListener != null) buyClickListener.onBuyClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnBuy;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvProductName);
            tvPrice = v.findViewById(R.id.tvProductPrice);
            btnBuy = v.findViewById(R.id.btnBuy);
        }
    }
}
