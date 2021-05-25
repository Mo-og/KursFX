package sample;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class Lesson implements Comparable<Lesson> {
    private int order;
    private String name;
    private List<String> teachers;
    private String type;

    public void removeTeacher(String s) {
        teachers.remove(s);
    }

    public void addTeacher(String s) {
        if (teachers.contains(s))
            return;
        teachers.add(s);
        teachers.sort(String::compareToIgnoreCase);
    }

    public void renameTeacher(String from, String to) {
        int index = teachers.indexOf(from);
        if (index != -1)
            teachers.set(index, to);
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public int compareTo(Lesson o) {
        return Integer.compare(this.order, o.getOrder());
    }
}
