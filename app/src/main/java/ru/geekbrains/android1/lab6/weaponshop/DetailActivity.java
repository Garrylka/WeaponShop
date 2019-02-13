package ru.geekbrains.android1.lab6.weaponshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  Детальная информация о Продукте.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String LOGCAT_TAG = "DetailActivity";          //  Для вывода в LogCat.

    public static final String EXTRA_SUBCATEGORY_ID = "SUBCATEGORY_ID"; //  Для Интента.
    public static final String EXTRA_PRODUCT_ID     = "PRODUCT_ID";     //  Для Интента.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.d(LOGCAT_TAG, "onCreate()");

        //  Получение ID выбранной Подкатегории и Продукта.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();                                                   //  Ничего не передано, выходим из Activity!
            return;
        }
        if (!bundle.containsKey(EXTRA_SUBCATEGORY_ID)) {
            finish();                                                   //  Нет ключа, выходим из Activity!
            return;
        }
        if (!bundle.containsKey(EXTRA_PRODUCT_ID)) {
            finish();                                                   //  Нет ключа, выходим из Activity!
            return;
        }

        int subcategoryId = bundle.getInt(EXTRA_SUBCATEGORY_ID);
        int productId =     bundle.getInt(EXTRA_PRODUCT_ID);

        Log.d(LOGCAT_TAG, "subcategoryId = " + subcategoryId);
        Log.d(LOGCAT_TAG, "productId = " + productId);

        ProductHelper product = ProductHelper.products[subcategoryId][productId];

        //  Заполнение картинки Прдукта.
        ImageView imageProduct = (ImageView) findViewById(R.id.imageProduct);
        imageProduct.setImageResource(product.getImageResId());
        imageProduct.setContentDescription(product.getName());

        //  Заполнение наименования Продукта.
        TextView nameProduct = (TextView) findViewById(R.id.nameProduct);
        nameProduct.setText(product.toString());

        //  Заполнение описания Продукта.
        TextView descProduct = (TextView) findViewById(R.id.descProduct);
        descProduct.setText(product.getDescription());

        //  Вертикальная прокрутка. Просто указанием в макете НЕ РАБОТАЕТ! ХЗ!
        descProduct.setMovementMethod(new ScrollingMovementMethod());
    }
}
