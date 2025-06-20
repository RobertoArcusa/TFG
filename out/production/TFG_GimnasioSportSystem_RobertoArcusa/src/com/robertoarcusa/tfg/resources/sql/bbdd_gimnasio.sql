CREATE DATABASE IF NOT EXISTS bbdd_gimnasio;

USE bbdd_gimnasio;

CREATE TABLE Socio (
    id_socio INT PRIMARY KEY AUTO_INCREMENT,
    nombre_socio VARCHAR(50) NOT NULL,
    apellidos_socio VARCHAR(50) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    fecha_nacimiento DATE,
    telefono VARCHAR(15),
    tipo_membresia ENUM('BASICA', 'PREMIUM', 'VIP'),
    foto_perfil LONGBLOB,
    tipo_usuario ENUM('ADMIN','EDITOR','BASIC') NOT NULL,
    contrasena varchar(300) NOT NULL
);

CREATE TABLE Entrenador (
    id_entrenador INT PRIMARY KEY AUTO_INCREMENT,
    nombre_entrenador VARCHAR(50) NOT NULL,
    apellidos_entrenador VARCHAR(50) NOT NULL,
    especialidad VARCHAR(50) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    salario DOUBLE NOT NULL,
    foto_perfil LONGBLOB
);

CREATE TABLE Clase (
    id_clase INT PRIMARY KEY AUTO_INCREMENT,
    nombre_clase VARCHAR(50) NOT NULL,
    id_entrenador INT NOT NULL,
    capacidad_maxima INT NOT NULL,
    nivel_dificultad ENUM('PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO') NOT NULL,
    sala VARCHAR(50) NOT NULL,
    imagen_clase LONGBLOB,
    FOREIGN KEY (id_entrenador) REFERENCES Entrenador(id_entrenador) ON DELETE CASCADE
);

CREATE TABLE SesionClase (
    id_sesion INT PRIMARY KEY AUTO_INCREMENT,
    id_clase INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    capacidad_disponible INT NOT NULL,
    FOREIGN KEY (id_clase) REFERENCES Clase(id_clase) ON DELETE CASCADE
);

CREATE TABLE Pago (
    id_pago INT PRIMARY KEY AUTO_INCREMENT,
    id_socio INT NOT NULL,
    fecha_pago DATE NOT NULL,
    importe DOUBLE NOT NULL,
    tipo_cuota ENUM ('MENSUAL', 'TRIMESTRAL', 'ANUAL') NOT NULL,
    tipo_pago ENUM ('MATRICULA','CUOTA','INCIDENCIA') NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL,
    estado ENUM('PAGADO', 'PENDIENTE') NOT NULL,
    recibo LONGBLOB,
    FOREIGN KEY (id_socio) REFERENCES Socio(id_socio) ON DELETE CASCADE
);

CREATE TABLE Inscripcion (
    id_inscripcion INT PRIMARY KEY AUTO_INCREMENT,
    id_socio INT NOT NULL,
    id_sesion INT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    FOREIGN KEY (id_socio) REFERENCES Socio(id_socio) ON DELETE CASCADE,
    FOREIGN KEY (id_sesion) REFERENCES SesionClase(id_sesion) ON DELETE CASCADE
);

INSERT INTO Socio (nombre_socio, apellidos_socio, dni, fecha_nacimiento, telefono, tipo_membresia, foto_perfil, tipo_usuario, contrasena) VALUES
('Carlos', 'Martinez', '11111111H', '1985-03-12', '600123456', 'PREMIUM', NULL, 'ADMIN', '$2a$12$e5Bme/Gzcqc83Tdwfq9zDOU4k3jb3wsibcgEg1/pDdOR/ssbTJat.'),
('Roberto', 'Alonso', '12345678Z', '1994-11-24', '606997856', 'PREMIUM', NULL, 'EDITOR', '$2a$12$Zdx0DyvzKjYd1Yo6FRb8aeciKLUfeZrug8cOEBFkw2n4eo65QAly.'),
('Laura', 'Gomez', '22222222J', '1990-07-23', '612345678', 'VIP', NULL, 'BASIC', '$2a$12$Frgc.4C4xgeYQXhtCK3cAus9vqU16YqYzOpIuHZOqFU9Vscf1paH.'),
('Miguel', 'Sanchez', '00000004G', '1988-11-05', '698765432', 'BASICA', NULL, 'EDITOR', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Lucia', 'Fernandez', '87654321X', '1992-04-18', '611223344', 'PREMIUM', NULL, 'BASIC', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Javier', 'Lopez', '88990011K', '1987-06-10', '633445566', 'VIP', NULL, 'EDITOR', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Ana', 'Ruiz', '14151617Q', '1993-09-25', '644556677', 'BASICA', NULL, 'BASIC', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Diego', 'Morales', '00000001R', '1980-12-03', '655667788', 'PREMIUM', NULL, 'BASIC', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Sofia', 'Navarro', '00000002W', '1996-01-30', '666778899', 'VIP', NULL, 'EDITOR', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO'),
('Pablo', 'Torres', '00000003A', '1991-08-14', '677889900', 'BASICA', NULL, 'BASIC', '$2a$12$N/Ui5GdwnKhal96eHKHlcu9VGKGByAoI.SXb0wXm3zW5LKsqyCGcO');

