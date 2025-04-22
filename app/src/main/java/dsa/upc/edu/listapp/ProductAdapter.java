package dsa.upc.edu.listapp;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import java.util.*;
import dsa.upc.edu.listapp.store.Producto;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {
    private List<Producto> data = new ArrayList<>();
    public void setData(List<Producto> d){ data=d; notifyDataSetChanged(); }
    @Override public VH onCreateViewHolder(ViewGroup p,int vt){
        View v=LayoutInflater.from(p.getContext())
                .inflate(R.layout.row_product,p,false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(VH h,int i){
        Producto pr=data.get(i);
        h.name.setText(pr.getNombre());
        h.price.setText("€"+pr.getPrecio());
    }
    @Override public int getItemCount(){ return data.size(); }

    public Producto getProductAt(int position) {
        return data.get(position);
    }
    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
    static class VH extends RecyclerView.ViewHolder {
        TextView name,price;
        VH(View v){
            super(v);
            name=v.findViewById(R.id.tvProductName);
            price=v.findViewById(R.id.tvProductPrice);
        }
    }
}