package com.moezbhatti.counter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.github.adnansm.timelytextview.TimelyView;
import com.moezbhatti.counter.R;

/**
 * @author Moez Bhatti
 */
public class CounterView extends LinearLayout {
    private final String TAG = "CounterView";

    public enum Direction {
        UP,
        DOWN;
    }

    private int mCount = 0;
    private Direction mDirection = Direction.UP;

    public CounterView(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        addDigit();
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public void handleTouch() {
        if (mDirection == Direction.UP) {
            increment();
        } else {
            decrement();
        }
    }

    public void increment() {
        int[] digitsStart = getDigitsFromNumber(mCount);
        mCount++;
        int[] digits = getDigitsFromNumber(mCount);

        if (digits.length > digitsStart.length) {
            addDigit();
        }

        for (int i = 0; i < digits.length; i++) {
            int from;
            int to;

            if (digits.length > digitsStart.length) {
                if (i == 0) {
                    from = 0;
                    to = digits[i];
                } else {
                    from = digitsStart[i - 1];
                    to = digits[i];
                }
            } else {
                from = digitsStart[i];
                to = digits[i];
            }

            ((TimelyView) getChildAt(i).findViewById(R.id.digit)).animate(from, to).start();
        }
    }

    public void decrement() {
        if (mCount == 0) {
            Toast.makeText(getContext(), R.string.number_bottom_warning, Toast.LENGTH_SHORT).show();
            return;
        }

        int[] digitsStart = getDigitsFromNumber(mCount);
        mCount--;
        int[] digits = getDigitsFromNumber(mCount);

        if (digits.length < digitsStart.length) {
            removeViewAt(digits.length);
        }

        for (int i = 0; i < digits.length; i++) {
            int from = digitsStart[i];
            int to = digits[i];

            ((TimelyView) getChildAt(i).findViewById(R.id.digit)).animate(from, to).start();
        }
    }

    private int[] getDigitsFromNumber(int count) {
        String s = Integer.toString(count);

        int[] digits = new int[s.length()];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(Character.toString(s.charAt(i)));
        }

        return digits;
    }

    private void addDigit() {
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        View view = inflate(getContext(), R.layout.digit, null);
        view.setLayoutParams(lp);
        TimelyView timelyView = (TimelyView) view.findViewById(R.id.digit);
        timelyView.animate(0).setDuration(0).start();

        addView(view);
    }

    public int getCount() {
        return mCount;
    }
}