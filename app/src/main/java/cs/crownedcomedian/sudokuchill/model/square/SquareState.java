package cs.crownedcomedian.sudokuchill.model.square;

import android.graphics.Canvas;

import cs.crownedcomedian.sudokuchill.ui.SingleSquareView;

public abstract class SquareState {
    public final SingleSquareView view;

    public SquareState(SingleSquareView view) {
        this.view = view;
    }

    public abstract void drawView(Canvas canvas);
//    public abstract void onSelect();
    public abstract void setValue(int value);
    public abstract void toggleGuess(int guessIndex);
}
