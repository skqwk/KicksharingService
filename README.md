Финальное задание по дополнительным курсам от ООО "SENLA" "Java Developer"

# Система управления прокатом электросамокатов

## Оглавление
- [Что внутри?](#what_inside)
- [Какие инструменты используются?](#tools)
- [Сборка и деплой](#build_and_deploy)
- [Разработка](#development)
    - [Схема БД](#scheme) 

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


## <a name="build_and_deploy"></a> Сборка и деплой

Для сборки проекта нужно запустить скрипт `build.sh`, для запуска сервиса и БД в Docker - `run.sh`, для остановки - `down.sh`
