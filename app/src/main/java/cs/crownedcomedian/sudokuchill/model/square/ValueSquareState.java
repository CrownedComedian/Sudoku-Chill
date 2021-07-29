package cs.crownedcomedian.sudokuchill.model.square;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;

import cs.crownedcomedian.sudokuchill.model.DataCache;
import cs.crownedcomedian.sudokuchill.ui.SingleSquareView;

public class ValueSquareState extends SquareState {

    public ValueSquareState(SingleSquareView view) {
        super(view);
    }

    @Override
    public void drawView(Canvas canvas) {
        //I dunno
    }

    @Override
    public void setValue(int value) {
        if(view.getText().toString() != DataCache.getInstance().values.get(0)) {
            //animate old out
            view.textGradientColors[0] = Color.TRANSPARENT;
            view.textGradientColors[1] = DataCache.getInstance().activeTheme.baseTextColor;
            ValueAnimator v = animateText(view.getText().toString());
            v.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    setRawValue(value);
                }
            });
        } else {
            setRawValue(value);
        }
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

    @Override
    public void toggleGuess(int guessIndex) {
        //toggle to guess state
        view.setText(DataCache.getInstance().values.get(0));
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
}
