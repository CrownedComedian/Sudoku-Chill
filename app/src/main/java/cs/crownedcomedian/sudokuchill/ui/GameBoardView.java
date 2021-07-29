package cs.crownedcomedian.sudokuchill.ui;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs.crownedcomedian.sudoku.Cell;
import cs.crownedcomedian.sudoku.GameBoard;
import cs.crownedcomedian.sudoku.generator.CompleteBoardGenerator;
import cs.crownedcomedian.sudokuchill.R;
import cs.crownedcomedian.sudokuchill.model.DataCache;

public class GameBoardView extends GridLayout {
    SingleSquareView selectedSquare = null;
    GameBoard gb;
    private List<Cell> noteSquares = new ArrayList();
    private List<Cell> conflictingSquares = new ArrayList();

    public GameBoardView(@NonNull Context context) {
        super(context);
        init();
    }

    public GameBoardView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBoardView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        SudokuActivity act = (SudokuActivity)getContext();
//        if(act.difficulty == getResources().getString(R.string.beginner_difficulty)) {
//            gb = DataCache.getInstance().beginnerGame;
//        } else if(act.difficulty == getResources().getString(R.string.easy_difficulty)) {
//            gb = DataCache.getInstance().easyGame;
//        } else if(act.difficulty == getResources().getString(R.string.medium_difficulty)) {
//            gb = DataCache.getInstance().mediumGame;
//        } else if(act.difficulty == getResources().getString(R.string.hard_difficulty)) {
//            gb = DataCache.getInstance().hardGame;
//        } else {
//            gb = DataCache.getInstance().expertGame;
//        }
//
//        if(gb == null) {
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            Handler handler = new Handler(Looper.getMainLooper());
//
//            executor.execute(() -> {
//                gb = new CompleteBoardGenerator().generateNew();
//
//                handler.post(() -> {
//                    onBoardGenerated();
//                });
//            });
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());

        setMeasuredDimension(size, size);

        for(int d = 0; d < getChildCount(); d++) {
            SingleSquareView s = (SingleSquareView)getChildAt(d);
            s.setWidth(size/(gb.SQROOT*gb.SQROOT));
            s.setHeight(size/(gb.SQROOT*gb.SQROOT));
        }
    }

    public void onBoardGenerated(GameBoard gameBoard) {
        Log.d("board ready", "~~~generated board");
        gb = gameBoard;
        ((SudokuActivity) getContext()).createValueBtns();
        this.setColumnCount(gb.SQROOT*gb.SQROOT);
        this.setRowCount(gb.SQROOT*gb.SQROOT);

        for(int i = 0; i < gb.SQROOT*gb.SQROOT; i++) {
            for(int j = 0; j < gb.SQROOT*gb.SQROOT; j++) {
                SingleSquareView cell = new SingleSquareView(getContext(), this);
                cell.setText(DataCache.getInstance().values.get(0));
                this.addView(cell);
            }
        }

        selectedSquare = (SingleSquareView) this.getChildAt(0);
        List<Cell> immutableCells = new ArrayList<>();

        for(int i = 0; i < gb.SQROOT*gb.SQROOT; i++) {
            for(int j = 0; j < gb.SQROOT*gb.SQROOT; j++) {
                if(gb.getSquare(i, j).getValue() != 0) {
                    immutableCells.add(new Cell(i, j));
                }
            }
        }

        loopCells(immutableCells);

        //TODO - do this one at a time with small delay intervals in random order for fun animation
//        for(Cell c : immutableCells) {
//            SingleSquareView v = (SingleSquareView) this.getChildAt(c.row*gb.SQROOT*gb.SQROOT +c.col);
//            v.setImmutable(gb.getSquare(c.row, c.col).getValue());
//        }
    }

    private void loopCells(List<Cell> cells) {
        Handler handler = new Handler();
        Collections.shuffle(cells);
        Iterator<Cell> itr = cells.iterator();
        Runnable runnableCode = new Runnable() {

            @Override
            public void run() {
                if(itr.hasNext()) {
                    Cell c = itr.next();
                    SingleSquareView v = (SingleSquareView) getChildAt(c.row*gb.SQROOT*gb.SQROOT +c.col);
                    v.setImmutable(gb.getSquare(c.row, c.col).getValue());
                    handler.postDelayed(this, 60);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.post(runnableCode);
//        int i = 0;
//
//        do {
//            //action
//            Log.d("i", "~~~val: " + i);
//        } while(i++ < cells.size());
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        invalidate();
//        canvas.drawRect(0, 0, 50, 50, paint);
//        paint.setColor(Color.parseColor("#387225"));
//        Paint mPaint = new Paint();
//        mPaint.setColor(Color.parseColor("#000ff0"));
//        canvas.drawRect(10, 10, getMeasuredWidth(), getMeasuredHeight(), paint);
//        canvas.drawLine(0, getMeasuredHeight() / 4, getMeasuredWidth(), getMeasuredHeight() / 4, mPaint);
//        canvas.drawLine(0, 2 * getMeasuredHeight() / 4, getMeasuredWidth(), 2 * getMeasuredHeight() / 4, mPaint);
//        canvas.drawLine(0, 3 * getMeasuredHeight() / 4, getMeasuredWidth(), 3 * getMeasuredHeight() / 4, mPaint);
//        canvas.drawLine(getMeasuredWidth() / 3, 0, getMeasuredWidth() / 3, getMeasuredHeight(), mPaint);
//        canvas.drawLine(2 * getMeasuredWidth() / 3, 0, 2 * getMeasuredWidth() / 3, getMeasuredHeight(), mPaint);
//    }

    public void setSelectedSquare(SingleSquareView ssv) {
        //TODO - deselect color transitions
        this.selectedSquare = ssv;
        //select color transition
    }

    public boolean setSelectedValue(int val) {
        selectedSquare.setValue(val);
        //TODO - return conflicting squares

        return true;
    }

    public void toggleGuess(int guessVal) {
        selectedSquare.toggleGuess(guessVal);
    }
}