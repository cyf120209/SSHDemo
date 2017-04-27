package com.spring.utils;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class ComPortutils {


    public static List<String> listPort() {
        List<String> listName=new ArrayList<String>();
        Enumeration enumeration= CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier portId;
        while(enumeration.hasMoreElements()){
            portId=(CommPortIdentifier)enumeration.nextElement();
            if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {
                listName.add( portId.getName());
            }
        }
        return listName;
    }
}
