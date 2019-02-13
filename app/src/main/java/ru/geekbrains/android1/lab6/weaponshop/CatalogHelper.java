package ru.geekbrains.android1.lab6.weaponshop;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

/**
 *  Created by Savronov Yuriy on 21.04.2017.
 *
 *  Класс помогает получить доступ к ресурсам Категорий продукции (Каталог продукции) и
 *  к названиям Подкатегорий продукции.
 */

public class CatalogHelper {

    private static final int LAND_FORCES_CATEGORY_ID         = 0;
    private static final int AEROSPACE_SYSTEMS_CATEGORY_ID   = 1;
    private static final int NAVAL_SYSTEMS_CATEGORY_ID       = 2;
    private static final int AIR_DEFENCE_SYSTEMS_CATEGORY_ID = 3;
    private static final int SPECIAL_WEAPONS_CATEGORY_ID     = 4;
    private static final int UNKNOWN_CATEGORY_ID = -1;

    //  Возвращает массив имен Категорий.
    public static String[] getCatalog(Context context) {
        return context.getResources().getStringArray(R.array.category_names);
    }

    //  Возвращает имя выбранной Категории.
    public static String getCategoryName(Context context, int categoryId) {
        String[] names = getCatalog(context);
        return names[categoryId];
    }

    //  Возвращает иконку выбранной Категории.
    public static Drawable getCategoryIcon(Context context, int categoryId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.category_icons);
        Drawable result = typedArray.getDrawable(categoryId);
        typedArray.recycle();
        return result;
    }

    //  Возвращает ID ресурса массива картинок выбранной Категории.
    private static int getCategoryImgArrayId(Context context, int categoryId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.category_images);
        int result = typedArray.getResourceId(categoryId, 0);
        typedArray.recycle();
        return result;
    }

    //  Возвращает картинку выбранной Категории по индексу картинки из массива.
    public static Drawable getCategoryImage(Context context, int categoryId, int imageId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(
                getCategoryImgArrayId(context, categoryId));
        int index = imageId % typedArray.length();
        Drawable result = typedArray.getDrawable(index);
        typedArray.recycle();
        return result;
    }

    //  Возвращает цвет выбранной Категории.
    public static int getCategoryColor(Context context, int categoryId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.category_colors);
        int result = typedArray.getColor(categoryId, 0);
        typedArray.recycle();
        return result;
    }

    //  Возвращает массив имен Подкатегорий.
    public static String[] getSubCategories(Context context, int categoryId) {
        int category = getSubCategoryId(categoryId);
        if (category == UNKNOWN_CATEGORY_ID) return null;
        return context.getResources().getStringArray(category);
    }

    //  Возвращает ID ресурса массива Подкатегорий выбранной Категории.
    private static int getSubCategoryId(int categoryId) {
        switch (categoryId) {
            case LAND_FORCES_CATEGORY_ID:
                return R.array.sub_category01;
            case AEROSPACE_SYSTEMS_CATEGORY_ID:
                return R.array.sub_category02;
            case NAVAL_SYSTEMS_CATEGORY_ID:
                return R.array.sub_category03;
            case AIR_DEFENCE_SYSTEMS_CATEGORY_ID:
                return R.array.sub_category04;
            case SPECIAL_WEAPONS_CATEGORY_ID:
                return R.array.sub_category05;
            default:
                return UNKNOWN_CATEGORY_ID;
        }
    }


}
