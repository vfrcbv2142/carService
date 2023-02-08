INSERT INTO roles (id, name) VALUES (1, 'ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'USER');

 INSERT INTO accounts (id, login, password, role_id) VALUES (1, 'admin', '$2a$10$CJgEoobU2gm0euD4ygru4ukBf9g8fYnPrMvYk.q0GMfOcIDtUhEwC', 1);
-- 2222
 INSERT INTO accounts (id, login, password, role_id) VALUES (2, 'user',  '$2a$10$yYQaJrHzjOgD5wWCyelp0e1Yv1KEKeqUlYfLZQ1OQvyUrnEcX/rOy', 2);
-- 3333

insert into prices (id, disassembling, painting, preparing_aluminum, preparing_iron, preparing_plastic, soldering, straightening, account_id) VALUE
    (1, 30, 100, 30, 20, 25, 15, 15, 1);

insert into clients (id, email, first_name, last_name, phone_number, account_id) values (1, 'clent@gmail.com', 'clientIgor', 'gordon',
                                                                                     '+3805678769', 1);

insert into employees (id, email, first_name, last_name, phone_number, position, account_id) values (1, 'employees@gmail.com', 'employeeName',
                                                                                                 'employeeLastName', '+3800000', 'painter', 1);

insert into orders (id, creation_date, name, account_id, client_id) values (1, '2014-02-25', 'order1', 1, 1);

insert into orders_executors (order_id, employee_id) VALUES (1, 1);

insert into notes (id, text, order_id) VALUE (1, 'there are scratches at the hood. We don"t touch them', 1);

insert into items (id, name, order_id) VALUE (1, 'door', 1);
insert into item_prices (item_id, price, work_type) VALUES (1, 100, 'painting');
insert into item_prices (item_id, price, work_type) VALUES (1, 200, 'preparingPlastic');
