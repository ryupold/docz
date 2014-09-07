/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package docz;

/**
 *
 * @author Michael
 */
public class Log {
    public static void l(Exception ex){
        l(ex, true);
    }
    
    public static void l(Exception ex, boolean showStackTrace){
        System.out.println(ex.getClass().getName()+": "+ex.getMessage());
        if(showStackTrace){
            ex.printStackTrace();
        }
    }
    
    public static void l(String line){
        System.out.println(line);
    }
    
    public static void l(Object toString){
        System.out.println(toString.toString());
    }
}
