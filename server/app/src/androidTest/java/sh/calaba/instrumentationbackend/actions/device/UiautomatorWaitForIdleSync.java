package sh.calaba.instrumentationbackend.actions.device;

import androidx.test.uiautomator.UiDevice;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class UiautomatorWaitForIdleSync implements Action {
    @Override
    public Result execute(String... args) {
        UiDevice mDevice = InstrumentationBackend.getUiDevice();
        try {
            if (args.length == 0){
                mDevice.waitForIdle();
            } else {
                mDevice.waitForIdle(Integer.parseInt(args[0]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new Result(true);
    }
    @Override
    public String key() {
        return "uiautomator_wait_for_idle_sync";
    }
}