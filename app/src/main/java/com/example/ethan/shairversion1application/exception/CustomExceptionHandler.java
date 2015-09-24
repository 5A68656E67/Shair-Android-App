/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: CustomExceptionHandler
 *
 *  class properties:
 *  VALID_EMAIL_ADDRESS_REGEX: Pattern
 *
 *  class methods:
 *  checkSearchInformation(String info): boolean
 *  checkStuffName(String name): boolean
 *  checkStuffDescription(String name): boolean
 *  checkStuffPrice(String name): boolean
 *  checkStuffDuration(String name): boolean
 *  checkStuffSecurityDepositr(String name): boolean
 *  checkProfileName(String name): boolean
 *  checkRegisterName(String name): boolean
 *  checkRegisterPassword(String name): boolean
 *  checkDeadLine(int time): boolean
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.exception;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomExceptionHandler {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);


    public boolean checkSearchInformation(String info) {
        //noinspection RedundantIfStatement
        if (info.equals("") || info.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkStuffName(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressWarnings("unused")
    public boolean checkStuffDescription(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkStuffPrice(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkStuffDuration(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".") || name.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkStuffSecurityDepositr(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkProfileName(String name) {
        //noinspection RedundantIfStatement
        if (name.equals("") || name.equals(".")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkRegisterName(String name) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(name);
        //noinspection RedundantIfStatement
        if (!matcher.find()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkRegisterPassword(String name) {
        //Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(name);
//noinspection RedundantIfStatement
        if (name.contains(".") || name.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDeadLine(int time) {
        //Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(name);
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        System.out.println("local time:   " + timeStamp);
        System.out.println("input time:  " + time);
        int localtime = Integer.parseInt(timeStamp);
        //noinspection RedundantIfStatement
        if (time - localtime < 0) {
            return false;
        } else {
            return true;
        }
    }



}
