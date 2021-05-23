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
    MultipleSelectionModel<Day> selectedDays;
    MultipleSelectionModel<Lesson> selectedLessons;
    MultipleSelectionModel<String> selectedTeachers;
    MultipleSelectionModel<String> selectedTypes;

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
    public void start(Stage stage) throws Exception {

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
        displayedDays.setPrefSize(240, 200);
        displayedLessons.setPrefSize(240, 200);
        displayedTeachers.setPrefSize(240, 200);
        displayedLessonTeachers.setPrefSize(240, 110);
        displayedLessonType.setPrefWidth(240);
        displayedTypes.setPrefSize(240, 95);

        displayedLessonType.setEditable(false);
        Text constTeacherLabel = new Text("Преподаватели:");
        Text constTypeLabel = new Text("Форма занятия:");
        //////////////////////////////////////////////////////////////////////

        // BUTTONS
        Button addDayBtn = new Button("Добавить");
        Button addLessonBtn = new Button("Добавить");
        Button addTeacherBtn = new Button("Добавить");
        Button addTeacherToLessonBtn = new Button("Добавить к предмету");
        Button addTypeToLessonBtn = new Button("Добавить к предмету");
        Button deleteTypeFromLessonBtn = new Button("Убрать из предмета");
        Button deleteTeacherFromLessonBtn = new Button("Убрать из предмета");
        Button deleteDayBtn = new Button("Удалить");
        Button deleteLessonBtn = new Button("Удалить");
        Button deleteTeacherBtn = new Button("Удалить");

        //====== RadioButton group for lessons types
        ToggleGroup tgroup = new ToggleGroup();
        RadioButton rb_techer = new RadioButton("Выбранного преподавателя");
        rb_techer.setToggleGroup(tgroup);
        RadioButton rb_type = new RadioButton("Выбранной формы занятия");
        rb_type.setToggleGroup(tgroup);
        RadioButton rb_3 = new RadioButton("Не подствечивать");
        rb_3.setToggleGroup(tgroup);
        rb_3.setSelected(true);

        ListView<Toggle> displayedToggles = new ListView<>(tgroup.getToggles());
        displayedToggles.setPrefSize(240, 95);

        // BUTTONS AND FIELDS CONTAINERS
        FlowPane buttonsDays = new FlowPane(10, 10, daysOrder, daysField, addDayBtn, deleteDayBtn);
        FlowPane buttonsLessons = new FlowPane(10, 10, lessonsOrder, lessonsField, addLessonBtn, deleteLessonBtn);
        FlowPane buttonsTeachers = new FlowPane(10, 10, teachersField, addTeacherBtn, deleteTeacherBtn);
        FlowPane buttonsLessonTeachers = new FlowPane(10, 10, addTeacherToLessonBtn, deleteTeacherFromLessonBtn);
        FlowPane buttonsLessonType = new FlowPane(Orientation.VERTICAL,10, 10, addTypeToLessonBtn, deleteTypeFromLessonBtn);
        // FOR FINDING WHAT IS SELECTED
        selectedDays = displayedDays.getSelectionModel();
        selectedDays.setSelectionMode(SelectionMode.MULTIPLE);
        selectedLessons = displayedLessons.getSelectionModel();
        selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
        selectedTeachers = displayedTeachers.getSelectionModel();
        selectedTypes = displayedTypes.getSelectionModel();
//        langsSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
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
                        if (rb_techer.isSelected()) {
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
                            for (int i = 0; i < lessons.size(); i++) {
                                if (lessons.get(i).getType()
                                        .compareToIgnoreCase(selectedTypes.getSelectedItem().trim()) == 0) {
                                    selectedLessons.selectIndices(i);
                                    break;
                                }
                            }
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
                    displayedLessonType.setText(lesson.getType());
                } else {
                    displayedLessonType.setText("Выбрано более одного параметра");
                }
                displayedLessonTeachers.setItems(FXCollections.observableArrayList(lesson.getTeachers()));

            }
        });
        selectedTeachers.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (rb_techer.isSelected()) {
                int i = selectedDays.getSelectedIndex();
                selectedDays.clearSelection();
                selectedDays.select(i);
            }

        });
        selectedTypes.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (rb_type.isSelected())
                selectedDays.clearAndSelect(selectedDays.getSelectedIndex());
        });
        daysOrder.setOnKeyTyped(event -> {
            showError(daysOrder, displayedDays);
        });
        daysField.setOnKeyTyped(event -> {
            daysField.setStyle("-fx-text-inner-color: black;");
            displayedDays.setStyle("-fx-background-color: null;");
        });
        lessonsOrder.setOnKeyTyped(event -> {
            showError(lessonsOrder, displayedLessons);
        });
        lessonsField.setOnKeyTyped(event -> {
            lessonsField.setStyle("-fx-text-inner-color: black;");
            displayedLessons.setStyle("-fx-background-color: null;");
        });
        addDayBtn.setOnAction(event -> {
            for (Day d : days)
                if (daysField.getText().trim().compareToIgnoreCase(d.getName()) == 0) {
                    daysField.setStyle("-fx-text-inner-color: red;");
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
            displayedDays.setItems(FXCollections.observableArrayList(days));
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
        addLessonBtn.setOnAction(event -> {
            if (lessonsOrder.getText().replaceAll("\\D", "").isEmpty()) {
                lessonsOrder.setStyle("-fx-text-inner-color: red;");
                return;
            } else lessonsOrder.setStyle("-fx-text-inner-color: black;");

            Day curDay = null;
            for (Day d : days)
                if (d.compareTo(displayedDays.getItems().get(selectedDays.getSelectedIndex())) == 0) {
                    curDay = d;
                    break;
                }
            if (curDay == null)
                return;
            List<Lesson> lessons = curDay.getLessons();
            for (Lesson l : lessons)
                if (lessonsField.getText().trim().compareToIgnoreCase(l.getName()) == 0) {
                    lessonsField.setStyle("-fx-text-inner-color: red;");
                    selectedLessons.select(l.getOrder() - 1);
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
            Collections.sort(lessons);
            curDay.setLessons(lessons);
            System.out.println(lessons);
            displayedLessons.setItems(FXCollections.observableArrayList(lessons));
            displayedLessons.refresh();
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

            List<Lesson> lessons = curDay.getLessons();
            for (int i = 0; i < lessons.size(); i++) {

                if (lessons.get(i).getName().trim().compareToIgnoreCase(lessonsField.getText().trim()) == 0) {
                    order = lessons.get(i).getOrder();
                    lessons.remove(lessons.get(i));
                }
            }
            if (order != -5)
                for (Day d : days)
                    if (d.getOrder() >= order)
                        d.setOrder(d.getOrder() - 1);
            curDay.setLessons(lessons);
            displayedLessons.setItems(FXCollections.observableArrayList(lessons));

        });
        rb_techer.setOnAction(event -> {
            if (selectedTeachers.getSelectedItems().isEmpty())
                selectedTeachers.select(0);
            displayedLessonType.setDisable(true);
            displayedLessonTeachers.setDisable(true);
//            selectedDays.setSelectionMode(SelectionMode.MULTIPLE);
            selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
            selectedDays.clearAndSelect(selectedDays.getSelectedIndex());
            int i = selectedTeachers.getSelectedIndex();
//            selectedTeachers.clearSelection();
            selectedTeachers.select(i);
        });
        rb_type.setOnAction(event -> {
            if (selectedTypes.getSelectedItems().isEmpty())
                selectedTypes.select(0);
            displayedLessonType.setDisable(true);
            displayedLessonTeachers.setDisable(true);
//            selectedDays.setSelectionMode(SelectionMode.MULTIPLE);
            selectedLessons.setSelectionMode(SelectionMode.MULTIPLE);
            selectedDays.clearAndSelect(selectedDays.getSelectedIndex());
            int i = selectedTypes.getSelectedIndex();
            selectedTypes.clearSelection();
            selectedTypes.select(i);
        });
        rb_3.setOnAction(event -> {
            displayedLessonType.setDisable(false);
            displayedLessonTeachers.setDisable(false);
            selectedDays.setSelectionMode(SelectionMode.SINGLE);
            selectedLessons.setSelectionMode(SelectionMode.SINGLE);
            selectedDays.clearAndSelect(selectedDays.getSelectedIndex());
        });
        //////////////////////////////////////////////////////////////////////
        FlowPane daysPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Дни недели"), buttonsDays, displayedDays);
        FlowPane lessonsPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Предметы"), buttonsLessons, displayedLessons);
        FlowPane teachersPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Преподаватели"), buttonsTeachers, displayedTeachers);
        FlowPane lessonDetailsPane = new FlowPane(Orientation.VERTICAL, 10, 20, constTypeLabel, displayedLessonType, constTeacherLabel, displayedLessonTeachers);
        FlowPane typesPane = new FlowPane(Orientation.VERTICAL, 10, 10, new Text("Формы занятий"), displayedTypes, new Text("Подсветить пары:"), displayedToggles);

        lessonDetailsPane.setTranslateY(30);
        //        lessonDetailsPane.setPadding(new Insets(30,0,-30,0));

        FlowPane rootPane = new FlowPane(Orientation.HORIZONTAL, 10, 10, daysPane, lessonsPane, lessonDetailsPane, teachersPane, typesPane);
        rootPane.setAlignment(Pos.BASELINE_CENTER);
        rootPane.setPadding(new Insets(20));
        Scene scene = new Scene(rootPane, 1280, 720);


        stage.setScene(scene);
        stage.setTitle("Курсовая работа, вариант 15");
//        stage.setMaximized(true);
        stage.setOpacity(0);
        stage.show();

//        selectedDays.setSelectionMode(SelectionMode.MULTIPLE);
//        selectedDays.selectAll();

        for (double i = 0; i < 100; i++) {
            stage.setOpacity(i / 100);
            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }
        }

    }
}

