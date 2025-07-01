CREATE DATABASE restaurant_db;
USE restaurant_db;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(50),
  password VARCHAR(100) NOT NULL,
  phone VARCHAR(20),
  date_birth DATE,
  role VARCHAR(20) DEFAULT 'User'
);

CREATE TABLE foods (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  price DECIMAL(10,2),
  available INT DEFAULT 1,
  image_url TEXT,
  description TEXT,
  rating_avg DECIMAL(3,2) DEFAULT 0
);

CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  status VARCHAR(100) DEFAULT 'pending',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  discount_id INT, -- mã giảm giá nếu có
  final_amount DECIMAL(10,2), -- tổng tiền sau khi áp dụng giảm giá
  address TEXT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (discount_id) REFERENCES discounts(id)
);

CREATE TABLE order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  food_id INT NOT NULL,
  quantity INT DEFAULT 1,
  rating INT CHECK (rating BETWEEN 1 AND 5),
  review TEXT,
  note TEXT,
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (food_id) REFERENCES foods(id)
);

CREATE TABLE discounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(20) UNIQUE NOT NULL,
  discount_percent INT CHECK (discount_percent BETWEEN 1 AND 100),
  max_discount_amount DECIMAL(10,2),
  valid_from DATE,
  valid_to DATE,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE pending_users (
  email VARCHAR(100) PRIMARY KEY,
  name VARCHAR(50),
  password VARCHAR(100),
  phone VARCHAR(20),
  date_birth DATE,
  role VARCHAR(20),
  otp VARCHAR(6),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE password_resets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    otp VARCHAR(10) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
  id int NOT NULL AUTO_INCREMENT,
  user_id int NOT NULL,
  order_id int DEFAULT NULL,
  title varchar(255) NOT NULL,
  message text NOT NULL,
  is_read tinyint(1) DEFAULT '0',
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE messages (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  message TEXT NOT NULL,
  timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
  is_read TINYINT(1) DEFAULT 0,
  FOREIGN KEY (sender_id) REFERENCES users(id),
  FOREIGN KEY (receiver_id) REFERENCES users(id)
);


CREATE TABLE reviews (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  food_id INT NOT NULL,
  comment TEXT,
  rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (food_id) REFERENCES foods(id)
);

CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    food_id INT NOT NULL,
    quantity INT NOT NULL,
    note TEXT,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id)
);

DELIMITER $$
CREATE TRIGGER trg_update_rating_avg_after_insert
AFTER INSERT ON reviews
FOR EACH ROW
BEGIN
    UPDATE foods
    SET rating_avg = (
        SELECT AVG(rating)
        FROM reviews
        WHERE food_id = NEW.food_id
    )
    WHERE id = NEW.food_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `trg_after_order_update` AFTER UPDATE ON `orders`
 FOR EACH ROW BEGIN
    -- Chỉ thực hiện nếu trạng thái thay đổi
    IF NEW.status != OLD.status THEN
        INSERT INTO notifications (user_id, order_id, title, message)
        VALUES (
            NEW.user_id,
            NEW.id,
            'Cập nhật đơn hàng',
            CONCAT('Đơn hàng với mã số #', NEW.id, ' chuyển đổi sang trạng thái : "', NEW.status, '"')
        );
    END IF;
END$$
DELIMITER ;

INSERT INTO users (email, name, password, phone, date_birth, role) VALUES
('user1@gmail.com', 'Nguyễn Văn A', 'password123', '0901234567', '1990-01-01', 'User'),
('user2@gmail.com', 'Trần Thị B', 'password123', '0902345678', '1992-05-15', 'User'),
('2', 'Nguyễn Văn A', '2', '0901234567', '1990-01-01', 'User'),
('admin@gmail.com', 'Admin', 'adminpass', '0903456789', '1988-12-20', 'admin'),
('1', 'Admin1', '1', '0903456789', '1988-12-20', 'admin');

INSERT INTO foods (name, category, price, available, image_url, description) VALUES
('Phở bò', 'Món chính', 50000, 1, 'https://bing.com/th?id=OSK.352e2a482609aaaf581fea4adb20acea', 'Phở bò truyền thống Việt Nam'),
('Cơm gà xối mỡ', 'Món chính', 45000, 1, 'https://songkhoe.medplus.vn/wp-content/uploads/2022/06/med-41.png', 'Cơm gà vàng giòn, béo ngậy'),
('Bánh mì thịt', 'Khai vị', 25000, 1, 'https://www.allrecipes.com/thmb/HTJHVC_LYKmXaMF54dhe2gZQkNI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/roasted-pork-banh-mi-vietnamese-sandwitch-ddmfs-3X4-0332-cfb4d2e149e7476ab2a2b4030c543f1b.jpg', 'Bánh mì Việt Nam với thịt và rau'),
('Chè đậu xanh', 'Tráng miệng', 20000, 1, 'https://th.bing.com/th/id/OIP.CpO8qay1fbXlbYz9Tr8-PgHaFj?w=264&h=180&c=7&r=0&o=7&dpr=1.1&pid=1.7&rm=3', 'Món chè thanh mát cho ngày hè'),
('nước mía', 'Thức uống', 10000, 1, 'https://photo.znews.vn/Uploaded/sgorvz/2024_04_13/nuoc_mia_2.jpg', 'nước mía mát lạnh giải nhiệt ngày hè');

INSERT INTO discounts (code, discount_percent, max_discount_amount, valid_from, valid_to, is_active) VALUES
('DISCOUNT10', 10, 20000, '2025-01-01', '2025-12-31', TRUE),
('SALE20', 20, 50000, '2025-06-01', '2025-12-31', TRUE),
('OFF30', 30, 70000, '2025-07-01', '2025-08-31', TRUE);
