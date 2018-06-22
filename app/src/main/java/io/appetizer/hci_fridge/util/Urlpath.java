package io.appetizer.hci_fridge.util;

/**
 * Created by user on 2018/6/4.
 */

public class Urlpath {
    public static String ip = "http://120.78.218.52:8080";

    public static String registerUrl = ip+"/fridge/userAction/userSignup";
    public static String loginUrl = ip+"/fridge/userAction/userLogin";
    public static String getItemsUrl = ip+"/fridge/fridgeAction/getItems";
    public static String changeItemUrl = ip+"/fridge/fridgeAction/changeItem";


}
