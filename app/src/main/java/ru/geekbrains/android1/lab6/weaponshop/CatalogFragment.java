package ru.geekbrains.android1.lab6.weaponshop;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


/**
 *  Фрагмент Каталога продукции со списком Категорий продукции.
 */
public class CatalogFragment extends Fragment {

    private static final String LOGCAT_TAG = "DEBUG";                   //  Для вывода в LogCat.

    //  Определяем Интерфейс с методом обратного вызова для развязки Фрагмента и Активности
    //  при обработке клика на пункте ListView.
    //  Активность автоматически подписывается в событии onAttach() Фрагмента.
    interface CatalogListListener {
        void onCategoryItemClicked(int categoryId);
    };

    private Context listener;                                           //  Слушатель - будет Активность.


    //  Конструктор по умолчанию можно не определять, если не определены другие Конструкторы.
    public CatalogFragment() {
        // Required empty public constructor
    }


    //  Вызывается при присоединении Фрагмента к Активности.
    //  Назначаем слушателя с обработчиком onItemClicked().
    //      Странно, работает onAttach(Context context) на устройстве с API Level 16, хотя не должен!
    //      В настройках build.gradle (Module: app):
    //          compileSdkVersion 24
    //          minSdkVersion 15
    //          targetSdkVersion 24
    //          и подключен support-v4: compile 'com.android.support:support-v4:24.2.1'
    //      Для работы с фрагментами использую пакеты import android.support.v4.app.XXX.
    //      Запускаю приложение на эмуляторе с API Level 16, но onAttach(Context context) срабатывает!
    //      Возможно опять support-v4 карты путает :) , хотя это только предположение.
    @Override
    public void onAttach(Context context) {                             //  Owner - Activity.
        super.onAttach(context);
        this.listener = context;                                        //  Активность должна реализовать наш Интерфейс CatalogListListener!

        Log.d(LOGCAT_TAG, "Обработчик CatalogFragment.onAttach()");
    }


    //  Используется для выполнения инициализации Фрагмента.
    //  ACHTUNG!!! Т.к. инициализация Фрагмента CatalogFragment НЕ ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию можно выполнить в обработчике onCreateView()!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //  Получение корневого объекта View Фрагмента. Теперь можно использовать метод findViewById().
        View rootView = inflater.inflate(R.layout.fragment_catalog, container, false);

        //  final требуется указать для переменных, которые используются в Анонимном классе ArrayAdapter<>.
        //final Context context = this.listener;                          //  Получение Контекста.
        String[] catalog = CatalogHelper.getCatalog(listener);          //  Массив строк для списка из ресурсов.

        //  Заполнение списка Категорий в Каталоге продукции.
        ListView listCatalog = (ListView) rootView.findViewById(R.id.listCatalog);

        //  OLD Создание адаптера для ListView.
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        //  android.R.layout.simple_list_item_1, catalog);

        //  NEW ListView с иконками (указал свой row-макет и представление rowText).
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listener,
                R.layout.row_list_catalog, R.id.rowText, catalog) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView icon = (ImageView) view.findViewById(R.id.rowIcon);
                icon.setImageDrawable(CatalogHelper.getCategoryIcon(listener, position));
                icon.setBackgroundColor(CatalogHelper.getCategoryColor(listener, position));

                //  Этот TextView автоматически заполнится, т.к. в конструкторе ArrayAdapter<String>
                //  указаны параметры: R.id.rowText, catalog.
                //TextView text = (TextView) view.findViewById(R.id.rowText);
                //text.setText(catalog[position]);

                return view;
            }
        };
        listCatalog.setAdapter(adapter);

        //  Создаем обработчик клика на пункте Каталога продукции.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Проверка слушателя на реализацию интерфейса CatalogListListener.
                if (listener != null && listener instanceof CatalogListListener) {
                    //                    //  Вызов обработчика слушателя.
                    ((CatalogListListener) listener).onCategoryItemClicked((int) id);
                }
            }
        };
        listCatalog.setOnItemClickListener(itemClickListener);

        return rootView;
    }


    //  Вызывается перед тем, как Фрагмент становится видимым.
    //  ACHTUNG!!! Т.к. инициализация Фрагмента CatalogFragment НЕ ЗАВИСИТ от внешних параметров,
    //  то всю инициализацию можно выполнить в обработчике onCreateView()!
    @Override
    public void onStart() {
        super.onStart();
        //  Получение корневого объекта View Фрагмента. Теперь можно использовать метод findViewById().
        //View rootView = getView();
        //  Получение Контекста.
        //Context context = getActivity();
        //Context context = getContext();
    }
}
