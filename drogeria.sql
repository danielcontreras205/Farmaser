-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-11-2020 a las 03:48:05
-- Versión del servidor: 10.4.13-MariaDB
-- Versión de PHP: 7.4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `drogeria`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizarStock` (IN `codigo` VARCHAR(15), IN `N_cantidad` TINYINT)  UPDATE medicamentos set Cantidad = N_cantidad WHERE id = codigo$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `BuscarUsuarioCambioContraseña` (`usuario` VARCHAR(10), `contraseña` VARCHAR(100))  SELECT a.user, a.password
FROM persona a
WHERE a.user like usuario and a.password LIKE contraseña$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `CodigoGaveta` (`letra` VARCHAR(1), `fila` INT)  SELECT b.id FROM stan a, gaveta b WHERE a.Letra_Stan LIKE letra and b.No_gaveta LIKE fila and b.id_Stan = a.id$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminarMedicamento` (`codigo` VARCHAR(15))  DELETE FROM medicamentos WHERE id = codigo$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminarPersonaCargo` (`cedula` VARCHAR(11))  DELETE FROM persona WHERE cc = cedula$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `TablaBackup` (IN `cedula` VARCHAR(11))  INSERT INTO backupventas 
SELECT a.id, a.Fecha, a.Hora_Venta, b.cc, b.Nombre, b.Primer_Apellido, d.id, d.NombreGenerico, d.precentacion, c.Cantidad, a.Total_Venta
FROM ventas a, persona b, venta_medicamento c, medicamentos d
WHERE b.cc like cedula and b.cc = a.cc_persona and c.id_Ventas = a.id and c.id_medicamentos = d.id$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ver_gavetas` (IN `KeyStan` VARCHAR(1))  SELECT a.No_gaveta FROM gaveta a, stan b WHERE b.Letra_Stan like KeyStan AND a.id_Stan = b.id$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `acciones`
--

