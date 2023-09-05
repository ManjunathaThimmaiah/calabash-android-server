package sh.calaba.instrumentationbackend.actions.device;

import androidx.test.uiautomator.UiObjectNotFoundException;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.lang.reflect.InvocationTargetException;

import static sh.calaba.instrumentationbackend.actions.device.ScrollToElementActionHelper.scrollToTarget;

public class UiautomatorVerticallyScrollingFowardOrBackward implements Action {
    @Override
    public Result execute(String... args) {
        InstrumentationBackend.getUiDevice();
        try {
            String direction = args[0];
            String targetBySelectorStrategy = args[1];
            String targetLocator = args[2];

            int maxScrolls = 10;
            if (args.length >= 3) {
                maxScrolls = Integer.parseInt(args[3]);
            }

            scrollToTarget(targetBySelectorStrategy, targetLocator, direction, maxScrolls, false);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return new Result(true);
    }

    @Override
    public String key() { return "uiautomator_vertically_scroll_forward_or_backward_to_element"; }
}
