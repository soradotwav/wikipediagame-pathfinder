package net.soradotwav;

import java.util.Comparator;

public class CustomComparator {
    private String url;
    private double priority;

    public CustomComparator(String url, double priority) {
        this.url = url;
        this.priority = priority;
    }

    public String getUrl() {
        return url;
    }

    public double getPriority() {
        return priority;
    }
}

// Custom comparator based on priority values (descending order)
class PriorityComparator implements Comparator<CustomComparator> {
    @Override
    public int compare(CustomComparator s1, CustomComparator s2) {
        double priority1 = s1.getPriority();
        double priority2 = s2.getPriority();
        return Double.compare(priority2, priority1); // Compare in reverse order
    }
}