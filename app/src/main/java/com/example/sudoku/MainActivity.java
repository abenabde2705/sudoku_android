package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private class Cellule {
        int value;
        boolean fixee;
        Button bt;

        public Cellule(int valInitial, MainActivity context) {
            value = valInitial;
            if (value != 0) fixee = true;
            else fixee = false;
            bt = new Button(context);
            if (fixee) bt.setText(String.valueOf(value));
            else bt.setTextColor(Color.RED);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fixee) return;
                    value++;
                    if (value > 9) value = 1;
                    bt.setText(String.valueOf(value));
                    if (correct()) {
                        tv.setText("asba its correct");
                        if (isGridFilledCorrectly()) {
                            afficherMessageJeuGagne();
                        }
                    } else {
                        tv.setText("There's a repeated digit");
                    }
                }
            });
        }
    }

    boolean complete(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if (tab[i][j].value==0)
                    return false;
            }
        }
        return true;
    }
    private boolean isGridFilledCorrectly() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tab[i][j].value == 0 || !correct(i, 0, i + 1, 9) || !correct(0, j, 9, j + 1) || !correct(3 * (i / 3), 3 * (j / 3), 3 * (i / 3) + 3, 3 * (j / 3) + 3)) {
                    return false; // Si une cellule est vide ou si une règle est violée, retourne faux
                }
            }
        }
        return true; // La grille est remplie correctement
    }

    private void afficherMessageJeuGagne() {
        tv.setText("Félicitations ! Jeu gagné !");
    }
    boolean correct(int i1,int j1,int i2,int j2)
    {
        boolean[] vu=new boolean[10];
        for(int i=0;i<9;i++) vu[i]=false;
        for(int i=i1;i<i2;i++)
        {
            for(int j=j1;j<j2;j++)
            {
                int valeur=tab[i][j].value;
                if(valeur !=0) {
                    if(vu[valeur]) return false;
                    vu[valeur]=true;
                }
            }
        }
        return true;
    }

    boolean correct() {
        // Vérifier les lignes
        for (int i = 0; i < 9; i++) {
            if (!correct(i, 0, i + 1, 9)) return false;
        }
        // Vérifier les colonnes
        for (int j = 0; j < 9; j++) {
            if (!correct(0, j, 9, j + 1)) return false;
        }
        // Vérifier les sous-grilles
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!correct(3 * i, 3 * j, 3 * i + 3, 3 * j + 3)) return false;
            }
        }
        return true;
    }

    Cellule[][] tab;
    TableLayout tl;

    TextView tv;
    LinearLayout linLay;

    // Méthode pour générer une grille de Sudoku basée sur le niveau de difficulté choisi
    // Méthode pour générer une grille de Sudoku basée sur le niveau de difficulté choisi
    private String generateSudokuGrid(String[][] niveau) {
        Random random = new Random();
        String[] grille = niveau[random.nextInt(niveau.length)];

        StringBuilder gridBuilder = new StringBuilder();
        for (String ligne : grille) {
            gridBuilder.append(ligne).append("\n"); // Ajouter un saut de ligne après chaque ligne de la grille
        }
        return gridBuilder.toString();
    }


    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer le niveau de difficulté passé depuis l'intent
        int niveau = getIntent().getIntExtra("niveau", 0);

        // Choisissez la grille correspondant au niveau choisi
        String[][] grilleChoisie;
        switch (niveau) {
            case 1:
                grilleChoisie = GrillesSudoku.NIVEAU_FACILE;
                break;
            case 2:
                grilleChoisie = GrillesSudoku.NIVEAU_MOYEN;
                break;
            case 3:
                grilleChoisie = GrillesSudoku.NIVEAU_DIFFICILE;
                break;
            default:
                grilleChoisie = GrillesSudoku.NIVEAU_FACILE; // Par défaut, choisissez le niveau facile
        }

        // Générer la grille Sudoku
        String input = generateSudokuGrid(grilleChoisie);

        // Affichage de la grille dans la TableLayout
        afficherGrille(input);
    }

    // Méthode pour afficher la grille de Sudoku
    // Méthode pour afficher la grille de Sudoku
    private void afficherGrille(String input) {
        tab = new Cellule[9][9];

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        String[] lignes = input.split("\n"); // Fractionner la grille en lignes

        for (int i = 0; i < 9; i++) {
            TableRow tr = new TableRow(this);
            String[] colonnes = lignes[i].split(" "); // Fractionner la ligne en colonnes en utilisant l'espace comme délimiteur

            for (int j = 0; j < 9; j++) {
                char c = colonnes[j].charAt(0);
                tab[i][j] = new Cellule(c == '?' ? 0 : Character.getNumericValue(c), this);
                tr.addView(tab[i][j].bt);
            }
            tableLayout.addView(tr);
        }

        tv = findViewById(R.id.textView);

        linLay = findViewById(R.id.linLayout);
        linLay.setOrientation(LinearLayout.VERTICAL);
    }


}
