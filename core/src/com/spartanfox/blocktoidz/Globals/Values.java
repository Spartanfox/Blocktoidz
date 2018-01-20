/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;

/**
 *
 * @author Ben
 */
public abstract class Values {
    public static enum Controls{Simple,Complex};
    public final static int WIDTH = 1080;
    public final static int HEIGHT = 1920;
    public final static int AD_PADDING = 50;
    public final static byte VERSION = 1;
    public final static byte SUBVERSION = 3;
    public final static byte PATCH = 3;
    public final static String FULLVERSION = VERSION+"."+SUBVERSION+"."+PATCH+"  free edition";
    public final static short MAX_CHARACTERS = 12;
    //game will expire in 7 days
    //public final static long DAYS = (42*24*60*60*1000);
    //public final static long DATE_SET = 1506245862053l;//(1505660605140l);
    //public final static long DEADLINE = DATE_SET+DAYS;
    public static String transferMessage;
    public static long progress;
    public static boolean DEBUG_MODE = false;
    public static boolean hints = false;
    public static boolean preview = false;
    public static boolean transitioning = false;
    public static Controls control = Controls.Complex;
    
}
