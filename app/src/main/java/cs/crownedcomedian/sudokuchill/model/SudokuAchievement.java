package cs.crownedcomedian.sudokuchill.model;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import androidx.core.content.res.ResourcesCompat;

import achievements.Achievement;
import achievements.AchievementUIState;
import cs.crownedcomedian.sudokuchill.R;

public class SudokuAchievement extends Achievement {
    private Drawable icon;

    public SudokuAchievement(Drawable icon, String title, String description) {
        super(title, description);

        this.icon = icon;
    }

    public SudokuAchievement(Drawable icon, String title, String description, AchievementUIState UIstate) {
        super(title, description, UIstate);

        this.icon = icon;
    }

    public Drawable getIcon(Context context) {
        switch (this.getUIState()) {
            case PARTLY_VISIBLE:
                Drawable ret = icon.mutate().getConstantState().newDrawable();
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);  //0 means grayscale
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                ret.setColorFilter(cf);

                return ret;
            case SECRET:
                return context.getDrawable(R.drawable.trophy);
            case HIDDEN:
                return null;
            default:
                return icon;
        }
    }
}
