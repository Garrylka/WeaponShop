package ru.geekbrains.android1.lab6.weaponshop;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 *  Фрагмент Категории продукции со списком Подкатегорий продукции.
 */
public class CategoryFragment extends Fragment {

    private static final String LOGCAT_TAG = "DEBUG";                   //  Для вывода в LogCat.

    private int categoryId;                                             //  Сохраняем переданную из Активности Категорию.

    //  Определяем Интерфейс с методом обратного вызова для развязки Фрагмента и Активности
    //  при обработке клика на пункте ListView.
    //  Активность автоматически подписывается в событии onAttach() Фрагмента.
    interface CategoryListListener {
        void onSubCategoryItemClicked(int subCategoryId);
    }

    private Context listener;                                           //  Слушатель - будет Активность.


    //  Конструктор по умолчанию можно не определять, если не определены другие Конструкторы.
    public CategoryFragment() {
        Log.d(LOGCAT_TAG, "Конструктор CategoryFragment()");
    }


    //  Создание Фрагмента с передачей параметров через Bundle.
//    public static CategoryFragment newInstance(int categoryId) {
//        CategoryFragment fragment = new CategoryFragment();
//        Bundle params = new Bundle();
//        params.putInt(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
//        fragment.setArguments(params);
//        return fragment;
//    }


    //  Метод для установки нового значения Категории, вызывается из Активности.
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;

        Log.d(LOGCAT_TAG, "Метод CategoryFragment.setCategoryId()");
    }


    //  Вызывается при присоединении Фрагмента к Активности.
    //  Назначаем слушателя с обработчиком onItemClicked().
    @Override
    public void onAttach(Context context) {                             //  Owner - Activity.
        super.onAttach(context);
        this.listener = context;                                        //  Активность должна реализовать наш Интерфейс CategoryListListener!

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onAttach()");
    }


    //  Отлов параметров при создании Фрагмента с передачей параметров через Bundle.
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onCreate()");
//
//        categoryId = getArguments().getInt(CategoryActivity.EXTRA_CATEGORY_ID);
//
//        Log.d(LOGCAT_TAG, "Параметр CategoryFragment.categoryId = " + categoryId);
//    }


    //  Используется для выполнения инициализации Фрагмента.
    //  ACHTUNG!!! Т.к. инициализация Фрагмента CategoryFragment ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию надо выполнить в обработчике onStart()!
    //  Т.к. метод setCategoryId() вызывается владельцем Фрагмента только после создания Фрагмента!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onCreateView()");

        //  Восстановление Категории при повороте экрана.
        if (savedState != null) {
            Log.d(LOGCAT_TAG, "Восстановление состояния в обработчике CategoryFragment.onCreateView()");

            categoryId = savedState.getInt(CategoryActivity.EXTRA_CATEGORY_ID, 0);

            Log.d(LOGCAT_TAG, "CategoryFragment.categoryId = " + categoryId);
        }

        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    //  Вызывается перед тем, как Фрагмент становится видимым.
    //  ACHTUNG!!! Т.к. инициализация Фрагмента CategoryFragment ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию надо выполнить в обработчике onStart()!
    //  Т.к. метод setCategoryId() вызывается владельцем Фрагмента только после создания Фрагмента!
    @Override
    public void onStart() {
        super.onStart();

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onStart()");

        //  Получение корневого объекта View Фрагмента. Теперь можно использовать метод findViewById().
        View rootView = getView();

        //  Получение Контекста.
        //Context context = getActivity();
        //Context context = getContext();

        //  Заполнение представлений макета.
        //  Имя Категории.
        String categoryName = CatalogHelper.getCategoryName(listener, categoryId);
        int categoryBgColor = CatalogHelper.getCategoryColor(listener, categoryId);

        //  OLD Вывод картинки Категории.
        //ImageView imageCategory = (ImageView) rootView.findViewById(R.id.imageCategory);
        //imageCategory.setImageDrawable(CatalogHelper.getCategoryImage(listener, categoryId, 0));
        //imageCategory.setContentDescription(categoryName);


        //  NEW Теперь за вывод картинки отвечает вложенный Фрагмент CategoryImageFragment!
        //  Фрагмент CategoryImageFragment является СТАТИЧЕСКИМ, т.е. во всех макетах
        //  Фрагмента CategoryFragment он определен при помощи элемента <fragment>!
        FragmentManager childFragmentManager = getChildFragmentManager();
        CategoryImageFragment fragmentImageCategory =
                (CategoryImageFragment) childFragmentManager.findFragmentById(R.id.fragmentImageCategory);

        //  Передаем Категорию вложенному Фрагменту, он заполняет представления правильным контентом.
        fragmentImageCategory.setCategoryId(categoryId);


        //  Вывод иконки Категории.
        ImageView iconCategory = (ImageView) rootView.findViewById(R.id.iconCategory);
        iconCategory.setImageDrawable(CatalogHelper.getCategoryIcon(listener, categoryId));
        iconCategory.setContentDescription(categoryName);
        iconCategory.setBackgroundColor(categoryBgColor);

        //  Вывод названия Категории.
        TextView nameCategory = (TextView) rootView.findViewById(R.id.nameCategory);
        nameCategory.setText(categoryName);
        nameCategory.setBackgroundColor(categoryBgColor);

        //  Заполнение списка Подкатегорий.
        ListView listSubCategory = (ListView) rootView.findViewById(R.id.listSubCategory);
        String[] subCategories = CatalogHelper.getSubCategories(listener, categoryId);

        //  Создание адаптера для ListView.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listener,
                android.R.layout.simple_list_item_1, subCategories);
        listSubCategory.setAdapter(adapter);

        //  FIXME Костыль на ограничение функционала приложения, т.к. Продукция заведена частично
        //  FIXME и только для Категории "СУХОПУТНЫЕ ВОЙСКА"!
        if (categoryId != 0) {
            return;                                                     //  Выходим из метода без назначения onItemClick()!
        }

        //  Создаем обработчик клика на пункте Подкатегории продукции.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Проверка слушателя на реализацию интерфейса CategoryListListener.
                if (listener != null && listener instanceof CategoryListListener) {
                    //  Вызов обработчика слушателя.
                    ((CategoryListListener) listener).onSubCategoryItemClicked((int) id);
                }
            }
        };
        listSubCategory.setOnItemClickListener(itemClickListener);
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onResume()");
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onPause()");
    }


    @Override
    public void onStop() {
        super.onStop();

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onStop()");
    }


    //  Сохранение состояния Фрагмента перед уничтожением.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onSaveInstanceState()");

        outState.putInt(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOGCAT_TAG, "Обработчик CategoryFragment.onDestroy()");
    }
}
