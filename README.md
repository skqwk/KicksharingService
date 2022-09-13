Финальное задание по дополнительным курсам от ООО "SENLA" "Java Developer"

# Система управления прокатом электросамокатов

## Оглавление
- [Что внутри?](#what_inside)
- [Какие инструменты используются?](#tools)
- [Сборка и деплой](#build_and_deploy)
- [Логирование](#logging)

## <a name="what_inside"></a> Что внутри?

![image](https://user-images.githubusercontent.com/71013663/189409786-d80960ac-e62f-4a14-9789-23691236eff6.png)

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

## <a name="build_and_deploy"></a> Сборка и деплой

Для сборки проекта нужно запустить скрипт `build.sh`, для запуска сервиса и БД в Docker - `run.sh`, для остановки - `down.sh`

## <a name="logging"></a> Логирование

Логирование настроено в файле `src/main/resources/logback.xml`. На уровне `INFO` пишутся логи в файл `logs/operations.log`, на уровне `DEBUG` в консоль. При размере файла `operations.log > 10MB`, создается новый `operations.log`, а старый добавляется в `logs/archived` 