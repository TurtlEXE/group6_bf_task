-- Insert Admin User (Password: Admin@123)
INSERT INTO users (username, email, password, first_name, last_name, role, enabled, email_verified, created_at, updated_at)
SELECT 'admin', 'admin@wanderlust.com', '$2a$10$wEInA1y7Z.1u1P/oQzB7M.97iBq.dJg/zH87Z/M5/1uV8g1H6k02y', 'System', 'Admin', 'ROLE_ADMIN', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert Sample Tours
INSERT INTO tours (title, description, location, duration_days, price, image_url, category, featured, active, created_at, updated_at)
SELECT 'Golden Triangle India', 'Experience the rich history and culture of India with our 7-day Golden Triangle tour. Visit the iconic Taj Mahal and more.', 'Delhi, Agra, Jaipur', 7, 1200.00, 'https://images.unsplash.com/photo-1548013146-72479768bada?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80', 'CULTURAL', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tours WHERE title = 'Golden Triangle India');

INSERT INTO tours (title, description, location, duration_days, price, image_url, category, featured, active, created_at, updated_at)
SELECT 'Swiss Alps Trekking', 'Join us for an unforgettable trekking experience through the breathtaking landscapes of the Swiss Alps.', 'Switzerland', 5, 850.00, 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80', 'ADVENTURE', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tours WHERE title = 'Swiss Alps Trekking');

INSERT INTO tours (title, description, location, duration_days, price, image_url, category, featured, active, created_at, updated_at)
SELECT 'Tokyo Highlights', 'Discover the vibrant metropolis of Tokyo. From ancient temples to futuristic technology, experience it all.', 'Tokyo, Japan', 6, 1500.00, 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80', 'CULTURAL', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tours WHERE title = 'Tokyo Highlights');

INSERT INTO tours (title, description, location, duration_days, price, image_url, category, featured, active, created_at, updated_at)
SELECT 'Lagoon Kayaking Adventure', 'Full-day guided paddling with lunch and island stops.', 'Palawan, Philippines', 1, 129.00, 'https://images.unsplash.com/photo-1544551763-46a013bb70d5?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80', 'ADVENTURE', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tours WHERE title = 'Lagoon Kayaking Adventure');
