package androidx.test.uiautomator;

public class UiScrollableCustom extends UiScrollable {
    public UiScrollableCustom(UiSelector container) {
        super(container);
    }

    public boolean scrollIntoView(UiSelector selector) throws UiObjectNotFoundException {
        return scrollIntoView(selector, 100);
    }

    public boolean scrollIntoView(UiSelector selector, int scrollSize) throws UiObjectNotFoundException {
        Tracer.trace(new Object[]{selector});
        for (int x = 0; x < getMaxSearchSwipes() && !findElement(selector); x++) {
            if(!this.scrollForward(scrollSize) && x!=0) {
                findElement(selector);
                break;
            }
            findElement(selector);
        }
        return findElement(selector);
    }

    private boolean findElement(UiSelector selector){
        UiSelector childSelector = this.getSelector().childSelector(selector);
        if (this.exists(selector) || this.exists(childSelector)){
            return true;
        }else {
            return false;
        }
    }
}
