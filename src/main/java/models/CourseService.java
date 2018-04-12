package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private static java.util.List<Course> List = new ArrayList<>();;

    static {
        try {
            List.add(
                    new Course(
                            1,
                            "TPSI",
                            "Kowalski"
                    )
            );

            List.add(
                    new Course(
                            2,
                            "MiASI",
                            "Nowak"
                    )
            );

            List.add(
                    new Course(
                            3,
                            "TPAL",
                            "Kowalczyk"
                    )
            );
        } catch (Exception ignored) {
        }
    }
}
