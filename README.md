# Постирайка

### Мобильное приложение для студентов общежитий

- [X] Фича одобрена       
- [ ] Фича еще не одобрена  

#### Возможности на выполнении:
- [X] Создать проект

#### Возможности, требующие подтверждения:
- [ ] Дистанционный просмотр статуса стиральной машины в общежитиях для каждого этажа (Вкл/Выкл)
- [ ] Дистанционная очередь на стирку
- [ ] Просмотр времени окончания стирки
- [ ] Система рейтинга ответственности пользователей
  - Люди, которые не опаздывают на стирку и всегда приходят вовремя получают положительный рейтинг
  - Если человек занял очередь, но не пришел на стирку, его рейтинг снижается
- [ ] Аутентификация с проверкой пользователя на студента по ФИО
  - Подтверждение аккаунта и вход только с помощью кампусной почты 
  - Никнейм - тег почты (krs004)
- [ ] Избранное, чтобы постоянно не искать свое общежитие среди других
  - Можно сохранить этаж, на котором вы живете
- [ ] Поиск ближайшей свободной стиралки (Указывается номер этажа)
- [ ] Структура БД:
  > Общежития
  >> Этажи
  >>> Стиральные машины
  >>>> Очередь
- [ ] Общежитие:
  - Уникальный идентификатор
  - Название
  - Адрес
  - Открыть на карте
  - Этажи
- [ ] Этаж:
  - Уникальный идентификатор
  - Стиральные машины
- [ ] Стиральная машина:
  - Номер на этаже
  - Уникальный идентификатор
  - Статус (Свободна/Занята)
  - Время окончания работы: 15.11.20 18:45
  - Список людей в очереди
- [ ] Список людей в очереди:
  - Случайная аватарка, ник, рейтинг
____
![https://img.shields.io/github/v/tag/owlcodr/Washing-TimeTable](https://img.shields.io/github/v/tag/owlcodr/Washing-TimeTable) ![https://img.shields.io/github/all-contributors/owlcodr/Washing-TimeTable/master](https://img.shields.io/github/all-contributors/owlcodr/Washing-TimeTable/master) ![https://img.shields.io/aur/last-modified/Washing-TimeTable](https://img.shields.io/aur/last-modified/owlcodr/Washing-TimeTable)  ![https://img.shields.io/github/languages/top/owlcodr/Washing-TimeTable](https://img.shields.io/github/languages/top/owlcodr/Washing-TimeTable) ![https://img.shields.io/github/license/owlcodr/Washing-TimeTable](https://img.shields.io/github/license/owlcodr/Washing-TimeTable)
