package nanjing.jun.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SocketThread.OnClientListener {

    private SocketThread socketThread;
    private StringBuilder stringBuilder = new StringBuilder();
    private TextView serviceTv;
    private EditText contentEt;
    private Button sendBtn;
    private Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        serviceTv = (TextView) findViewById(R.id.tv_service);
        contentEt = (EditText) findViewById(R.id.et_content);
        sendBtn = (Button) findViewById(R.id.btn_send);
        exitBtn = (Button) findViewById(R.id.btn_safeExit);
        socketThread = new SocketThread(this);
        socketThread.start();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.append("我:\n");
                stringBuilder.append(contentEt.getText().toString());
                stringBuilder.append("\n");
                serviceTv.setText(stringBuilder.toString());
                socketThread.sendMessage(contentEt.getText().toString());
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, nanjing.jun.chat.Log.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketThread.disconnect();
    }

    @Override
    public void onNewMessage(String msg) {
        Log.i("收到的信息i",msg);
        String[] s = msg.split("#");
        stringBuilder.append(s[0]);
        stringBuilder.append("\n");
        stringBuilder.append(s[1]);
        stringBuilder.append("\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serviceTv.setText(stringBuilder.toString());
            }
        });
    }
}
