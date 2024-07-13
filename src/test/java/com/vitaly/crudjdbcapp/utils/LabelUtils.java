package com.vitaly.crudjdbcapp.utils;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;

import java.util.Arrays;
import java.util.List;

public class LabelUtils {
    public static List<Label> getLabels() {

        return Arrays.asList(new

                        Label(1, "label1", Status.ACTIVE),
                new

                        Label(2, "label2", Status.ACTIVE),
                new

                        Label(3, "label3", Status.ACTIVE));
    }

    public static Label getLabel() {
        return new Label(1, "label1", Status.ACTIVE);
    }
}
