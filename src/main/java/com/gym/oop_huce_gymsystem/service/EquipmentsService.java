package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.EquipmentsDao;
import com.gym.oop_huce_gymsystem.model.Equipments;
import java.sql.SQLException;
import java.util.List;

public class EquipmentsService {

    private final EquipmentsDao equipmentsDao;

    public EquipmentsService() {
        this.equipmentsDao = new EquipmentsDao();
    }

    public void addEquipment(Equipments equipment) throws SQLException {
        if (equipment == null) {
            throw new IllegalArgumentException("Thông tin thiết bị không được để trống.");
        }
        validateEquipments(equipment); // Kiểm tra tính hợp lệ
        if (equipmentsDao.checkEquipment(equipment)) {
            throw new IllegalArgumentException("Tên thiết bị đã tồn tại.");
        }
        equipmentsDao.addEquipment(equipment);
    }

    public void updateEquipment(Equipments equipment) throws SQLException {
        if (equipment == null || equipment.getEquipmentId() <= 0) {
            throw new IllegalArgumentException("ID thiết bị không hợp lệ.");
        }
        validateEquipments(equipment); // Kiểm tra tính hợp lệ
        equipmentsDao.updateEquipment(equipment);
    }

    public void deleteEquipment(int equipmentId) throws SQLException {
        if (equipmentId <= 0) {
            throw new IllegalArgumentException("ID thiết bị không hợp lệ.");
        }
        equipmentsDao.deleteEquipment(equipmentId);
    }

    public List<Equipments> getAllEquipments() throws SQLException {
        return equipmentsDao.getAllEquipments();
    }

    public Equipments getEquipmentById(int equipmentId) throws SQLException {
        if (equipmentId <= 0) {
            throw new IllegalArgumentException("ID thiết bị không hợp lệ.");
        }
        return equipmentsDao.getEquipmentById(equipmentId);
    }

    public boolean checkEquipment(Equipments equipment) throws SQLException {
        if (equipment == null || equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không được để trống.");
        }
        return equipmentsDao.checkEquipment(equipment);
    }

    public void validateEquipments(Equipments equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("Thông tin thiết bị không được để trống.");
        }
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không được để trống.");
        }
        if (equipment.getStatus() == null || equipment.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống.");
        }
        if (equipment.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng không được âm.");
        }
        if (equipment.getPurchase_Date() == null) {
            throw new IllegalArgumentException("Ngày nhập không được để trống.");
        }
    }
}