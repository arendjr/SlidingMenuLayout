package nl.arendjr.slidingmenulayout;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


public class SlidingMenuLayout extends ViewGroup {

    public SlidingMenuLayout(Context context) {

        super(context);

        init();
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        init();
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        init();
    }

    public boolean isOpen() {

        return m_open;
    }

    public void openMenu() {

        if (m_open) {
            return;
        }

        m_currentDX = 0;
        if (m_timer != null) {
        	m_timer.cancel();
		}        
        m_timer = new Timer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_currentDX += m_slidedDX / 10;
                if (m_currentDX >= m_slidedDX) {
                    m_currentDX = m_slidedDX;
                    m_timer.cancel();
                }
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        requestLayout();
                    }
                });
            }
        }, 16, 16);

        m_open = true;
    }

    public void closeMenu() {

        if (!m_open) {
            return;
        }
        if (m_timer != null) {
    		m_timer.cancel();
		}
        m_timer = new Timer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_currentDX -= m_slidedDX / 10;
                if (m_currentDX <= 0) {
                    m_currentDX = 0;
                    m_timer.cancel();
                }
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        requestLayout();
                    }
                });
            }
        }, 16, 16);

        m_open = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        m_slidedDX = width * 4 / 5;

        super.setMeasuredDimension(width, height);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (i == count - 1) {
                measureChild(view, MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
            } else {
                measureChild(view, MeasureSpec.makeMeasureSpec(m_slidedDX, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int width = right - left;
        int height = bottom - top;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (i == count - 1) {
                view.layout(m_currentDX, 0, width + m_currentDX, height);
            } else {
                view.layout(0, 0, m_slidedDX, height);
            }
        }
    }

    private void init() {

        setChildrenDrawingOrderEnabled(true);
    }

    private boolean m_open = false;

    private int m_currentDX = 0;
    private int m_slidedDX = 0;

    private Timer m_timer;
}
