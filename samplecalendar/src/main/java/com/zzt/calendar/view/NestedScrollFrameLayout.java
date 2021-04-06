package com.zzt.calendar.view;


import android.content.Context;
import android.os.Build;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * @author: zeting
 * @date: 2021/4/1
 * Nested scroll relative layout.
 */

public abstract class NestedScrollFrameLayout extends FrameLayout
        implements NestedScrollingChild, NestedScrollingParent {
    // widget
    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private NestedScrollingParentHelper nestedScrollingParentHelper;

    // data
    private boolean isBeingDragged;
    private int swipeDir;
    private float oldY;
    private int lastOffsetY;
    private float touchSlop;

    private static final int DIR_TOP = 1;
    private static final int DIR_BOTTOM = -1;
    private static final int DIR_NULL = 0;

    /**
     * <br> life cycle.
     */

    public NestedScrollFrameLayout(Context context) {
        super(context);
        this.initialize();
    }

    public NestedScrollFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public NestedScrollFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NestedScrollFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    private void initialize() {
        this.nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        setNestedScrollingEnabled(true);

        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * <br>
     */

    public abstract boolean isParentOffset();

    /**
     * <br> touch event.
     */

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isBeingDragged = false;
                swipeDir = DIR_NULL;
                oldY = ev.getY();
                lastOffsetY = 0;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (oldY - ev.getY() + lastOffsetY);
                if (!isBeingDragged) {
                    if (Math.abs(deltaY) > touchSlop) {
                        isBeingDragged = true;
                    } else {
                        swipeDir = DIR_NULL;
                        oldY = ev.getY();
                        lastOffsetY = 0;
                    }
                }
                if (isBeingDragged) {
                    if (swipeDir == DIR_NULL
                            || swipeDir * deltaY > 0 || (swipeDir * deltaY < 0 && Math.abs(deltaY) > touchSlop)) {
                        int[] total = new int[]{0, deltaY};
                        int[] consumed = new int[]{0, 0};
                        int y = (int) (isParentOffset() ? ((View) getParent()).getY() : getY());
                        if (dispatchNestedPreScroll(total[0], total[1], consumed, null)) {
                            total[0] -= consumed[0];
                            total[1] -= consumed[1];
                        }
                        dispatchNestedScroll(0, 0, total[0], total[1], null);
                        swipeDir = deltaY == 0 ? DIR_NULL : (deltaY > 0 ? DIR_TOP : DIR_BOTTOM);
                        oldY = ev.getY();
                        lastOffsetY = (int) (y - (isParentOffset() ? ((View) getParent()).getY() : getY()));
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isBeingDragged = false;
                stopNestedScroll();
                break;
        }
        return true;
    }

    /**
     * <br> interface.
     */

    // nested scrolling parent.
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        startNestedScroll(nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        stopNestedScroll();
        nestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, new int[]{0, 0});
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        dispatchNestedPreScroll(dx, dy, consumed, new int[]{0, 0});
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    // nested scrolling child.

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return nestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(
                dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}