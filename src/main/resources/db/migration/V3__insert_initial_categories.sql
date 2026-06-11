INSERT INTO categories (name)
VALUES
    ('Smartphone'),
    ('PC'),
    ('Book'),
    ('Game'),
    ('Home')
ON CONFLICT (name) DO NOTHING;
