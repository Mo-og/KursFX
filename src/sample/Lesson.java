package sample;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Lesson implements Comparable<Lesson>{
    private int order;
    private String name;
    private List<String> teachers;
    private String type;

    @Override
    public String toString() {
        return name;
    }


    @Override
    public int compareTo(Lesson o) {
        return Integer.compare(this.order,o.getOrder());
    }
}
