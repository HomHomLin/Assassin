package com.meetyou.aop.assassin;

import com.meetyou.assassin.impl.AssassinPro;
import com.meetyou.assassin.plugin.AntiAssassin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Linhh on 17/6/5.
 */
@AntiAssassin
public class TestD implements AssassinPro {
    public static HashMap<String,ArrayList<String>> map = new HashMap<>();

    public HashMap<String, ArrayList<String>> getMap(){
        return map;
    }

    static{
        ArrayList<String> list = new ArrayList<>();
        list.add("ts1-1");
        list.add("ts1-2");
        map.put("test",list);
        list = new ArrayList<>();
        list.add("ts2-1");
        map.put("test2",list);
        list = new ArrayList<>();
        list.add("ts3-1");
        map.put("test3",list);
    }
}