CREATE TABLE `acciones` (
  `cedual` varchar(11) NOT NULL,
  `Nombre` varchar(10) DEFAULT NULL,
  `Apellido` varchar(10) DEFAULT NULL,
  `Fecha` varchar(10) DEFAULT NULL,
  `Hora` varchar(10) DEFAULT NULL,
  `Accion` varchar(9) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `backupventas`
--

CREATE TABLE `backupventas` (
  `id_Ventas` varchar(20) NOT NULL,
  `Fecha_Venta` varchar(10) DEFAULT NULL,
  `Hora_venta` varchar(6) DEFAULT NULL,
  `Cedula` varchar(11) NOT NULL,
  `Nombre` varchar(10) DEFAULT NULL,
  `Apellidos` varchar(10) DEFAULT NULL,
  `id_Medicamento` varchar(10) NOT NULL,
  `NombreGenerico` varchar(30) DEFAULT NULL,
  `Precentacion` varchar(30) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  `Total_Venta` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cargo`
--

CREATE TABLE `cargo` (
  `id` varchar(3) NOT NULL,
  `Nombre_Cargo` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cargo`
--

INSERT INTO `cargo` (`id`, `Nombre_Cargo`) VALUES
('1', 'administra'),
('2', 'empleado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gaveta`
--

CREATE TABLE `gaveta` (
  `id` varchar(4) NOT NULL,
  `No_gaveta` varchar(3) DEFAULT NULL,
  `id_Stan` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `gaveta`
--

INSERT INTO `gaveta` (`id`, `No_gaveta`, `id_Stan`) VALUES
('0', '0', '004'),
('1', '1', '004'),
('10', '0', '100'),
('11', '1', '100'),
('12', '2', '100'),
('13', '3', '100'),
('2', '2', '004'),
('20', '0', '200'),
('21', '1', '200'),
('22', '2', '200'),
('23', '3', '200'),
('24', '4', '200'),
('3', '3', '004'),
('30', '0', '300'),
('31', '1', '300'),
('32', '2', '300'),
('4', '4', '004'),
('5', '5', '004');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gaveta_medicamento`
--

CREATE TABLE `gaveta_medicamento` (
  `id_Gaveta` varchar(4) NOT NULL,
  `id_Medicamentos` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `gaveta_medicamento`
--

INSERT INTO `gaveta_medicamento` (`id_Gaveta`, `id_Medicamentos`) VALUES
('2', '123345543'),
('11', '123456789'),
('21', '123456790'),
('32', '123456791'),
('23', '123456792');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `laboratorio`
--

CREATE TABLE `laboratorio` (
  `id` varchar(10) NOT NULL,
  `Nombre` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `laboratorio`
--

INSERT INTO `laboratorio` (`id`, `Nombre`) VALUES
('001', 'Bayer'),
('002', 'ABABOR PHARMACEUTICALS'),
('003', 'ABAMED PHARMA'),
('004', 'ABELLO LINDE'),
('10283745', 'Jenfar');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medicamentos`
--

CREATE TABLE `medicamentos` (
  `id` varchar(15) NOT NULL,
  `NombreGenerico` varchar(30) DEFAULT NULL,
  `NombreComercial` varchar(30) DEFAULT NULL,
  `FechaDeVencimiento` varchar(10) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  `Funcion` varchar(300) DEFAULT NULL,
  `precentacion` varchar(30) DEFAULT NULL,
  `id_Laboratorio` varchar(10) DEFAULT NULL,
  `Precio_medicamento` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `medicamentos`
--

INSERT INTO `medicamentos` (`id`, `NombreGenerico`, `NombreComercial`, `FechaDeVencimiento`, `Cantidad`, `Funcion`, `precentacion`, `id_Laboratorio`, `Precio_medicamento`) VALUES
('123345543', 'aspirina', 'pasta', '9-5-2020', 94, 'dolores generales', 'Tabletas', '10283745', 900),
('123456789', 'Aspirina', 'ácido acetil-salicílico', '1-5-2020', 32, 'reducir las sustancias en el cuerpo que producen dolor, fiebre e inflamación. ', 'Píldoras', '001', 800),
('123456790', 'Omeprazol', 'Omapren', '15-5-2020', 15, 'acidez de estómago', 'Píldoras', '002', 1000),
('123456791', 'Paracetamol', 'acetaminofeno', '15-5-2020', 27, 'usado para reducir la fiebre. ', 'Tabletas', '002', 1000),
('123456792', 'Atorvastatina', ' Lipitor', '9-5-2020', 27, ' reduce los niveles de colesterol “malo” ', 'Tabletas', '004', 2000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `cc` varchar(11) NOT NULL,
  `Nombre` varchar(10) DEFAULT NULL,
  `Primer_Apellido` varchar(10) DEFAULT NULL,
  `Segundo_Apellido` varchar(10) DEFAULT NULL,
  `correo` varchar(50) NOT NULL,
  `user` varchar(10) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `id_Cargo` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`cc`, `Nombre`, `Primer_Apellido`, `Segundo_Apellido`, `correo`, `user`, `password`, `id_Cargo`) VALUES
('1014274480', 'Daniel ', 'Contreras', 'Abella', 'chachis1996@hotmail.com', 'daniel', '3417973cd67f37b077e56b82f0cc306f', '1'),
('1233688098', 'viviana', 'garcia', 'gomez', 'vivi_0605_garcia@hotmail.com', 'vivi', '827ccb0eea8a706c4c34a16891f84e7b', '1'),
('960117066', 'Alex', 'Correa', 'Peña', 'Alex123@hotmail.com', 'alex', '827ccb0eea8a706c4c34a16891f84e7b', '2');

--
-- Disparadores `persona`
--
DELIMITER $$
CREATE TRIGGER `tri_Cedula_BeforeIsert` BEFORE INSERT ON `persona` FOR EACH ROW BEGIN
IF new.cc < 0 THEN
signal sqlstate '22501' set message_text = 'no se pueden valores negativos';
end if;
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `stan`
--

CREATE TABLE `stan` (
  `id` varchar(3) NOT NULL,
  `Letra_Stan` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `stan`
--

INSERT INTO `stan` (`id`, `Letra_Stan`) VALUES
('004', 'D'),
('100', 'A'),
('200', 'B'),
('300', 'C');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `telefono`
--

CREATE TABLE `telefono` (
  `No_telefono` varchar(10) NOT NULL,
  `id_Laboratorio` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `telefono`
--

INSERT INTO `telefono` (`No_telefono`, `id_Laboratorio`) VALUES
('3210984657', '10283745'),
('3215670938', '002'),
('3215670939', '001'),
('3215670939', '003'),
('3215670940', '004');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id` varchar(20) NOT NULL,
  `Fecha` varchar(12) DEFAULT NULL,
  `Hora_Venta` varchar(8) DEFAULT NULL,
  `cc_persona` varchar(11) DEFAULT NULL,
  `Total_Venta` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `ventas`
--

INSERT INTO `ventas` (`id`, `Fecha`, `Hora_Venta`, `cc_persona`, `Total_Venta`) VALUES
('1111122815', '2020-11-11', '12:28:15', '1014274480', 1700);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta_medicamento`
--

CREATE TABLE `venta_medicamento` (
  `id_Ventas` varchar(20) NOT NULL,
  `id_medicamentos` varchar(15) NOT NULL,
  `Cantidad` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `venta_medicamento`
--

INSERT INTO `venta_medicamento` (`id_Ventas`, `id_medicamentos`, `Cantidad`) VALUES
('1111122815', '123345543', '1'),
('1111122815', '123456789', '1');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vistabackup`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vistabackup` (
`id_Ventas` varchar(20)
,`Fecha_Venta` varchar(10)
,`Cedula` varchar(11)
,`Nombre` varchar(10)
,`Apellidos` varchar(10)
,`Productos` bigint(21)
,`Total_Venta` int(7)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vistabackup`
--
DROP TABLE IF EXISTS `vistabackup`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vistabackup`  AS  select `a`.`id_Ventas` AS `id_Ventas`,`a`.`Fecha_Venta` AS `Fecha_Venta`,`a`.`Cedula` AS `Cedula`,`a`.`Nombre` AS `Nombre`,`a`.`Apellidos` AS `Apellidos`,count(0) AS `Productos`,`a`.`Total_Venta` AS `Total_Venta` from `backupventas` `a` group by `a`.`id_Ventas` ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `acciones`
--
ALTER TABLE `acciones`
  ADD PRIMARY KEY (`cedual`);

--
-- Indices de la tabla `backupventas`
--
ALTER TABLE `backupventas`
  ADD PRIMARY KEY (`id_Ventas`,`Cedula`,`id_Medicamento`);

--
-- Indices de la tabla `cargo`
--
ALTER TABLE `cargo`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `gaveta`
--
ALTER TABLE `gaveta`
  ADD PRIMARY KEY (`id`,`id_Stan`),
  ADD KEY `id_Stan` (`id_Stan`);

--
-- Indices de la tabla `gaveta_medicamento`
--
ALTER TABLE `gaveta_medicamento`
  ADD PRIMARY KEY (`id_Medicamentos`,`id_Gaveta`),
  ADD KEY `id_Gaveta` (`id_Gaveta`);

--
-- Indices de la tabla `laboratorio`
--
ALTER TABLE `laboratorio`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `medicamentos`
--
ALTER TABLE `medicamentos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_Laboratorio` (`id_Laboratorio`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`cc`),
  ADD KEY `id_Cargo` (`id_Cargo`);

--
-- Indices de la tabla `stan`
--
ALTER TABLE `stan`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `telefono`
--
ALTER TABLE `telefono`
  ADD PRIMARY KEY (`No_telefono`,`id_Laboratorio`),
  ADD KEY `id_Laboratorio` (`id_Laboratorio`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cc_persona` (`cc_persona`);

--
-- Indices de la tabla `venta_medicamento`
--
ALTER TABLE `venta_medicamento`
  ADD PRIMARY KEY (`id_Ventas`,`id_medicamentos`),
  ADD KEY `id_medicamentos` (`id_medicamentos`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `gaveta`
--
ALTER TABLE `gaveta`
  ADD CONSTRAINT `gaveta_ibfk_1` FOREIGN KEY (`id_Stan`) REFERENCES `stan` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `gaveta_medicamento`
--
ALTER TABLE `gaveta_medicamento`
  ADD CONSTRAINT `gaveta_medicamento_ibfk_1` FOREIGN KEY (`id_Gaveta`) REFERENCES `gaveta` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `gaveta_medicamento_ibfk_2` FOREIGN KEY (`id_Medicamentos`) REFERENCES `medicamentos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `medicamentos`
--
ALTER TABLE `medicamentos`
  ADD CONSTRAINT `medicamentos_ibfk_1` FOREIGN KEY (`id_Laboratorio`) REFERENCES `laboratorio` (`id`);

--
-- Filtros para la tabla `persona`
--
ALTER TABLE `persona`
  ADD CONSTRAINT `persona_ibfk_1` FOREIGN KEY (`id_Cargo`) REFERENCES `cargo` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `telefono`
--
ALTER TABLE `telefono`
  ADD CONSTRAINT `telefono_ibfk_1` FOREIGN KEY (`id_Laboratorio`) REFERENCES `laboratorio` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `ventas_ibfk_1` FOREIGN KEY (`cc_persona`) REFERENCES `persona` (`cc`) ON DELETE CASCADE;

--
-- Filtros para la tabla `venta_medicamento`
--
ALTER TABLE `venta_medicamento`
  ADD CONSTRAINT `venta_medicamento_ibfk_1` FOREIGN KEY (`id_Ventas`) REFERENCES `ventas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `venta_medicamento_ibfk_2` FOREIGN KEY (`id_medicamentos`) REFERENCES `medicamentos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `venta_medicamento_ibfk_3` FOREIGN KEY (`id_medicamentos`) REFERENCES `medicamentos` (`id`),
  ADD CONSTRAINT `venta_medicamento_ibfk_4` FOREIGN KEY (`id_medicamentos`) REFERENCES `medicamentos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
