package com.heitorzanetti.chamatest.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.heitorzanetti.chamatest.R;


/**
 * Created by heitorzc on 18/01/17.
 */
public class OpenSansTextView extends TextView {


    public OpenSansTextView(Context context) {
        super(context);
        setTypeFace(context);
    }

    public OpenSansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public OpenSansTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }


    private void setTypeFace(Context context){

        Typeface tDefault = this.getTypeface();

        int style = (tDefault != null) ? tDefault.getStyle() : Typeface.NORMAL;
        String font = (style == Typeface.BOLD) ? "fonts/opensansbold.ttf" : "fonts/opensans.ttf";

        Typeface regular  = Typeface.createFromAsset(context.getAssets(), font);

        this.setTypeface(regular);

    }


    public void setTextAnimated(String text){
        this.setText(text);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);

        this.setAnimation(fadeIn);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {

        String content = (text != null) ? text.toString() : getContext().getString(R.string.data_blank_placeholder);
        super.setText(content, type);

    }
}
