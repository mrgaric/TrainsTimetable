package com.igordubrovin.trainstimetable.customView;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.igordubrovin.trainstimetable.R;

/**
 * Created by Игорь on 21.02.2017.
 */

public class CustomEditText extends EditText {

    private OnTextChangedListener listener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK){
            clearFocus();
        }*/
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0)
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_material, 0);
        else
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        if (listener != null) listener.onTextChangedListener(this, text.toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            Drawable drawable = this.getCompoundDrawables()[2];
            if (drawable != null) {
                Rect bounds = drawable.getBounds();
                int x = (int) event.getX();
                int y = (int) event.getY();
                final int fuzz = 10;
                Rect r = new Rect(this.getRight() - bounds.width() - fuzz, this.getPaddingTop() - fuzz,
                        this.getRight() - this.getPaddingRight() + fuzz,
                        this.getHeight() - this.getPaddingBottom() + fuzz);
                if (r.contains(x, y)){
                    this.setText("");
                    this.requestFocus();
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnTextChangedListener(OnTextChangedListener l){
        listener = l;
    }

    public interface OnTextChangedListener{
        void onTextChangedListener(View v, String s);
    }

}
