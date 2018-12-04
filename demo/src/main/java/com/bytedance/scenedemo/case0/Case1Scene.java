package com.bytedance.scenedemo.case0;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytedance.scene.Scene;
import com.bytedance.scene.animation.AnimationInfo;
import com.bytedance.scene.animation.NavigationAnimatorExecutor;
import com.bytedance.scene.animation.TransitionUtils;
import com.bytedance.scene.group.GroupScene;
import com.bytedance.scene.interfaces.PushOptions;
import com.bytedance.scene.navigation.NavigationScene;
import com.bytedance.scenedemo.utility.ColorUtil;

/**
 * Created by JiangQi on 9/5/18.
 */
public class Case1Scene extends GroupScene {
    @NonNull
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        Button button = new Button(getActivity());
        button.setText("Push 100次");
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    getNavigationScene().push(EmptyScene.class, null, new PushOptions.Builder().setAnimation(new AAA()).build());
                }
            }
        });

        layout.setBackgroundColor(ColorUtil.getMaterialColor(getResources(), 0));
        layout.setFitsSystemWindows(true);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((NavigationScene) getNavigationScene()).getView().setBackgroundColor(Color.YELLOW);
    }

    private static class AAA extends NavigationAnimatorExecutor {
        @Override
        public boolean isSupport(@NonNull Class<? extends Scene> from, @NonNull Class<? extends Scene> to) {
            return true;
        }

        @Override
        protected boolean disableConfigAnimationDuration() {
            return true;
        }

        @NonNull
        @Override
        protected Animator onPushAnimator(AnimationInfo from, final AnimationInfo to) {
            final View fromView = from.mSceneView;
            final View toView = to.mSceneView;

            ValueAnimator fromAlphaAnimator = ObjectAnimator.ofFloat(fromView, View.ALPHA, 1.0f, 1.0f);//之前是0.7，但是动画后面会露出NavigationScene的背景色白色很怪异
            fromAlphaAnimator.setInterpolator(new FastOutSlowInInterpolator());
            fromAlphaAnimator.setDuration(120 * 20);

            ValueAnimator toAlphaAnimator = ObjectAnimator.ofFloat(toView, View.ALPHA, 0.0f, 1.0f);
            toAlphaAnimator.setInterpolator(new DecelerateInterpolator(2));
            toAlphaAnimator.setDuration(120 * 20);

            ValueAnimator toTranslateAnimator = ObjectAnimator.ofFloat(toView, View.TRANSLATION_Y, 0.08f * toView.getHeight(), 0);
            toTranslateAnimator.setInterpolator(new DecelerateInterpolator(2.5f));
            toTranslateAnimator.setDuration(200 * 20);
            return TransitionUtils.mergeAnimators(fromAlphaAnimator, toAlphaAnimator, toTranslateAnimator);
        }

        @NonNull
        @Override
        protected Animator onPopAnimator(final AnimationInfo fromInfo, final AnimationInfo toInfo) {
            final View toView = toInfo.mSceneView;
            final View fromView = fromInfo.mSceneView;

            ValueAnimator fromAlphaAnimator = ObjectAnimator.ofFloat(fromView, View.ALPHA, 1.0f, 0.0f);
            fromAlphaAnimator.setInterpolator(new LinearInterpolator());
            fromAlphaAnimator.setDuration(150 * 20);
            fromAlphaAnimator.setStartDelay(50 * 20);

            ValueAnimator fromTranslateAnimator = ObjectAnimator.ofFloat(fromView, View.TRANSLATION_Y, 0, 0.08f * toView.getHeight());
            fromTranslateAnimator.setInterpolator(new AccelerateInterpolator(2));
            fromTranslateAnimator.setDuration(200 * 20);

            ValueAnimator toAlphaAnimator = ObjectAnimator.ofFloat(toView, View.ALPHA, 0.7f, 1.0f);
            toAlphaAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            toAlphaAnimator.setDuration(20 * 20);
            return TransitionUtils.mergeAnimators(fromAlphaAnimator, fromTranslateAnimator, toAlphaAnimator);
        }
    }

    public static class EmptyScene extends Scene {
        @NonNull
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LinearLayout layout = new TestLinearLayout0(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(getActivity());
            textView.setText("EmptyScene 第一个Push");
            layout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNavigationScene().popToRoot();
                }
            });

            layout.setBackgroundColor(ColorUtil.getMaterialColor(getResources(), 1));

            layout.setFitsSystemWindows(true);

            return layout;
        }
    }

    public static class EmptyScene2 extends Scene {
        @NonNull
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LinearLayout layout = new TestLinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(getActivity());
            textView.setText("EmptyScene2，第二个Push");
            layout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNavigationScene().popToRoot();
                }
            });

            layout.setBackgroundColor(ColorUtil.getMaterialColor(getResources(), 2));

            layout.setFitsSystemWindows(true);

            return layout;
        }
    }

    private static class TestLinearLayout0 extends LinearLayout{

        public TestLinearLayout0(Context context) {
            super(context);
        }

        public TestLinearLayout0(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TestLinearLayout0(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public TestLinearLayout0(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }

        @Override
        public void setVisibility(int visibility) {
            super.setVisibility(visibility);
        }
    }

    private static class TestLinearLayout extends LinearLayout{

        public TestLinearLayout(Context context) {
            super(context);
        }

        public TestLinearLayout(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TestLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public TestLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }
    }
}