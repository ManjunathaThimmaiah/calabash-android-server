package androidx.test.uiautomator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UiScrollableCustom extends UiScrollable {
    public UiScrollableCustom(UiSelector container) {
        super(container);
    }

    private static final Long TIMEOUT = 1000L;

    public boolean scrollIntoView(UiSelector selector) throws UiObjectNotFoundException {
        return scrollIntoView(selector, false);
    }

    /**
     * Method required because scrollForward return type is not reliable. It might return false, meaning that there is no
     * more space to scroll while it is not true. This method allows you to force to scroll even if scrollForward returns
     * false. When forcing to scroll, the method will stop scrolling when mMaxSearchSwipes is reached.
     */
    public boolean scrollIntoView(UiSelector selector, boolean forceScroll) throws UiObjectNotFoundException {
        Tracer.trace(new Object[]{selector});
        UiSelector childSelector = this.getSelector().childSelector(selector);
        boolean found = false;

        for (int x = 0; x < getMaxSearchSwipes() && !found; ++x) {
            if (this.exists(childSelector)) {
                found = true;
            } else {
                boolean scrolled = this.scrollForward(100);
                getDevice().waitForIdle();
                if (!forceScroll && !scrolled) break;
            }
        }
        return found;
    }

    public boolean scrollIntoView(UiSelector selector, String targetBySelectorStrategy, boolean forceScroll) throws UiObjectNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InvocationTargetException {
        Tracer.trace(new Object[]{selector});
        UiSelector childSelector = this.getSelector().childSelector(selector);
        Method strategyMethod = By.class.getMethod(targetBySelectorStrategy, String.class);
        BySelector byselector = (BySelector) strategyMethod.invoke(By.class, (String.valueOf(childSelector)));
        boolean found = false;
        if (this.exists(childSelector)) {
            found = true;
        }else {
            for (int x = 0; x < getMaxSearchSwipes(); ++x) {
                boolean scrolled = this.scrollForward(50);
                getDevice().waitForIdle();
                getDevice().wait(Until.findObject(byselector), TIMEOUT);
                if (this.exists(childSelector)) {
                    found = true; break;
                };
                if (!forceScroll && !scrolled) break;
            }
        }
        return found;
    }
}
