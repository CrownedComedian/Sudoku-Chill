package cs.crownedcomedian.sudokuchill.model;

import android.content.Context;
import android.os.Handler;
import android.view.View;

public class ClickCountListener implements View.OnClickListener {
    public interface SingleClickCallback {
        void onSingleClick(View v);
    }

    public interface DoubleClickCallback {
        void onDoubleClick(View v);
    }

    private boolean isWaiting = false;
    private int counter = 0;

    private final SingleClickCallback singleClickListener;
    private final DoubleClickCallback doubleClickListener;

    public ClickCountListener(Context context) {
        singleClickListener = (SingleClickCallback)context;
        doubleClickListener = (DoubleClickCallback)context;
    }

    @Override
    public void onClick(View v) {
        counter++;

        if(!isWaiting) {
            isWaiting = true;

            int WAIT_TIME = 300;
            new Handler().postDelayed(() -> {
                isWaiting = false;
                notifyListeners(v);

                counter = 0;
            }, WAIT_TIME);
        }
    }

    private void notifyListeners(View v) {
        switch (counter) {
            case 2:
                doubleClickListener.onDoubleClick(v);
            case 1:
                singleClickListener.onSingleClick(v);
        }
    }
}
