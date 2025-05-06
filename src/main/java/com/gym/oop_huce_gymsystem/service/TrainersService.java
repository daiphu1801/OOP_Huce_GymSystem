package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.TrainersDao;
import com.gym.oop_huce_gymsystem.model.Trainers;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class TrainersService {
    private final TrainersDao trainersDAO;

    // Regex để kiểm tra định dạng email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    // Regex để kiểm tra số điện thoại (10-11 chữ số, bắt đầu bằng 0)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9,10}$");

    public TrainersService() {
        this.trainersDAO = new TrainersDao();
    }

    public TrainersService(TrainersDao trainersDAO) {
        this.trainersDAO = trainersDAO;
    }

    // Thêm huấn luyện viên với validation
    public void addTrainers(Trainers trainer) throws Exception {
        validateTrainer(trainer, true);
        trainersDAO.addTrainers(trainer);
    }

    // Cập nhật thông tin huấn luyện viên với validation
    public void updateTrainers(Trainers trainer) throws Exception {
        if (trainer == null || trainer.getTrainerId() <= 0) {
            throw new IllegalArgumentException("ID huấn luyện viên không hợp lệ.");
        }

        // Validate tên
        if (trainer.getName() == null || trainer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên huấn luyện viên không được để trống.");
        }

        // Validate giới tính
        if (trainer.getGender() == null || !trainer.getGender().matches("^(Nam|Nữ|Khác)$")) {
            throw new IllegalArgumentException("Giới tính phải là 'Nam', 'Nữ' hoặc 'Khác'.");
        }

        if(trainer.getPhone() == null) {
            throw  new Exception("Số điện thoại không để trống.");
        }

        if (!trainer.getPhone().matches("\\d{10}") || trainer.getPhone().length() != 10 || trainer.getPhone().charAt(0) != '0'){
            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số) hoặc là phải là chữ số 0 ở đầu.");
        }

        if (!PHONE_PATTERN.matcher(trainer.getPhone()).matches()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ. Phải có 10-11 chữ số và bắt đầu bằng 0.");
        }

        // Validate email
        if (trainer.getEmail() != null && !trainer.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(trainer.getEmail()).matches()) {
                throw new IllegalArgumentException("Email không hợp lệ.");
            }
        }

        // Validate chuyên môn
        if (trainer.getSpecialization() == null || trainer.getSpecialization().trim().isEmpty()) {
            throw new IllegalArgumentException("Chuyên môn không được để trống.");
        }
        if (trainer.getSpecialization().length() > 200) {
            throw new IllegalArgumentException("Chuyên môn không được dài quá 200 ký tự.");
        }
        trainersDAO.updateTrainers(trainer);
    }

    // Xóa huấn luyện viên với validation
    public void deleteTrainers(int trainerId) throws SQLException {
        if (trainerId <= 0) {
            throw new IllegalArgumentException("ID huấn luyện viên không hợp lệ.");
        }
        if (!trainersDAO.trainerExists(trainerId)) {
            throw new SQLException("Huấn luyện viên với ID: " + trainerId + " không tồn tại.");
        }
        trainersDAO.deleteTrainers(trainerId);
    }

    // Lấy danh sách tất cả huấn luyện viên
    public List<Trainers> getAllTrainers() throws SQLException {
        return trainersDAO.getAllTrainers();
    }

    // Lấy chi tiết huấn luyện viên theo ID với validation
    public Trainers getTrainerById(int trainerId) throws SQLException {
        if (trainerId <= 0) {
            throw new IllegalArgumentException("ID huấn luyện viên không hợp lệ.");
        }
        return trainersDAO.getTrainerById(trainerId);
    }


    // Hàm validation dữ liệu huấn luyện viên
    private void validateTrainer(Trainers trainer, boolean isNew) throws Exception {
        if (trainer == null) {
            throw new IllegalArgumentException("Thông tin huấn luyện viên không được để trống.");
        }

        // Validate tên
        if (trainer.getName() == null || trainer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên huấn luyện viên không được để trống.");
        }

        // Validate giới tính
        if (trainer.getGender() == null || !trainer.getGender().matches("^(Nam|Nữ|Khác)$")) {
            throw new IllegalArgumentException("Giới tính phải là 'Nam', 'Nữ' hoặc 'Khác'.");
        }

        if(trainer.getPhone() == null) {
            throw  new Exception("Số điện thoại không để trống.");
        }

        if (!trainer.getPhone().matches("\\d{10}") || trainer.getPhone().length() != 10 || trainer.getPhone().charAt(0) != '0'){
            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số) hoặc là phải là chữ số 0 ở đầu.");
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (trainersDAO.isPhoneExists(trainer.getPhone())) {
            throw new Exception("Số điện thoại đã tồn tại trong hệ thống.");
        }

        if (!PHONE_PATTERN.matcher(trainer.getPhone()).matches()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ. Phải có 10-11 chữ số và bắt đầu bằng 0.");
        }

        // Validate email
        if (trainer.getEmail() != null && !trainer.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(trainer.getEmail()).matches()) {
                throw new IllegalArgumentException("Email không hợp lệ.");
            }
        } else if (isNew) {
            throw new IllegalArgumentException("Email không được để trống khi thêm mới.");
        }

        // Validate chuyên môn
        if (trainer.getSpecialization() == null || trainer.getSpecialization().trim().isEmpty()) {
            throw new IllegalArgumentException("Chuyên môn không được để trống.");
        }
        if (trainer.getSpecialization().length() > 200) {
            throw new IllegalArgumentException("Chuyên môn không được dài quá 200 ký tự.");
        }
    }
}