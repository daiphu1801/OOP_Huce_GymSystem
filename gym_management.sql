-- Xóa database cũ (nếu tồn tại) và tạo database mới
DROP DATABASE IF EXISTS gym_management;
CREATE DATABASE gym_management;
USE gym_management;

-- Bảng đếm ID tự động
CREATE TABLE card_sequence (
                               next_val INT NOT NULL
);

-- Khởi tạo giá trị đầu tiên
INSERT INTO card_sequence VALUES (1);

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

CREATE TRIGGER trg_generate_card_id
    BEFORE INSERT ON membership_cards
    FOR EACH ROW
BEGIN
    DECLARE new_id INT;
    DECLARE new_code VARCHAR(20);

    -- Lấy giá trị mới từ bảng card_sequence
    SELECT next_val INTO new_id FROM card_sequence FOR UPDATE;

    -- Tạo mã thẻ kiểu CARD000001
    SET new_code = CONCAT('CARD', LPAD(new_id, 6, '0'));
    SET NEW.card_id = new_code;

    -- Cập nhật lại next_val
    UPDATE card_sequence SET next_val = next_val + 1;
END$$

DELIMITER ;

-- Bảng Members (Quản lý thông tin hội viên)
CREATE TABLE members (
                         member_id INT AUTO_INCREMENT PRIMARY KEY, -- Ma HV
                         card_code VARCHAR(20) NOT NULL,           -- Mã thẻ (khóa ngoại)
                         full_name VARCHAR(100) NOT NULL,         -- Họ tên
                         phone VARCHAR(15) NOT NULL,              -- SDT
                         gender VARCHAR(10) NOT NULL,             -- Gender
                         email VARCHAR(100) NOT null,
                         FOREIGN KEY (card_code) REFERENCES membership_cards(card_id) ON DELETE CASCADE
);

-- Bảng Equipment (Quản lý thiết bị)
CREATE TABLE equipment (
                           equipment_id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           status VARCHAR(20) NOT NULL,
                           quantity INT NOT NULL CHECK (quantity >= 0),
                           purchase_date DATE NOT NULL
);

-- Bảng Products (Quản lý sản phẩm)
CREATE TABLE products (
                          product_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                          quantity INT NOT NULL CHECK (quantity >= 0),
                          quantity_sold INT NOT NULL
);

-- Bảng Trainers (Quản lý huấn luyện viên)
CREATE TABLE trainers (
                          trainer_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          gender VARCHAR(10) NOT NULL,
                          phone VARCHAR(15) NOT NULL UNIQUE,
                          email VARCHAR(50) NOT NULL UNIQUE,
                          specialization VARCHAR(100) NOT NULL
);

-- Bảng Revenue (Lưu trữ thông tin doanh thu)
CREATE TABLE revenue (
                         revenue_id INT AUTO_INCREMENT PRIMARY KEY,
                         source_type VARCHAR(20) NOT NULL CHECK (source_type IN ('MEMBERSHIP', 'PRODUCT')),
                         amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
                         transaction_date DATE NOT NULL,
                         description VARCHAR(255)
);

-- Thêm chỉ mục cho các cột thường xuyên được tìm kiếm
CREATE INDEX idx_transaction_date ON revenue(transaction_date);