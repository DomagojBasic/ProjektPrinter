package com.example.projektdb;

import android.content.Context;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.ViewHolder> {

    //Contex - pruža informacije o trenutnom stanju aplikacije i omogućava pristup resursima, kao što su stringovi, slike, itd.
    Context context;
    //Deklaracija Liste koja će se prikazivati u Recycle View-u
    static ArrayList<Printeri> arrayList;
    //Koristi ce se za definiranje akcije prilikom odabira na item u Recycle View-u
    static OnItemClickListener onItemClickListener;
    static boolean showButtons;

    public PrinterAdapter(Context context, ArrayList<Printeri> arrayList, boolean showButtons) {
        this.context = context; //predstavlja kontekst
        this.arrayList = arrayList; // Lista podataka koja ce se koristiti
        PrinterAdapter.showButtons = showButtons;
    }

    public PrinterAdapter(Context context, ArrayList<Printeri> arrayList) {
        this.context = context; //predstavlja kontekst
        this.arrayList = arrayList; // Lista podataka koja ce se koristiti
        PrinterAdapter.showButtons = true;
    }


    /* 1. metoda - Ova metoda je obrazac koji se ponavlja u Recycle Viewv - za svaki red.
    * Metoda postavlja Layout(okvir) u kojem ce se prikazivati neki podatci
    * Kreiranje novog View holdera*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = LayoutInflater.from(context).inflate(R.layout.printeri_objekti_list_item, parent, false);
        return new ViewHolder(view);
    }
    /*1. metoda ________________________________________________________________________________________________________*/


    /*2. metoda - Ovom metodom ubacujemo podatke u Layout */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // postavljanje teksta u TextView (title). arrayList.get(position) - pozicija.. .getTitle (metoda kreirana u Printeri.class)
        holder.title.setText(arrayList.get(position).getTitle());
       holder.subtitle.setText(arrayList.get(position).getContent());
       //holder.checkBoxInformatika.setText(arrayList.get(position).getContent());
       //holder.checkBoxServis.setText(arrayList.get(position).getContent());
      // holder.checkBoxLDC.setText(arrayList.get(position).getContent());
        //Prilikom klika na red poziva se metoda onClick.
       holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));

       Printeri p = arrayList.get(position);



       if(p.isCheckBoxServis()) {
           holder.checkBoxServis.setVisibility(View.INVISIBLE);

           RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.checkBoxLDC.getLayoutParams();
           params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

           holder.checkBoxLDC.setLayoutParams(params); //causes layout update
       } else if(p.isCheckBoxLDC()) {
           holder.checkBoxLDC.setVisibility(View.GONE);
           holder.spinner.setVisibility(View.VISIBLE);

       } else if(p.isCheckBoxInformatika()) {
           holder.checkBoxInformatika.setVisibility(View.GONE);
       }
    }

    /*2. metoda __________________________________________________________________________________________________________*/



    /*Metoda 3. Koristi za pristup i manipulaciju title i subtitle TextView elementima unutar svakog pojedinačnog reda u RecyclerView-u.*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        Button checkBoxInformatika,checkBoxLDC, checkBoxServis;
        Spinner spinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //definicija reda koji ce se prikazati u Recycle View
            title = itemView.findViewById(R.id.list_item_title);
            subtitle = itemView.findViewById(R.id.list_item_subtitle);
            checkBoxInformatika = itemView.findViewById(R.id.checkBoxInformatika);
            checkBoxServis = itemView.findViewById(R.id.checkBoxServis);
            checkBoxLDC = itemView.findViewById(R.id.checkBoxLDC);
            spinner = itemView.findViewById(R.id.spinner);

            spinner.setVisibility(View.INVISIBLE);

            if(!showButtons) {
                checkBoxInformatika.setVisibility(View.GONE);
                checkBoxLDC.setVisibility(View.GONE);
                checkBoxServis.setVisibility(View.GONE);
            }


            itemView.findViewById(R.id.list_item_title).setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onClick(arrayList.get(position));
                    }
                }
            });




            itemView.findViewById(R.id.checkBoxLDC).setOnClickListener(v -> {




                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onCheckLDC(arrayList.get(position),position); // za btn LDC u mainu
                    }
                }
            });

        }
    }
    /*Metoda 3.___________________________________________________________________________________________________________________*/

    /*Metoda 3. Koristi za pristup i manipulaciju title i subtitle TextView elementima unutar svakog pojedinačnog reda u RecyclerView-u.*/


    /*Metoda 3.___________________________________________________________________________________________________________________*/

    //Medota vraća  broj elemenata u listi koji će biti prikazani u RecyclerView-u.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //______________________________________________________________________________________________

    //Metoda događanja nakon klika  na item u Recycle View-u
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Kada korisnik klikne na stavku u RecyclerView-u, implementacija ove metode će se pozvati
    public interface OnItemClickListener {
        void onClick(Printeri printeri);

        void onCheckLDC(Printeri printeri, int position);

    }

    //______________________________________________________________________________________________
    //Za refresh nakon brisanja iz ArrayList-e

    public void updateData(ArrayList<Printeri> updatedList) {
    }




}