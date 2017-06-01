package com.meetyou.assassin.impl;

import android.util.Log;

/**
 * Created by Linhh on 17/5/31.
 */

public class AssassinReveiver {
  private static IAssassinDelegate mIAssassinDelegate;

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
