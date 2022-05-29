package com.example.myapplication.ui.slideshow;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlideshowFragment extends Fragment {
    CardView cardView, cardView1, cardView2, cardView3;
    Button button;
    LinearLayout linearLayout, feedback;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String user, ticketsString;
    int tickets;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        button = root.findViewById(R.id.button);
        linearLayout = root.findViewById(R.id.linearlayout);
        cardView = root.findViewById(R.id.cardview);
        cardView1 = root.findViewById(R.id.cardview1);
        cardView2 = root.findViewById(R.id.cardview2);
        cardView3 = root.findViewById(R.id.cardview3);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1();
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2();
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3();
            }
        });
        return root;
    }

    public void dialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Buying tickets?");
        dialog.setMessage("Are you sure that you want to buy 1 ticket?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ticketsString = snapshot.getValue(String.class);
                        tickets = Integer.valueOf(ticketsString);
                        tickets = tickets + 1;
                        ticketsString = String.valueOf(tickets);
                        databaseReference.child("Users").child(user).child("tickets").setValue(ticketsString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.create().show();
    }

    public void dialog1() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Buying tickets?");
        dialog.setMessage("Are you sure that you want to buy 5 tickets?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ticketsString = snapshot.getValue(String.class);
                        tickets = Integer.valueOf(ticketsString);
                        tickets = tickets + 5;
                        ticketsString = String.valueOf(tickets);
                        databaseReference.child("Users").child(user).child("tickets").setValue(ticketsString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.create().show();
    }

    public void dialog2() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Buying tickets?");
        dialog.setMessage("Are you sure that you want to buy 10 tickets?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ticketsString = snapshot.getValue(String.class);
                        tickets = Integer.valueOf(ticketsString);
                        tickets = tickets + 10;
                        ticketsString = String.valueOf(tickets);
                        databaseReference.child("Users").child(user).child("tickets").setValue(ticketsString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.create().show();
    }

    public void dialog3() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Buying tickets?");
        dialog.setMessage("Are you sure that you want to buy 15 tickets?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ticketsString = snapshot.getValue(String.class);
                        tickets = Integer.valueOf(ticketsString);
                        tickets = tickets + 15;
                        ticketsString = String.valueOf(tickets);
                        databaseReference.child("Users").child(user).child("tickets").setValue(ticketsString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.create().show();
    }

}