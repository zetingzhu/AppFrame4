package com.zzt.nested.layoutview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * @author: zeting
 * @date: 2021/4/1
 * 四个布局滚动调用
 */
public class CalendarFrameLayout extends FrameLayout implements NestedScrollingParent2 {
    private static final String TAG = CalendarFrameLayout.class.getSimpleName();

    // 最上面布局
    View topView;
    int topHeight;
    // 上层view
    View aboveView;
    int aboveHeight;
    // 下层view
    View belowView;
    int belowHeight;
    // 在下方滚动view
    View bottomView;

    private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    public CalendarFrameLayout(@NonNull Context context) {
        super(context);
    }

    public CalendarFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >= 3) {
            if (getChildCount() > 1) {
                aboveView = getChildAt(1);
            }
            if (getChildCount() > 0) {
                belowView = getChildAt(0);
            }
            if (getChildCount() > 2) {
                bottomView = getChildAt(2);
            }
            if (getChildCount() > 3) {
                topView = getChildAt(3);
            }
        } else {
            throw new IllegalArgumentException("这里的布局必须是大于三个");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (aboveView != null) {
            aboveHeight = aboveView.getMeasuredHeight();
        }
        if (belowView != null) {
            belowHeight = belowView.getMeasuredHeight();
        }
        if (topView != null) {
            topHeight = topView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int measuredWidth = getMeasuredWidth();

        aboveView.layout(0, topHeight + belowHeight - aboveHeight, measuredWidth, topHeight + belowHeight);
        belowView.layout(0, topHeight, measuredWidth, topHeight + belowHeight);
        bottomView.layout(0, topHeight + belowHeight, measuredWidth, bottomView.getMeasuredHeight() + topHeight + belowHeight);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.d(TAG, "滚动距离： dy:" + dy + " getScrollY:" + getScrollY() + "  aboveHeight:" + aboveHeight + "  belowHeight:" + belowHeight + "  topHeight:" + topHeight +
                "\n(topHeight + belowHeight - aboveHeight):" + (topHeight + belowHeight - aboveHeight));
        boolean hideTop = dy > 0 && getScrollY() < (belowHeight - aboveHeight);
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }

        // 设置最上面估计布局
        if (topView != null) {
            topView.layout(0, getScrollY(), getMeasuredWidth(), getScrollY() + topHeight);
        }

        if (getScrollY() == (belowHeight - aboveHeight)) {
            if (aboveView != null) {
                aboveView.setVisibility(VISIBLE);
            }
        } else {
            if (aboveView != null) {
                aboveView.setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > belowHeight - aboveHeight) {
            y = belowHeight - aboveHeight;
        }
        super.scrollTo(x, y);
    }
}
