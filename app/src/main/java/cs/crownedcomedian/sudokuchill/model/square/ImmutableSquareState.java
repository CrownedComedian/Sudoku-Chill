package cs.crownedcomedian.sudokuchill.model.square;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import java.util.Random;

import cs.crownedcomedian.sudokuchill.model.DataCache;
import cs.crownedcomedian.sudokuchill.ui.SingleSquareView;

public class ImmutableSquareState extends SquareState {
    RectF f = new RectF(0, 0, view.getMeasuredWidth()/2, view.getMeasuredHeight()/2);
    Paint p = new Paint();
    Random rand = new Random();
    int backgroundColor;

    public ImmutableSquareState(SingleSquareView view, int finalVal) {
        super(view);
        view.setTypeface(view.getTypeface(), Typeface.BOLD);
        view.setText(DataCache.getInstance().values.get(finalVal));
        //stone animation here?  ...or drawView?
        setRawValue(finalVal);
        int colorFrom = Color.TRANSPARENT;
        int colorTo = DataCache.getInstance().activeTheme.immutableBackgroundColor;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500); // milliseconds
        colorAnimation.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

    private void setRawValue(int value) {
        view.setText(DataCache.getInstance().values.get(value));

        if(value != 0) {
            //animate new in
            view.textGradientColors[0] = DataCache.getInstance().activeTheme.baseTextColor;
            view.textGradientColors[1] = Color.TRANSPARENT;
            ValueAnimator v = animateText(view.getText().toString());
            v.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.textGradientColors[0] = Color.TRANSPARENT;
                }
            });
        }
    }

    private ValueAnimator animateText(String text) {
        Rect bounds = new Rect();
        view.getPaint().getTextBounds(text, 0, text.length(), bounds);
        float hypotenuse = (float)StrictMath.hypot(bounds.width(), bounds.height());
        ValueAnimator growingRadius = ValueAnimator.ofFloat(1f, hypotenuse/2f);

        growingRadius.setDuration(300);
        growingRadius.addUpdateListener(updatedAnimation -> {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
            view.getPaint().setShader(
                    new RadialGradient(
                            view.getMeasuredWidth() /2f, view.getMeasuredHeight() /2f,
                            (float) updatedAnimation.getAnimatedValue(), view.textGradientColors,
                            new float[]{1f, 0f}, Shader.TileMode.CLAMP));
        });

        growingRadius.start();
        return growingRadius;
    }
    
    @Override
    public void drawView(Canvas canvas) {
//        Log.d("draw im bak", "~~~drawing...");
//        backgroundColor = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
//        p.setColor(backgroundColor);
//        canvas.drawRect(f, p);
    }

    @Override
    public void setValue(int value) { return; }

    @Override
    public void toggleGuess(int guessIndex) { return; }
}
