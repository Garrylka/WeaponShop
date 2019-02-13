package ru.geekbrains.android1.lab6.weaponshop;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 *  Список Продукции выбранной Подкатегории.
 */
public class ProductActivity extends ListActivity {

    private static final String LOGCAT_TAG = "ProductActivity";         //  Для вывода в LogCat.

    public static final String EXTRA_SUBCATEGORY_ID = "SUBCATEGORY_ID"; //  Для Интента.

    private int subcategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOGCAT_TAG, "onCreate()");

        //  В манифесте android:label не помог вывести заголовок ListActivity!?
        setTitle(R.string.shop_product_text);

        //  Получаем ссылку на встроенный ListView.
        ListView listProducts = getListView();

        //  Получение ID выбранной Подкатегории.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();                                                   //  Ничего не передано, выходим из Activity!
            return;                                                     //  Обязательно return!
        }
        if (!bundle.containsKey(EXTRA_SUBCATEGORY_ID)) {
            finish();                                                   //  Нет ключа, выходим из Activity!
            return;                                                     //  Обязательно return!
        }

        subcategoryId = bundle.getInt(EXTRA_SUBCATEGORY_ID);

        Log.d(LOGCAT_TAG, "subcategoryId = " + subcategoryId);

        //  Код проверки убрал в вызывающую активность.
        //if (ProductHelper.MIN_SUBCATEGORY_ID > subcategoryId || subcategoryId > ProductHelper.MAX_SUBCATEGORY_ID) {
        //    Log.d(LOGCAT_TAG, "finish()");
        //    finish();
        //    return;
        //}

        final ProductHelper[] products = ProductHelper.products[subcategoryId];

        //  Создаем адаптер для сопряжения встроенного ListView с массивом Продукции.
        //ArrayAdapter<ProductHelper> listAdapter = new ArrayAdapter<>(this,
        //        android.R.layout.simple_list_item_1, products);
        ArrayAdapter<ProductHelper> listAdapter = new ArrayAdapter<ProductHelper>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, products) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(products[position].getName());
                text2.setText(products[position].getType());

                return view;
            }
        };
        listProducts.setAdapter(listAdapter);
    }

    //  Обработчик клика на пункте Подкатегории продукции.
    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        //  Запускаем активность ListView cо списком Продукции выбранной Подкатегории.
        //  Передаем в активность ID выбранной Подкатегории.
        Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_SUBCATEGORY_ID, subcategoryId);
        intent.putExtra(DetailActivity.EXTRA_PRODUCT_ID, (int)id);
        startActivity(intent);
    }
}
