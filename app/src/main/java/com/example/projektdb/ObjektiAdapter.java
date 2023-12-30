package com.example.projektdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ObjektiAdapter extends RecyclerView.Adapter<ObjektiAdapter.ViewHolder> {

    Context context;
    static ArrayList<Objekti> arrayListObjekti;
    static OnItemClickListener onItemClickListener;
    public ObjektiAdapter(Context context, ArrayList<Objekti> arrayList){
        this.context = context; //predstavlja kontekst
       this.arrayListObjekti = arrayList;
    }


    @NonNull
    @Override
    public ObjektiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.objekti_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjektiAdapter.ViewHolder holder, int position) {
        holder.title.setText(arrayListObjekti.get(position).getTitle());
        holder.subtitle.setText(arrayListObjekti.get(position).getContent());
        //Prilikom klika na red poziva se metoda onClick.
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayListObjekti.get(position)));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, subtitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //definicija reda koji ce se prikazati u Recycle View
            title = itemView.findViewById(R.id.list_item_title);
            subtitle = itemView.findViewById(R.id.list_item_subtitle);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(Objekti objekt);
    }
    @Override
    public int getItemCount() {
        return arrayListObjekti.size();
    }

}
