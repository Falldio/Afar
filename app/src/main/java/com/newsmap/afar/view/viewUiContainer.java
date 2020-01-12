package com.newsmap.afar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.newsmap.afar.R;


//ui容器类
public class viewUiContainer extends ConstraintLayout {
    public viewUiContainer(Context context) {
        this(context, null);
    }

    public viewUiContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public viewUiContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_ui_container, this, true);
    }
}
