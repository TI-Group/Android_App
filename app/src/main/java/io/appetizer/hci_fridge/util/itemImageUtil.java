package io.appetizer.hci_fridge.util;

import java.util.Map;

import io.appetizer.hci_fridge.R;

/**
 * Created by user on 2018/6/24.
 */

public class itemImageUtil {
    private static int itemId2image[] = {
            R.mipmap.grape,
            R.mipmap.soft_drinks,
            R.mipmap.litchi,
            R.mipmap.pineapple,
            R.mipmap.chinese_cabbage,
            R.mipmap.watermelon,
            R.mipmap.milk,
            R.mipmap.banana,
            R.mipmap.cucumber,
            R.mipmap.tomato
    };

    public static int getImage(int itemId){
        return itemId2image[itemId];
    }
}
