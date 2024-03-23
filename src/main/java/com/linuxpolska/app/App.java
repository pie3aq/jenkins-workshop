package com.linuxpolska.app;

import java.time.LocalDateTime;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        while (true) { // Nieskończona pętla
            LocalDateTime now = LocalDateTime.now(); 
            System.out.println(now + " Hello World!"); 

            try {
                Thread.sleep(5000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }
}
