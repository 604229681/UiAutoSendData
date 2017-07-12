package com.auto.yunpan.uiautosenddata;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new UiautomatorThread().start();

            }
        }, 2000);
    }

    /**
     * 运行uiautomator是个费时的操作，不应该放在主线程，因此另起一个线程运行
     */
    class UiautomatorThread extends Thread {
        @Override
        public void run() {
            super.run();
            String command = generateCommand(getPackageName(), "ExampleInstrumentedTest", "sendMessageByThread");
            CMDUtils.CMD_Result rs = CMDUtils.runCMD(command, true, true);
            Log.e(TAG, "run: " + rs.error + "-------" + rs.success);
        }

        /**
         * 生成命令
         * $ adb shell am instrument -w -r   -e debug false -e class com.auto.yunpan.uiautosenddata.ExampleInstrumentedTest com.auto.yunpan.uiautosenddata.test/android.support.test.runner.AndroidJUnitRunner
         * @param pkgName 包名
         * @param clsName 类名
         * @param mtdName 方法名
         * @return
         */
        public String generateCommand(String pkgName, String clsName, String mtdName) {
            String command = "adb shell am instrument -w -r   -e debug false -e class "
                    + pkgName + "." + clsName + " "
                    + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
            Log.e("test1: ", command);
            return command;
        }
//        public String generateCommand(String pkgName, String clsName, String mtdName) {
//            String command = "am instrument  --user 0 -w -r   -e debug false -e class "
//                    + pkgName + "." + clsName + "#" + mtdName + " "
//                    + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
//            Log.e("test1: ", command);
//            return command;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
