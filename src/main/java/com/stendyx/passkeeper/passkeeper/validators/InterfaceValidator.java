package com.stendyx.passkeeper.passkeeper.validators;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public interface InterfaceValidator {




    default Paint getPaint(boolean isError) {
        if (isError)
            return Paint.valueOf("FIREBRICK");
        else
            return Paint.valueOf("green");
    }

    default boolean isEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().length() == 0)
                return true;
        }
        return false;
    }


    default boolean validateEmptyFields(Button submit, TextField... fields) {
        if (isEmpty(fields)) {
            submit.setDisable(true);
            return false;
        }
        else {
            submit.setDisable(false);
            return true;
        }
    }


}
