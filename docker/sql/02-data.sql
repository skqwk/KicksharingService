-- Add models
INSERT INTO public.model (manufacturer, name)
VALUES ('China', 'Xiaomi Mi Electric Scooter Pro 2');
INSERT INTO public.model (manufacturer, name)
VALUES ('China', 'Xiaomi Mijia Electric Scooter Pro');
INSERT INTO public.model (manufacturer, name)
VALUES ('China', 'Xiaomi MIJIA Electric Scooter 1S');
INSERT INTO public.model (manufacturer, name)
VALUES ('China', 'Xiaomi Mijia M365');
INSERT INTO public.model (manufacturer, name)
VALUES ('Germany', 'Citybird T2');
INSERT INTO public.model (manufacturer, name)
VALUES ('Germany', 'Citybird Swift');
INSERT INTO public.model (manufacturer, name)
VALUES ('USA', 'Ninebot KickScooter MAX');
INSERT INTO public.model (manufacturer, name)
VALUES ('USA', 'Ninebot Max Plus');
INSERT INTO public.model (manufacturer, name)
VALUES ('USA', 'Ninebot Max Pro');
INSERT INTO public.model (manufacturer, name)
VALUES ('USA', 'Ninebot Max');
INSERT INTO public.model (manufacturer, name)
VALUES ('Russia', 'Kugoo G1 Jilong');
INSERT INTO public.model (manufacturer, name)
VALUES ('Russia', 'Kugoo Max Speed 11 Ah Jilong');
INSERT INTO public.model (manufacturer, name)
VALUES ('Russia', 'Kugoo HX Pro Jilong');
INSERT INTO public.model (manufacturer, name)
VALUES ('Russia', 'Kugoo M4 Pro 13.5Ah Jilong');

-- Add scooters
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (2, 0, 1);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (2, 0, 2);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (2, 0, 3);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (2, 0, 4);

INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 5);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 6);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 7);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 8);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 1);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 2);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 11);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 9);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 4);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 2);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 3);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 12);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 13);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 14);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 1);
INSERT INTO public.scooter (status, time_in_use, model_id)
VALUES (0, 0, 2);

-- Add tariffs
INSERT INTO public.tariff (activation_cost, description, discount, duration_in_hours, name, settlement_cost,
                           settlement_for, tariff_cost, type)
VALUES (50, 'Активируй самокат за 50 рублей и катайся сколько хочешь за 5 руб/мин!', 0, null, 'Поминутный', 5, 4,
        null, 1);

INSERT INTO public.tariff (activation_cost, description, discount, duration_in_hours, name, settlement_cost,
                           settlement_for, tariff_cost, type)
VALUES (null,
        'Купи тариф за 1500 рублей и целый месяц плати только за время проката самоката (6 руб/мин) без стоимости активации!',
        0, 720, 'Тариф с подпиской', 6, 4,
        1500, 0);

-- Add rent-points
INSERT INTO public.rent_point (latitude, longitude) VALUES (55.730404, 37.639080);
INSERT INTO public.rent_point (latitude, longitude) VALUES (55.728817, 37.625005);
INSERT INTO public.rent_point (latitude, longitude) VALUES (55.721007, 37.628986);
INSERT INTO public.rent_point (latitude, longitude) VALUES (55.736636, 37.607765);
INSERT INTO public.rent_point (latitude, longitude) VALUES (55.743287, 37.552353);

-- Add scooters to rent points
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (1, 5);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (1, 6);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (1, 7);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (5, 8);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (2, 9);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (2, 10);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (2, 11);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (5, 12);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (2, 13);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (3, 14);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (3, 15);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (3, 16);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (3, 17);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (4, 18);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (5, 19);
INSERT INTO public.rent_point_scooters (rent_point_id, scooters_id) VALUES (4, 20);



