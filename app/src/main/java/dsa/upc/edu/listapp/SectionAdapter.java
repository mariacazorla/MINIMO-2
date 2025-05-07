package dsa.upc.edu.listapp;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import java.util.*;
import dsa.upc.edu.listapp.store.Seccion;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.VH> {
    private List<Seccion> data = new ArrayList<>();
    private OnClickListener listener;
    public interface OnClickListener { void onClick(Seccion s); }
    public SectionAdapter(OnClickListener l){ this.listener = l; }
    public void setData(List<Seccion> d){ data=d; notifyDataSetChanged(); }
    @Override public VH onCreateViewHolder(ViewGroup p,int vt){
        View v=LayoutInflater.from(p.getContext())
                .inflate(R.layout.row_section,p,false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(VH h,int i){
        Seccion s=data.get(i);
        h.tv.setText(s.getNombre());
        h.itemView.setOnClickListener(x-> listener.onClick(s));
    }
    @Override public int getItemCount(){ return data.size(); }
    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v){ super(v); tv=v.findViewById(R.id.tvSectionName); }
    }
}

