package cs.crownedcomedian.sudokuchill.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cs.crownedcomedian.sudoku.GameBoard;
import cs.crownedcomedian.sudokuchill.R;
import cs.crownedcomedian.sudokuchill.model.DataCache;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton achievementFab;
    Button newGameBtn;
    Button resumeBtn;
    RadioGroup difficultyBtns;
    int checkedDifficultyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCache.loadInstance(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        achievementFab = findViewById(R.id.achievement_fab);
        newGameBtn = findViewById(R.id.new_game_btn);
        resumeBtn = findViewById(R.id.resume_btn);
        difficultyBtns = findViewById(R.id.difficulty_btns);
        checkedDifficultyID = difficultyBtns.getCheckedRadioButtonId();
        updateResumeBtn();

        achievementFab.setOnClickListener(v -> {
            //TODO - achievement activity
        });
        newGameBtn.setOnClickListener(this::newGameClicked);
        resumeBtn.setOnClickListener(this::resumeClicked);
        difficultyBtns.setOnCheckedChangeListener(this::checkedDifficultyChanged);

        //if there is no game to resume, there is no need to display the resume button
        if(DataCache.getInstance().beginnerGame == null &&
           DataCache.getInstance().easyGame == null &&
           DataCache.getInstance().mediumGame == null &&
           DataCache.getInstance().hardGame == null &&
           DataCache.getInstance().expertGame == null) {
            resumeBtn.setVisibility(View.GONE);
        }
    }

    private void newGameClicked(View v) {
        //if there was a game in progress: prompt for confirmation
        if(resumeBtn.isEnabled()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("title");
            dialog.setMessage("message");
            dialog.setPositiveButton("ok", this::newGame);
            dialog.setNegativeButton("cancel", null);
            dialog.show();
        } else {
            newGame(null, 0);
        }
    }

    public void newGame(DialogInterface d, int i) {
        String selectedDifficulty = ((Button)findViewById(difficultyBtns.getCheckedRadioButtonId())).getText().toString();
        Intent newGame = new Intent(this, SudokuActivity.class);
        DataCache.getInstance().mediumGame = new GameBoard();
        newGame.putExtra(getResources().getString(R.string.difficulty), selectedDifficulty);
        startActivity(newGame);
    }

    private void resumeClicked(View v) {
        //TODO - resume game from save
        Intent newGame = new Intent(this, SudokuActivity.class);
        startActivity(newGame);
    }

    private void checkedDifficultyChanged(RadioGroup radioGroup, int i) {
        checkedDifficultyID = i;
        updateResumeBtn();
    }

    private void updateResumeBtn() {
        switch (checkedDifficultyID) {
            case R.id.beginner_btn:
                if(DataCache.getInstance().beginnerGame != null) {
                    resumeBtn.setEnabled(true);
                } else {
                    resumeBtn.setEnabled(false);
                }

                return;
            case R.id.easy_btn:
                if(DataCache.getInstance().easyGame != null) {
                    resumeBtn.setEnabled(true);
                } else {
                    resumeBtn.setEnabled(false);
                }

                return;
            case R.id.medium_btn:
                if(DataCache.getInstance().mediumGame != null) {
                    resumeBtn.setEnabled(true);
                } else {
                    resumeBtn.setEnabled(false);
                }

                return;
            case R.id.hard_btn:
                if(DataCache.getInstance().hardGame != null) {
                    resumeBtn.setEnabled(true);
                } else {
                    resumeBtn.setEnabled(false);
                }

                return;
            case R.id.expert_btn:
                if(DataCache.getInstance().expertGame != null) {
                    resumeBtn.setEnabled(true);
                } else {
                    resumeBtn.setEnabled(false);
                }

                return;
        }
    }
}