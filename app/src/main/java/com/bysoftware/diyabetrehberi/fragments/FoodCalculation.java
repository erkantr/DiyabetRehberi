package com.bysoftware.diyabetrehberi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bysoftware.diyabetrehberi.adapter.FoodAdapter;
import com.bysoftware.diyabetrehberi.R;
import com.bysoftware.diyabetrehberi.model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FoodCalculation extends Fragment {
    RecyclerView recyclerView;
    View root;
    FoodAdapter foodAdapter;
    FirebaseAuth mAuth;
    List<Food> foods;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_food_calculation, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        //recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();
        foods = new ArrayList<>();
        readFoods();

        //foodAdapter = new FoodAdapter(getContext());
       // recyclerView.setAdapter(foodAdapter);

        return root;
    }

    public void readFoods(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://diyabet-rehberi-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Foods");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //foods.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    foods.add(food);

                }
                foodAdapter = new FoodAdapter(getContext(), foods);
                recyclerView.setAdapter(foodAdapter);
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener( getActivity(), new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}