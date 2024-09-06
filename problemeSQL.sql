-- Creearea tabelelor
--
--

-- Tabela `Comenzi`
CREATE TABLE Comenzi (
    numar_factura BIGINT,
    numar_comanda BIGINT,
    cod_articol BIGINT,
    id_cantitate VARCHAR(50)
);

-- Tabela `Sucursale`
CREATE TABLE Sucursale (
    sucursala VARCHAR(50),
    numar_comanda BIGINT
);

-- Tabela `Cantitati`
CREATE TABLE Cantitati (
    id_cantitate VARCHAR(50),
    cantitate DECIMAL(10, 2) -- DECIMAL pentru a gestiona corect valorile numerice
);

-- Tabela `Data`
CREATE TABLE Data (
    numar_factura BIGINT,
    data DATE
);

-- Încărcarea datelor 
--
--

-- Tabela `Comenzi`
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Comenzi.csv'
INTO TABLE Comenzi
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n' -- Schimbă terminatorul de linie pentru a gestiona Windows-style carriage returns
IGNORE 1 ROWS;


-- Tabela `Sucursale`
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Sucursale.csv'
INTO TABLE Sucursale
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Tabela `Cantitati`
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Cantitati.csv'
INTO TABLE Cantitati
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id_cantitate, @cantitate_var)
SET cantitate = CAST(@cantitate_var AS DECIMAL(10,2)); -- Se asigură că valoarea cantitate este convertită corect la număr

-- Tabela `Data`
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Data.csv'
INTO TABLE Data
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(numar_factura, @date_var)
SET data = STR_TO_DATE(@date_var, '%m/%d/%Y'); -- Conversie corectă a datelor din format MM/DD/YYYY


SELECT * FROM Comenzi LIMIT 10;
SELECT * FROM Sucursale LIMIT 10;
SELECT * FROM Cantitati LIMIT 10;
SELECT * FROM Data LIMIT 10;

-- Exercițiul 1
--
--

SELECT 
	s.sucursala,
    COUNT(DISTINCT c.numar_comanda) AS numar_comenzi_returnate
FROM 
    Sucursale s
JOIN 
    Comenzi c ON s.numar_comanda = c.numar_comanda
JOIN 
    Cantitati ct ON c.id_cantitate=ct.id_cantitate
WHERE 
    ct.cantitate < 0
GROUP BY 
    s.sucursala;

-- Exercițiul 2
--
--

SELECT 
	s.sucursala,
    COUNT(DISTINCT c.numar_comanda) AS numar_comenzi_returnate
FROM 
    Sucursale s
JOIN 
    Comenzi c ON s.numar_comanda = c.numar_comanda
JOIN 
    Cantitati ct ON c.id_cantitate = ct.id_cantitate
JOIN 
    Data d ON c.numar_factura = d.numar_factura
WHERE 
    s.sucursala = 'Bragadiru'
    AND ct.cantitate < 0
    AND d.data BETWEEN '2024-01-01' AND '2024-01-31';




