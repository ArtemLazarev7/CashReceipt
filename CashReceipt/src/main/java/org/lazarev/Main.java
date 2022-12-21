package org.lazarev;


import com.sun.source.tree.PatternTree;
import org.lazarev.exception.InvalidId;
import org.lazarev.list.AbstractLists;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args)  {
        MathResult mathResult=new MathResult();
        AbstractLists abstractLists=new AbstractLists();
    //    abstractLists.startWriterCheckFail

        mathResult.translate("0-1 1-1.323 5-1.32 2-4 3-1 21-3 card 1234") ;
     //   abstractLists.endWriterCheckFail();






    }

}