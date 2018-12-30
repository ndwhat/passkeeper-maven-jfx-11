package com.stendyx.passkeeper.passkeeper.validators;

import com.stendyx.passkeeper.passkeeper.objects.workspace.WorkSpaces;
import com.stendyx.passkeeper.passkeeper.properties.Validator;

public class CategoryValidator implements InterfaceValidator {

    public Validator validate(String login, String category) {
        Validator status = checkUnique(login, category);

        if (status.isError())
            return status;

        return Validator.SUCCESS_ADD;
    }

    private Validator checkUnique(String login, String category) {
        WorkSpaces workSpaces = new WorkSpaces();
        Validator validator = workSpaces.isUnique(login, category);
        if (validator.isError()) {
            return validator;
        }
        return Validator.SUCCESS_ADD;
    }

}