INSERT INTO Entrenador (nombre_entrenador, apellidos_entrenador, especialidad, telefono, fecha_contratacion, salario, foto_perfil) VALUES
('Juan', 'Perez', 'Fitness', '612345678', '2020-01-15', 1800.00, NULL),
('Maria', 'Lopez', 'Yoga', '623456789', '2018-06-20', 1900.00, NULL),
('Carlos', 'Garcia', 'Pilates', '634567890', '2019-09-10', 1750.00, NULL),
('Ana', 'Martinez', 'Entrenamiento funcional', '645678901', '2021-03-05', 2000.00, NULL),
('Luis', 'Fernandez', 'Crossfit', '656789012', '2017-11-30', 2100.00, NULL),
('Sofia', 'Ruiz', 'Fitness', '667890123', '2022-02-28', 1850.00, NULL),
('Miguel', 'Santos', 'Boxeo', '678901234', '2016-05-17', 1950.00, NULL),
('Elena', 'Gomez', 'Zumba', '689012345', '2019-08-23', 1700.00, NULL),
('Javier', 'Moreno', 'Entrenamiento personal', '690123456', '2020-12-01', 2200.00, NULL),
('Laura', 'Diaz', 'Spinning', '601234567', '2015-07-14', 1250.00, NULL);

INSERT INTO Clase (nombre_clase, id_entrenador, capacidad_maxima, nivel_dificultad, sala, imagen_clase) VALUES
('Fitness Basico', 1, 15, 'PRINCIPIANTE', 'Sala 1', NULL),
('Yoga Hatha', 2, 20, 'INTERMEDIO', 'Sala 2', NULL),
('Pilates Avanzado', 3, 12, 'AVANZADO', 'Sala 3', NULL),
('Funcional Power', 4, 18, 'AVANZADO', 'Sala 4', NULL),
('Crossfit Beginners', 5, 10, 'PRINCIPIANTE', 'Sala 5', NULL),
('Fitness Power', 6, 25, 'AVANZADO', 'Sala 1', NULL),
('Boxeo Total', 7, 22, 'INTERMEDIO', 'Sala 6', NULL),
('Zumba Sensation', 8, 20, 'INTERMEDIO', 'Sala 7', NULL),
('Total Transformation', 9, 15, 'AVANZADO', 'Sala 8', NULL),
('Spinning Session', 10, 12, 'AVANZADO', 'Sala 9', NULL);

INSERT INTO SesionClase (id_clase, fecha_hora, capacidad_disponible) VALUES
(1, '2025-05-19 17:00:00', 15),
(2, '2025-05-19 10:00:00', 12),
(3, '2025-05-20 12:00:00', 20),
(4, '2025-05-21 18:30:00', 18),
(5, '2025-05-21 20:00:00', 25),
(6, '2025-05-22 17:30:00', 22),
(7, '2025-05-22 19:30:00', 20),
(8, '2025-05-23 12:00:00', 15),
(9, '2025-05-23 16:30:00', 10),
(10, '2025-05-24 18:00:00', 12);

INSERT INTO Inscripcion (id_socio, id_sesion, fecha_inscripcion) VALUES
(1, 3, '2025-05-10'),
(2, 5, '2025-05-11'),
(3, 2, '2025-05-09'),
(4, 6, '2025-05-12'),
(5, 1, '2025-05-13'),
(6, 7, '2025-05-14'),
(7, 4, '2025-05-08'),
(8, 9, '2025-05-15'),
(9, 10, '2025-05-12'),
(10, 8, '2025-05-10');

-- ACTUALIZAMOS LA CAPACIDAD DISPONIBLE SEGÚN LAS INSCRIPCIONES
UPDATE SesionClase sc
JOIN (
    SELECT i.id_sesion, c.capacidad_maxima - COUNT(i.id_inscripcion) AS nueva_disponible
    FROM Inscripcion i
    JOIN SesionClase s ON i.id_sesion = s.id_sesion
    JOIN Clase c ON s.id_clase = c.id_clase
    GROUP BY i.id_sesion, c.capacidad_maxima
) AS t ON sc.id_sesion = t.id_sesion
SET sc.capacidad_disponible = t.nueva_disponible;

INSERT INTO Pago (id_socio, fecha_pago, importe, tipo_cuota, tipo_pago, metodo_pago, estado, recibo) VALUES
(1, '2025-05-01', 40.00, 'MENSUAL', 'CUOTA', 'TARJETA', 'PAGADO', NULL),
(2, '2025-05-03', 120.00, 'TRIMESTRAL', 'CUOTA', 'TRANSFERENCIA', 'PAGADO', NULL),
(3, '2025-05-04', 20.00, 'MENSUAL', 'INCIDENCIA', 'EFECTIVO', 'PAGADO', NULL),
(4, '2025-05-05', 200.00, 'ANUAL', 'CUOTA', 'TARJETA', 'PAGADO', NULL),
(5, '2025-05-06', 30.00, 'MENSUAL', 'CUOTA', 'EFECTIVO', 'PAGADO', NULL),
(6, '2025-05-07', 50.00, 'MENSUAL', 'MATRICULA', 'TARJETA', 'PAGADO', NULL),
(7, '2025-05-08', 40.00, 'MENSUAL', 'CUOTA', 'TRANSFERENCIA', 'PAGADO', NULL),
(8, '2025-05-09', 25.00, 'MENSUAL', 'INCIDENCIA', 'EFECTIVO', 'PAGADO', NULL),
(9, '2025-05-10', 120.00, 'TRIMESTRAL', 'CUOTA', 'TARJETA', 'PAGADO', NULL),
(10, '2025-05-11', 40.00, 'MENSUAL', 'CUOTA', 'TRANSFERENCIA', 'PENDIENTE', NULL);