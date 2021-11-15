
package com.zzt.popupwindows.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: zeting
 * @date: 2021/6/21
 * PopupWindow 弹框普通布局
 */
public class ZNormalPopup extends ZBasePopup {
    private static final String TAG = ZNormalPopup.class.getSimpleName();
    public static final int DIRECTION_TOP = 0;
    public static final int DIRECTION_BOTTOM = 1;
    public static final int DIRECTION_CENTER_IN_SCREEN = 2;

    @IntDef({DIRECTION_CENTER_IN_SCREEN, DIRECTION_TOP, DIRECTION_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    private int mEdgeProtectionTop;
    private int mEdgeProtectionLeft;
    private int mEdgeProtectionRight;
    private int mEdgeProtectionBottom;
    private boolean mShowArrow = true;
    private int mRadius = 0;
    private int mBorderUsedColor = Color.TRANSPARENT;
    private int mBorderWidth = 0;
    private int mBgUsedColor = Color.TRANSPARENT;
    private int mOffsetX = 0;
    private int mOffsetYIfTop = 0;
    private int mOffsetYIfBottom = 0;
    private @Direction
    int mPreferredDirection = DIRECTION_BOTTOM;
    protected final int mInitWidth;
    protected final int mInitHeight;
    private int mArrowWidth = NOT_SET;
    private int mArrowHeight = NOT_SET;
    private View mContentView;
    // 背景渐变背景
    private GradientDrawable mBgUsedColorGradient;

    public ZNormalPopup(Context context, int width, int height) {
        super(context);
        mInitWidth = width;
        mInitHeight = height;
    }

    public ZNormalPopup arrow(boolean showArrow) {
        mShowArrow = showArrow;
        return this;
    }

    public ZNormalPopup arrowSize(int width, int height) {
        mArrowWidth = width;
        mArrowHeight = height;
        return this;
    }

    public ZNormalPopup radius(int radius) {
        mRadius = radius;
        return this;
    }

    public ZNormalPopup edgeProtection(int distance) {
        mEdgeProtectionLeft = distance;
        mEdgeProtectionRight = distance;
        mEdgeProtectionTop = distance;
        mEdgeProtectionBottom = distance;
        return this;
    }

    public ZNormalPopup edgeProtection(int left, int top, int right, int bottom) {
        mEdgeProtectionLeft = left;
        mEdgeProtectionTop = top;
        mEdgeProtectionRight = right;
        mEdgeProtectionBottom = bottom;
        return this;
    }

    public ZNormalPopup offsetX(int offsetX) {
        mOffsetX = offsetX;
        return this;
    }

    public ZNormalPopup offsetYIfTop(int y) {
        mOffsetYIfTop = y;
        return this;
    }

    public ZNormalPopup offsetYIfBottom(int y) {
        mOffsetYIfBottom = y;
        return this;
    }

    public ZNormalPopup preferredDirection(@Direction int preferredDirection) {
        mPreferredDirection = preferredDirection;
        return this;
    }

    public ZNormalPopup view(View contentView) {
        mContentView = contentView;
        return this;
    }

    public ZNormalPopup view(@LayoutRes int contentViewResId) {
        return view(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    public ZNormalPopup borderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        return this;
    }

    public ZNormalPopup borderColor(int borderColor) {
        mBorderUsedColor = borderColor;
        return this;
    }

    public int getBgColor() {
        return mBgUsedColor;
    }

    public int getBorderColor() {
        return mBorderUsedColor;
    }

    public ZNormalPopup bgColor(int bgColor) {
        mBgUsedColor = bgColor;
        return this;
    }

    public ZNormalPopup bgColorGradient(int bgColors[]) {
        mBgUsedColorGradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, bgColors);
        return this;
    }

    class ShowInfo {
        private int[] anchorRootLocation = new int[2];
        private int[] anchorLocation = new int[2];
        Rect visibleWindowFrame = new Rect();
        int width;
        int height;
        int x;
        int y;
        View anchor;
        int anchorCenter;
        int direction = mPreferredDirection;
        int contentWidthMeasureSpec;
        int contentHeightMeasureSpec;
        int decorationLeft = 0;
        int decorationRight = 0;
        int decorationTop = 0;
        int decorationBottom = 0;

        ShowInfo(View anchor) {
            this.anchor = anchor;
            // for muti window
            anchor.getRootView().getLocationOnScreen(anchorRootLocation);
            anchor.getLocationOnScreen(anchorLocation);
            anchorCenter = anchorLocation[0] + anchor.getWidth() / 2;
            anchor.getWindowVisibleDisplayFrame(visibleWindowFrame);
        }


        float anchorProportion() {
            return (anchorCenter - x) / (float) width;
        }

        int windowWidth() {
            return decorationLeft + width + decorationRight;
        }

        int windowHeight() {
            return decorationTop + height + decorationBottom;
        }

        int getVisibleWidth() {
            return visibleWindowFrame.width();
        }

        int getVisibleHeight() {
            return visibleWindowFrame.height();
        }

        int getWindowX() {
            return x - anchorRootLocation[0];
        }

        int getWindowY() {
            return y - anchorRootLocation[1];
        }
    }


    public void show(@NonNull View anchor) {
        if (mContentView == null) {
            throw new RuntimeException("you should call view() to set your content view");
        }
        ShowInfo showInfo = new ShowInfo(anchor);
        calculateWindowSize(showInfo);
        calculateXY(showInfo);
        adjustShowInfo(showInfo);
        decorateContentView(showInfo);
        mWindow.setWidth(showInfo.windowWidth());
        mWindow.setHeight(showInfo.windowHeight());
        showAtLocation(anchor, showInfo.getWindowX(), showInfo.getWindowY());
    }


    private void decorateContentView(ShowInfo showInfo) {
        ContentView contentView = ContentView.wrap(mContentView, mInitWidth, mInitHeight);
        if (mBgUsedColorGradient != null) {
            contentView.setBackground(mBgUsedColorGradient);
        } else {
            contentView.setBackgroundColor(mBgUsedColor);
        }
        contentView.setBorderColor(mBorderUsedColor);
        contentView.setBorderWidth(mBorderWidth);

        contentView.setRadius(mRadius);

        Log.d(TAG, "contentView:" + contentView.getWidth() + " - " + contentView.getHeight());

        DecorRootView decorRootView = new DecorRootView(mContext, showInfo);
//        decorRootView.setBackgroundColor(Color.parseColor("#111111"));
        decorRootView.setContentView(contentView);
        mWindow.setContentView(decorRootView);
    }

    private void adjustShowInfo(ShowInfo showInfo) {
        if (mShowArrow && showInfo.direction != DIRECTION_CENTER_IN_SCREEN) {
            if (mArrowWidth == NOT_SET) {
                mArrowWidth = dp2px(mContext, 10);
            }
            if (mArrowHeight == NOT_SET) {
                mArrowHeight = dp2px(mContext, 8);
            }
            if (showInfo.direction == DIRECTION_BOTTOM) {
                showInfo.decorationTop = Math.max(showInfo.decorationTop, mArrowHeight);
            } else if (showInfo.direction == DIRECTION_TOP) {
                showInfo.decorationBottom = Math.max(showInfo.decorationBottom, mArrowHeight);
                showInfo.y -= mArrowHeight;
            }
        }
    }

    private void calculateXY(ShowInfo showInfo) {
        if (showInfo.anchorCenter < showInfo.visibleWindowFrame.left + showInfo.getVisibleWidth() / 2) { // anchor point on the left
            showInfo.x = Math.max(mEdgeProtectionLeft + showInfo.visibleWindowFrame.left, showInfo.anchorCenter - showInfo.width / 2 + mOffsetX);
        } else { // anchor point on the left
            showInfo.x = Math.min(
                    showInfo.visibleWindowFrame.right - mEdgeProtectionRight - showInfo.width,
                    showInfo.anchorCenter - showInfo.width / 2 + mOffsetX);
        }
        int nextDirection = DIRECTION_CENTER_IN_SCREEN;
        if (mPreferredDirection == DIRECTION_BOTTOM) {
            nextDirection = DIRECTION_TOP;
        } else if (mPreferredDirection == DIRECTION_TOP) {
            nextDirection = DIRECTION_BOTTOM;
        }
        handleDirection(showInfo, mPreferredDirection, nextDirection);
    }

    private void handleDirection(ShowInfo showInfo, int currentDirection, int nextDirection) {
        if (currentDirection == DIRECTION_CENTER_IN_SCREEN) {
            showInfo.x = showInfo.visibleWindowFrame.left + (showInfo.getVisibleWidth() - showInfo.width) / 2;
            showInfo.y = showInfo.visibleWindowFrame.top + (showInfo.getVisibleHeight() - showInfo.height) / 2;
            showInfo.direction = DIRECTION_CENTER_IN_SCREEN;
        } else if (currentDirection == DIRECTION_TOP) {
            showInfo.y = showInfo.anchorLocation[1] - showInfo.height - mOffsetYIfTop;
            if (showInfo.y < mEdgeProtectionTop + showInfo.visibleWindowFrame.top) {
                handleDirection(showInfo, nextDirection, DIRECTION_CENTER_IN_SCREEN);
            } else {
                showInfo.direction = DIRECTION_TOP;
            }
        } else if (currentDirection == DIRECTION_BOTTOM) {
            showInfo.y = showInfo.anchorLocation[1] + showInfo.anchor.getHeight() + mOffsetYIfBottom;
            if (showInfo.y > showInfo.visibleWindowFrame.bottom - mEdgeProtectionBottom - showInfo.height) {
                handleDirection(showInfo, nextDirection, DIRECTION_CENTER_IN_SCREEN);
            } else {
                showInfo.direction = DIRECTION_BOTTOM;
            }
        }
    }

    protected int proxyWidth(int width) {
        return width;
    }

    protected int proxyHeight(int height) {
        return height;
    }

    private void calculateWindowSize(ShowInfo showInfo) {
        boolean needMeasureForWidth = false, needMeasureForHeight = false;
        if (mInitWidth > 0) {
            showInfo.width = proxyWidth(mInitWidth);
            showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    showInfo.width, View.MeasureSpec.EXACTLY);
        } else {
            int maxWidth = showInfo.getVisibleWidth() - mEdgeProtectionLeft - mEdgeProtectionRight;
            if (mInitWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.width = proxyWidth(maxWidth);
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        showInfo.width, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForWidth = true;
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        proxyWidth(maxWidth), View.MeasureSpec.AT_MOST);
            }
        }
        if (mInitHeight > 0) {
            showInfo.height = proxyHeight(mInitHeight);
            showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    showInfo.height, View.MeasureSpec.EXACTLY);
        } else {
            int maxHeight = showInfo.getVisibleHeight() - mEdgeProtectionTop - mEdgeProtectionBottom;
            if (mInitHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.height = proxyHeight(maxHeight);
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        showInfo.height, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForHeight = true;
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        proxyHeight(maxHeight), View.MeasureSpec.AT_MOST);
            }
        }

        if (needMeasureForWidth || needMeasureForHeight) {
            mContentView.measure(
                    showInfo.contentWidthMeasureSpec, showInfo.contentHeightMeasureSpec);
            if (needMeasureForWidth) {
                showInfo.width = proxyWidth(mContentView.getMeasuredWidth());
            }
            if (needMeasureForHeight) {
                showInfo.height = proxyHeight(mContentView.getMeasuredHeight());
            }
        }
    }

    static class ContentView extends ZPopFrameLayout {
        private ContentView(Context context) {
            super(context);
        }

        static ContentView wrap(View businessView, int width, int height) {
            ContentView contentView = new ContentView(businessView.getContext());
            if (businessView.getParent() != null) {
                ((ViewGroup) businessView.getParent()).removeView(businessView);
            }
            contentView.addView(businessView, new LayoutParams(width, height));
            return contentView;
        }
    }

    class DecorRootView extends FrameLayout {
        private ShowInfo mShowInfo;
        private View mContentView;
        private Paint mArrowPaint;
        private Path mArrowPath;
        private RectF mArrowSaveRect = new RectF();

        private int mPendingWidth;
        private int mPendingHeight;
        private Runnable mUpdateWindowAction = new Runnable() {
            @Override
            public void run() {
                mShowInfo.width = mPendingWidth;
                mShowInfo.height = mPendingHeight;
                calculateXY(mShowInfo);
                adjustShowInfo(mShowInfo);
                mWindow.update(mShowInfo.getWindowX(), mShowInfo.getWindowY(), mShowInfo.windowWidth(), mShowInfo.windowHeight());
            }
        };

        private DecorRootView(Context context, ShowInfo showInfo) {
            super(context);
            mShowInfo = showInfo;
            mArrowPaint = new Paint();
            mArrowPaint.setAntiAlias(true);
            mArrowPath = new Path();
        }


        public void setContentView(View contentView) {
            if (mContentView != null) {
                removeView(mContentView);
            }
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            mContentView = contentView;
            addView(contentView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            removeCallbacks(mUpdateWindowAction);
            if (mContentView != null) {
                mContentView.measure(mShowInfo.contentWidthMeasureSpec, mShowInfo.contentHeightMeasureSpec);
                int measuredWidth = mContentView.getMeasuredWidth();
                int measuredHeight = mContentView.getMeasuredHeight();
                if (mShowInfo.width != measuredWidth || mShowInfo.height != measuredHeight) {
                    mPendingWidth = measuredWidth;
                    mPendingHeight = measuredHeight;
                    post(mUpdateWindowAction);
                }
            }
            setMeasuredDimension(mShowInfo.windowWidth(), mShowInfo.windowHeight());
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (mContentView != null) {
                mContentView.layout(mShowInfo.decorationLeft, mShowInfo.decorationTop,
                        mShowInfo.width + mShowInfo.decorationLeft,
                        mShowInfo.height + mShowInfo.decorationTop);
                Log.d(TAG, "zzzzzzzz mShowInfo.decorationTop:" + mShowInfo.decorationTop);
                Log.d(TAG, "zzzzzzzz mArrowHeight:" + mArrowHeight);
                Log.d(TAG, "zzzzzzzz mContentView:" + mContentView.getTop());


            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            removeCallbacks(mUpdateWindowAction);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (mShowArrow) {
                if (mShowInfo.direction == DIRECTION_TOP) {
                    canvas.save();
                    mArrowSaveRect.set(0f, 0f, mShowInfo.width, mShowInfo.height);
                    mArrowPaint.setStyle(Paint.Style.FILL);
                    mArrowPaint.setColor(mBgUsedColor);
                    mArrowPaint.setXfermode(null);
                    int l = mShowInfo.anchorCenter - mShowInfo.x - mArrowWidth / 2;
                    l = Math.min(Math.max(l, mShowInfo.decorationLeft),
                            getWidth() - mShowInfo.decorationRight - mArrowWidth);
                    int t = mShowInfo.decorationTop + mShowInfo.height - mBorderWidth;
                    canvas.translate(l, t);
                    mArrowPath.reset();
                    mArrowPath.setLastPoint(-mArrowWidth / 2f, 0);
                    mArrowPath.lineTo(mArrowWidth / 2f, mArrowHeight);
                    mArrowPath.lineTo(mArrowWidth * 3 / 2f, 0);
                    mArrowPath.close();
                    canvas.drawPath(mArrowPath, mArrowPaint);
                    canvas.restore();
                } else if (mShowInfo.direction == DIRECTION_BOTTOM) {
                    canvas.save();
                    mArrowPaint.setStyle(Paint.Style.FILL);
                    mArrowPaint.setXfermode(null);
                    mArrowPaint.setColor(mBgUsedColor);
                    int l = mShowInfo.anchorCenter - mShowInfo.x - mArrowWidth / 2;
                    l = Math.min(Math.max(l, mShowInfo.decorationLeft),
                            getWidth() - mShowInfo.decorationRight - mArrowWidth);
                    int t = mShowInfo.decorationTop + mBorderWidth;
                    canvas.translate(l, t);
                    Log.w(TAG, "zzzzzzzz 11:" + mContentView.getTop() + " 22:" + mShowInfo.decorationTop + " 33:" + mArrowHeight);
                    mArrowPath.reset();
                    mArrowPath.setLastPoint(-mArrowWidth / 2f, 0);
                    mArrowPath.lineTo(mArrowWidth / 2f, -mArrowHeight);
                    mArrowPath.lineTo(mArrowWidth * 3 / 2f, 0);
                    mArrowPath.close();
                    canvas.drawPath(mArrowPath, mArrowPaint);
                    canvas.restore();
                }
            }
        }
    }

}
