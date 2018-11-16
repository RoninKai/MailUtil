package mail.tanker.com.mailutil;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tanker.mail.SendMailUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
    }

    public void onTest(View view) {
        File file = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + "/muFile3/myFile1.trace");
            bufferedReader = new BufferedReader(new FileReader(file));
            String readline = "";
            while ((readline = bufferedReader.readLine()) != null) {
                sb.append(readline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        SendMailUtil.send(file,"189898933@qq.com",sb.toString());
        Toast.makeText(this,"发送完成！",Toast.LENGTH_SHORT).show();
    }

}