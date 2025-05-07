package dsa.upc.edu.listapp.store;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import dsa.upc.edu.listapp.R;

public class PartidaAdapter extends RecyclerView.Adapter<PartidaAdapter.ViewHolder> {

    public interface OnPartidaClickListener {
        void onClick(Partida partida);
        void onEliminarPartida(String idPartida, int position);
    }

    private List<Partida> partidas;
    private OnPartidaClickListener listener;

    public PartidaAdapter(List<Partida> partidas, OnPartidaClickListener listener) {
        this.partidas = partidas;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Partida partida = partidas.get(position);
        holder.tvPartida.setText("Partida #" + (position + 1));

        // Agregar un listener al botón de eliminar
        holder.btnEliminarPartida.setOnClickListener(v -> {
            listener.onEliminarPartida(partida.getId_partida(), position);
        });

        holder.itemView.setOnClickListener(v -> listener.onClick(partida));
    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPartida;
        Button btnEliminarPartida;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPartida = itemView.findViewById(R.id.tvPartidaId);
            btnEliminarPartida = itemView.findViewById(R.id.btnEliminarPartida);
        }
    }
}