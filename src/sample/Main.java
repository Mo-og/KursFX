package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {
    static ObservableList<String> teachers = FXCollections.observableArrayList(
            "Учитель А", "Учитель Б", "Учитель В", "Учитель Г",
            "Учитель Д", "Учитель Е", "Учитель Ё", "Учитель Ж", "Учитель З");
    static ObservableList<String> lessons = FXCollections.observableArrayList(
            "Алгоритмизация", "Английский", "Высшая математика", "Физкультура",
            "История", "Физика", "Правоведение", "Информатика");
    static ObservableList<String> types = FXCollections.observableArrayList(
            "Лабораторная", "Практическая", "Лекция");
    static final int ORDER_MAX_LENGTH = 1; //amount of digits in order (№_ for 1, №_____ for 5)
    static MultipleSelectionModel<Day> selectedDays;
    static MultipleSelectionModel<Lesson> selectedLessons;
    static MultipleSelectionModel<String> selectedTeachers;
    static MultipleSelectionModel<String> selectedTypes;

    public static void refreshLessonTeachers() {
        int index = selectedLessons.getSelectedIndices().get(0);
        selectedLessons.clearSelection();
        selectedLessons.select(index);
    }
    public static void refreshDays() {
        int index = selectedDays.getSelectedIndices().get(0);
        selectedDays.clearSelection();
        selectedDays.select(index);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


    //////////////////////////////////////////////////////////////////////
    //           TEST DATA METHODS                                      //
    //////////////////////////////////////////////////////////////////////
    public static Day getDay(int order, String name, List<Lesson> lessons) {
        if (lessons == null)
            return new Day(order, name);
        return new Day(order, name, lessons);
    }

    public static Lesson getLesson(int order, String name, String... teachers) {
        ArrayList<String> ts = new ArrayList<>(Arrays.asList(teachers));
        return new Lesson(order, name, ts, getRandType());
    }

    public static Lesson getRandLesson(int order) {
        return getLesson(order, getRandName(), getRandTeacherName());
    }

    public static String getRandName() {
        Random random = new Random();
        return lessons.get(random.nextInt(8));
    }

    public static String getRandTeacherName() {
        Random random = new Random();
        return teachers.get(random.nextInt(8));
    }

    public static String getRandType() {
        Random random = new Random();
        return types.get(random.nextInt(3));
    }
    //////////////////////////////////////////////////////////////////////

    // Highlights field red if it has no digits and forces it to have only one
    private void showError(TextField t, ListView<?> l) {
        String s = "№" + t.getText().replaceAll("\\D", "");

        if (t.getText().length() > ORDER_MAX_LENGTH) {
            if (s.length() > ORDER_MAX_LENGTH)
                t.setText(s.substring(0, ORDER_MAX_LENGTH));
            else t.setText(s);
            t.positionCaret(ORDER_MAX_LENGTH + 1);
        }
        t.setStyle("-fx-text-inner-color: black;");
        l.setStyle("-fx-background-color: null;");
    }

    @Override
    public void start(Stage stage) {

        //////////////////////////////////////////////////////////////////////
        //           TEST DATA                                              //
        //////////////////////////////////////////////////////////////////////
        ObservableList<Lesson> lessons1 = FXCollections.observableArrayList(getRandLesson(1), getRandLesson(2), getRandLesson(3));
        ObservableList<Lesson> lessons2 = FXCollections.observableArrayList(getRandLesson(1), getRandLesson(2), getRandLesson(3));
        ObservableList<Lesson> lessons3 = FXCollections.observableArrayList(getRandLesson(1), getRandLesson(2));
        ObservableList<Lesson> lessons4 = FXCollections.observableArrayList(getRandLesson(1), getRandLesson(2), getRandLesson(3));
        ObservableList<Lesson> lessons5 = FXCollections.observableArrayList(getRandLesson(1), getRandLesson(2), getRandLesson(3), getRandLesson(4));

        ObservableList<Day> days = FXCollections.observableArrayList(
                getDay(1, "Понедельник", lessons1),
                getDay(2, "Вторник", lessons2),
                getDay(3, "Среда", lessons3),
                getDay(4, "Четверг", lessons4),
                getDay(5, "Пятница", lessons5));
        //////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////
        //  TOP FIELDS - '[№1]' '[Понедельник]' | '[№1]' '[Алгоритмизация]' | '[Учитель 1]' - поля ввода
        //////////////////////////////////////////////////////////////////////
        TextField daysOrder = new TextField("№");
        daysOrder.setMaxWidth(52);
        TextField lessonsOrder = new TextField("№");
        lessonsOrder.setMaxWidth(50);

        TextField daysField = new TextField();
        daysField.setPrefWidth(155);
        TextField lessonsField = new TextField();
        lessonsField.setPrefWidth(155);
        TextField teachersField = new TextField();
        teachersField.setPrefWidth(217);
        //////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////
        //           DISPLAYED DATA LISTS                                   //
        //////////////////////////////////////////////////////////////////////
        ListView<Day> displayedDays = new ListView<>(days);
        ListView<Lesson> displayedLessons = new ListView<>();
        ListView<String> displayedTeachers = new ListView<>(teachers);
        ListView<String> displayedLessonTeachers = new ListView<>();
        TextField displayedLessonType = new TextField();
        ListView<String> displayedTypes = new ListView<>(types);
        ListView<String> displayedTeacherLessons = new ListView<>();
        displayedDays.setPrefSize(240, 200);
        displayedLessons.setPrefSize(240, 160);
        displayedTeachers.setPrefSize(240, 100);
        displayedLessonTeachers.setPrefSize(240, 110);
        displayedLessonType.setPrefWidth(240);
        displayedTypes.setPrefSize(240, 100);
        displayedTeacherLessons.setPrefSize(240, 140);

        displayedLessonType.setEditable(false);
        Text constTeacherLabel = new Text("Преподаватели:");
        Text constTypeLabel = new Text("Форма занятия:");
        //////////////////////////////////////////////////////////////////////

        // BUTTONS
        Button addDayBtn = new Button("Добавить");
        Button addLessonBtn = new Button("Добавить");
        Button addTeacherBtn = new Button("Добавить");
        Button addTeacherToLessonBtn = new Button("Добавить к предмету");
        Button applyTypeToLessonBtn = new Button("Применить к предмету");
        applyTypeToLessonBtn.setPrefWidth(240);
        Button deleteTeacherFromLessonBtn = new Button("Убрать из предмета");
        addTeacherToLessonBtn.setDisable(true);
        deleteTeacherFromLessonBtn.setDisable(true);
        Button clearTeacherSelection = new Button("Очистить выделение");
        Button clearLessonSelection = new Button("Очистить выделение");
        clearLessonSelection.setPrefWidth(240);
        Button deleteDayBtn = new Button("Удалить");
        Button deleteLessonBtn = new Button("Удалить");
        Button deleteTeacherBtn = new Button("Удалить");

        //====== RadioButton group for lessons types
        ToggleGroup tgroup = new ToggleGroup();
        RadioButton rb_teacher = new RadioButton("Выбранного преподавателя");
        rb_teacher.setToggleGroup(tgroup);
        RadioButton rb_type = new RadioButton("Выбранной формы занятия");
        rb_type.setToggleGroup(tgroup);
        RadioButton rb_3 = new RadioButton("Не подствечивать");
        rb_3.setToggleGroup(tgroup);
        rb_3.setSelected(true);

        FlowPane displayedToggles = new FlowPane(Orientation.VERTICAL, rb_teacher, rb_type, rb_3);
        displayedToggles.setPrefSize(240, 95);
        // BUTTONS AND FIELDS CONTAINERS
        FlowPane buttonsDays = new FlowPane(10, 10, daysOrder, daysField, addDayBtn, deleteDayBtn);
        //You can add days if you disable this
        buttonsDays.setVisible(false); //todo
        //^
        FlowPane buttonsLessons = new FlowPane(10, 10, lessonsOrder, lessonsField, addLessonBtn, deleteLessonBtn);
        FlowPane buttonsTeachers = new FlowPane(10, 10, teachersField, addTeacherBtn, deleteTeacherBtn);
        FlowPane buttonsLessonTeachers = new FlowPane(10, 10, addTeacherToLessonBtn, deleteTeacherFromLessonBtn, clearTeacherSelection);
        buttonsLessonTeachers.setAlignment(Pos.BASELINE_CENTER);
        // FOR FINDING WHAT IS SELECTED
        selectedDays = displayedDays.getSelectionModel();
        selectedDays.setSelectionMode(SelectionMode.MULTIPLE);
        selectedLessons = displayedLessons.getSelectionModel();
        selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
        selectedTeachers = displayedTeachers.getSelectionModel();
        selectedTypes = displayedTypes.getSelectionModel();
        //////////////////////////////////////////////////////////////////////
        //           EVENTS                                                 //
        //////////////////////////////////////////////////////////////////////
        selectedDays.selectedItemProperty().addListener((changed, oldValue, day) -> {
            if (day != null) {
                daysField.setText(day.toString());
                daysOrder.setText("№" + day.getOrder());
                if (!selectedDays.getSelectedItems().isEmpty()) {
                    List<Lesson> tempLessons = day.getLessons();
                    Collections.sort(tempLessons);
                    ObservableList<Lesson> lessons = FXCollections.observableArrayList(tempLessons);
                    displayedLessons.setItems(lessons);
                    if (rb_3.isSelected())
                        selectedLessons.select(0);
                    else {
                        if (rb_teacher.isSelected()) {
                            selectedLessons.clearSelection();
                            for (int i = 0; i < lessons.size(); i++) {
                                for (String s : lessons.get(i).getTeachers())
                                    if (s.compareToIgnoreCase(selectedTeachers.getSelectedItem().trim()) == 0) {
                                        selectedLessons.selectIndices(i);
                                        break;
                                    }
                            }
                        } else {
                            selectedLessons.clearSelection();
                            for (Lesson l : lessons)
                                if (l.getType().compareToIgnoreCase(selectedTypes.getSelectedItem())==0)
                                    selectedLessons.select(l);
                        }
                    }
                } else selectedDays.select(0);
            }
        });
        selectedLessons.selectedItemProperty().addListener((changed, oldValue, lesson) -> {
            ObservableList<Lesson> selected = selectedLessons.getSelectedItems();
            if (lesson != null) {
                lessonsField.setText(lesson.getName());
                lessonsOrder.setText("№" + lesson.getOrder());
                if (selected.size() == 1) {
                    addLessonBtn.setText("Изменить");
                    addTeacherToLessonBtn.setDisable(false);
                    deleteTeacherFromLessonBtn.setDisable(false);
                    displayedLessonType.setText(lesson.getType());
                    if (rb_3.isSelected())
                        selectedTypes.select(lesson.getType());
                    displayedLessonTeachers.setItems(FXCollections.observableArrayList(lesson.getTeachers()));
                } else {
                    addLessonBtn.setText("Добавить");
                    addTeacherToLessonBtn.setDisable(true);
                    deleteTeacherFromLessonBtn.setDisable(true);
                    displayedLessonType.setText("Выбрано более одного предмета");
                    displayedLessonTeachers.setItems(FXCollections.observableArrayList("Выбрано","2 или более","элемента"));
                }

            } else {
                addLessonBtn.setText("Добавить");
                addTeacherToLessonBtn.setDisable(true);
                deleteTeacherFromLessonBtn.setDisable(true);
            }
        });
        selectedTeachers.selectedItemProperty().addListener((observable, oldValue, teacher) -> {
            if (rb_teacher.isSelected()) {
                int i = selectedDays.getSelectedIndex();
                selectedDays.clearSelection();
                selectedDays.select(i);
                ObservableList<String> lessonsAndDays = FXCollections.observableArrayList();
                for (Day d : days)
                    for (Lesson l : d.getLessons())
                        for (String t : l.getTeachers())
                            if (t.compareToIgnoreCase(selectedTeachers.getSelectedItem()) == 0)
                                lessonsAndDays.add(d.getName() + ", " + l.getName());
                displayedTeacherLessons.setItems(lessonsAndDays);
            } else displayedTeacherLessons.setItems(FXCollections.observableArrayList());
            if (selectedTeachers.isEmpty()) {
                addTeacherBtn.setText("Добавить");
                teachersField.setText("");
                deleteTeacherBtn.setDisable(true);
            } else {
                teachersField.setText(teacher);
                addTeacherBtn.setText("Изменить");
                deleteTeacherBtn.setDisable(false);
            }
        });
        selectedTypes.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (rb_type.isSelected())
                selectedDays.clearAndSelect(selectedDays.getSelectedIndex());
        });
        clearTeacherSelection.setOnAction(event -> {
            selectedTeachers.clearSelection();
            addTeacherToLessonBtn.setDisable(true);
            deleteTeacherFromLessonBtn.setDisable(true);
        });
        clearLessonSelection.setOnAction(event -> {
            selectedLessons.clearSelection();
            lessonsField.setStyle("-fx-text-inner-color: black;");
            displayedLessons.setStyle("-fx-background-color: null;");
        });
        daysOrder.setOnKeyTyped(event -> showError(daysOrder, displayedDays));
        daysField.setOnKeyTyped(event -> {
            daysField.setStyle("-fx-text-inner-color: black;");
            displayedDays.setStyle("-fx-background-color: null;");
        });
        teachersField.setOnKeyTyped(event -> {
            teachersField.setStyle("-fx-text-inner-color: black;");
            displayedTeachers.setStyle("-fx-background-color: null;");
        });
        lessonsOrder.setOnKeyTyped(event -> showError(lessonsOrder, displayedLessons));
        lessonsField.setOnKeyTyped(event -> {
            lessonsField.setStyle("-fx-text-inner-color: black;");
            displayedLessons.setStyle("-fx-background-color: null;");
        });
        addDayBtn.setOnAction(event -> {
            for (Day d : days)
                if (daysField.getText().trim().isEmpty() || daysField.getText().trim().compareToIgnoreCase(d.getName()) == 0) {
                    daysField.setStyle("-fx-text-inner-color: red;");
                    if (!daysField.getText().trim().isEmpty())
                        selectedDays.clearAndSelect(days.indexOf(d));
                    displayedDays.setStyle("-fx-background-color: red;");
                    return;
                }
            if (daysOrder.getText().replaceAll("\\D", "").isEmpty()) {
                daysOrder.setStyle("-fx-text-inner-color: red;");
                return;
            } else daysOrder.setStyle("-fx-text-inner-color: black;");
            int order = Integer.parseUnsignedInt(daysOrder.getText().replaceAll("\\D", ""));
            if (order < 7) {
                for (Day d : days) {
                    if (d.getOrder() >= order)
                        d.setOrder(d.getOrder() + 1);
                }
            }
            if (days.size() >= 6)
                addDayBtn.setDisable(true);
            days.add(new Day(order, daysField.getText(), new ArrayList<>()));
            FXCollections.sort(days);
            displayedDays.setItems(days);
        });
        addLessonBtn.setOnAction(event -> {
            if (lessonsOrder.getText().replaceAll("\\D", "").isEmpty()) {
                lessonsOrder.setStyle("-fx-text-inner-color: red;");
                return;
            } else lessonsOrder.setStyle("-fx-text-inner-color: black;");
            Day curDay = null;
            if (selectedDays.getSelectedItems().size() == 1)
                curDay = selectedDays.getSelectedItem();
            if (curDay == null)
                return;

            List<Lesson> lessons = curDay.getLessons();
            if (selectedLessons.getSelectedItems().size() == 1) {
                Lesson curLesson = selectedLessons.getSelectedItem();
                curLesson.setName(lessonsField.getText());
            } else {
                for (Lesson l : lessons)
                    if (lessonsField.getText().trim().compareToIgnoreCase(l.getName()) == 0) {
                        lessonsField.setStyle("-fx-text-inner-color: red;");
                        selectedLessons.select(l);
                        displayedLessons.setStyle("-fx-background-color: red;");
                        return;
                    }
                int order = Integer.parseUnsignedInt(lessonsOrder.getText().replaceAll("\\D", ""));
                if (order < lessons.size()) {
                    for (Lesson l : lessons) {
                        if (l.getOrder() >= order)
                            l.setOrder(l.getOrder() + 1);
                    }
                }
                lessons.add(new Lesson(order, lessonsField.getText().trim(), new ArrayList<>(5), ""));
            }
            Collections.sort(lessons);
            curDay.setLessons(lessons);
            displayedLessons.setItems(FXCollections.observableArrayList(lessons));
            displayedLessons.refresh();
        });
        addTeacherBtn.setOnAction(event -> {
            String teacherName = teachersField.getText().trim();
            String teacher = selectedTeachers.getSelectedItem();
            if (!selectedTeachers.isEmpty() && teacher.compareToIgnoreCase(teacherName) != 0) {
                teachers.set(teachers.indexOf(teacher), teacherName);
                for (Day d : days)
                    for (Lesson l : d.getLessons())
                        l.renameTeacher(teacher, teacherName);
                refreshLessonTeachers();
            } else {
                for (String t : teachers)
                    if (teacherName.isEmpty() || teacherName.compareToIgnoreCase(t) == 0) {
                        teachersField.setStyle("-fx-text-inner-color: red;");
                        if (!teacherName.isEmpty())
                            selectedTeachers.clearAndSelect(teachers.indexOf(t));
                        displayedTeachers.setStyle("-fx-background-color: red;");
                        return;
                    }
                teachersField.setStyle("-fx-text-inner-color: black;");
                teachers.add(teacherName);
            }
            FXCollections.sort(teachers);
            displayedTeachers.setItems(teachers);
            displayedTeachers.refresh();
            selectedTeachers.select(teacherName);
        });
        addTeacherToLessonBtn.setOnAction(event -> {
            selectedLessons.getSelectedItem().addTeacher(selectedTeachers.getSelectedItem());
            refreshLessonTeachers();
        });
        applyTypeToLessonBtn.setOnAction(event -> {
            selectedLessons.getSelectedItem().setType(selectedTypes.getSelectedItem());
            displayedLessonType.setText(selectedTypes.getSelectedItem());
        });
        deleteDayBtn.setOnAction(event -> {
            int order = -5;
            for (int i = 0; i < days.size(); i++) {
                if (days.get(i).getName().compareToIgnoreCase(daysField.getText()) == 0) {
                    order = days.get(i).getOrder();
                    days.remove(days.get(i));
                }
            }
            if (order != -5)
                for (Day d : days)
                    if (d.getOrder() >= order)
                        d.setOrder(d.getOrder() - 1);
            if (days.size() < 7)
                addDayBtn.setDisable(false);
            if (!displayedDays.getItems().isEmpty())
                selectedDays.select(0);
        });
        deleteLessonBtn.setOnAction(event -> {
            int order = -5;
            Day curDay = null;
            for (Day d : days)
                if (d.compareTo(displayedDays.getItems().get(selectedDays.getSelectedIndex())) == 0) {
                    curDay = d;
                    break;
                }
            if (curDay == null)
                return;
            int count=0;
            List<Lesson> lessons = curDay.getLessons();
            int fieldOrder = Integer.parseInt(lessonsOrder.getText().replaceAll("\\D",""));
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                if (lesson.getName().trim().compareToIgnoreCase(lessonsField.getText().trim()) == 0
                        && lesson.getOrder()==fieldOrder) {
                    order = lesson.getOrder();
                    lessons.remove(lesson);
                    count++;
                }
            }
            if (order != -5)
                for (Lesson l : lessons)
                    if (l.getOrder() >= order)
                        l.setOrder(l.getOrder() - count);
            curDay.setLessons(lessons);
            displayedLessons.setItems(FXCollections.observableArrayList(lessons));
        });
        deleteTeacherBtn.setOnAction(event -> {
            String teacher = teachersField.getText().trim();
            teachers.remove(teacher);
            for (Day d : days)
                for (Lesson l : d.getLessons())
                    l.removeTeacher(teacher);
            selectedTeachers.clearSelection();
            displayedTeachers.refresh();
        });
        deleteTeacherFromLessonBtn.setOnAction(event -> {
            selectedLessons.getSelectedItem().removeTeacher(selectedTeachers.getSelectedItem());
            refreshLessonTeachers();
        });
        rb_teacher.setOnAction(event -> {
            if (selectedTeachers.getSelectedItems().isEmpty())
                selectedTeachers.select(0);
//            displayedLessonType.setDisable(true);
//            displayedLessonTeachers.setDisable(true);
            selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
            refreshDays();
            int i = selectedTeachers.getSelectedIndex();
            selectedTeachers.select(i);
        });
        rb_type.setOnAction(event -> {
            if (selectedTypes.getSelectedItems().isEmpty())
                selectedTypes.select(0);
//            displayedLessonType.setDisable(true);
//            displayedLessonTeachers.setDisable(true);
            selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
            refreshDays();
            int i = selectedTypes.getSelectedIndex();
            selectedTypes.clearSelection();
            selectedTypes.select(i);
            displayedTeacherLessons.setItems(FXCollections.observableArrayList());
        });
        rb_3.setOnAction(event -> {
//            displayedLessonType.setDisable(false);
//            displayedLessonTeachers.setDisable(false);
            selectedDays.setSelectionMode(SelectionMode.SINGLE);
            selectedLessons.setSelectionMode(SelectionMode.SINGLE);
            refreshDays();
        });
        //////////////////////////////////////////////////////////////////////
        Text daysLabelText = new Text("Дни недели");
        if (!buttonsDays.isVisible())
            daysLabelText.setTranslateY(30);
        FlowPane daysPane = new FlowPane(Orientation.VERTICAL, 10, 10, daysLabelText, buttonsDays, displayedDays);
        FlowPane lessonsPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Предметы"), buttonsLessons, displayedLessons,clearLessonSelection);
        FlowPane teachersPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Преподаватели"), buttonsTeachers, displayedTeachers, buttonsLessonTeachers);
        FlowPane lessonDetailsPane = new FlowPane(Orientation.VERTICAL, 10, 20, constTypeLabel, displayedLessonType, constTeacherLabel, displayedLessonTeachers);
        FlowPane typesPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Формы занятий"), displayedTypes, applyTypeToLessonBtn, new Text("Подсветить пары:"), displayedToggles);
        FlowPane teacherLessonsPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Пары выбранного преподавателя"), displayedTeacherLessons);
        lessonDetailsPane.setTranslateY(30); //moving down to align with other lists

        daysPane.setMaxHeight(280);
        lessonsPane.setMaxHeight(280);
        lessonDetailsPane.setMaxHeight(280);
        teachersPane.setMaxHeight(320);
        typesPane.setMaxHeight(320);
        teacherLessonsPane.setMaxHeight(320);

        FlowPane rootPane = new FlowPane(Orientation.HORIZONTAL, 10, 10, daysPane, lessonsPane, lessonDetailsPane, teachersPane, typesPane, teacherLessonsPane);
        rootPane.setAlignment(Pos.TOP_CENTER);
        rootPane.setPadding(new Insets(20));
        Scene scene = new Scene(rootPane, 1200, 620);

        //////////////////////////////////////////////////////////////////////
        stage.setScene(scene);
        stage.setTitle("Курсовая работа, вариант 15");
//        stage.setMaximized(true);
//        stage.setResizable(false);
        stage.setOpacity(0);
        stage.show();
        clearLessonSelection.setPrefWidth(lessonsPane.getWidth());
        //launching opacity transition 0 -> 1
        for (double i = 0; i < 100; i++) {
            stage.setOpacity(i / 100);
            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }
        }
    }
}

