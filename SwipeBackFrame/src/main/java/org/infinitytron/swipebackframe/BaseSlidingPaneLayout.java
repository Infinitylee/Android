/**
 * @fileName BaseSlidingPaneLayout
 * @describe 滑动窗格布局
 * @author 李培铭
 * @time 2017-08-16
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.swipebackframe;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class BaseSlidingPaneLayout extends SlidingPaneLayout {

	private float mInitialMotionX;
	private float mInitialMotionY;
	private float mEdgeSlop;

	public BaseSlidingPaneLayout(Context context) {
		this(context, null);
	}

	public BaseSlidingPaneLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ViewConfiguration config = ViewConfiguration.get(context);
		mEdgeSlop = config.getScaledEdgeSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (MotionEventCompat.getActionMasked(ev)) {
			case MotionEvent.ACTION_DOWN: {
				mInitialMotionX = ev.getX();
				mInitialMotionY = ev.getY();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final float y = ev.getY();
				// 用户的操作应该都在窗格关闭时,因此当窗格是关闭的时候,只检查视图的孩子的左右滑动操作
				if (mInitialMotionX > mEdgeSlop && !isOpen() && canScroll(this, false, Math.round(x - mInitialMotionX), Math.round(x), Math.round(y))) {
					// 如何设定 super.mIsUnableToDrag = true?
					// 给父控件发送取消事件消息
					MotionEvent cancelEvent = MotionEvent.obtain(ev);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
					return super.onInterceptTouchEvent(cancelEvent);
				}
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
}
