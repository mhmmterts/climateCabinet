-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3325
-- Generation Time: Jul 13, 2021 at 01:50 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `burn_in_tester`
--

-- --------------------------------------------------------

--
-- Table structure for table `benutzer`
--

CREATE TABLE `benutzer` (
  `benutzer_id` int(11) NOT NULL,
  `password` char(30) NOT NULL,
  `name` char(30) NOT NULL,
  `nachname` char(30) NOT NULL,
  `rolle` char(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `benutzer`
--

INSERT INTO `benutzer` (`benutzer_id`, `password`, `name`, `nachname`, `rolle`) VALUES
(1, '1', 'Muhammet', 'Ertaş', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `gerate`
--

CREATE TABLE `gerate` (
  `gerate_id` int(11) NOT NULL,
  `gerate_name` char(30) NOT NULL,
  `auftrag` char(30) NOT NULL,
  `slot_id` int(11) NOT NULL,
  `benutzer_id` int(11) NOT NULL,
  `vortest_ergebnis` char(30) NOT NULL,
  `haupttest_ergebnis` char(30) NOT NULL,
  `zeit` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `gerate`
--

INSERT INTO `gerate` (`gerate_id`, `gerate_name`, `auftrag`, `slot_id`, `benutzer_id`, `vortest_ergebnis`, `haupttest_ergebnis`, `zeit`) VALUES
(11, '', '1', 1, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-09 18:50:12'),
(4212, '', '1', 2, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-09 18:50:12'),
(421, '', '213', 6, 1, 'Vortest bestanden.', 'Haupttest nicht bestanden.', '2021-07-09 18:50:12'),
(123, '', '1', 1, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-13 11:34:40'),
(4211, '', '1', 6, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-13 11:34:40'),
(5, '', '5', 2, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-13 11:41:54'),
(321, '', '1', 2, 1, 'Vortest bestanden.', 'Haupttest bestanden.', '2021-07-13 11:44:12');

-- --------------------------------------------------------

--
-- Table structure for table `klimaschrank`
--

CREATE TABLE `klimaschrank` (
  `klimaschrank_id` char(30) NOT NULL,
  `benutzer_id` char(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `klimaschrank`
--

INSERT INTO `klimaschrank` (`klimaschrank_id`, `benutzer_id`) VALUES
('1', '1'),
('2', '1'),
('3', '1');

-- --------------------------------------------------------

--
-- Table structure for table `slot`
--

CREATE TABLE `slot` (
  `slot_id` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `slot`
--

INSERT INTO `slot` (`slot_id`) VALUES
('1'),
('2'),
('3'),
('4'),
('5'),
('6'),
('7'),
('8'),
('9'),
('10'),
('11'),
('12'),
('13'),
('14'),
('15'),
('16'),
('17'),
('18'),
('19'),
('20');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
