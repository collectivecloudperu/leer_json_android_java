package com.example.miapp.adaptadores;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.miapp.R;
import com.example.miapp.modelos.MyAppModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyAppModel> myappModelArrayList;

    public MyAppAdapter(Context ctx, ArrayList<MyAppModel> myappModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.myappModelArrayList = myappModelArrayList;
    }

    @Override
    public MyAppAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyAppAdapter.MyViewHolder holder, int position) {

        holder.nombre.setText(myappModelArrayList.get(position).getNombre());
        holder.precio.setText(myappModelArrayList.get(position).getPrecio());
        holder.stock.setText(myappModelArrayList.get(position).getStock());
        Picasso.get().load(myappModelArrayList.get(position).getImagen()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return myappModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, precio, stock;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre);
            precio = (TextView) itemView.findViewById(R.id.precio);
            stock = (TextView) itemView.findViewById(R.id.stock);
            img = (ImageView) itemView.findViewById(R.id.img);
        }

    }

}
