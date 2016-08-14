package com.hackathon.inout.middleman;

import java.util.Vector;

/**
 * Created by Sarthak on 14/08/16.
 */
public class Filter {
    public Vector<Integer> buffer;

    public final int limit =5;
    public int decision = 0;

    public Filter(){
        buffer = new Vector<Integer>();
    }

    public int add(int a){
        if(buffer.size()<limit){
            buffer.add(a);
        }else{
            buffer.add(a);
            buffer.removeElementAt(0);
        }
        float average = 0;
        for(int i =0;i<buffer.size();i++){
            average += buffer.get(i);
        }
        System.out.printf("LOL"+String.valueOf(buffer.size()));
        average=average/buffer.size();
        if(average<0.5){
            return 0;
        }
        else{
            return 1;
        }
    }
}
