# FriendsBot
____
Новостной бот для моих друзей.

![](https://github.com/AlexeyMarino/FriendsBot/blob/master/screenshots/bot1.jpg)

## Built With

* [Java](https://oracle.com/java)
* [Maven](https://maven.apache.org/)
* [Spring](https://spring.io/)

Бот выполнен многопоточным и асинхронным (основной поток направляет все входящие сообщения в специальный контейнер,
второй поток забирает сообщения из входного контейнера, обрабатывает их и направляет ответ в контейнер исходящих сообщений - ответов,
и третий поток отвечает за отправку ответов. Контейнеры реализованы многопоточной очередью ArrayBlockingQueue.) 
Особое место в проекте занимает библиотека Jsoup.

## P.S.

Данный бот был создан в учебных целях для практической отработки на начальном 
этапе изучения языка программирования Java. Возможно в будущем, после того как 
я устроюсь в EPAM или в Лигу Цифровой Экономики, в своем первом отпуске я вернусь к
этому проекту, отрефакторю его, реализую весь задуманный функционал.

##Acknowledgments 

* [LomotinKonstantin](https://github.com/LomotinKonstantin) за поддержку.
