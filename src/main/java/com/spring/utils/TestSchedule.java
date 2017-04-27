package com.spring.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/10.
 */
public class TestSchedule {

    public static void main(String[] args) {
//        for (int i = 0; i < 5; i++) {
//            final String[] a = {""};
//            Runnable runnable=new Runnable() {
//                @Override
//                public void run() {
//                        System.out.println("----run----"+ a[0]);
//                        if("".equals(a[0])){
//                            STExecutor.submit(this);
//                            a[0] +="sdfa";
//                    }
//                }
//            };
//            STExecutor.submit(runnable);
//        }
        List<String> arrayList1=new ArrayList<>();
        arrayList1.add("1");
        arrayList1.add("1");
        arrayList1.add("1");
        arrayList1.add("1");
        List<String> arrayList2=arrayList1;
//        arrayList2.add("1");
//        arrayList2.add("1");
//        arrayList2.add("1");
//        arrayList2.add("1");
        boolean b = arrayList1==(arrayList2);
        System.out.println(b);

    }
}
