-- Додавання колонки для рейтингу товарів
ALTER TABLE products ADD COLUMN order_count BIGINT DEFAULT 0;

-- Оновлення рейтингу на основі існуючих замовлень
UPDATE products p
SET order_count = (
    SELECT COALESCE(SUM(od.amount), 0)
    FROM order_details od
    WHERE od.product_id = p.id
);
