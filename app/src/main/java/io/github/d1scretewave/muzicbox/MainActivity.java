package io.github.d1scretewave.muzicbox;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * MuzicBox 的主界面。
 *
 * <p>第一章只建立最小可运行工程，并通过日志观察 Activity 生命周期。
 * 音乐扫描与播放会从第二章开始加入。</p>
 */
public class MainActivity extends Activity {

    private static final String TAG = "MuzicBoxLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: 主界面已创建");

        Button roadmapButton = findViewById(R.id.button_roadmap);
        roadmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        MainActivity.this,
                        R.string.roadmap_toast,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 主界面变为可见");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 主界面可以交互");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: 主界面暂停交互");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: 主界面不可见");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 主界面准备重新显示");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: 主界面被销毁");
        super.onDestroy();
    }
}
