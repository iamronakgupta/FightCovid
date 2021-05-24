package com.example.fightcovid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fightcovid.daos.PostDao;
import com.example.fightcovid.daos.UserDaos;
import com.example.fightcovid.models.Posts;
import com.example.fightcovid.models.Users;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.auth.User;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
MaterialToolbar toolbar;

NavigationView navigationView;
FloatingActionButton floatingActionButton;
Dialog dialog;
private Task<DocumentSnapshot> snapshotTask;
private Users loginUser;
private FirebaseAuth mAuth;
ArrayList<String> tags=new ArrayList<>();
ArrayList<Posts> othersRequest;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar=findViewById(R.id.topAppBar);
        mRecyclerView=findViewById(R.id.recyclerView);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        firebaseFirestore=FirebaseFirestore.getInstance();

        documentReference=firebaseFirestore.collection("user").document(currentUser.getUid());
        documentReference.get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    try {
                        loginUser=documentSnapshot.toObject(Users.class);
                    }catch (Exception e){
                        Toast.makeText(HomePage.this,e.toString(),Toast.LENGTH_LONG).show();
                        Log.d("Homepage",e.toString() );
                    }

                }else{
                    Toast.makeText(HomePage.this,"Something Wrong with server",Toast.LENGTH_LONG).show();
                }


            }
        });
//        tags=loginUser.getTags();
        setRecyclerView();


        navigationView=findViewById(R.id.navigation);
        floatingActionButton = findViewById(R.id.floating_action_button);



        floatingActionButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                addRequestDialog();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                navigation();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener( ) {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.search){
                    search();
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener( ) {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.close){
                    closeNavigation();
                }
                item.setChecked(true);
                return false;
            }
        });
    }

    private void setRecyclerView() {
        tags.add("food");
        othersRequest=new ArrayList();

//        if(tags.size()!=0){
//            for(int i =0;i<=tags.size();i++){
                firebaseFirestore.collection("posts")
                        .whereEqualTo("food", true)
                        .get( )
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful( )) {
                                    for (QueryDocumentSnapshot document : task.getResult( )) {
                                        CollectionReference list = document.getReference( ).
                                                collection(loginUser.getLocation( ));
                                        Query query = list.orderBy("createdAt", Query.Direction.DESCENDING);
                                        FirestoreRecyclerOptions firestoreRecyclerOptions;
                                        list.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()){
                                                   List<DocumentSnapshot> posts=task.getResult().getDocuments();
                                                    for (int j=0;j<=posts.size();j++){
                                                        othersRequest.add(posts.get(j).toObject(Posts.class));
                                                    }
                                               }
                                            }
                                        });

                                                Log.d("Posts", document.getId( ) + " => " + document.getData( ));
                                    }
                                } else {
                                    Log.d("Posts", "Error getting documents: ", task.getException( ));
                                }
                            }
                        }) ;




//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PostAdapter(othersRequest);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void addRequestDialog() {
        Intent intent = new Intent(HomePage.this,AddRequest.class);
        intent.putExtra("User",loginUser);
        startActivity(intent);
    }

    private void closeNavigation() {
        navigationView.setVisibility(View.INVISIBLE);
    }

    private void navigation() {
//        drawerLayout.openDrawer(Gravity.LEFT);
        navigationView.setVisibility(View.VISIBLE);
    }

    private void search() {
        Toast.makeText(HomePage.this,"Search For Help",Toast.LENGTH_SHORT).show();
    }
}