package library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/**
 * Created by jose on 8/2/15.
 */
public class CollapsibleFrameLayout extends FrameLayout{


    private int mLastY;
    private int mDefaultHeight;
    private boolean mExpanded = true;
    private static final int COLLAPSE_THRESHOLD = 300;

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
                        if (yTranslate < 0 && yTranslate >= -COLLAPSE_THRESHOLD) {
                            setTranslationY(yTranslate);

                        }
                    } else {
                        if ((yTranslate - COLLAPSE_THRESHOLD) <= 0 && (yTranslate - COLLAPSE_THRESHOLD) >= -COLLAPSE_THRESHOLD) {
                            setTranslationY(yTranslate - COLLAPSE_THRESHOLD);

                        }
                    }

                    return false;
                case MotionEvent.ACTION_UP:
                    int transY = Y - mLastY;
                    if (transY <= -150) { //
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(CollapsibleFrameLayout.this, "translationY", -COLLAPSE_THRESHOLD);
                        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float val = (float) animation.getAnimatedValue();
                                setTranslationY(val);
//                                ViewGroup.LayoutParams rLayoutParams = feedListBlankHeader.getLayoutParams();
//                                rLayoutParams.height = (int) (mDefaultHeaderHeight - Math.abs(val));
//                                feedListBlankHeader.setLayoutParams(rLayoutParams);
                            }
                        });
                        objectAnimator.addListener(new DefaultAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLastY = -COLLAPSE_THRESHOLD;
                                mExpanded = false;
//                                expandHandle.setAlpha(1);
//                                collapseHandle.setAlpha(0);
                            }
                        });
                        objectAnimator.start();


                    } else {
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(CollapsibleFrameLayout.this, "translationY", 0);
                        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float val = (float) animation.getAnimatedValue();
                                setTranslationY(val);
//                                ViewGroup.LayoutParams rLayoutParams = feedListBlankHeader.getLayoutParams();
//                                rLayoutParams.height = (int) (mDefaultHeaderHeight - Math.abs(val));
//                                feedListBlankHeader.setLayoutParams(rLayoutParams);
                            }
                        });
                        objectAnimator.addListener(new DefaultAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLastY = 0;
                                mExpanded = true;
//                                expandHandle.setAlpha(0);
//                                collapseHandle.setAlpha(1);
                            }
                        });
                        objectAnimator.start();

                    }

                    break;
            }
            return true;

        }
    };



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

//        public void forceHide(){
//            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(header, "translationY", -COLLAPSE_THRESHOLD);
//            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float val = (float) animation.getAnimatedValue();
//                    ViewGroup.LayoutParams rLayoutParams = feedListBlankHeader.getLayoutParams();
//                    rLayoutParams.height = (int) (mDefaultHeaderHeight - Math.abs(val));
//                    feedListBlankHeader.setLayoutParams(rLayoutParams);
//                }
//            });
//            objectAnimator.addListener(new DefaultAnimationListener() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLastY = -COLLAPSE_THRESHOLD;
//                    mExpanded = false;
//                    expandHandle.setAlpha(1);
//                    collapseHandle.setAlpha(0);
//                }
//            });
//            objectAnimator.start();
//        }
//        }
//    };

    private void init(){
        setOnTouchListener(mTouchListener);
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
