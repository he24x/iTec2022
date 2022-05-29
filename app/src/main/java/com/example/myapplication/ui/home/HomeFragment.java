package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.notificationHandler.CHANNEL_1_ID;

import android.Manifest;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class HomeFragment extends Fragment {

    TextView textView;
    Button generator;
    ImageView qrImage;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    char c;
    String QR,QRConfirmation;
    NotificationManagerCompat notificationManager;
    String user,qr,ticketsString,result;
    int tickets;
    Handler mHandler;

    private static final long START_TIME_IN_MILLIS = 10000;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        qrImage= root.findViewById(R.id.imageView);
        generator= root.findViewById(R.id.generator);
        mAuth= FirebaseAuth.getInstance();
        textView=root.findViewById(R.id.tickets);
        user=mAuth.getCurrentUser().getUid();
        notificationManager= NotificationManagerCompat.from(getContext());
        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Generate new ticket");
                dialog.setMessage("Are you sure that you want to generate a new ticket?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        minusTicket();
                        sendNotification();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.create().show();
            }
        });
        qrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationQR();
                startTimer();
            }
        });
        mHandler = new Handler();
        startRepeatingTask();
        return root;
    }

    private void generateQR(){
        QR=generatorQR(6);
        QRConfirmation = generatorQR(5);
        mDatabase.child("Users").child(user).child("qrcode").setValue(user+QR);
        mDatabase.child("Users").child(user).child("confirmation").setValue(user + QRConfirmation);
    }

    private void displayQR(){
        mDatabase.child("Users").child(user).child("qrcode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String qr = snapshot.getValue(String.class);
                QRGEncoder qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, 500);
                Bitmap bitmap = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void confirmationQR(){
        mDatabase.child("Users").child(user).child("confirmation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String qr = snapshot.getValue(String.class);
                QRGEncoder qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, 500);
                Bitmap bitmap = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void minusTicket(){
        generateQR();
        displayQR();
        mDatabase.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketsString = snapshot.getValue(String.class);
                tickets=Integer.valueOf(ticketsString);
                if(tickets==0){
                    generator.setClickable(false);
                }else if(tickets>=1){
                    generator.setClickable(true);
                    tickets=tickets-1;
                    ticketsString=String.valueOf(tickets);
                    mDatabase.child("Users").child(user).child("tickets").setValue(ticketsString);
                }
                afisare();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String generatorQR(int lenght){
        char[] chars="QWERTYUIOPASDFGHJKLZXCVBNM1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<lenght;i++){
            c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public void afisare(){
        mDatabase.child("Users").child(user).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketsString = snapshot.getValue(String.class);
                textView.setText("You have "+ticketsString+" tickets left!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }else{
            afisare();
        }
        imageDisplay();
        super.onStart();
    }

    public void sendNotification(){
        String title="Ticket generated";
        String message="A new ticket was generated!";

        Notification notification = new NotificationCompat.Builder(getContext(),CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentText(message)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mDatabase.child("Users").child(user).child("confirmation").setValue("b");
                mDatabase.child("Users").child(user).child("qrcode").setValue("c");
                resetTimer();
                imageDisplay();
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText(timeLeftFormatted);
    }
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = this.getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    public void imageDisplay(){
        mDatabase.child("Users").child(user).child("qrcode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qr=snapshot.getValue(String.class);
                assert qr != null;
                if(qr.equals("a")) {
                    confirmationQR();
                    startTimer();
                }else if(qr.equals("c")){
                    QRGEncoder qrgEncoder = new QRGEncoder("No QR!", null, QRGContents.Type.TEXT, 500);
                    Bitmap bitmap = qrgEncoder.getBitmap();
                    qrImage.setImageBitmap(bitmap);
                    textView.setText("No QR!");
                }else{
                    displayQR();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                imageDisplay(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 5000);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

}