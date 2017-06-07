package com.meetyou.aop.assassin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.meetyou.assassin.plugin.Assassin;


@Assassin("onClick")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //无痕埋点
            }
        });
    }

    public Object show2(){
        return null;
    }

    public static void show(){

    }

    public void show5(){}
}
