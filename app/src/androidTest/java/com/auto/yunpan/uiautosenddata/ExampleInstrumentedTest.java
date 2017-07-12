package com.auto.yunpan.uiautosenddata;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.auto.yunpan.uiautosenddata.usb.SocketServer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UiDevice device;

    private static UiObject uiObject;

    private int item = 0;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.auto.yunpan.uiautosenddata", appContext.getPackageName());
    }

    private SocketServer socketServer;

    @Before
    public void before() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        assertThat(device, notNullValue());
        device.pressHome();
        // open app
        openApp("com.auto.yunpan.testuiauto");

//        CountDownLatch latch = new CountDownLatch(1); //创建CountDownLatch

        createSockt();

//        latch.await();
    }

    @Test
    public void createSockt() throws Exception {
        while (true) {
            if (null == socketServer) {
                socketServer = new SocketServer();
                socketServer.setSocketCallBack(new SocketServer.SocketCallBack() {
                    @Override
                    public void onSuccess(String code) {

                        sendMessage(code);

                    }

                    @Override
                    public void onFailed(String msg) {

                    }
                });
                new Thread(socketServer).start();
            }

            sleep(2000);
        }
    }

    public synchronized void sendMessage(String msg){

//        UiSelector s = new UiSelector().className("android.widget.TextView").instance(1);
//        UiObject wx_input= new UiObject(new UiSelector().className("android.widget.RelativeLayout").childSelector(new UiSelector().className("android.widget.EditText")));

        try {
            if (null == device) {
                device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            }

            if (null == uiObject)
                uiObject = device.findObject(new UiSelector().resourceId("com.auto.yunpan.testuiauto:id/tv_name"));

            if (uiObject != null) {
                boolean success = uiObject.setText(msg + (item++));
                if (!success) {
                    socketServer.disCloseSocket();
                    socketServer = null;
                }else {
                    uiObject.click();
                }
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            uiObject = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void openApp(String packageName) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
