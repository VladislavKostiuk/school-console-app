package com.foxminded.helper;

import com.foxminded.CourseName;
import com.foxminded.constants.ErrorMessages;

import java.util.*;

public class NamesGenerator {
    private final List<String> groupNamesCache;
    private final List<String[]> studentFullNames;
    private final List<CourseName> courseNames;
    private final Random random;

    public NamesGenerator() {
        random = new Random();
        groupNamesCache = new ArrayList<>();
        studentFullNames = new ArrayList<>();
        initStudentFullNames();
        courseNames = new ArrayList<>(Arrays.asList(CourseName.values()));
        Collections.shuffle(courseNames);
        Collections.shuffle(studentFullNames);
    }
    public String generateGroupName() {
        String groupName;

        do {
            groupName = String.valueOf((char) (random.nextInt(26) + 'a')) +
                    (char) (random.nextInt(26) + 'a') + "-" +
                    random.nextInt(10) +
                    random.nextInt(10);
        } while (groupNamesCache.contains(groupName));

        groupNamesCache.add(groupName);
        return groupName;
    }

    public String[] generateStudentFullName() {
        if (studentFullNames.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.NO_MORE_AVAILABLE_NAMES_FOR_STUDENTS);
        }
        String[] studentFullName = studentFullNames.get(0);
        studentFullNames.remove(studentFullName);
        return studentFullName;
    }

    public CourseName generateCourseName() {
        if (courseNames.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.NO_MORE_AVAILABLE_NAMES_FOR_COURSES);
        }
        CourseName courseName = courseNames.get(0);
        courseNames.remove(courseName);
        return courseName;
    }

    private void initStudentFullNames() {
        String[] firstNames = new String[] {"Liam", "Noah", "Oliver", "James", "Elijah",
                "William", "Henry", "Lucas", "Benjamin", "Theodore",
                "Olivia", "Emma", "Charlotte", "Amelia", "Sophia",
                "Isabella", "Ava", "Mira", "Evelyn", "Luna"};
        String[] lastNames = new String[] {"Jackson", "Mason", "Logan", "Wyatt", "Hudson",
                "Grayson", "Carter", "Lincoln", "Nolan", "Cameron",
                "Addison", "Ainsley", "Arley", "Avery", "Parker",
                "Rawley", "Collins", "Eston", "Hadley", "Kensley"};

        for (int i = 0; i < firstNames.length; i++) {
            for (int j = 0; j < lastNames.length; j++) {
                studentFullNames.add(new String[] {firstNames[i], lastNames[j]});
            }
        }
    }
}
