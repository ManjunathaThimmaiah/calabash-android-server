package sh.calaba.instrumentationbackend.actions.device;

import androidx.test.uiautomator.Configurator;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollableCustom;
import androidx.test.uiautomator.UiSelector;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static sh.calaba.instrumentationbackend.actions.device.StrategyUtils.convertBySelectorStrategyToUiSelectorStrategy;
import static sh.calaba.instrumentationbackend.actions.device.StrategyVerifier.verifyStrategy;

public class UiautomatorVerticallyScrollToElement implements Action {
    @Override
    public Result execute(String... args) {
        InstrumentationBackend.getUiDevice();
        Configurator.getInstance().setScrollAcknowledgmentTimeout(2000l);

        try {
            String bySelectorTargetStrategy = args[0];
            String targetLocator = args[1];
            int scrollSize= 50;

            verifyStrategy(bySelectorTargetStrategy);
            String uiTargetSelectorStratagy = convertBySelectorStrategyToUiSelectorStrategy(bySelectorTargetStrategy);

            Method targetStrategyMethod = UiSelector.class.getDeclaredMethod(uiTargetSelectorStratagy, String.class);
            UiSelector targetViewSelector = (UiSelector) targetStrategyMethod.invoke(new UiSelector(), targetLocator);
            UiScrollableCustom scrollable;

            try {
                if(args.length>=3 && args[3]!=null){
                    String bySelectorParentStrategy = args[2];
                    String parentLocator = args[3];

                    verifyStrategy(bySelectorParentStrategy);
                    String uiParentSelectorStrategy = convertBySelectorStrategyToUiSelectorStrategy(bySelectorParentStrategy);

                    Method parentStrategyMethod = UiSelector.class.getDeclaredMethod(uiParentSelectorStrategy, String.class);
                    UiSelector parentViewSelector = (UiSelector) parentStrategyMethod.invoke(new UiSelector(), parentLocator);
                    scrollable = new UiScrollableCustom(parentViewSelector);
                }else {
                    UiSelector scrollViewSelector = new UiSelector().scrollable(true);
                    scrollable = new UiScrollableCustom(scrollViewSelector);
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            if (!scrollable.scrollIntoView(targetViewSelector, scrollSize) ) {
                String errorMessage = String.format("Found no elements for locator: %s by strategy: %s", targetLocator, bySelectorTargetStrategy);
                throw new UiObjectNotFoundException(errorMessage);
            }
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
    public String key() { return "ui_automator_vertically_find_element_by";}
}
