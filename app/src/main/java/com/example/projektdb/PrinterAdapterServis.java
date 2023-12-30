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

public class PrinterAdapterServis extends RecyclerView.Adapter<PrinterAdapterServis.ViewHolder>{
    Context context;
    //Deklaracija Liste koja će se prikazivati u Recycle View-u
    static ArrayList<Printeri> arrayList;
    static PrinterAdapterServis.OnItemClickListener onItemClickListener;

    public PrinterAdapterServis(Context context, ArrayList<Printeri> arrayList) {
        this.context = context; //predstavlja kontekst
        this.arrayList = arrayList; // Lista podataka koja ce se koristiti
    }
    @NonNull
    @Override
    public PrinterAdapterServis.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.printeri_list_servis, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PrinterAdapterServis.ViewHolder holder, int position) {
        // postavljanje teksta u TextView (title). arrayList.get(position) - pozicija.. .getTitle (metoda kreirana u Printeri.class)
        holder.title.setText(arrayList.get(position).getTitle());
        holder.subtitle.setText(arrayList.get(position).getContent());
        //Prilikom klika na red poziva se metoda onClick.
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //definicija reda koji ce se prikazati u Recycle View
            title = itemView.findViewById(R.id.list_item_title);
            subtitle = itemView.findViewById(R.id.list_item_subtitle);

            itemView.findViewById(R.id.btnInformatika).setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onBtnInformatikaClick(arrayList.get(position), position);
                    }
                }
            });

            itemView.findViewById(R.id.btnLDC).setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onBtnLDCClick(arrayList.get(position), position);
                    }
                }
            });
        }
    }
    /*Metoda 3.___________________________________________________________________________________________________________________*/

    //Metoda događanja nakon klika  na item u Recycle View-u
    public void setOnItemClickListener(PrinterAdapterServis.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Kada korisnik klikne na stavku u RecyclerView-u, implementacija ove metode će se pozvati
    public interface OnItemClickListener {
        void onClick(Printeri printeri);
        void onBtnLDCClick(Printeri printeri, int position);
        void onBtnInformatikaClick(Printeri printeri, int position);
        void onBtnServisClick(Printeri printeri, int position);
    }

    //______________________________________________________________________________________________
    //Za refresh nakon brisanja iz ArrayList-e


    //Medota vraća  broj elemenata u listi koji će biti prikazani u RecyclerView-u.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}