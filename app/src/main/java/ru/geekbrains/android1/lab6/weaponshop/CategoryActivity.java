package ru.geekbrains.android1.lab6.weaponshop;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


/**
 *  Для Смартфонов 1 Фрагмент: Список Подкатегорий продукции выбранной Категории.
 *  Для Планшетов: активность не запускается. Активность запускается только на Смартфонах!
 */
public class CategoryActivity extends AppCompatActivity implements CategoryFragment.CategoryListListener {

    private static final String LOGCAT_TAG = "CategoryActivity";        //  Для вывода в LogCat.
    public static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";       //  Для Интента.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Log.d(LOGCAT_TAG, "onCreate()");

        //  Получение ID выбранной Категории.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();                                                   //  Ничего не передано, выходим из Activity!
            return;
        }
        if (!bundle.containsKey(EXTRA_CATEGORY_ID)) {
            finish();                                                   //  Нет ключа, выходим из Activity!
            return;
        }
        int categoryId = bundle.getInt(EXTRA_CATEGORY_ID);

        Log.d(LOGCAT_TAG, "categoryId = " + categoryId);

        if (CatalogHelper.getSubCategories(this, categoryId) == null) {
            finish();                                                   //  Неправильный categoryId, выходим из Activity!
            return;
        }

        //  Поиск Фрагмента.
        //  Еще есть метод getFragmentManager().
        //  Для support.v4 надо использовать getSupportFragmentManager().
        CategoryFragment categoryFragment =
                (CategoryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCategory);

        //  Передаем Категорию Фрагменту, он заполняет представления правильным контентом.
        categoryFragment.setCategoryId(categoryId);
    }


    //  Обработка клика на пункте Подкатегории продукции.
    @Override
    public void onSubCategoryItemClicked(int subCategoryId) {
        //  Запускаем ListActivity cо списком Продукции выбранной Подкатегории.
        //  Передаем в активность ID выбранной Подкатегории.

        //  FIXME Костыль: Проверка ID по хелперу Продукции.
        if (ProductHelper.MIN_SUBCATEGORY_ID <= subCategoryId && subCategoryId <= ProductHelper.MAX_SUBCATEGORY_ID) {
            Intent intent = new Intent(CategoryActivity.this, ProductActivity.class);
            intent.putExtra(ProductActivity.EXTRA_SUBCATEGORY_ID, subCategoryId);
            startActivity(intent);
        }
    }
}
