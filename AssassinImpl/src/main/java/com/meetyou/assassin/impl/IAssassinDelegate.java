package com.meetyou.assassin.impl;

import com.meetyou.assassin.plugin.AntiAssassin;

/**
 * Created by Linhh on 17/6/1.
 */
@AntiAssassin
public abstract class IAssassinDelegate {

    public void onMethodEnd(Object obj, String name, Object[] objects, String rtype){

    }

    public Object onMethodEnter(Object obj, String name, Object[] objects, String rtype){
        return null;
    }
}
