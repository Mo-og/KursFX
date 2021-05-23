package sample;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Day implements Comparable<Day> {
    private int order;
    private String name;
    private List<Lesson> lessons;

    public Day(int order, String name) {
        this.order = order;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public int compareTo(Day o) {
        return Integer.compare(this.order, o.getOrder());
    }
}
