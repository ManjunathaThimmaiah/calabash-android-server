package androidx.test.uiautomator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UiScrollableCustom extends UiScrollable {
    public UiScrollableCustom(UiSelector container) {
        super(container);
    }
    Long TIMEOUT = 1000L;

    /**
     * Method required because scrollForward return type is not reliable. It might return false, meaning that there is no
     * more space to scroll while it is not true. This method allows you to force to scroll even if scrollForward returns
     * false. When forcing to scroll, the method will stop scrolling when mMaxSearchSwipes is reached.
     */
    public boolean scrollIntoView(UiSelector selector, String bystrategy, boolean forceScroll) throws UiObjectNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Tracer.trace(new Object[]{selector});
        UiSelector childSelector = this.getSelector().childSelector(selector);
        boolean found = false;
        if (this.exists(childSelector)) {
            found = true;
        }else {
            for (int x = 0; x < getMaxSearchSwipes() && !found; ++x) {
                boolean scrolled = this.scrollForward(50);
                getDevice().waitForIdle();
                Method strategyMethod = By.class.getMethod(bystrategy, String.class);
                getDevice().wait(Until.findObject((BySelector) strategyMethod.invoke(By.class, (String.valueOf(childSelector)))), TIMEOUT);
                found = true; if (this.exists(childSelector));
                if (!forceScroll && !scrolled && found) break;
            }
        }
        return found;
    }
}
