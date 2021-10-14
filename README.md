<h2>Урок 1. Шаблон проекта и обзор стандартных компонентов</h2>
1. Изучите API NASA, который мы будем использовать в течение курса, и продумайте, как вы выстроите своё приложение, какие там будут экраны.<br>
2. Создайте первый экран приложения с фотографией дня.<br>
3. Добавьте описание (приходит с сервера) под фотографией в виде текстовой подписи или в виде BottomSheet.<br>
4. Добавьте текстовое поле для поиска неизвестных слов в «Википедии».<br>
5. Добавьте адаптивную иконку для вашего приложения.<br>
6. Имплементируйте нижнюю навигацию.<br>
7. Определитесь с архитектурой вашего приложения (MVVM, MVP, Clean).<br>
8. Добавьте чипы над фото для сегодняшней, вчерашней и позавчерашней фотографии (пример запроса) или для фото в формате HD. По нажатию на соответствующий чип загружайте соответствующие данные и обновляйте их на экране. API Picture of the Day.<br>

<h2>Урок 2. Темы приложения</h2>
1. Создайте стили для ваших компонентов и применяйте эти стили.<br>
2. Создайте две темы для вашего приложения и в отдельном экране настроек добавьте возможность менять эти темы. Тем может быть больше двух, и это не обязательно тёмная и светлая темы. Это могут быть, например, космическая, лунная, марсианская темы.<br>
3. Создайте свой уникальный стиль для приложения и его виджетов.<br>
4. Используйте свои шрифты в приложении.<br>
5. Примените автоматическую тёмную тему для девайсов на 10+ Android.<br>

<h2>Урок 3. Навигация</h2>
1. Теперь вы знаете про навигацию всё. Определитесь с навигацией в вашем приложении. В нём сейчас есть главный экран с фото дня и выход в «Википедию», условный экран настроек и заглушки для экранов с фото Земли, Марса, погоды. Но вы можете отображать всё или что-то из следующего: <br>
  - фото текущего дня, вчерашнего дня, выбранной даты (в высоком и низком разрешении);<br>
  - фото Земли сегодня или за любой день;<br>
  - фото Марса и погоду на нём;<br>
  - фото Луны в высоком разрешении;<br>
  - космическую погоду;<br>
  - сведения о метеоритах, наиболее близких к Земле;<br>
  - любые иные данные и фото, доступные благодаря обширному API;<br>
  - экран настроек приложения.<br>
2. Навигацию по экранам можно осуществлять различными способами или комбинировать их:<br>
  - классический NavigationDrawer;<br>
  - классический AppBar и меню сверху;<br>
  - AppBar снизу;<br>
  - BottomNavigationView;<br>
  - TabLayout + ViewPager.<br>
3. После создания экранов и навигации между ними добавьте функционал для работы с выбранными API. Фото дня вы уже скачиваете, напишите по аналогии остальные.<br>
4. Добавьте кастомную промотку экранов из примера в документации.<br>
5. Посмотрите, какие есть атрибуты у TabLayout, которые вы можете использовать в XML для украшения вкладок.<br>
6. Примените свою тему для BottomNavigationView.<br>
7. Иногда с сервера приходит ссылка не на фотографию, а на ролик на YouTube или Vimeo формата "https://www.youtube.com/embed/PpyPgJHKxSw?rel=0". Вместо отображения ошибки отображайте или сам видеоролик, или ссылку, которую можно открыть в браузере или соответствующем приложении.