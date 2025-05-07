package dsa.upc.edu.listapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.VH> {
    private List<String> sections = new ArrayList<>();
    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(String sectionName);
    }

    public SectionAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    /** Recibe directamente la lista de nombres de secciones */
    public void setDataFromStrings(List<String> names) {
        this.sections = new ArrayList<>(names);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_section, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        String name = sections.get(position);
        holder.tv.setText(name);
        holder.itemView.setOnClickListener(v -> listener.onClick(name));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvSectionName);
        }
    }
}


