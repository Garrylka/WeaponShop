package ru.geekbrains.android1.lab6.weaponshop;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


/**
 *  Для Смартфонов 1 Фрагмент: Каталог Продукции (список Категорий).
 *  Для Планшетов  2 Фрагмента: Каталог Продукции + Список Подкатегорий продукции выбранной Категории.
 */
public class MainActivity extends AppCompatActivity
        implements CatalogFragment.CatalogListListener, CategoryFragment.CategoryListListener {

    private static final String LOGCAT_TAG = "MainActivity";            //  Для вывода в LogCat.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOGCAT_TAG, "onCreate()");
    }


    //  Обработка клика на пункте Категории продукции.
    @Override
    public void onCategoryItemClicked(int categoryId) {

        //  Имеем файлы макета:
        //  Для Смартфона: layout/activity_main.xml
        //  Для Планшета:  large/activity_main.xml и large-port/activity_main.xml
        //  Определяем на каком устройстве запущено приложение (Смартфон или Планшет) при помощи
        //  поиска FrameLayout контейнера для Фрагмента. Если контейнер найден, то значит Андроид
        //  использует макет для Планшета, иначе макет для Смартфона.
        if (findViewById(R.id.containerFragmentCategory) != null) {
            //  Замена Фрагмента.
            CategoryFragment categoryFragment = new CategoryFragment();

            //  Передаем Категорию Фрагменту, он заполняет представления правильным контентом.
            categoryFragment.setCategoryId(categoryId);

            //  Замена Фрагмента происходит в виде Транзакции фрагмента.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerFragmentCategory, categoryFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        } else {
            //  Запускаем активность cо списком Подкатегорий выбранной Категории Каталога продукции.
            //  Передаем в активность ID выбранной Категории.
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
            startActivity(intent);
        }
    }


    //  Будет вызываться только для устройства Планшет!
    //  Обработка клика на пункте Подкатегории продукции.
    @Override
    public void onSubCategoryItemClicked(int subCategoryId) {
        //  Запускаем ListActivity cо списком Продукции выбранной Подкатегории.
        //  Передаем в активность ID выбранной Подкатегории.

        //  FIXME Костыль: Проверка ID по хелперу Продукции.
        if (ProductHelper.MIN_SUBCATEGORY_ID <= subCategoryId && subCategoryId <= ProductHelper.MAX_SUBCATEGORY_ID) {
            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
            intent.putExtra(ProductActivity.EXTRA_SUBCATEGORY_ID, subCategoryId);
            startActivity(intent);
        }
    }
}
