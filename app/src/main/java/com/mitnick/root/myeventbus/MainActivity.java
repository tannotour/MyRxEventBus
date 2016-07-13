package com.mitnick.root.myeventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mitnick.root.myeventbus.util.rxbus.RxBus;
import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private Subscription subscription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post("login",null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = RxBus.viewSubscription(this,new String[]{"login"});
    }

    @Override
    protected void onPause() {
        if(null != subscription){
            subscription.unsubscribe();
        }
        super.onPause();
    }

    public void loginOk(RxBusEvent rxBusEvent){
        Log.e("MainActivity","loginOk");
    }

    public void loginError(RxBusEvent rxBusEvent){
        Log.e("MainActivity","loginError");
    }
}
