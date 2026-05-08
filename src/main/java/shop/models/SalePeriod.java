package shop.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalePeriod {
    private String periodID;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SaleRecord> records; 
    private boolean isOpen;

    public SalePeriod(String id, String name) {
        this.periodID = id;
        this.name = name;
        this.startTime = LocalDateTime.now();
        this.records = new ArrayList<>();
        this.isOpen = true;
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

    @Override
    public String toString() {
        return "SalePeriod {" +
            " periodID='" + getPeriodID() + "'" +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", records='" + getRecords() + "'" +
            ", isOpen='" + getIsOpen() + "'" +
            " }";
    }

    public String getPeriodID() {
        return this.periodID;
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

    public List<SaleRecord> getRecords() {
        return this.records;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }
}
