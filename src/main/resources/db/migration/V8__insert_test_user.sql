INSERT INTO users (username, password, role)
VALUES ('user', '$2a$12$AwOfJbYPtqVhRjFO4jntOuYpxjRrYX94UOfemIufJNQKM6MQaYozy', 'USER')
ON CONFLICT (username) DO NOTHING;
