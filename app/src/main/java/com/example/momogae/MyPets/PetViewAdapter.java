package com.example.momogae.MyPets;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.momogae.Login.SharedPreference;
import com.example.momogae.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PetViewAdapter extends RecyclerView.Adapter<PetViewAdapter.MyHolder>{

    ArrayList<PetModel> petModelList;
    Context context;
    String userID;

    public PetViewAdapter(ArrayList<PetModel> petModelList, Context context){
        this.petModelList = petModelList;
        this.context = context;
        userID = SharedPreference.getAttribute(context,"userID");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pet_main, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final int pos = position;
        PetModel myList = petModelList.get(position);
        holder.name.setText(myList.getPetName());
        holder.age.setText(myList.getPetAge());
        holder.species.setText(myList.getPetSpecies());
        holder.firstdate.setText(myList.getPetFirstDate());

        FirebaseStorage.getInstance().getReference("pet/" + userID + "/" + myList.getPetName() + "/profile").child("profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imageView); //파이어베이스상 경로에 이미지 등록되어있을시 이미지로드
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener(){ //홀더 이미지 클릭시 LargeActivity로 넘어감
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                Intent large_intent = new Intent(context, PetLargeActivity.class);
                large_intent.putExtra("position", pos);
                large_intent.putExtra("petName",myList.getPetName());
                context.startActivity(large_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (petModelList.size() == 0){ arr = 0;}
            else { arr = petModelList.size(); }
        }
        catch (Exception e) {}
        return arr;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, age, firstdate, species;
        ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.pet_profile);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            species = (TextView) itemView.findViewById(R.id.species);
            firstdate = (TextView) itemView.findViewById(R.id.firstdate);
        }
    }

}
