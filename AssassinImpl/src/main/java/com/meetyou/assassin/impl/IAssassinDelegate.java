package com.meetyou.assassin.impl;

import android.util.Log;

/**
 * Created by Linhh on 17/6/1.
 */

public interface IAssassinDelegate {

    public void onMethodEnd(Object obj, String name, Object[] objects, String rtype);

    public Object onMethodEnter(Object obj, String name, Object[] objects, String rtype);
}
