insert into profile(name, telegram_id, role, created_at, updated_at)
    values ('Костя', 110437153, 'ADMIN', now(), now());
-- soft drinks
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(1, 'Кока-кола', 'MILLILITERS', 0, 42, 1500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(2, 'Грейпфрутовый сок', 'MILLILITERS', 0, 42, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(3, 'Грейпфрутовый лимонад', 'MILLILITERS', 0, 20, 1000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(4, 'Ананасовый сок', 'MILLILITERS', 0, 44, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(5, 'Апельсиновый сок', 'MILLILITERS', 0, 45, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(6, 'Яблочный сок', 'MILLILITERS', 0, 40, 1000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(7, 'Имбирный эль', 'MILLILITERS', 0, 39, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(8, 'Клюквенный сок', 'MILLILITERS', 0, 46, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(9, 'Сок лайма', 'MILLILITERS', 0, 29, 400, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(10, 'Сок лимона', 'MILLILITERS', 0, 28, 400, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(11, 'Спрайт', 'MILLILITERS', 0, 18, 2000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(12, 'Тоник', 'MILLILITERS', 0, 18, 1500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(13, 'Содовая', 'MILLILITERS', 0, 0, 1500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(26, 'Сироп Сахарный тростник', 'MILLILITERS', 0, 256, 1000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(28, 'Кокосовые сливки', 'MILLILITERS', 0, 246, 500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(29, 'Сироп Гренадин', 'MILLILITERS', 0, 256, 250, now(), now());
-- alcohol drinks
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(15, 'Белый ром', 'MILLILITERS', 40, 234, 700, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(16, 'Водка', 'MILLILITERS', 40, 220, 500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(17, 'Коньяк', 'MILLILITERS', 40, 230, 250, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(18, 'Джин', 'MILLILITERS', 40, 220, 1000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(19, 'Ликер Куантро', 'MILLILITERS', 40, 329, 700, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(20, 'Белуга хантинг', 'MILLILITERS', 40, 280, 1000, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(21, 'Текила', 'MILLILITERS', 38, 210, 700, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(22, 'Водка грейпфрут', 'MILLILITERS', 38, 210, 500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(23, 'Трипл-сек', 'MILLILITERS', 40, 330, 700, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(24, 'Ликер персиковый', 'MILLILITERS', 30, 200, 500, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(25, 'Ликер кокосовый', 'MILLILITERS', 21, 200, 700, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(30, 'Пиво светлое', 'MILLILITERS', 5, 45, 1350, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(31, 'Пиво светлое н/ф', 'MILLILITERS', 5, 43, 3150, now(), now());
insert into ingredient(id, name, count_type, alcohol, calories, volume,  created_at, updated_at)
    values(32, 'Вино белое сухое', 'MILLILITERS', 11, 70, 750, now(), now());
-- recipes
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(1, 'Космополитен',
    'Добавьте все ингредиенты в шейкер, наполненный льдом. Хорошо встряхните и процедите в большой бокал для коктейлей.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(2, 'Cuba Libre',
    'Перелейте все ингредиенты в бокал для хайбола, наполненный льдом.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(3, 'Лошадиная шея',
    'Налейте коньяк и имбирный эль непосредственно в бокал для хайбола с кубиками льда. Осторожно перемешайте.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(4, 'Long Island Ice Tea',
    'Добавьте все ингредиенты в стакан для хайбола, наполненный льдом. Осторожно перемешайте.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(5, 'Маргарита',
    'Добавьте все ингредиенты в шейкер со льдом. Встряхните и процедите в охлажденный бокал для коктейля.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(6, 'Мохито',
    'Смешайте мятный ликер с сахаром и соком лайма. Добавьте немного содовой воды и наполните стакан льдом. Налейте ром и долейте содовую воду. Слегка перемешайте, чтобы смешались все ингредиенты.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(7, 'Пина Колада',
    'Все в шейкер(шейкер) и взбить. Можно увеличить сока до 90 мл.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(8, 'Морской бриз',
    'Смешайте все ингредиенты в стакане для хайбола, наполненном льдом.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(9, 'Секс на пляже',
    'Смешайте все ингредиенты в стакане для хайбола, наполненном льдом.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(10, 'Текила Санрайз',
    'Налейте текилу и апельсиновый сок непосредственно в стакан для хайбола, наполненный кубиками льда. Добавьте сироп гренадин.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(11, 'Палома',
    'Налейте текилу в стакан для хайбола, выжмите сок лайма. Добавьте лед и соль, добавьте розовую грейпфрутовую содовую. Осторожно перемешайте.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(12, 'Malibu Bay Breeze',
    'Добавьте немного льда и перелейте все ингредиенты в бокал.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(13, 'Том коллинз',
    'Налей в шейкер лимонный сок, сахарный сироп и джин. Наполни шейкер кубиками льда и взбей. Долей содовую доверху и аккуратно размешай коктейльной ложкой.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(14, 'Яблочный джин-тоник',
    'Наполни бокал льдом. Влей джин и все остальные ингредиенты, кроме тоника. Добавь мяту и яблоки, аккуратно перемешай и долей коктейль тоником.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(15, 'Кислый серфер',
    'Встряхни все ингредиенты в шейкере со льдом до однородности. Процеди коктейль в бокал, наполненный колотым льдом.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(16, 'Егерь мул',
    'Наполни бокал льдом, влей сок лайма, а потом – Егермейстер и имбирный эль. Перемешай коктейль барной ложкой.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(17, 'Апельсиновый Егермейстер',
    'Наполни бокал кубиками льда. Влей Егермейстер, гренадин и долей все апельсиновым соком.',
    true,
    now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(28, 'Пиво светлое', 'Налить в стакан.', true, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(29, 'Пиво светлое н/ф', 'Налить в стакан.', true, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(30, 'Вино белое сухое', 'Налить в бокал.', true, now(), now());
-- recipe-ingredient
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(1, 22, 40);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(1, 19, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(1, 9, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(1, 8, 30);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(2, 15, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(2, 1, 120);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(2, 9, 10);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(3, 17, 40);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(3, 7, 120);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(3, 20, 5);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 16, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 21, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 15, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 18, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 19, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 10,30);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 26, 20);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(4, 1, 70);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(5, 21, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(5, 23, 20);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(5, 9, 15);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(6, 15, 45);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(6, 9, 20);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(6, 26, 15);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(6, 13, 100);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(7, 15, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(7, 28, 30);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(7, 4, 50);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(8, 16, 40);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(8, 8, 120);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(8, 2, 30);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(9, 16, 40);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(9, 24, 20);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(9, 5, 40);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(9, 8, 40);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(10, 21, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(10, 5, 100);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(10, 29, 15);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(11, 21, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(11, 9, 5);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(11, 3, 100);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(12, 25, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(12, 4, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(12, 8, 50);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(13, 18, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(13, 26, 25);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(13, 13, 100);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(13, 10, 25);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(14, 18, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(14, 10, 25);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(14, 6, 70);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(14, 26, 20);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(14, 12, 150);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(15, 20, 30);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(15, 25, 30);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(15, 4, 30);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(16, 20, 50);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(16, 7, 120);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(16, 9, 30);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(17, 5, 150);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(17, 20, 30);
insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(17, 29, 15);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(28, 30, 450);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(29, 31, 450);

insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(30, 32, 125);

--soft drinks
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(18, 'Кока-кола', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(19, 'Грейпфрутовый сок', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(20, 'Грейпфрутовый лимонад', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(21, 'Ананасовый сок', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(22, 'Апельсиновый сок', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(23, 'Яблочный сок', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(24, 'Клюквенный сок', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(25, 'Спрайт', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(26, 'Тоник', 'Налить в стакан.', false, now(), now());
insert into recipe(id, name, instruction, alcoholic, created_at, updated_at)
    values(27, 'Имбирный эль', 'Налить в стакан.', false, now(), now());

 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(18, 1, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(19, 2, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(20, 3, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(21, 4, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(22, 5, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(23, 6, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(24, 8, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(25, 11, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(26, 12, 200);
 insert into recipe_ingredient(recipe_id, ingredient_id, `count`) values(27, 7, 200);