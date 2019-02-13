package ru.geekbrains.android1.lab6.weaponshop;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 *  Вложенный Фрагмент в CategoryFragment. Анимация картинок выбранной Категории.
 *  XML макет "fragment_category_image.xml" вставляется в XML макет "fragment_category.xml" в
 *  контейнер FrameLayout android:id="@+id/containerFragmentImageCategory".
 */
public class CategoryImageFragment extends Fragment {

    private static final String LOGCAT_TAG = "DEBUG";                   //  Для вывода в LogCat.
    private static final long ANIMATION_DELAY = 2000;                   //  Задержка анимации в мс.

    private int categoryId;                                             //  Сохраняем переданную из родительского Фрагмента Категорию.
    private int imageIndex;                                             //  Текущий индекс отображаемой картинки в массиве Картинок.
    private boolean isRunning;                                          //  Признак выполнения анимации Картинок.


    //  Конструктор по умолчанию можно не определять, если не определены другие Конструкторы.
    public CategoryImageFragment() {
        Log.d(LOGCAT_TAG, "Конструктор CategoryImageFragment()");
    }


    //  Создание Фрагмента с передачей параметров через Bundle.
//    public static CategoryImageFragment newInstance(int categoryId) {
//        CategoryImageFragment fragment = new CategoryImageFragment();
//        Bundle params = new Bundle();
//        params.putInt(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
//        fragment.setArguments(params);
//        return fragment;
//    }


    //  Метод для установки нового значения Категории, вызывается из Фрагмента CategoryFragment.
    public void setCategoryId(int categoryId) {
        Log.d(LOGCAT_TAG, "Метод CategoryImageFragment.setCategoryId()");

        this.categoryId = categoryId;
        this.imageIndex = 0;
    }


    //  ACHTUNG!!! Т.к. инициализация Фрагмента CategoryImageFragment ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию надо выполнить в обработчике onStart() или onResume!
    //  Т.к. метод setCategoryId() вызывается владельцем Фрагмента только после создания Фрагмента!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onCreateView()");

        return inflater.inflate(R.layout.fragment_category_image, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onStart()");
    }


    //  ACHTUNG!!! Т.к. инициализация Фрагмента CategoryImageFragment ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию надо выполнить в обработчике onStart() или onResume!
    //  Т.к. метод setCategoryId() вызывается владельцем Фрагмента только после создания Фрагмента!
    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;                                               //  Запускаем анимацию.

        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onResume()");

        Log.d(LOGCAT_TAG, "Вызов CategoryImageFragment.runAnimation(rootView)");
        runAnimation(getView());                                        //  Анимация картинок.
    }


    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;                                              //  Останавливаем анимацию.

        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onPause()");
    }


    @Override
    public void onStop() {
        super.onStop();

        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onStop()");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOGCAT_TAG, "Обработчик CategoryImageFragment.onDestroy()");
    }


    //  Анимация картинок.
    private void runAnimation(View rootView) {
        final ImageView imageCategory = (ImageView) rootView.findViewById(R.id.imageCategory);
        final Handler handler = new Handler();

        String categoryName = CatalogHelper.getCategoryName(getActivity(), categoryId);
        imageCategory.setContentDescription(categoryName);

        //  Анимация картинок.
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    Log.d(LOGCAT_TAG, "Метод CategoryImageFragment.run()");
                    Log.d(LOGCAT_TAG, "run().categoryId = " + categoryId);
                    Log.d(LOGCAT_TAG, "run().imageIndex = " + imageIndex);

                    //  Вывод картинки Категории.
                    imageCategory.setImageDrawable(
                            CatalogHelper.getCategoryImage(getActivity(), categoryId, imageIndex));

                    imageIndex++;

                    handler.postDelayed(this, ANIMATION_DELAY);
                }
            }
        });

    }

}
