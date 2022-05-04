package sh.calaba.instrumentationbackend.actions.device;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class UiautomatorExecute implements Action {
    @Override
    public Result execute(String... args) {
        UiDevice mDevice = InstrumentationBackend.getUiDevice();
        String text = null;
        try {
            verifyStrategy(args[0]);
            verifyAction(args[3]);
            Method methodName = By.class.getMethod(args[0], String.class);
            BySelector selector = (BySelector) methodName.invoke(By.class, args[1]);

            Method methodOperation = UiObject2.class.getMethod(args[3]);

            List<UiObject2> objects = mDevice.findObjects(selector);
            if (objects.isEmpty()) {
                String errorMessage = String.format("Found no elements for locator: %s by strategy: %s", args[1], args[0]);
                throw new UiObjectNotFoundException(errorMessage);
            }
            UiObject2 object = objects.get(Integer.parseInt(args[2]));

            Object res = methodOperation.invoke(object);
            if (res != null) {
                text = res.toString();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            return new Result(false, e.getMessage());
        } catch (UiObjectNotFoundException e) {
            return new Result(false, e.getMessage());
        }

        return new Result(true, text);
    }

    @Override
    public String key() {
        return "uiautomator_execute";
    }

    private static void verifyStrategy(String strategy) {
        try {
            Strategies.valueOf(strategy);
        } catch (IllegalArgumentException e) {
            List<Strategies> availableStrategies = Arrays.asList(Strategies.values());
            String errorMessage = String.format("Unsupported strategy: %s. The list of available strategies is %s", strategy, availableStrategies);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void verifyAction(String action) {
        try {
            Actions.valueOf(action);
        } catch (IllegalArgumentException e) {
            List<Actions> availableActions = Arrays.asList(Actions.values());
            String errorMessage = String.format("Unsupported action: %s. The list of available actions is %s", action, availableActions);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
