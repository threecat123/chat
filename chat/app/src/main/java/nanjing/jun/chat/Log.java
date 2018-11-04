package nanjing.jun.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;




public class Log extends AppCompatActivity {
    HashMap map;
    EditText editText1;
    EditText editText2;
    String phone_number;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void Login(View view) {
        map=new HashMap();
        map.put("ergou","123");
        map.put("gua","456");
        editText1=(EditText) findViewById(R.id.cellphone_number);
        editText2= (EditText) findViewById(R.id.login_password);
        phone_number=editText1.getText().toString();
        password=editText2.getText().toString();
        if(map.containsKey(phone_number)) {
            if (password.equals("123") || password.equals("456")) {
                Intent intent=new Intent(Log.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Context context = getApplicationContext();
                CharSequence text ="密码错误，请重新输入";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "账号错误，请重新输入";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
