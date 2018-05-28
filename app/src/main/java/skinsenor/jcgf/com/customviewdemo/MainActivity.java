package skinsenor.jcgf.com.customviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    WYGridChartView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (WYGridChartView) findViewById(R.id.gridview);
    }

    int n = 77;

    public void ClickBtn(View view) {
        gridview.setSelepoint(n);
        if(n>=110){
            n=0;
        }
        n++;

    }
}
