package com.smarthome.service;

import com.smarthome.model.Customer;
import com.smarthome.model.Gadget;
import com.smarthome.model.DeletedDeviceEnergyRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EnergyManagementService {
    
    public static class EnergyReport {
        private double totalEnergyKWh;
        private double totalCostRupees;
        private String reportPeriod;
        private List<Gadget> devices;
        
        public EnergyReport(double totalEnergyKWh, double totalCostRupees, String reportPeriod, List<Gadget> devices) {
            this.totalEnergyKWh = totalEnergyKWh;
            this.totalCostRupees = totalCostRupees;
            this.reportPeriod = reportPeriod;
            this.devices = devices;
        }
        
        public double getTotalEnergyKWh() { return totalEnergyKWh; }
        public double getTotalCostRupees() { return totalCostRupees; }
        public String getReportPeriod() { return reportPeriod; }
        public List<Gadget> getDevices() { return devices; }
    }
    
    public EnergyReport generateEnergyReport(Customer customer) {
        List<Gadget> devices = customer.getGadgets();
        double totalEnergyKWh = 0.0;

        // Calculate energy from active devices
        for (Gadget device : devices) {
            double currentSessionEnergy = 0.0;
            if (device.isOn() && device.getLastOnTime() != null) {
                double currentSessionHours = device.getCurrentSessionUsageHours();
                currentSessionEnergy = (device.getPowerRatingWatts() / 1000.0) * currentSessionHours;
            }

            totalEnergyKWh += device.getTotalEnergyConsumedKWh() + currentSessionEnergy;
        }

        // CRITICAL: Include energy consumption from deleted devices for accurate monthly billing
        double deletedDeviceEnergy = customer.getTotalDeletedDeviceEnergyForCurrentMonth();
        totalEnergyKWh += deletedDeviceEnergy;

        double totalCost = calculateSlabBasedCost(totalEnergyKWh);
        String reportPeriod = "Monthly Report - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        return new EnergyReport(totalEnergyKWh, totalCost, reportPeriod, devices);
    }
    
    public double calculateSlabBasedCost(double totalKWh) {
        double totalCost = 0.0;

        if (totalKWh <= 30) {
            totalCost = totalKWh * 1.90;
        } else if (totalKWh <= 75) {
            totalCost = 30 * 1.90 + (totalKWh - 30) * 3.0;
        } else if (totalKWh <= 125) {
            totalCost = 30 * 1.90 + 45 * 3.0 + (totalKWh - 75) * 4.50;
        } else if (totalKWh <= 225) {
            totalCost = 30 * 1.90 + 45 * 3.0 + 50 * 4.50 + (totalKWh - 125) * 6.0;
        } else if (totalKWh <= 400) {
            totalCost = 30 * 1.90 + 45 * 3.0 + 50 * 4.50 + 100 * 6.0 + (totalKWh - 225) * 8.75;
        } else {
            totalCost = 30 * 1.90 + 45 * 3.0 + 50 * 4.50 + 100 * 6.0 + 175 * 8.75 + (totalKWh - 400) * 9.75;
        }

        return Math.round(totalCost * 100.0) / 100.0;
    }
    
    public String getSlabBreakdown(double totalKWh) {
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("\n=== Energy Cost Breakdown (Slab-wise) ===\n");
        breakdown.append("+---------------+----------+----------+----------+\n");
        breakdown.append(String.format("| %-13s | %-8s | %-8s | %-8s |\n", "Slab Range", "Units", "Rate/Unit", "Cost"));
        breakdown.append("+---------------+----------+----------+----------+\n");
        
        double remainingKWh = totalKWh;
        double totalCost = 0.0;
        
        if (remainingKWh > 0) {
            double slab1 = Math.min(remainingKWh, 30);
            double cost1 = slab1 * 1.90;
            totalCost += cost1;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "0-30 kWh", slab1, 1.90, cost1));
            remainingKWh -= slab1;
        }
        
        if (remainingKWh > 0) {
            double slab2 = Math.min(remainingKWh, 45);
            double cost2 = slab2 * 3.0;
            totalCost += cost2;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "31-75 kWh", slab2, 3.0, cost2));
            remainingKWh -= slab2;
        }
        
        if (remainingKWh > 0) {
            double slab3 = Math.min(remainingKWh, 50);
            double cost3 = slab3 * 4.50;
            totalCost += cost3;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "76-125 kWh", slab3, 4.50, cost3));
            remainingKWh -= slab3;
        }
        
        if (remainingKWh > 0) {
            double slab4 = Math.min(remainingKWh, 100);
            double cost4 = slab4 * 6.0;
            totalCost += cost4;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "126-225 kWh", slab4, 6.0, cost4));
            remainingKWh -= slab4;
        }
        
        if (remainingKWh > 0) {
            double slab5 = Math.min(remainingKWh, 175);
            double cost5 = slab5 * 8.75;
            totalCost += cost5;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "226-400 kWh", slab5, 8.75, cost5));
            remainingKWh -= slab5;
        }
        
        if (remainingKWh > 0) {
            double cost6 = remainingKWh * 9.75;
            totalCost += cost6;
            breakdown.append(String.format("| %-13s | %8.2f | %8.2f | %8.2f |\n", "400+ kWh", remainingKWh, 9.75, cost6));
        }
        
        breakdown.append("+---------------+----------+----------+----------+\n");
        breakdown.append(String.format("| %-13s | %8.2f | %-8s | %8.2f |\n", "TOTAL", totalKWh, "-", totalCost));
        breakdown.append("+---------------+----------+----------+----------+\n");
        
        return breakdown.toString();
    }
    
    public void displayDeviceEnergyUsage(Customer customer) {
        List<Gadget> devices = customer.getGadgets();

        System.out.println("\n=== Device Energy Usage Details ===");
        System.out.println("+-------------------------+---------+---------+-------------+-------------+-------------+");
        System.out.printf("| %-23s | %-7s | %-7s | %-11s | %-11s | %-11s |\n",
                         "Device", "Power", "Status", "Usage Time", "Energy(kWh)", "Cost(Rs.)");
        System.out.println("+-------------------------+---------+---------+-------------+-------------+-------------+");

        // Display active devices
        for (Gadget device : devices) {
            double totalEnergy = device.getTotalEnergyConsumedKWh();

            if (device.isOn() && device.getLastOnTime() != null) {
                double currentSessionHours = device.getCurrentSessionUsageHours();
                double currentSessionEnergy = (device.getPowerRatingWatts() / 1000.0) * currentSessionHours;
                totalEnergy += currentSessionEnergy;
            }

            double deviceCost = calculateSlabBasedCost(totalEnergy);
            String status = device.isOn() ? "RUNNING" : "OFF";
            String deviceName = String.format("%s %s (%s)", device.getType(), device.getModel(), device.getRoomName());
            if (deviceName.length() > 23) {
                deviceName = deviceName.substring(0, 20) + "...";
            }

            System.out.printf("| %-23s | %7.0fW | %-7s | %11s | %11.3f | %11.2f |\n",
                             deviceName,
                             device.getPowerRatingWatts(),
                             status,
                             device.getCurrentUsageTimeFormatted(),
                             totalEnergy,
                             deviceCost);

            if (device.isOn() && device.getLastOnTime() != null) {
                System.out.printf("| %-23s | %-7s | %-7s | %-11s | %-11s | %-11s |\n",
                                 "  Current Session:", "", "", String.format("%.2fh", device.getCurrentSessionUsageHours()), "", "");
            }
        }

        // Display deleted devices energy consumption for current month
        double deletedDeviceEnergy = customer.getTotalDeletedDeviceEnergyForCurrentMonth();
        if (deletedDeviceEnergy > 0) {
            System.out.println("+-------------------------+---------+---------+-------------+-------------+-------------+");
            double deletedDeviceCost = calculateSlabBasedCost(deletedDeviceEnergy);
            System.out.printf("| %-23s | %-7s | %-7s | %-11s | %11.3f | %11.2f |\n",
                             "[DELETED DEVICES]", "-", "DELETED", "-", deletedDeviceEnergy, deletedDeviceCost);
            System.out.printf("| %-23s | %-7s | %-7s | %-11s | %-11s | %-11s |\n",
                             "  (Historical data)", "", "", "", "", "");
        }

        System.out.println("+-------------------------+---------+---------+-------------+-------------+-------------+");

        // Show detailed breakdown of deleted devices if any exist
        if (deletedDeviceEnergy > 0) {
            displayDeletedDeviceBreakdown(customer);
        }
    }

    public void displayDeletedDeviceBreakdown(Customer customer) {
        LocalDateTime now = LocalDateTime.now();
        String currentMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());

        List<DeletedDeviceEnergyRecord> deletedDevices = customer.getDeletedDeviceEnergyRecords();
        List<DeletedDeviceEnergyRecord> currentMonthDeleted = deletedDevices.stream()
                .filter(record -> currentMonth.equals(record.getDeletionMonth()))
                .toList();

        if (!currentMonthDeleted.isEmpty()) {
            System.out.println("\n=== Deleted Devices Energy Breakdown (This Month) ===");
            System.out.println("+-------------------------+---------+-------------+-------------+----------------------+");
            System.out.printf("| %-23s | %-7s | %-11s | %-11s | %-20s |\n",
                             "Deleted Device", "Power", "Usage Time", "Energy(kWh)", "Deletion Date");
            System.out.println("+-------------------------+---------+-------------+-------------+----------------------+");

            for (DeletedDeviceEnergyRecord record : currentMonthDeleted) {
                String deviceName = String.format("%s %s (%s)", record.getDeviceType(), record.getDeviceModel(), record.getRoomName());
                if (deviceName.length() > 23) {
                    deviceName = deviceName.substring(0, 20) + "...";
                }

                String deletionDate = record.getDeletionTime().format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));

                System.out.printf("| %-23s | %7.0fW | %-11s | %11.3f | %-20s |\n",
                                 deviceName,
                                 record.getPowerRatingWatts(),
                                 record.getFormattedUsageTime(),
                                 record.getTotalEnergyConsumedKWh(),
                                 deletionDate);
            }
            System.out.println("+-------------------------+---------+-------------+-------------+----------------------+");
        }
    }
    
    public String getEnergyEfficiencyTips(double totalKWh) {
        StringBuilder tips = new StringBuilder();
        tips.append("\n=== Energy Efficiency Tips ===\n");
        
        if (totalKWh > 300) {
            tips.append("[HIGH USAGE ALERT] Your consumption is quite high!\n");
            tips.append("- Consider using Timer functions to auto-schedule device operations\n");
            tips.append("- Turn off devices when not in use\n");
            tips.append("- Use energy-efficient appliances\n");
        } else if (totalKWh > 150) {
            tips.append("[MODERATE USAGE] You're doing well, but there's room for improvement!\n");
            tips.append("- Use Timer scheduling for AC and Geyser during off-peak hours\n");
            tips.append("- Consider LED lighting for better efficiency\n");
        } else {
            tips.append("[EXCELLENT] Great job on managing your energy consumption!\n");
            tips.append("- Keep up the good work with energy-conscious usage\n");
        }
        
        tips.append("- Peak hours (6-10 PM): Avoid using high-power devices\n");
        tips.append("- Use our Calendar Events to schedule energy-intensive operations\n");
        
        return tips.toString();
    }
}