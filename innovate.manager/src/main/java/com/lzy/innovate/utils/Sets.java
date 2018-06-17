package com.lzy.innovate.utils;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lzy on 2017/1/13.
 *
 * 该工具类利用java的类型推导特性
 * 使每次new集合时不必重复写泛型的参数（map参数很长的时候）
 * 弥补编译器的缺陷
 */
public class Sets {

    private Sets(){}

    public static <K,V>HashMap<K,V> map(){
        return new HashMap<K,V>();
    }

    public static <T>ArrayList<T> list(){
        return new ArrayList<T>();
    }

    public static <T>LinkedList<T> linkedList(){
        return new LinkedList<T>();
    }

    public static <T>HashSet<T>  set(){
        return new HashSet<T>();
    }

    public static <T>LinkedHashSet<T> linkedSet(){
        return new LinkedHashSet<T>();
    }

    public static <K,V>LinkedHashMap<K,V> linkedMap(){
        return new LinkedHashMap<K,V>();
    }

    public static <K,V>ConcurrentHashMap<K,V> concurrentHashMap(){
        return new ConcurrentHashMap<K,V>();
    }
}
