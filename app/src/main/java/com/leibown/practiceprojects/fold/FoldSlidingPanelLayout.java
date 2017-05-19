package com.leibown.practiceprojects.fold;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/11/29.
 */

public class FoldSlidingPanelLayout extends SlidingPaneLayout {


    public FoldSlidingPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View child = getChildAt(0);
        if (child != null) {
            removeView(child);
            final FoldLayout foldLayout = new FoldLayout(getContext());
            foldLayout.addView(child);
            ViewGroup.LayoutParams layPar = child.getLayoutParams();
            addView(foldLayout, 0, layPar);
            setPanelSlideListener(new PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    foldLayout.setFactor(slideOffset);
                }

                @Override
                public void onPanelOpened(View panel) {

                }

                @Override
                public void onPanelClosed(View panel) {

                }
            });
        }
    }
}
