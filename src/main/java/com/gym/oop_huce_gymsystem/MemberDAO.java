package com.gym.oop_huce_gymsystem;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberDAO {
    private static MemberDAO instance;
    // private final String DATA_FILE = "members.dat";
    private List<Member> members;

    // Các biến cho kết nối database
    private final String DB_URL = "jdbc:mysql://localhost:3306/gym_management?useSSL=false&serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";
    private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Counter maps để theo dõi số thứ tự cho từng loại gói
    private Map<String, Integer> packageCounters;

    private MemberDAO() {
        members = new ArrayList<>();
        packageCounters = new HashMap<>();
        loadDataFromDatabase();
    }

    public static MemberDAO getInstance() {
        if (instance == null) {
            instance = new MemberDAO();
        }
        return instance;
    }

    // Phương thức tạo kết nối database
    private Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver không tìm thấy: " + e.getMessage());
        }
    }

    // Phương thức khởi tạo bảng trong database nếu chưa tồn tại
    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Tạo bảng members nếu chưa tồn tại
            // Bỏ các trường date_of_birth và gender vì lớp Member không có thuộc tính này
            String createTableSQL = "CREATE TABLE IF NOT EXISTS members (" +
                    "member_id VARCHAR(20) PRIMARY KEY," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "phone_number VARCHAR(20) NOT NULL," +
                    "date_of_birth DATE," +
                    "gender VARCHAR(10)," +
                    "register_date DATE NOT NULL," +
                    "card_type VARCHAR(50) NOT NULL," +
                    "package_type VARCHAR(50) NOT NULL," +
                    "card_status VARCHAR(20) NOT NULL" +
                    ")";

            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Lỗi khi khởi tạo database: " + e.getMessage());
        }
    }

    // Phương thức tải dữ liệu từ database
    private void loadDataFromDatabase() {
        // Khởi tạo database nếu cần
        initializeDatabase();

        // Xóa danh sách hiện tại
        members.clear();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM members")) {

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setFullName(rs.getString("full_name"));
                member.setPhoneNumber(rs.getString("phone_number"));

                // Xử lý date_of_birth (có thể null)
                Date dobDate = rs.getDate("date_of_birth");
                if (dobDate != null) {
                    member.setDateOfBirth(dobDate.toLocalDate());
                }

                member.setGender(rs.getString("gender"));
                member.setRegisterDate(rs.getDate("register_date").toLocalDate());
                member.setCardType(rs.getString("card_type"));
                member.setPackageType(rs.getString("package_type"));
                member.setCardStatus(rs.getString("card_status"));

                members.add(member);
            }

            // Khởi tạo bộ đếm dựa trên dữ liệu hiện có
            initializeCounters();

        } catch (SQLException e) {
            System.err.println("Lỗi khi tải dữ liệu từ database: " + e.getMessage());
            // Khởi tạo danh sách trống và các bộ đếm mặc định
            members = new ArrayList<>();
            initializeCounters();
        }
    }

    // Phương thức cũ để tải dữ liệu từ file - đã comment
    /*
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                members = (List<Member>) ois.readObject();

                // Initialize counters based on existing members
                initializeCounters();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
                members = new ArrayList<>();
                initializeCounters();
            }
        } else {
            members = new ArrayList<>();
            initializeCounters();
        }
    }
    */

    // Khởi tạo bộ đếm dựa trên dữ liệu thành viên hiện có
    private void initializeCounters() {
        packageCounters = new HashMap<>();

        // Khởi tạo mặc định cho tất cả các loại gói
        packageCounters.put("1 tháng", 0);
        packageCounters.put("3 tháng", 0);
        packageCounters.put("6 tháng", 0);
        packageCounters.put("12 tháng", 0);

        // Tìm số lớn nhất cho mỗi loại gói
        for (Member member : members) {
            String packageType = member.getPackageType();
            String memberId = member.getMemberId();

            if (memberId != null && memberId.length() >= 10) {
                try {
                    // Trích xuất số thứ tự từ 4 chữ số cuối
                    int counter = Integer.parseInt(memberId.substring(memberId.length() - 4));
                    int currentMax = packageCounters.getOrDefault(packageType, 0);
                    if (counter > currentMax) {
                        packageCounters.put(packageType, counter);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Định dạng ID thành viên không hợp lệ: " + memberId);
                }
            }
        }
    }

    // Lưu dữ liệu vào database
    private void saveToDatabase() {
        try (Connection conn = getConnection()) {
            // Xóa sạch bảng và cập nhật lại tất cả
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM members");
            }

            // Chuẩn bị câu lệnh SQL để chèn dữ liệu
            String insertSQL = "INSERT INTO members (member_id, full_name, phone_number, date_of_birth, gender, " +
                    "register_date, card_type, package_type, card_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Member member : members) {
                    pstmt.setString(1, member.getMemberId());
                    pstmt.setString(2, member.getFullName());
                    pstmt.setString(3, member.getPhoneNumber());

                    // Xử lý ngày sinh có thể null
                    if (member.getDateOfBirth() != null) {
                        pstmt.setDate(4, Date.valueOf(member.getDateOfBirth()));
                    } else {
                        pstmt.setNull(4, Types.DATE);
                    }

                    pstmt.setString(5, member.getGender());
                    pstmt.setDate(6, Date.valueOf(member.getRegisterDate()));
                    pstmt.setString(7, member.getCardType());
                    pstmt.setString(8, member.getPackageType());
                    pstmt.setString(9, member.getCardStatus());

                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu dữ liệu vào database: " + e.getMessage());
        }
    }

    // Phương thức cũ để lưu dữ liệu vào file
    /*
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    */

    // Lấy tất cả thành viên - không thay đổi
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    // Thêm thành viên mới
    public boolean addMember(Member member) {
        // Tạo ID duy nhất
        String memberId = generateMemberId(member);
        member.setMemberId(memberId);

        // Cập nhật trạng thái thẻ
        updateCardStatus(member);

        // Thêm vào danh sách và lưu
        members.add(member);
        saveToDatabase();
        return true;
    }

    // Tạo ID thành viên dựa vào định dạng đã chỉ định - không thay đổi
    private String generateMemberId(Member member) {
        LocalDate registerDate = member.getRegisterDate();
        int year = registerDate.getYear();

        String packageCode;
        switch (member.getPackageType()) {
            case "1 tháng": packageCode = "T1"; break;
            case "3 tháng": packageCode = "T3"; break;
            case "6 tháng": packageCode = "T6"; break;
            case "12 tháng": packageCode = "T12"; break;
            default: packageCode = "T1";
        }

        String cardTypeCode = member.getCardType().contains("VIP") ? "V" : "T";

        // Tăng và lấy bộ đếm cho loại gói này
        int counter = packageCounters.getOrDefault(member.getPackageType(), 0) + 1;
        packageCounters.put(member.getPackageType(), counter);

        // Định dạng bộ đếm thành số 4 chữ số
        String counterStr = String.format("%04d", counter);

        return year + packageCode + cardTypeCode + counterStr;
    }

    // Cập nhật trạng thái thẻ dựa trên ngày đăng ký và loại gói - không thay đổi
    public void updateCardStatus(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = calculateExpiryDate(member.getRegisterDate(), member.getPackageType());

        if (today.isAfter(expiryDate)) {
            member.setCardStatus("Hết hạn");
        } else {
            member.setCardStatus("Còn hạn");
        }
    }

    // Tính ngày hết hạn dựa vào loại gói - không thay đổi
    private LocalDate calculateExpiryDate(LocalDate registerDate, String packageType) {
        int months;
        switch (packageType) {
            case "1 tháng": months = 1; break;
            case "3 tháng": months = 3; break;
            case "6 tháng": months = 6; break;
            case "12 tháng": months = 12; break;
            default: months = 1;
        }

        return registerDate.plusMonths(months);
    }

    // Xóa thành viên
    public boolean deleteMember(String memberId) {
        boolean removed = members.removeIf(m -> m.getMemberId().equals(memberId));
        if (removed) {
            saveToDatabase();
        }
        return removed;
    }

    // Tìm kiếm thành viên - không thay đổi
    public List<Member> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMembers();
        }

        String searchTerm = keyword.toLowerCase().trim();

        return members.stream()
                .filter(m -> m.getMemberId().toLowerCase().contains(searchTerm) ||
                        m.getFullName().toLowerCase().contains(searchTerm) ||
                        m.getPhoneNumber().toLowerCase().contains(searchTerm) ||
                        m.getCardType().toLowerCase().contains(searchTerm) ||
                        m.getPackageType().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    // Cập nhật tất cả trạng thái thẻ
    public void updateAllCardStatuses() {
        for (Member member : members) {
            updateCardStatus(member);
        }
        saveToDatabase();
    }

    // Phương thức mới để cập nhật thông tin thành viên
    public boolean updateMember(Member member) {
        // Tìm và thay thế thành viên trong danh sách
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId().equals(member.getMemberId())) {
                // Cập nhật trạng thái thẻ trước khi lưu
                updateCardStatus(member);

                // Thay thế thành viên cũ bằng thành viên mới
                members.set(i, member);

                // Lưu vào database
                saveToDatabase();
                return true;
            }
        }
        return false; // Không tìm thấy thành viên
    }

    // Phương thức mới để lấy thành viên theo ID
    public Member getMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }
}