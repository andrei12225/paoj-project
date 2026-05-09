package shop.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalePeriod {
    private int periodID;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SaleRecord> records; 
    private boolean isOpen;

    public SalePeriod(int id, String name) {
        this.periodID = id;
        this.name = name;
        this.startTime = LocalDateTime.now();
        this.records = new ArrayList<>();
        this.isOpen = true;
    }

    public SalePeriod(int id, String name, LocalDateTime startTime, LocalDateTime endTime, boolean isOpen) {
        this.periodID = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.records = new ArrayList<>();
        this.isOpen = isOpen;
    }

    public void closePeriod() {
        this.endTime = LocalDateTime.now();
        this.isOpen = false;
    }

    public double getTotalRevenue() {
        return this.records.stream()
                    .mapToDouble(s -> s.getTotalSaleAmount())
                    .sum();
    }

    public String getDisplayId() {
        return "SP-" + String.format("%06d", periodID);
    }

    @Override
    public String toString() {
        return "SalePeriod {" +
            " periodID='" + getDisplayId() + "'" +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", records='" + getRecords() + "'" +
            ", isOpen='" + getIsOpen() + "'" +
            " }";
    }

    public int getPeriodID() {
        return this.periodID;
    }

    public void setPeriodID(int periodID) {
        this.periodID = periodID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setRecords(List<SaleRecord> records) {
        this.records = records;
    }

    public List<SaleRecord> getRecords() {
        return this.records;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }
}
