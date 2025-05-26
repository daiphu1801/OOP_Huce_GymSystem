-- Xóa database cũ (nếu có) và tạo lại database mới
DROP DATABASE IF EXISTS gym_management;
CREATE DATABASE gym_management;
USE gym_management;

-- Các bảng sequence cho ID định dạng tùy chỉnh
CREATE TABLE card_sequence (next_val INT NOT NULL);
CREATE TABLE member_sequence (next_val INT NOT NULL);
CREATE TABLE equipment_sequence (next_val INT NOT NULL);
CREATE TABLE trainer_sequence (next_val INT NOT NULL);
CREATE TABLE product_sequence (next_val INT NOT NULL);
CREATE TABLE member_card_sequence (next_val INT NOT NULL);

-- Giá trị khởi tạo
INSERT INTO card_sequence VALUES (1);
INSERT INTO member_sequence VALUES (1);
INSERT INTO equipment_sequence VALUES (1);
INSERT INTO trainer_sequence VALUES (1);
INSERT INTO product_sequence VALUES (1);
INSERT INTO member_card_sequence VALUES (1);

-- Bảng MembershipCards
CREATE TABLE membership_cards (
    card_id VARCHAR(20) PRIMARY KEY,
    price DECIMAL(10, 2) NOT NULL,
    training_package VARCHAR(20) NOT NULL,
    card_type VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    CONSTRAINT chk_expiry_after_issue CHECK (expiry_date > registration_date)
);

DELIMITER $$

-- Trigger sinh mã thẻ CARD000001 tự động
CREATE TRIGGER trg_generate_card_id
BEFORE INSERT ON membership_cards
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);
    SELECT next_val INTO new_id FROM card_sequence FOR UPDATE;
    SET new_code = CONCAT('CARD', LPAD(new_id, 5, '0'));
    SET NEW.card_id = new_code;
    UPDATE card_sequence SET next_val = next_val + 1;
END$$

-- Bảng Members (member_id: MB000001, lấy card_id mới nhất)
CREATE TABLE members (
    member_id VARCHAR(20) PRIMARY KEY,
    card_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(100) NOT NULL,
    FOREIGN KEY (card_code) REFERENCES membership_cards(card_id) ON DELETE CASCADE
);

-- Trigger sinh member_id tự động dạng MB000001
CREATE TRIGGER trg_generate_member_id
BEFORE INSERT ON members
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);
    SELECT next_val INTO new_id FROM member_sequence FOR UPDATE;
    SET new_code = CONCAT('MB', LPAD(new_id, 5, '0'));
    SET NEW.member_id = new_code;
    UPDATE member_sequence SET next_val = next_val + 1;
    
END$$

-- Bảng Equipment (equipment_id: EQM0001)
CREATE TABLE equipment (
    equipment_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    purchase_date DATE NOT NULL
);

-- Trigger sinh equipment_id tự động
CREATE TRIGGER trg_generate_equipment_id
BEFORE INSERT ON equipment
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);
    SELECT next_val INTO new_id FROM equipment_sequence FOR UPDATE;
    SET new_code = CONCAT('EQM', LPAD(new_id, 4, '0'));
    SET NEW.equipment_id = new_code;
    UPDATE equipment_sequence SET next_val = next_val + 1;
END$$

-- Bảng Trainers (trainer_id: TN0001)
CREATE TABLE trainers (
    trainer_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    specialization VARCHAR(100) NOT NULL
);

-- Trigger sinh trainer_id tự động
CREATE TRIGGER trg_generate_trainer_id
BEFORE INSERT ON trainers
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);
    SELECT next_val INTO new_id FROM trainer_sequence FOR UPDATE;
    SET new_code = CONCAT('TN', LPAD(new_id, 4, '0'));
    SET NEW.trainer_id = new_code;
    UPDATE trainer_sequence SET next_val = next_val + 1;
END$$

-- Bảng Products (product_id: PD0001)
CREATE TABLE products (
    product_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    quantity_sold INT NOT NULL
);

-- Trigger sinh product_id tự động
CREATE TRIGGER trg_generate_product_id
BEFORE INSERT ON products
FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);
    SELECT next_val INTO new_id FROM product_sequence FOR UPDATE;
    SET new_code = CONCAT('PD', LPAD(new_id, 4, '0'));
    SET NEW.product_id = new_code;
    UPDATE product_sequence SET next_val = next_val + 1;
END$$

-- Tạo bảng sale_transactions để lưu lịch sử giao dịch bán hàng
CREATE TABLE IF NOT EXISTS sale_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    quantity_sold INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_transaction_date (transaction_date)
);

-- Tạo bảng revenue mới
CREATE TABLE revenue (
                         revenue_id INT AUTO_INCREMENT PRIMARY KEY,
                         source_type VARCHAR(20) NOT NULL, -- Loại nguồn (MEMBERSHIP, PRODUCT, hoặc các nguồn khác trong tương lai)
                         amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0), -- Số tiền (không âm)
                         transaction_date DATETIME NOT NULL, -- Thời gian giao dịch chi tiết
                         card_id VARCHAR(20) NULL, -- Khóa ngoại liên kết với membership_cards
                         product_id VARCHAR(20) NULL, -- Khóa ngoại liên kết với products
                         payment_method VARCHAR(20) NULL, -- Phương thức thanh toán (ví dụ: CASH, CREDIT_CARD, ONLINE)
                         description VARCHAR(255) NULL, -- Mô tả tùy chọn
                         FOREIGN KEY (card_id) REFERENCES membership_cards(card_id) ON DELETE SET NULL,
                         FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE SET NULL
);

-- Thêm chỉ mục cho transaction_date để tối ưu hóa truy vấn theo thời gian
CREATE INDEX idx_transaction_date ON revenue(transaction_date);

-- Bảng mới cho Check-ins
CREATE TABLE checkins (
                          checkin_id INT AUTO_INCREMENT PRIMARY KEY,
                          member_id VARCHAR(20) NULL,
                          checkin_time DATETIME NOT NULL,
                          FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE
);

-- Chỉ mục cho checkin_time để tối ưu hóa truy vấn theo thời gian
CREATE INDEX idx_checkin_time ON checkins(checkin_time);
