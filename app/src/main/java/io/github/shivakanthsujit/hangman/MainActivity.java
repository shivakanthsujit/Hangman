package io.github.shivakanthsujit.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private String[] words = {"ABCDE", "TABLET", "APPLE", "BANANA", "ORANGE"};
    private Random rand = new Random();
    private String currWord = "";
    private LinearLayout wordLayout;
    private TextView[] charViews;

    private ImageView bodyParts;
    private int numParts=6;
    private int currPart;
    private int numChars;
    private int numCorr;

    private GridView letters;
    private LetterAdapter ltrAdapt;
    private int score = 0;
    private int bestScore = 0;
    private TextView sc, bc;
    private int[] bp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordLayout = (LinearLayout)findViewById(R.id.word);
        bodyParts = (ImageView)findViewById(R.id.hangman);
        letters = (GridView)findViewById(R.id.letters);
        sc = (TextView)findViewById(R.id.score);
        bc = (TextView)findViewById(R.id.bestScore);
        bc.setText("Best Score: 0");
        bp = new int[numParts];
        score = 6;
        bp[0] = R.drawable.android_hangman_head;
        bp[1] = R.drawable.android_hangman_body;
        bp[2] = R.drawable.android_hangman_arm1;
        bp[3] = R.drawable.android_hangman_arm2;
        bp[4] = R.drawable.android_hangman_leg1;
        bp[5] = R.drawable.android_hangman_leg2;
        playGame();

    }
    private void playGame() {
        String newWord = words[rand.nextInt(words.length)];
        while(newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
        currWord = newWord;
        currPart=0;
        numChars=currWord.length();
        numCorr=0;
        charViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText(""+currWord.charAt(c));


            charViews[c].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.parseColor("#00000000"));
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[c]);
        }

        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);
    }

    public void checkLetter(View view) {

        EditText txtname = (EditText)findViewById(R.id.charL);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtname.getWindowToken(), 0);
        String ltr      =  txtname.getText().toString();
        char letterChar = ltr.charAt(0);
        boolean correct = false;
        for(int k = 0; k < currWord.length(); k++) {
            if(currWord.charAt(k)==letterChar){
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        txtname.setText("");
        letters.getChildAt(letterChar-65).setEnabled(false);
        letters.getChildAt(letterChar-65).setBackgroundResource(R.drawable.letter_down);

        if(correct){
            if(numCorr==numChars){
                disableBtns();
                if(score > bestScore)
                {
                    bestScore = score;
                    bc.setText("Best Score: " + bestScore);
                }
                sc.setText("Your Score: "+score);
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle(":D");
                winBuild.setMessage("You win!\n\nThe answer was:\n\n"+currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                score = 6;
                                sc.setText("Your Score: ");
                                MainActivity.this.playGame();
                            }});
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }});
                winBuild.show();
            }
        }
        else if(currPart<numParts){
            bodyParts.setImageResource(bp[currPart]);
            currPart++;
            score--;
        }
        else{
            disableBtns();
            if(score > bestScore)
            {
                bestScore = score;
                bc.setText("Best Score: " + bestScore);
            }
            sc.setText("Your Score: "+score);
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle(":(");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            score = 6;
                            sc.setText("Your Score: ");
                            MainActivity.this.playGame();
                        }});
            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }});
            loseBuild.show();
        }
    }

    public void disableBtns(){
        int numLetters = letters.getChildCount();
        for(int l=0; l<numLetters; l++){
            letters.getChildAt(l).setEnabled(false);
        }
    }

}
