-- Xóa database cũ (nếu tồn tại) và tạo database mới
DROP DATABASE IF EXISTS gym_management;
CREATE DATABASE gym_management;
USE gym_management;

-- Bảng Members (Quản lý thông tin hội viên)
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    membership_type VARCHAR(20) NOT NULL, -- Giới hạn loại thẻ hợp lệ
    training_package VARCHAR(20) not null,
    registration_date DATE not null,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng MembershipCards (Quản lý thẻ hội viên)
CREATE TABLE membership_cards (
    card_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    name_card VARCHAR(20) NOT NULL,
    renewal_count INT DEFAULT 0,
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    last_checkin_time DATETIME DEFAULT NULL,
    
    -- Ràng buộc CHECK ở mức bảng
    CONSTRAINT chk_renewal_count CHECK (renewal_count >= 0), -- Đảm bảo số lần gia hạn không âm
    CONSTRAINT chk_expiry_after_issue CHECK (expiry_date > issue_date), -- Đảm bảo ngày hết hạn lớn hơn ngày phát hành

    -- Khóa ngoại và chỉ mục
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id)
);

-- Bảng Equipment (Quản lý thiết bị)
CREATE TABLE equipment (
    equipment_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'MAINTENANCE', 'BROKEN')),
    quantity INT NOT NULL CHECK (quantity >= 0), -- Đảm bảo số lượng không âm
    purchase_date DATE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thêm cột theo dõi cập nhật
);

-- Bảng Products (Quản lý sản phẩm)
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0), -- Đảm bảo giá không âm
    quantity INT NOT NULL CHECK (quantity >= 0), -- Đảm bảo số lượng không âm
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thêm cột theo dõi cập nhật
);

-- Bảng Trainers (Quản lý huấn luyện viên)
CREATE TABLE trainers (
    trainer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE, -- Tăng độ dài email và thêm UNIQUE
    specialization VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thêm cột theo dõi cập nhật
);

-- Bảng Revenue (Lưu trữ thông tin doanh thu)
CREATE TABLE revenue (
    revenue_id INT AUTO_INCREMENT PRIMARY KEY,
    source_type VARCHAR(20) NOT NULL CHECK (source_type IN ('MEMBERSHIP', 'PRODUCT')),
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0), -- Đảm bảo số tiền không âm
    transaction_date DATE NOT NULL,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Thêm cột theo dõi cập nhật
);

-- Thêm chỉ mục cho các cột thường xuyên được tìm kiếm
CREATE INDEX idx_transaction_date ON revenue(transaction_date);