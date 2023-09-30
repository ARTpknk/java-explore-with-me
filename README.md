# Java-ExploreWithMe
## Перед вами бэкенд для сервиса с афишами, аналог Яндекс Афиши или Timepad
### Пользователи могут создавать свои мероприятия и откликаться на мероприятия других пользователей
### Пользователи имеют возможность подписаться друг на друга и получать мероприятия из своих подписок
#### Сервис хранит информацию о:
* __Мероприятиях:__
  * Название 
  * Описание
  * Категория
  * Дата проведения
  * Локация
  * Создатель 
  * Платное ли мероприятие
  * Максимальное количество участников
  * Сколько участников уже записалось
* __Пользователях:__
  * Имя
  * Почта
  * Количество подписчиков
  * Количество подписок
* __Запросах на участие:__ 
  * Кто запрашивает
  * Само мероприятие
  * Статус запроса

В работе используются Spring Boot, SQL, Lombok, JpaRepository, REST контроллеры, Docker <br>

__Для запуска приложения__ необходимо создать Базы Данных PostgreSQL-15: main и stats с пользователем postgres и паролем postgres <br>
Приложение запускается через Docker. После maven package необходимо через командную строку ввести команду docker compose up

Сервис разделён на два приложения: <br>
__Main-service__ - основной сервис<br>
__Stats-service__ - сервис для сбора статистики (количество просмотров у афиши, общие и уникальные) <br>
В перспективе будет создано ещё одно отдельное приложение Gateway для работы с запросами пользователей, 
чтобы основное приложение не тратило время и ресурсы на обработку некорректных запросов.

В приложении используется кодировка UTF-8

##### Для взаимодействия с сервисом созданы следующие эндпоинты:
##### Запросы админа:
* __Создать пользователя__ : POST http://localhost:8080/admin/users
{
"name": "user",
"email": "user@user.com"
} <br>
* __Удалить пользователя__ : DELETE http://localhost:8080/admin/users/{userId} <br>
* __Получить пользователей__ : GET http://localhost:8080/admin/users <br>
RequestParams: 
  * Long[] __ids__ (Получить пользователей по их id), not required
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
<br>


* __Создать категорию__ : POST http://localhost:8080/admin/categories
{
"name": "Спорт"
} <br>
* __Обновить категорию__ : PATCH http://localhost:8080/admin/categories/{categoryId}
{
"name": "Театр"
} <br>
* __Удалить категорию__ : DELETE http://localhost:8080/admin/categories/{categoryId} <br>


* __Опубликовать или отклонить в публикации мероприятие__ : PATCH http://localhost:8080/admin/events/{eventId}
  {
  "stateAction": "PUBLISH_EVENT"
  } <br>
или
  {
  "stateAction": "REJECT_EVENT"
  } <br>
* __Получить список мепроприятий__ : GET http://localhost:8080/admin/events
В RequestParam указываются фильтры для мероприятий:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
  * Long[] __users__ (Получить мероприятия только указанных пользователей), not required
  * Spring[] __states__ (В каком статусе мероприятия): PENDING, PUBLISHED, CANCELED, REJECTED, not required
  * Long[] categories (Получить мероприятия только с указанными категориями), not required
  * LocalDateTime rangeStart (Получить мероприятия, которые будут после указанного времени), not required
  * LocalDateTime rangeEnd (Получить мероприятия, которые будут раньше указанного времени), not required
  * String text (Поиск по тексту), not required
  * Boolean paid (Платные ли мероприятия), not required
  * String sort (Если указать sort = "VIEWS", то будет сортировка по количеству просмотров событий), not required
  * Boolean onlyAvailable , not required <br>
  Все фильтры являются необязательными <br>
  Пример запроса с фильтрами: GET http://localhost:8080/admin/events?states=PUBLISHED&categories=1&size=1000

  
* __Создать подборку событий__ : POST http://localhost:8080/admin/compilations
  {
  "events": [1, 2],
  "pinned": "true",
  "title" : "Главные события недели"
  } <br>
* __Обновить подборку событий__ : PATCH http://localhost:8080/admin/compilations/{compilationId}
  {
  "events": [3, 4]
  } <br>
* __Удалить подборку событий__ : DELETE http://localhost:8080/admin/compilations/{compilationId}

