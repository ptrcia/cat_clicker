package com.example.android_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_app.RoomDB.AppDataBase;
import com.example.android_app.RoomDB.QueryTest;

public class Game extends AppCompatActivity {

    TextView textScore;
    ScoreManager scoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonStore = findViewById(R.id.buttonStore);
        ImageButton buttonClickScore = findViewById(R.id.buttonClickeableCat);
        textScore = findViewById(R.id.scoreText);
        scoreManager = ScoreManager.getInstance();

        buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicker-> ", "Se ha hecho click en tienda");

                //Open a fragment to the store
                Fragment fragment = ActiveUpgradeFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Log.d("Clicker->", "Transacción de fragmento en progreso");

                transaction.replace(R.id.container_layout, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        buttonClickScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reference Script Score
                Log.d("Clicker-> ", "Se ha hecho click");
                scoreManager.ClickImage();
                UpdateScoreText();
            }
        });

        //Consulta de prueba para imprimir
        AppDataBase db = AppDataBase.getDatabase(this);
        QueryTest queryTest = new QueryTest(db, this);
        queryTest.userStatsQuery();
    }


    public void UpdateScoreText(){
        textScore.setText(scoreManager.getScore());
    }
}