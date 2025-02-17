package com.example.android_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonExit =  findViewById(R.id.buttonExit);
        Button butttonContinue =  findViewById(R.id.buttonContinue);
        ImageButton buttonConfig = findViewById(R.id.buttonConfig);
        ImageButton buttonInfo =  findViewById(R.id.buttonInfo);

        //Exit
        buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        //New game
        buttonStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Crear una nueva partida borrará tus datos guardados.");
                alertDialogBuilder
                        .setMessage("¿quieres continuar igualmente?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //EMPEZAR
                                startActivity(new Intent(MainActivity.this, Game.class));
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });

    }


}