##### Запросы зарегестрированных пользователей (private):
* __Создать событие__ : POST http://localhost:8080/users/{userId}/events
{
"annotation": "Посещение футбольного матча Реал Мадрид : Барселона",
"category": 1,
"description": "Реал Мадрид : Барселона. Арена Химки. Матч за выход в РПЛ",
"eventDate": "2024-12-31 15:10:05",
"location": {
"lat": 55.75,
"lon": 37.62
},
"paid": true,
"participantLimit": 10,
"requestModeration": false,
"title": "Реал Мадрид : Барселона"
} <br>
* __Пользователь получает своё событие__ : GET http://localhost:8080/users/{userId}/events/{eventId}
* __Пользователь получает свои события__ : GET http://localhost:8080/users/{userId}/events <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
  <br>
* __Изменить событие__ : PATCH http://localhost:8080/users/{userId}/events/{eventId}
  {
  "description": "Реал Мадрид : Барселона. Сантьяго Бернабеу. Матч за чемпионство Ла лиги",
  "requestModeration": true
  } <br>


* __Подать заявку на участие в мероприятии__ : POST http://localhost:8080/users/{userId}/requests?eventId=1 <br>
RequestParams:
  * int eventId (Мероприятие, на которое откликается пользователь) <br>
* __Отменить заявку на участие в мероприятии__ : PATCH http://localhost:8080/users/{userId}/requests/{requestId}/cancel <br>
* __Получить все свои заявки на участие в мероприятиях__ : GET http://localhost:8080/users/{userId}/requests <br>
* __Получить список запросов на своё мероприятие__ : GET http://localhost:8080/users/{userId}/events/requests
* __Принять или отказать в участии в мероприятии__ : PATCH http://localhost:8080/users/{userId}/events/requests
  {
  "requestIds": [
  1,
  2,
  3
  ],
  "status": "CONFIRMED"
  } <br>
То есть указываются id реквестов и даётся ответ. Возможны два статуса: CONFIRMED и REJECTED


* __Подписаться на другого пользователя__ : POST http://localhost:8080/users/{userId}/subscription/{creatorId} <br>
* __Отписаться от пользователя__ : DELETE http://localhost:8080/users/{userId}/subscription/{creatorId} <br>
* __Получить список пользователей, на которых user подписан__ : GET http://localhost:8080/users/{userId}/subscription <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
    <br>
* __Получить список мероприятий, на которых user подписан__ : GET http://localhost:8080/users/{userId}/subscription/events <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
    <br>
* __Получить список пользователей, которые подписаны на user__ : GET http://localhost:8080/users/{userId}/subscription/subscribers <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
    <br>

##### Запросы незарегестрированных пользователей (public) :

* __Получить категории__ : GET http://localhost:8080/categories <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 10
  <br>

* __Получить категорию__ : GET http://localhost:8080/categories/{categoryId}

* __Получить подборки событий__ : GET http://localhost:8080/compilations <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 10
  <br>

* __Получить подборку событий__ : GET http://localhost:8080/compilations/{compilationId} <br>

* __Получить событие__ : GET http://localhost:8080/events/{eventId} ,выдаст только публичное событие, 
так как запрос не от создателя события или админа
* __Получить список событий__ : GET http://localhost:8080/events ,выдаст только публичные события,
так как запрос не от создателя события или админа <br>
  В RequestParam указываются фильтры для мероприятий:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какого размера список нужно получить), default value = 10
  * Long[] __users__ (Получить мероприятия только указанных пользователей), not required
  * Spring[] __states__ (В каком статусе мероприятия): PENDING, PUBLISHED, CANCELED, REJECTED, not required
  * Long[] categories (Получить мероприятия только с указанными категориями), not required
  * LocalDateTime rangeStart (Получить мероприятия, которые будут после указанного времени), not required
  * LocalDateTime rangeEnd (Получить мероприятия, которые будут раньше указанного времени), not required
  * String text (Поиск по тексту), not required
  * Boolean paid (Платные ли мероприятия), not required
  * String sort (Если указать sort = "VIEWS", то будет сортировка по количеству просмотров событий), not required
    Все фильтры являются необязательными <br>
    Пример запроса с фильтрами: GET http://localhost:8080/events?categories=1&size=1000
Сервис использует базу данных PostgreSql
###
Схема базы данных Main-service <br>
![DB Main-service](https://github.com/ARTpknk/java-explore-with-me/assets/108333044/568c5cac-21a5-402a-b03b-9072d138058f) <br>
Схема базы данных Stats-service <br>
![DB Stats-Service](https://github.com/ARTpknk/java-explore-with-me/assets/108333044/e2e1a01c-9177-4e91-9dca-1c81c2b6ac59) <br>
###
В перспективе будут добавлены следующие функции:
* Получение топ 5 самых популярных пользователей по количеству подписчиков
* Закрытые мероприятия, которые видят только подписчики
* Закрытые аккаунты, для подписки на которых нужно одобрение пользователя <br>