-- 1. Crear la lista por defecto si no existe
INSERT INTO listas (nombre)
SELECT 'Sin clasificar'
WHERE NOT EXISTS (SELECT 1 FROM listas WHERE nombre = 'Sin clasificar');

-- 2. Asignar esa lista a los favoritos que no tienen
UPDATE favoritos
SET lista_id = (SELECT id FROM listas WHERE nombre = 'Sin clasificar' ORDER BY id LIMIT 1)
WHERE lista_id IS NULL;

-- 3. Recién ahora, hacer la columna obligatoria
ALTER TABLE favoritos ALTER COLUMN lista_id SET NOT NULL;