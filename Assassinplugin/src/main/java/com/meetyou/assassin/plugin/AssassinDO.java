package com.meetyou.assassin.plugin;

/**
 * Created by Linhh on 17/5/31.
 */

public class AssassinDO {
    public String key;
    public String value;
    public String mate;//匹配类型,fuzzy还是full
    public String type;//对象类型,method还是class

    @Override
    public boolean equals(Object o) {
        String key = (String)o;
        if(mate.equals("fuzzy")){
            //模糊
            return key.contains(this.key);
        }else{
            //完全匹配
            return key.equals(this.key);
        }
    }

    @Override
    public String toString() {
        return "key:" + key + ",value:" + value + ",mate:" + mate + ",type:" + type;
    }
}
