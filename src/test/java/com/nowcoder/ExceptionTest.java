package com.nowcoder;

/**
 * Created by xuery on 2016/9/14.
 */
public class ExceptionTest {

    public static void main(String[] args) {
        for(int i=0;i<3;i++){
            try {
                System.out.println(3 / i);
            }catch(Exception e){
                System.out.println("catch");
            }
        }
    }
}
