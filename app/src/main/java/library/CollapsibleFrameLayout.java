package library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by jose on 8/2/15.
 */
public class CollapsibleFrameLayout extends FrameLayout{

    private int mLastY;
    private int mDefaultHeight;
    private boolean mExpanded = true;
    private int mPosition;

    private View mCollapseAnchor;

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            final int Y = (int) event.getRawY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = Y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int yTranslate = Y - mLastY;
                    if (mExpanded) {
                        if (yTranslate < 0 && yTranslate >= -mDefaultHeight) {
                            setTranslationY(yTranslate);
                            translateAnchor(yTranslate);
                        }
                    } else {
                        if ((yTranslate - mDefaultHeight) <= 0 && (yTranslate - mDefaultHeight) >= -mDefaultHeight) {
                            float translation = yTranslate - mDefaultHeight;
                            setTranslationY(translation);
                            translateAnchor(translation);
                        }
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    int transY = Y - mLastY;
                    if (transY <= -(mDefaultHeight/2)) { //
                        collapse();
                    } else {
                        expand();
                    }
                    break;
            }
            return true;

        }
    };

    public void collapse(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(CollapsibleFrameLayout.this, "translationY", -mDefaultHeight);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                setTranslationY(val);
                translateAnchor(val);
            }
        });
        objectAnimator.addListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLastY = -mDefaultHeight;
                mExpanded = false;
            }
        });
        objectAnimator.start();
    }

    public void expand(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(CollapsibleFrameLayout.this, "translationY", 0);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                setTranslationY(val);
                translateAnchor(val);
            }
        });
        objectAnimator.addListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLastY = 0;
                mExpanded = true;

            }
        });
        objectAnimator.start();
    }

    private void translateAnchor(float translation){
        if(mCollapseAnchor != null){
            mCollapseAnchor.setTranslationY(translation);
        }
    }


    public void setCollapseAnchor(View collapseAnchor){
        mCollapseAnchor = collapseAnchor;
        mCollapseAnchor.setOnTouchListener(mTouchListener);
        setOnTouchListener(null);
    }


    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    //TODO if Layout is attached to top/bottom
    private void checkPosition(){
        Log.d("","Top:"+getTop()+" Bottom:"+getBottom());
        Log.d("","Actionbar height in PX:"+dpToPx(56));
        Log.d("","ScreenHeight:"+getResources().getDisplayMetrics().heightPixels);
    }




    private class DefaultAnimationListener implements Animator.AnimatorListener{


        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private void init(){
        setOnTouchListener(mTouchListener);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDefaultHeight = getHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public CollapsibleFrameLayout(Context context) {
        super(context);
        init();
    }

    public CollapsibleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CollapsibleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CollapsibleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


}
