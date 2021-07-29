package cs.crownedcomedian.sudokuchill.model.square;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cs.crownedcomedian.sudokuchill.ui.SingleSquareView;

public class GuessSquareState extends SquareState {
    private Set<Integer> guessValues;

    public GuessSquareState(SingleSquareView view) {
        super(view);
        guessValues = new HashSet<>(3*3);
    }

    @Override
    public void drawView(Canvas canvas) {
        //draw all those freakin' squares and guesses
        int line1 = view.getMeasuredWidth()*1/3;
        Paint guessPaint = new Paint();
        guessPaint.setTextSize(line1);

        // the display area.
        Rect areaRect = new Rect(0, 0, line1, line1);

        // draw the background style (pure color or image)
        Random rnd = new Random();
        guessPaint.setARGB(255, rnd.nextInt(56)+100, rnd.nextInt(56)+100, rnd.nextInt(56)+100);
        canvas.drawRect(areaRect, guessPaint);

        String pageTitle = "5";

        RectF bounds = new RectF(areaRect);
        // measure text width
        bounds.right = guessPaint.measureText(pageTitle, 0, pageTitle.length());
        // measure text height
        bounds.bottom = guessPaint.descent() - guessPaint.ascent();

        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;
    }

    @Override
    public void setValue(int value) {
        //toggle to value state
    }

    @Override
    public void toggleGuess(int guessIndex) {
        guessValues.add(guessIndex);
    }
}
