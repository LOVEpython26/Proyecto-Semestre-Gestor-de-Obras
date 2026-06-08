-- =========================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS - GESTOR DE OBRA (POSTGRESQL)
-- =========================================================================

DROP TABLE IF EXISTS control_avance;
DROP TABLE IF EXISTS apu_receta;
DROP TABLE IF EXISTS actividades;
DROP TABLE IF EXISTS materiales;

-- 1. CREAR TABLA DE MATERIALES (Base: MATERIALES.csv)
CREATE TABLE materiales (
    descripcion VARCHAR(255) PRIMARY KEY, -- Llave principal (Nombre exacto del Excel)
    unidad VARCHAR(50),
    precio_unitario NUMERIC(15, 2),       -- NUMERIC es vital en PostgreSQL para dinero
    tipo VARCHAR(50)
);

-- 2. CREAR TABLA DE ACTIVIDADES (Base: ACTIVIDADES.csv)
CREATE TABLE actividades (
    item VARCHAR(50) PRIMARY KEY,         -- Llave principal (Ej: '1.01')
    descripcion TEXT,
    unidad VARCHAR(50),
    cantidad NUMERIC(15, 2),              -- Cantidad total del presupuesto
    vr_unitario NUMERIC(15, 2),
    vr_parcial NUMERIC(15, 2)
);

-- 3. CREAR TABLA APU RECETA (Base: APU.csv)
CREATE TABLE apu_receta (
    id SERIAL PRIMARY KEY,                -- Autonumérico propio de PostgreSQL
    item_actividad VARCHAR(50),           -- El conector hacia Actividades
    descripcion_material VARCHAR(255),    -- El conector hacia Materiales
    cant_rend NUMERIC(15, 4),             -- Rendimiento (4 decimales como en tu Excel)
    precio_unitario NUMERIC(15, 2),
    
    -- RESTRICCIONES (Llaves Foráneas)
    CONSTRAINT fk_apu_actividad 
        FOREIGN KEY (item_actividad) 
        REFERENCES actividades(item) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_apu_material 
        FOREIGN KEY (descripcion_material) 
        REFERENCES materiales(descripcion) 
        ON DELETE CASCADE
);

-- 4. CREAR TABLA CONTROL DE AVANCE (Módulo Gestor para Java)
CREATE TABLE control_avance (
    id SERIAL PRIMARY KEY,
    fecha DATE DEFAULT CURRENT_DATE,      -- Pone la fecha de hoy automáticamente
    item_actividad VARCHAR(50),           -- El conector hacia la Actividad ejecutada
    cantidad_ejecutada NUMERIC(15, 2),    -- Lo que avanzó Wilson ese día
    
    -- RESTRICCIÓN
    CONSTRAINT fk_avance_actividad 
        FOREIGN KEY (item_actividad) 
        REFERENCES actividades(item) 
        ON DELETE CASCADE
);

SELECT * FROM control_avance;

INSERT INTO materiales (tipo, descripcion, unidad, precio_unitario) 
VALUES
(NULL, 'ABRAZADERA TIPO PERA METÁLICA Ø= 1 1/2"', 'Un', 2880, 1, 2880),
(NULL, 'ABRAZADERA TIPO PERA METÁLICA Ø= 1 1/4"', 'Un', 2336, 1, 2336),
(NULL, 'ABRAZADERA TIPO PERA METÁLICA Ø= 2 1/2"', 'Un', 4920, 1, 4920),
(NULL, 'ACERO DRAMIX MALLA EN BOLSA L= 60 mm', 'Kg', 15000, 1, 15000),
(NULL, 'ACERO Fy= 240 MPa', 'Kg', 5500, 1, 5500),
(NULL, 'ACERO Fy= 420 MPa', 'Kg', 5500, 1, 5500);
