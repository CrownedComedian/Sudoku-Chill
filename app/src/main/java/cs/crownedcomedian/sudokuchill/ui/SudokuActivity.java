package cs.crownedcomedian.sudokuchill.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;
import androidx.core.widget.TextViewCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs.crownedcomedian.sudoku.GameBoard;
import cs.crownedcomedian.sudoku.generator.ChistleStrategy;
import cs.crownedcomedian.sudoku.generator.CompleteBoardGenerator;
import cs.crownedcomedian.sudokuchill.R;
import cs.crownedcomedian.sudokuchill.model.ClickCountListener;
import cs.crownedcomedian.sudokuchill.model.DataCache;

public class SudokuActivity extends AppCompatActivity
                            implements ClickCountListener.SingleClickCallback,
                                       ClickCountListener.DoubleClickCallback {
    GameBoardView gbv;
    LinearLayout valueBtns;
    String difficulty;
    private boolean guessMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "~~~create");
        setContentView(R.layout.activity_sudoku);

        gbv = findViewById(R.id.game_board);
        valueBtns = findViewById(R.id.board_btns);
        difficulty = getIntent().getStringExtra(getResources().getString(R.string.difficulty));
        GameBoard gb = null;

        if(difficulty == getResources().getString(R.string.beginner_difficulty)) {
            gb = DataCache.getInstance().beginnerGame;
        } else if(difficulty == getResources().getString(R.string.easy_difficulty)) {
            gb = DataCache.getInstance().easyGame;
        } else if(difficulty == getResources().getString(R.string.medium_difficulty)) {
            gb = DataCache.getInstance().mediumGame;
        } else if(difficulty == getResources().getString(R.string.hard_difficulty)) {
            gb = DataCache.getInstance().hardGame;
        } else {
            gb = DataCache.getInstance().expertGame;
        }

        if(gb == null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                //TODO - set gb to the new board
                Log.d("generating board", "~~~generating board...");
                GameBoard freshGB = new ChistleStrategy().generateNew();

                handler.post(() -> {
                    gbv.onBoardGenerated(freshGB);
                });
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "~~~start");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.sudoku_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.theme_btn:

                return true;
            case R.id.achievements_btn:
                //TODO - implement achievements activity
//                intent = new Intent(this, SearchActivity.class);
//                startActivity(intent);
                return true;
            default:
                Log.d("menu clicked", "~~~what?");
                return super.onOptionsItemSelected(item);
        }
    }

    public void createValueBtns() {
        ClickCountListener dj = new ClickCountListener(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        for(int i = 1; i <= gbv.gb.SQROOT*gbv.gb.SQROOT; i++) {
            Button btn = new Button(this);
            btn.setId(i);
            btn.setPadding(2, 2, 2, 2 );
            btn.setMinWidth(12);
            btn.setLayoutParams(p);
            btn.setAutoSizeTextTypeWithDefaults(AppCompatButton.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn.setText(DataCache.getInstance().values.get(i));
            btn.setOnClickListener(dj);
            valueBtns.addView(btn);
        }
    }

    @Override
    public void onSingleClick(View v) {
        Button btn = (Button) v;
        int i = 0, id;

        do {
            id = valueBtns.getChildAt(i++).getId();
        } while (id != btn.getId());

        if(guessMode) {
            gbv.toggleGuess(i);
        } else {
            gbv.setSelectedValue(i);
        }
    }

    @Override
    public void onDoubleClick(View v) {
        guessMode = !guessMode;

        if(guessMode) {
            for(int i = 0; i < valueBtns.getChildCount(); i++) {
                Button btn = (Button) valueBtns.getChildAt(i);
                btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE);
                btn.setTextSize(gbv.getMeasuredWidth()/(float)Math.pow(gbv.gb.SQROOT, 3));
            }
        } else {
            for(int i = 0; i < valueBtns.getChildCount(); i++) {
                Button btn = (Button) valueBtns.getChildAt(i);
                btn.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "~~~resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "~~~pause");
//        DataCache.saveInstance(this);
    }
}