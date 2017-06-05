package com.meetyou.assassin.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.meetyou.assassin.plugin.AntiAssassin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Linhh on 17/5/31.
 */
@AntiAssassin
public class AssassinReveiver {
  private static IAssassinDelegate mIAssassinDelegate;

  private static HashMap<String, String> mMetas = new HashMap<>();
  private static HashMap<String, IAssassinDelegate> mDelegates = new HashMap<>();

  public static void putMeta(String key, String callClazz){
    mMetas.put(key,callClazz);
  }

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

  private static IAssassinDelegate newDelegate(String clazzName){
    try {
      Class<?> clazz = Class.forName(clazzName);
      return (IAssassinDelegate)clazz.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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

    IAssassinDelegate delegate = getDelegate(name);
    if(delegate != null){
      delegate.onMethodEnd(obj, name, objects, rtype);
    }
  }

  public static boolean onIntercept(String methodName){
    if(methodName.contains("onCreate")){
      return false;
    }
    return true;
  }

  /**
   * Get delegate by method name which be called.
   * @param methodName
   * @return
     */
  public static IAssassinDelegate getDelegate(String methodName){
    for(Map.Entry<String,String> entry : mMetas.entrySet()){
      String key = entry.getKey();
      if(methodName.contains(key)){
        //包含此key
        String v = entry.getValue();
        IAssassinDelegate delegate = mDelegates.get(v);
        if(delegate == null){
          delegate = newDelegate(v);
          mDelegates.put(v,delegate);
        }
        if(delegate == null){
          continue;
        }
        return delegate;
      }
    }
    return null;
  }

  public static Object onMethodEnter(Object obj, String name, Object[] objects, String rtype){
    Log.d("AssassinReveiver","onMethodEnter is called");
    if(mIAssassinDelegate != null){
      return mIAssassinDelegate.onMethodEnter(obj, name, objects, rtype);
    }
    IAssassinDelegate delegate = getDelegate(name);
    if(delegate != null){
      delegate.onMethodEnter(obj, name, objects, rtype);
    }
    return null;
  }

}
