CREATE DATABASE IF NOT EXISTS `banking`;
USE `banking`;

DROP TABLE IF EXISTS `transactions`;
DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `owner_name` varchar(100) NOT NULL,
  `balance` decimal(15,2) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` int NOT NULL,
  `type` varchar(20) NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `balance_after` decimal(15,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account`(`id`),
  CHECK (`amount` > 0),
  CHECK (`type` IN ('DEPOSIT', 'WITHDRAWAL'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO account (owner_name, balance) VALUES
('Account One', 269100.00),
('Account Two', 127000.00);

INSERT INTO transactions (account_id, type, amount, description, category, balance_after, created_at) VALUES
-- Account 1
(1, 'DEPOSIT',    250000.00, 'Salary',             'income',    250000.00, '2026-09-01 09:00:00'),
(1, 'WITHDRAWAL',  85000.00, 'Rent',               'housing',   165000.00, '2026-09-03 10:00:00'),
(1, 'WITHDRAWAL',   1200.00, 'Lawson',             'food',      163800.00, '2026-09-05 12:30:00'),
(1, 'WITHDRAWAL',   3500.00, 'Suica charge',       'transport', 160300.00, '2026-09-08 08:15:00'),
(1, 'WITHDRAWAL',   4800.00, 'Gyomu Super',        'food',      155500.00, '2026-09-12 18:40:00'),
(1, 'WITHDRAWAL',  12000.00, 'Electricity bill',   'utilities', 143500.00, '2026-09-20 09:00:00'),
(1, 'WITHDRAWAL',   6500.00, 'Dinner in Shibuya',  'food',      137000.00, '2026-09-25 20:10:00'),
(1, 'DEPOSIT',    250000.00, 'Salary',             'income',    387000.00, '2026-10-01 09:00:00'),
(1, 'WITHDRAWAL',  85000.00, 'Rent',               'housing',   302000.00, '2026-10-03 10:00:00'),
(1, 'WITHDRAWAL',   2300.00, '7-Eleven',           'food',      299700.00, '2026-10-05 13:05:00'),
(1, 'WITHDRAWAL',   5000.00, 'Suica charge',       'transport', 294700.00, '2026-10-07 08:20:00'),
(1, 'WITHDRAWAL',   9800.00, 'Yakiniku',           'food',      284900.00, '2026-10-09 19:45:00'),
(1, 'WITHDRAWAL',  15800.00, 'Amazon',             'shopping',  269100.00, '2026-10-10 22:00:00'),
-- Account 2
(2, 'DEPOSIT',    100000.00, 'Transfer in',        'income',    100000.00, '2026-09-10 11:00:00'),
(2, 'WITHDRAWAL',   3000.00, 'Starbucks',          'food',       97000.00, '2026-09-15 15:30:00'),
(2, 'WITHDRAWAL',  20000.00, 'Gym membership',     'health',     77000.00, '2026-10-02 07:00:00'),
(2, 'DEPOSIT',     50000.00, 'Freelance payment',  'income',    127000.00, '2026-10-06 17:00:00');

