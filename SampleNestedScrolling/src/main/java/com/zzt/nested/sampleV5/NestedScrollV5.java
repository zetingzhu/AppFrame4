package com.zzt.nested.sampleV5;

import android.content.Context;
import android.util.AttributeSet;

import com.zzt.nested.layoutview.NestedScrollFrameLayoutV5;

/**
 * @author: zeting
 * @date: 2021/4/1
 */
public class NestedScrollV5 extends NestedScrollFrameLayoutV5 {
    public NestedScrollV5(Context context) {
        super(context);
    }

    public NestedScrollV5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollV5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isParentOffset() {
        return true;
    }
}
