package com.practice.mission1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
    }


    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;
        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        return cb.isChecked() ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        setChecked(cb.isChecked() ? false : true) ;
    }
}
