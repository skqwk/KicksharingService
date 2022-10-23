Курсовая работа 5го семестра по дисциплине РСЧИР - "Разработка Серверных Частей Интернет-Ресурсов"

# Система управления прокатом электросамокатов

## Оглавление
- [Что внутри?](#what_inside)
- [Какие инструменты используются?](#tools)
- [Разработка](#development)
    - [Схема БД](#scheme) 
    - [Логирование](#logging)
- [Сборка и деплой](#build_and_deploy)

## <a name="what_inside"></a> Что внутри?

![image](https://user-images.githubusercontent.com/71013663/190074470-a3e2a7a0-a18b-472f-923c-4f71f2af5423.png)

## <a name="tools"></a> Какие инструменты используются?

Для разработки приложения использовались:
- Java-фреймворк Spring Boot
    - Starter Data JPA для ORM
    - Starter Test для тестирования
    - Starter Validation для проведения валидации запросов
    - Starter Web для быстрой разработки RESTful сервисов
- Библиотека Lombok для уменьшения шаблонного кода
- База данных PostgreSQL для продакшена
- База данных H2 для тестирования
- Docker для запуска упакованного jar-архива
- Maven для сборки приложения
- Swagger для создания документации

## <a name="development"></a> Разработка
### <a name="scheme"></a> Схема БД


![image](https://user-images.githubusercontent.com/71013663/190073860-f5b587f5-5d65-47e9-bd2e-5dba09d26fab.png)

### <a name="logging"></a> Логирование

Логирование настроено в файле `src/main/resources/logback.xml`. На уровне `INFO` пишутся логи в файл `logs/operations.log`, на уровне `DEBUG` в консоль. При размере файла `operations.log > 10MB`, создается новый `operations.log`, а старый добавляется в `logs/archived` 

## <a name="build_and_deploy"></a> Сборка и деплой

Для сборки проекта нужно запустить скрипт `build.sh`, для запуска сервиса и БД в Docker - `run.sh`, для остановки - `down.sh`
