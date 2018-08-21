package io.appetizer.hci_fridge.util;

/**
 * Created by user on 2018/6/4.
 */

public class Urlpath {
    public static String ip = "http://120.78.218.52:8080";

    public static String registerUrl = ip+"/fridge/userAction/userSignup";
    public static String loginUrl = ip+"/fridge/userAction/userLogin";
    public static String getItemsUrl = ip+"/fridge/fridgeAction/getItems";
    public static String getDailyItemsUrl = ip+"/fridge/userAction/getEatingRecords";
    public static String changeItemUrl = ip+"/fridge/fridgeAction/changeItem";
    public static String deleteItemUrl = ip+"/fridge/fridgeAction/deleteItem";
    public static String addItemUrl = ip+"/fridge/fridgeAction/addItem";
    public static String setRelationToFridgeUrl = ip+"/fridge/userAction/setRelationToFridge";
    public static String getRelationToFridgeUrl = ip+"/fridge/userAction/getRelationToFridge";
    public static String additemWithBarcodeUrl = ip+"/fridge/fridgeAction/addItemByBarcode";
    public static String imageUrl = ip+"/fridge/images/items/";


}
