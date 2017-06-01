package com.meetyou.aop.assassin;

import android.util.Log;

import com.meetyou.assassin.plugin.AntiAssassin;
import com.meetyou.assassin.impl.IAssassinDelegate;

/**
 * Created by Linhh on 17/6/1.
 */
@AntiAssassin
public class TestDelegate extends IAssassinDelegate{

    @Override
    public void onMethodEnd(Object obj, String name, Object[] objects, String rtype) {
        Log.d("TestDelegate","onMethodEnd is called");
    }

    @Override
    public Object onMethodEnter(Object obj, String name, Object[] objects, String rtype) {
        Log.d("TestDelegate","onMethodEnter is called");
        return super.onMethodEnter(obj,name,objects,rtype);
    }
}
