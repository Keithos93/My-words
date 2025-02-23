-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : jeu. 30 jan. 2025 à 22:32
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `istore`
--

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  `quantite_stock` int NOT NULL,
  `boutique_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `boutique_id` (`boutique_id`)
) ;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id`, `nom`, `prix`, `quantite_stock`, `boutique_id`) VALUES
(1, 'Ballon Adidas', 29.99, 50, 1),
(2, 'Chaussures Nike', 79.99, 30, 1),
(3, 'Maillot PSG', 89.99, 20, 1),
(4, 'Gants de Gardien', 39.99, 25, 1),
(5, 'Protège-Tibias', 19.99, 40, 1),
(6, 'Sifflet Arbitre', 9.99, 60, 1),
(7, 'Filet de But', 149.99, 5, 1),
(8, 'Sac de Sport', 39.99, 15, 1),
(9, 'Chaussettes Foot', 14.99, 100, 1),
(10, 'Short d Entraînement', 24.99, 50, 1),
(11, 'Rouge à Lèvres', 19.99, 80, 2),
(12, 'Fond de Teint', 34.99, 50, 2),
(13, 'Mascara Waterproof', 24.99, 40, 2),
(14, 'Palette Ombres', 49.99, 30, 2),
(15, 'Crayon Yeux', 9.99, 100, 2),
(16, 'Blush Rosé', 14.99, 60, 2),
(17, 'Vernis à Ongles', 7.99, 120, 2),
(18, 'Démaquillant Doux', 15.99, 90, 2),
(19, 'Pinceaux Maquillage', 29.99, 40, 2),
(20, 'Spray Fixateur', 22.99, 35, 2),
(21, 'T-shirt Blanc', 19.99, 200, 3),
(22, 'Jean Slim', 49.99, 80, 3),
(23, 'Veste en Cuir', 99.99, 30, 3),
(24, 'Robe Élégante', 79.99, 50, 3),
(25, 'Sweat à Capuche', 39.99, 60, 3),
(26, 'Chaussures Sneakers', 89.99, 40, 3),
(27, 'Casquette Sport', 14.99, 70, 3),
(28, 'Écharpe Hiver', 24.99, 90, 3),
(29, 'Lunettes de Soleil', 34.99, 50, 3),
(30, 'Montre Homme', 129.99, 25, 3),
(31, 'Air Jordan', 450.99, 10, 25);

-- --------------------------------------------------------

--
-- Structure de la table `boutique`
--

DROP TABLE IF EXISTS `boutique`;
CREATE TABLE IF NOT EXISTS `boutique` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `date_creation` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `boutique`
--

INSERT INTO `boutique` (`id`, `nom`, `date_creation`) VALUES
(1, 'Football', '2025-01-30 09:18:16'),
(2, 'MakeUp', '2025-01-30 09:18:16'),
(3, 'Vetement', '2025-01-30 09:18:16'),
(25, 'Chaussure', '2025-01-30 09:56:27');

-- --------------------------------------------------------

--
-- Structure de la table `employe`
--

DROP TABLE IF EXISTS `employe`;
CREATE TABLE IF NOT EXISTS `employe` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `date_embauche` date DEFAULT (curdate()),
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `employe`
--

INSERT INTO `employe` (`id`, `nom`, `prenom`, `email`, `telephone`, `date_embauche`) VALUES
(1, 'Dupont', 'Jean', 'jean.dupont@example.com', '0601020304', '2025-01-30'),
(2, 'Martin', 'Sophie', 'sophie.martin@example.com', '0605060708', '2025-01-30'),
(3, 'Durand', 'Paul', 'paul.durand@example.com', '0611121314', '2025-01-30'),
(4, 'Morel', 'Alice', 'alice.morel@example.com', '0615161718', '2025-01-30');

-- --------------------------------------------------------

--
-- Structure de la table `employe_boutique`
--

DROP TABLE IF EXISTS `employe_boutique`;
CREATE TABLE IF NOT EXISTS `employe_boutique` (
  `employe_id` int NOT NULL,
  `boutique_id` int NOT NULL,
  PRIMARY KEY (`employe_id`,`boutique_id`),
  KEY `fk_boutique` (`boutique_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `employe_boutique`
--

INSERT INTO `employe_boutique` (`employe_id`, `boutique_id`) VALUES
(1, 1),
(1, 2),
(2, 2),
(2, 3),
(3, 1),
(4, 3);

-- --------------------------------------------------------

--
-- Structure de la table `listeblanche`
--

DROP TABLE IF EXISTS `listeblanche`;
CREATE TABLE IF NOT EXISTS `listeblanche` (
  `email` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `listeblanche`
--

INSERT INTO `listeblanche` (`email`) VALUES
('mulambakeith3@gmail.com'),
('angeandrea@gmail.com'),
('mulambakeith3@gmail.coml');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lastName` varchar(50) DEFAULT NULL,
  `pseudo` varchar(50) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('admin','client') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `lastName`, `pseudo`, `email`, `password`, `role`) VALUES
(6, 'Mulamba', 'Keith', 'mulambakeith3@gmail.com', '$2a$10$ija98Q2P.WVZgtUrwFB5GOGjgQZWQn/Uz4rR/2NWc02x8vW5YNNOC', 'admin'),
(17, 'Konnang', 'Ange Andrea', 'angeandrea@gmail.com', '$2a$10$yCoJGHtVOBDmfVkgYx.vZenhVvfG9KlmhsTJC/c4hCp9EQi1LRiLy', 'client');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
