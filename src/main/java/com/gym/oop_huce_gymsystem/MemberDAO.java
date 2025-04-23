package com.gym.oop_huce_gymsystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberDAO {
    private static MemberDAO instance;
    private final String DATA_FILE = "members.dat";
    private List<Member> members;

    // Counter maps to track the next sequence number for each package type
    private Map<String, Integer> packageCounters;

    private MemberDAO() {
        members = new ArrayList<>();
        packageCounters = new HashMap<>();
        loadData();
    }

    public static MemberDAO getInstance() {
        if (instance == null) {
            instance = new MemberDAO();
        }
        return instance;
    }

    // Load data from file
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

    // Initialize counters based on existing members
    private void initializeCounters() {
        packageCounters = new HashMap<>();

        // Default initialize all package types
        packageCounters.put("1 tháng", 0);
        packageCounters.put("3 tháng", 0);
        packageCounters.put("6 tháng", 0);
        packageCounters.put("12 tháng", 0);

        // Find the highest counter for each package type
        for (Member member : members) {
            String packageType = member.getPackageType();
            String memberId = member.getMemberId();

            if (memberId != null && memberId.length() >= 10) {
                try {
                    // Extract counter from the last 4 digits
                    int counter = Integer.parseInt(memberId.substring(memberId.length() - 4));
                    int currentMax = packageCounters.getOrDefault(packageType, 0);
                    if (counter > currentMax) {
                        packageCounters.put(packageType, counter);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid member ID format: " + memberId);
                }
            }
        }
    }

    // Save data to file
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Get all members
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    // Add a new member
    public boolean addMember(Member member) {
        // Generate a unique member ID
        String memberId = generateMemberId(member);
        member.setMemberId(memberId);

        // Set initial card status
        updateCardStatus(member);

        // Add to list and save
        members.add(member);
        saveData();
        return true;
    }

    // Generate a member ID based on the specified format
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

        // Increment and get the counter for this package type
        int counter = packageCounters.getOrDefault(member.getPackageType(), 0) + 1;
        packageCounters.put(member.getPackageType(), counter);

        // Format the counter as a 4-digit number
        String counterStr = String.format("%04d", counter);

        return year + packageCode + cardTypeCode + counterStr;
    }

    // Update card status based on registration date and package type
    public void updateCardStatus(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = calculateExpiryDate(member.getRegisterDate(), member.getPackageType());

        if (today.isAfter(expiryDate)) {
            member.setCardStatus("Hết hạn");
        } else {
            member.setCardStatus("Còn hạn");
        }
    }

    // Calculate expiry date based on package type
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

    // Delete a member
    public boolean deleteMember(String memberId) {
        boolean removed = members.removeIf(m -> m.getMemberId().equals(memberId));
        if (removed) {
            saveData();
        }
        return removed;
    }

    // Search members
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

    // Update all card statuses (can be called periodically)
    public void updateAllCardStatuses() {
        for (Member member : members) {
            updateCardStatus(member);
        }
        saveData();
    }
}