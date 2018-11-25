package com.example.qihang.bpm_hw3.custom;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.qihang.bpm_hw3.R;


public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private Drawable[] mCompoundDrawables;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mCompoundDrawables = getCompoundDrawables();
        mClearDrawable = getResources().getDrawable(R.drawable.clear);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());

        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setClearIconVisible(s.toString().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mClearDrawable != null) {
                if (getWidth() - getCompoundPaddingRight() < event.getX() && getWidth() - getPaddingRight() > event.getX()) {
                    setText("");
                    setClearIconVisible(false);
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setClearIconVisible(boolean visible) {
        if (mClearDrawable == null)
            return;
        setCompoundDrawablesWithIntrinsicBounds(mCompoundDrawables[0], mCompoundDrawables[1], visible ? mClearDrawable : null, mCompoundDrawables[3]);
    }
}