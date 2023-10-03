Переработанный проект Netty-Chat и Netty-Intro
Была добавленна регистрация, авторизация и хранение пользователей и их паролей в БД.

Также добавлено логирование.


Создание таблицы

CREATE TABLE "Users" (
	"id"	INTEGER NOT NULL,
	"username"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
)