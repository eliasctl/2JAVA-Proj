-- phpMyAdmin SQL Dump
-- version 5.2.1deb1ubuntu1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mar. 20 fév. 2024 à 21:56
-- Version du serveur : 8.0.36-0ubuntu0.23.10.1
-- Version de PHP : 8.2.10-2ubuntu1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `java`
--

-- --------------------------------------------------------

--
-- Structure de la table `items`
--

CREATE TABLE `items` (
  `id` int NOT NULL,
  `inventory` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `stock` int NOT NULL
);

--
-- Déchargement des données de la table `items`
--

INSERT INTO `items` (`id`, `inventory`, `name`, `price`, `stock`) VALUES
(1, 1, 'Papier toilette', 1.5, 150),
(2, 1, 'Pomme', 0.5, 30),
(3, 2, 'Télé', 100, 10),
(4, 2, 'Olives', 1.25, 85),
(5, 3, 'Brioche', 2.5, 40),
(6, 3, 'Tronçonneuse', 54.99, 25),
(7, 3, 'Ravioli', 3, 200);

-- --------------------------------------------------------

--
-- Structure de la table `store`
--

CREATE TABLE `store` (
  `id` int NOT NULL,
  `name` varchar(255) NOT NULL
);

--
-- Déchargement des données de la table `store`
--

INSERT INTO `store` (`id`, `name`) VALUES
(1, 'Carrefour'),
(2, 'E.Leclerc'),
(3, 'LIDL');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `pseudo` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'EMPLOYEE' COMMENT 'EMPLOYEE or MANAGER or ADMIN',
  `store` int DEFAULT NULL
);

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `email`, `pseudo`, `password`, `role`, `store`) VALUES
(1, 'admin@istore.com', 'admin', '$2a$10$VM/GfVScMMgdLVtHwABv6uOGEdX4n4jmLhoGyyansxpC3EmDiEZMC', 'ADMIN', NULL),
(2, 'manager@carrefour.fr', 'Manager Carrefour', '$2a$10$VM/GfVScMMgdLVtHwABv6uR2QK5QzoJjc9ICKAdlmJMdVh1oo4m.y', 'MANAGER', 1),
(3, 'employee@carrefour.fr', 'Employee Carrefour', '$2a$10$VM/GfVScMMgdLVtHwABv6uBhaHQ.qBqxV9zAp1FhyLlq8Iz7sNJ4i', 'EMPLOYEE', 1);

-- --------------------------------------------------------

--
-- Structure de la table `whitelist`
--

CREATE TABLE `whitelist` (
  `id` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `store` int DEFAULT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'USER' COMMENT 'EMPLOYEE / MANAGER / ADMIN'
);

--
-- Déchargement des données de la table `whitelist`
--

INSERT INTO `whitelist` (`id`, `email`, `store`, `role`) VALUES
(3, 'manager@lidl.fr', 3, 'MANAGER'),
(4, 'employee@lidl.fr', 3, 'EMPLOYEE');


-- --------------------------------------------------------

--
-- Structure de la table `reset_password`
--

DROP TABLE IF EXISTS reset_password;
CREATE TABLE `reset_password` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `whitelist`
--
ALTER TABLE `whitelist`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `items`
--
ALTER TABLE `items`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `store`
--
ALTER TABLE `store`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `whitelist`
--
ALTER TABLE `whitelist`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
