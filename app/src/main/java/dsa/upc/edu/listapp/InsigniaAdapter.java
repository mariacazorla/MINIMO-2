package dsa.upc.edu.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import dsa.upc.edu.listapp.auth.Insignia;

public class InsigniaAdapter extends RecyclerView.Adapter<InsigniaAdapter.ViewHolder> {
    private List<Insignia> lista = new ArrayList<>();

    public void setLista(List<Insignia> data) {
        lista = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insignia_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Insignia i = lista.get(pos);
        h.tvNombre.setText(i.getName());
        Picasso.get().load(i.getAvatar()).into(h.imgInsignia);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgInsignia;
        TextView tvNombre;
        ViewHolder(View v) {
            super(v);
            imgInsignia = v.findViewById(R.id.imgInsignia);
            tvNombre    = v.findViewById(R.id.tvInsigniaNombre);
        }
    }
}
