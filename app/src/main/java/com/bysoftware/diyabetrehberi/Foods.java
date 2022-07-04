package com.bysoftware.diyabetrehberi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.bysoftware.diyabetrehberi.adapter.FoodAdapter;
import com.bysoftware.diyabetrehberi.fragments.AddFood;
import com.bysoftware.diyabetrehberi.fragments.Calculation;
import com.bysoftware.diyabetrehberi.model.Food;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
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

public class Foods extends AppCompatActivity {

    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    FirebaseAuth mAuth;
    LinearLayout linearLayout;
    List<Food> foods;
    public final int ID_FOOD_CALCULATION =1 ;
    public final int ID_CALCULATION =2;
    public final int ID_ADD_FOOD = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Foods.this));
        linearLayout = findViewById(R.id.main_content);
        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();
        foods = new ArrayList<>();
        readFoods();

        MeowBottomNavigation meowBottomNavigation = findViewById(R.id.bottomNavigation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_FOOD_CALCULATION, R.drawable.dish1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_CALCULATION,R.drawable.siringa));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_FOOD,R.drawable.ic_baseline_post_add_24));

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch(item.getId()){

                    case ID_FOOD_CALCULATION:
                        //fragment = new FoodCalculation();
                        Intent intent = new Intent(getApplicationContext(), Foods.class);
                        //startActivity(intent);
                        overridePendingTransition(0,0);
                        break;
                    case  ID_CALCULATION:

                        Intent intent1 = new Intent(getApplicationContext(), FoodAddActivity.class);
                       // startActivity(intent1);
                        overridePendingTransition(0,0);
                        fragment = new Calculation();
                        break;
                    case ID_ADD_FOOD:
                        Intent intent2 = new Intent(getApplicationContext(), Foods.class);
                       // startActivity(intent2);
                        overridePendingTransition(0,0);
                        fragment = new AddFood();
                        break;

                }
                //loadFragment(fragment);
            }
        });
        meowBottomNavigation.setCount(1, "10");
        meowBottomNavigation.show(2,true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
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
                foodAdapter = new FoodAdapter(Foods.this, foods);
                recyclerView.setAdapter(foodAdapter);
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener( Foods.this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(Foods.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}