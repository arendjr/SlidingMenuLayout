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

    public void toggleMenu() {
	toggleMenu(LEFT);
    }

    public void toggleMenu(int direction) {
	if (isOpen()) {
            closeMenu();
	    // JUST FOR TESTING open the menu on the specified side, if it was
	    // previously opened on opposed side
	    if (m_direction != direction) {
		openMenu(direction);
	    }
	} else {
		openMenu(direction);
	}
    }

    public void openMenu() {
	openMenu(LEFT);
    }
	
    public boolean isOpen() {

        return m_open;
    }
    /**
     * 
     * @return true if the menu is displayed on the right side of the screen
     */
    private boolean isRightMenu() {
	return m_direction == RIGHT;
    }

    public void openMenu(int direction) {

        if (m_open) {
            return;
        }
	m_direction = direction;
        m_currentDX = 0;
        if (m_timer != null) {
            m_timer.cancel();
	}        
        m_timer = new Timer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_currentDX += m_slidedDX / ANIMATION_STEP;
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
        }, ANIMATION_DELAY, ANIMATION_INTERVAL);

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
                m_currentDX -= m_slidedDX / ANIMATION_STEP;
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
        }, ANIMATION_DELAY, ANIMATION_INTERVAL);

        m_open = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height) { // to adjust the menu size differently according to the device orientation
		m_slidedDX = width * 4 / 5;
	} else {
		m_slidedDX = width * 2 / 5;
	}

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
            if (i == count - 1) { // the main view
		if (isRightMenu()) {
			view.layout(-m_currentDX, 0, width - m_currentDX, height);
		} else {
			view.layout(m_currentDX, 0, width + m_currentDX, height);
		}
	    } else { // the menu view
		if (isRightMenu()) {
			view.layout(width - m_slidedDX, 0, width, height);
		} else {
			view.layout(0, 0, m_slidedDX, height);
		}
	    }
        }
    }

    private void init() {

        setChildrenDrawingOrderEnabled(true);
    }

    private boolean m_open = false;

    private int m_currentDX = 0;
    private int m_slidedDX = 0;
    private int m_direction = LEFT;

    private Timer m_timer;
    private static final int ANIMATION_DELAY = 25;
    private static final int ANIMATION_INTERVAL = 21;
    private static final int ANIMATION_STEP = 10;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
}
