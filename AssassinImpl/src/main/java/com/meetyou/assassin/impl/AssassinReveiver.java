package com.meetyou.assassin.impl;

import android.text.TextUtils;
import android.util.Log;

import com.meetyou.assassin.plugin.AntiAssassin;

/**
 * Created by Linhh on 17/5/31.
 */
@AntiAssassin
public class AssassinReveiver {
  private static IAssassinDelegate mIAssassinDelegate;

  public static void register(String name){
    if(mIAssassinDelegate != null){
      return;
    }
    try {
      if(TextUtils.isEmpty(name)){
        return;
      }
      Class<?> clazz = Class.forName(name);
      mIAssassinDelegate = (IAssassinDelegate)clazz.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void register(IAssassinDelegate delegate){
    mIAssassinDelegate = delegate;
  }

  public static void unregister(){
    mIAssassinDelegate = null;
  }

  public static void onMethodEnd(Object obj, String name, Object[] objects, String rtype){
    Log.d("AssassinReveiver","onMethodEnd is called");
    if(mIAssassinDelegate != null){
      mIAssassinDelegate.onMethodEnd(obj, name, objects, rtype);
    }
  }

  public static Object onMethodEnter(Object obj, String name, Object[] objects, String rtype){
    Log.d("AssassinReveiver","onMethodEnter is called");
    if(mIAssassinDelegate != null){
      return mIAssassinDelegate.onMethodEnter(obj, name, objects, rtype);
    }
    return null;
  }

}
