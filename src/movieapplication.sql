-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 26, 2024 at 08:00 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `movieapplication`
--

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `movieID` int(11) NOT NULL,
  `movieName` varchar(100) NOT NULL,
  `movieDirector` varchar(100) NOT NULL,
  `movieGenre` varchar(100) NOT NULL,
  `movieRelease` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`movieID`, `movieName`, `movieDirector`, `movieGenre`, `movieRelease`) VALUES
(1, 'Star Wars', 'George Lucas', 'Science Fiction / Fantasy', 1977),
(3, 'The Godfather', 'Francis Ford Coppola', 'Crime / Drama', 1972),
(4, 'Inception', 'Christopher Nolan', 'Science Fiction / Thriller', 2010),
(5, 'Pulp Fiction', 'Quentin Tarantino', 'Crime / Drama', 1994),
(7, 'Inglourious Basterds', 'Quentin Tarantino', 'War / Drama', 2009),
(8, 'Interstellar', 'Christopher Nolan', 'Science Fiction / Drama', 2014),
(9, 'The Revenant', 'Alejandro González Iñárritu', 'Adventure / Drama', 2015),
(10, 'Mad Max: Fury Road', 'George Miller', 'Action / Sci-Fi', 2015),
(11, 'The Prestige', 'Christopher Nolan', 'Drama / Mystery', 2006),
(12, 'The Intouchables', 'Olivier Nakache, Éric Toledano', 'Comedy / Drama', 2011),
(13, 'The Lion King', 'Roger Allers, Rob Minkoff', 'Animation / Adventure', 1994),
(14, 'The Avengers', 'Joss Whedon', 'Superhero / Action', 2012),
(15, 'The Terminator', 'James Cameron', 'Science Fiction / Action', 1984);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `reviewID` int(11) NOT NULL,
  `movieID` int(11) NOT NULL,
  `reviewerName` varchar(100) NOT NULL,
  `movieRating` int(11) NOT NULL,
  `reviewText` varchar(10000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`reviewID`, `movieID`, `reviewerName`, `movieRating`, `reviewText`) VALUES
(1, 1, 'Hani', 4, 'Good movie!'),
(2, 1, 'Nadhira', 5, 'Incredibleeee!'),
(4, 12, 'Amanda', 5, 'The movie was gooddddd!\r\n'),
(5, 3, 'Puteri', 3, 'The movie very sad'),
(6, 4, 'Farhah', 4, 'GOODDDDD!'),
(7, 7, 'Sharon', 2, 'The movie a bit slow'),
(8, 10, 'Eilya', 5, 'AMAZING MOVIE!'),
(9, 15, 'Alya', 3, 'Not badd!'),
(10, 5, 'Hassan', 5, 'The movie was good!!!'),
(11, 8, 'Naim', 5, 'MOVIE IS AMAZINGG!'),
(12, 14, 'Hazim', 1, 'BOOO!'),
(13, 9, 'Shuib', 3, 'Storyline slow but good movie'),
(14, 11, 'Hanapi', 4, 'Good movie'),
(15, 13, 'Layang', 5, 'FANTASTICCC!'),
(16, 8, 'Akhsan', 3, 'The heroin beautifull!'),
(17, 4, 'Aziz', 5, 'Hero and heroin very sweet awww!'),
(18, 7, 'Hafilzuddin', 2, 'A bit cringe hihu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`movieID`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`reviewID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `movieID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `reviewID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
