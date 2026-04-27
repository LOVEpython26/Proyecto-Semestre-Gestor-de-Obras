-- =========================================
-- ELIMINAR TABLAS SI EXISTEN (opcional)
-- =========================================
DROP TABLE IF EXISTS historial, reportes, avances, apu, recursos, items, presupuestos, proyectos, usuarios, roles CASCADE;

-- =========================================
-- CREACIÓN DE TABLAS
-- =========================================

CREATE TABLE roles (
    id_rol SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    id_rol INT REFERENCES roles(id_rol)
);

CREATE TABLE proyectos (
    id_proyecto SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    ubicacion VARCHAR(100),
    fecha_inicio DATE,
    estado VARCHAR(50)
);

CREATE TABLE presupuestos (
    id_presupuesto SERIAL PRIMARY KEY,
    id_proyecto INT REFERENCES proyectos(id_proyecto),
    costo_total NUMERIC
);

CREATE TABLE items (
    id_item SERIAL PRIMARY KEY,
    id_proyecto INT REFERENCES proyectos(id_proyecto),
    descripcion TEXT,
    cantidad INT,
    precio_unitario NUMERIC
);

CREATE TABLE recursos (
    id_recurso SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    tipo VARCHAR(50),
    costo NUMERIC
);

CREATE TABLE apu (
    id_apu SERIAL PRIMARY KEY,
    id_item INT REFERENCES items(id_item),
    id_recurso INT REFERENCES recursos(id_recurso),
    cantidad NUMERIC,
    costo_unitario NUMERIC
);

CREATE TABLE avances (
    id_avance SERIAL PRIMARY KEY,
    id_proyecto INT REFERENCES proyectos(id_proyecto),
    fecha DATE,
    porcentaje NUMERIC,
    descripcion TEXT
);

CREATE TABLE reportes (
    id_reporte SERIAL PRIMARY KEY,
    id_proyecto INT REFERENCES proyectos(id_proyecto),
    fecha DATE,
    resumen TEXT
);

CREATE TABLE historial (
    id_historial SERIAL PRIMARY KEY,
    id_proyecto INT REFERENCES proyectos(id_proyecto),
    cambio TEXT,
    fecha DATE
);

-- =========================================
-- INSERCIONES DE PRUEBA
-- =========================================

INSERT INTO roles (nombre) VALUES
('Administrador'), ('Ingeniero'), ('Operario');

INSERT INTO usuarios (nombre, correo, contraseña, id_rol) VALUES
('Juan Castro','juan@dsw.com','1234',1),
('Ana Torres','ana@dsw.com','1234',2),
('Luis Perez','luis@dsw.com','1234',3);

INSERT INTO proyectos (nombre, ubicacion, fecha_inicio, estado) VALUES
('Puente Río','Santander','2026-01-10','En ejecución'),
('Edificio Central','Bucaramanga','2026-02-15','Planeación');

INSERT INTO presupuestos (id_proyecto, costo_total) VALUES
(1,500000000),(2,1200000000);

INSERT INTO items (id_proyecto, descripcion, cantidad, precio_unitario) VALUES
(1,'Excavación',100,50000),
(1,'Concreto',200,80000),
(2,'Cimentación',150,90000);

INSERT INTO recursos (nombre, tipo, costo) VALUES
('Cemento','Material',30000),
('Arena','Material',20000),
('Retroexcavadora','Equipo',150000),
('Obrero','Mano de obra',50000);

INSERT INTO apu (id_item, id_recurso, cantidad, costo_unitario) VALUES
(1,4,2,50000),
(2,1,3,30000),
(2,2,2,20000);

INSERT INTO avances (id_proyecto, fecha, porcentaje, descripcion) VALUES
(1,'2026-03-01',20,'Inicio'),
(1,'2026-03-15',45,'Estructura'),
(2,'2026-03-10',10,'Inicio');

INSERT INTO reportes (id_proyecto, fecha, resumen) VALUES
(1,'2026-03-20','Buen avance'),
(2,'2026-03-25','Inicial');

INSERT INTO historial (id_proyecto, cambio, fecha) VALUES
(1,'Ajuste presupuesto','2026-02-01'),
(2,'Proyecto creado','2026-02-15');

-- =========================================
-- CONSULTAS CON JOIN 
-- =========================================

-- 1. Proyectos con presupuesto
SELECT p.nombre, pr.costo_total
FROM proyectos p
JOIN presupuestos pr ON p.id_proyecto = pr.id_proyecto;

-- 2. Usuarios con rol
SELECT u.nombre, r.nombre AS rol
FROM usuarios u
JOIN roles r ON u.id_rol = r.id_rol;

-- 3. Items con total
SELECT descripcion, cantidad, precio_unitario,
       (cantidad * precio_unitario) AS total
FROM items;

-- 4. Avance de proyectos
SELECT p.nombre, a.fecha, a.porcentaje
FROM proyectos p
JOIN avances a ON p.id_proyecto = a.id_proyecto;

-- 5. APU completo (items + recursos)
SELECT i.descripcion, r.nombre AS recurso, a.cantidad, a.costo_unitario
FROM apu a
JOIN items i ON a.id_item = i.id_item
JOIN recursos r ON a.id_recurso = r.id_recurso;

-- =========================================
-- FUNCIONES
-- =========================================

-- Calcular costo total de un proyecto
CREATE OR REPLACE FUNCTION calcular_costo_proyecto(p_id INT)
RETURNS NUMERIC AS $$
DECLARE total NUMERIC;
BEGIN
    SELECT SUM(cantidad * precio_unitario)
    INTO total
    FROM items
    WHERE id_proyecto = p_id;

    RETURN total;
END;
$$ LANGUAGE plpgsql;

-- Reporte de avance
CREATE OR REPLACE FUNCTION reporte_proyecto(p_id INT)
RETURNS TABLE(nombre_proyecto VARCHAR, avance NUMERIC) AS $$
BEGIN
    RETURN QUERY
    SELECT p.nombre, MAX(a.porcentaje)
    FROM proyectos p
    JOIN avances a ON p.id_proyecto = a.id_proyecto
    WHERE p.id_proyecto = p_id
    GROUP BY p.nombre;
END;
$$ LANGUAGE plpgsql;

-- =========================================
-- PROCEDIMIENTOS
-- =========================================

-- Registrar avance
CREATE OR REPLACE PROCEDURE registrar_avance(
    p_id INT,
    p_porcentaje NUMERIC,
    p_desc TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO avances (id_proyecto, fecha, porcentaje, descripcion)
    VALUES (p_id, CURRENT_DATE, p_porcentaje, p_desc);
END;
$$;

-- Actualizar presupuesto automáticamente
CREATE OR REPLACE PROCEDURE actualizar_presupuesto(p_id INT)
LANGUAGE plpgsql
AS $$
DECLARE nuevo_total NUMERIC;
BEGIN
    SELECT SUM(cantidad * precio_unitario)
    INTO nuevo_total
    FROM items
    WHERE id_proyecto = p_id;

    UPDATE presupuestos
    SET costo_total = nuevo_total
    WHERE id_proyecto = p_id;
END;
$$;

-- =========================================
-- PRUEBAS DE FUNCIONES Y PROCEDIMIENTOS
-- =========================================

-- Ejecutar función
SELECT calcular_costo_proyecto(2);

-- Ejecutar procedimiento
CALL registrar_avance(1, 70, 'Nuevo avance');

CALL actualizar_presupuesto(1);

-- Ver resultado
SELECT * FROM avances;
SELECT * FROM presupuestos;


