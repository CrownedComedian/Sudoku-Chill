package cs.crownedcomedian.sudokuchill.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

import cs.crownedcomedian.sudokuchill.model.DataCache;
import cs.crownedcomedian.sudokuchill.model.square.ImmutableSquareState;
import cs.crownedcomedian.sudokuchill.model.square.SquareState;
import cs.crownedcomedian.sudokuchill.model.square.ValueSquareState;

public class SingleSquareView extends androidx.appcompat.widget.AppCompatTextView {
    private GameBoardView parent;
    private SquareState state;
    private final Paint textValuePaint = new Paint();
    private final Paint backgroundPaint = new Paint();
    public int[] textGradientColors = new int[]{
            DataCache.getInstance().activeTheme.baseTextColor,
            DataCache.getInstance().activeTheme.baseTextColor
    };

    public SingleSquareView(@NonNull Context context, GameBoardView parent) {
        super(context);
        init(parent);
    }

    public SingleSquareView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(parent);
    }

    public SingleSquareView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(parent);
    }

    private void init(GameBoardView parent) {
        this.parent = parent;
        this.state = new ValueSquareState(this);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this,TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setTextColor(textGradientColors[0]);

        textValuePaint.setColor(Color.parseColor("#454596"));
        textValuePaint.setTextAlign(Paint.Align.CENTER);

        this.setOnClickListener(this::onSelect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredWidth() > getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        } else {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
//        guessPaint.setColor(Color.WHITE);
//        canvas.drawText(pageTitle, bounds.left, bounds.top - guessPaint.ascent(), guessPaint);
        state.drawView(canvas);
        super.onDraw(canvas);
    }

    public void setImmutable(int finalVal) {
        this.state = new ImmutableSquareState(this, finalVal);
    }

    public void setValue(int val) {
        state.setValue(val);
    }

    public void toggleGuess(int guessIndex) {
        state.toggleGuess(guessIndex);
    }

    private void onSelect(View view) {
        parent.setSelectedSquare(this);
    }
}
