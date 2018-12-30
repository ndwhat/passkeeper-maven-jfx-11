package com.stendyx.passkeeper.passkeeper.objects.workspace;

import com.stendyx.passkeeper.passkeeper.objects.IOObjects;
import com.stendyx.passkeeper.passkeeper.properties.Objects;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.CategoryValidator;
import com.stendyx.passkeeper.passkeeper.validators.ProjectValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class WorkSpaces extends IOObjects<HashMap<HashMap<String, Category>, ArrayList<Project>>> implements Serializable {

    // LOGIN -> category , prj
    private HashMap<HashMap<String, Category>, ArrayList<Project>> WorkSpaces = new HashMap<>();


    public WorkSpaces() {
        super(Objects.WORKSPACES.getFile());
        if (isObjectsExists()) {
            WorkSpaces = getObjects();
        }
    }


    public HashMap<Category, ArrayList<Project>> getWorkSpaces(String login) {
        HashMap<Category, ArrayList<Project>> categoryArrayListHashMap = new HashMap<>();

        WorkSpaces.forEach((key, value) -> key.entrySet()
                .stream()
                .filter(stringCategoryEntry -> stringCategoryEntry.getKey().equals(login))
                .forEach(c -> {
                    HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
                    stringCategoryHashMap.put(c.getKey(), c.getValue());
                    ArrayList<Project> projects = WorkSpaces.get(stringCategoryHashMap);
                    categoryArrayListHashMap.put(c.getValue(), projects);
                }));

        return categoryArrayListHashMap;
    }

    public ArrayList<Project> getProjects(String login, Category category) {
        HashMap<Category, ArrayList<Project>> workSpaces = getWorkSpaces(login);

        return workSpaces.get(category);
    }


    public Validator isUnique(String login, String category) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, new Category(category));

        if (WorkSpaces.containsKey(stringCategoryHashMap))
            return Validator.ERROR_ALREADY_EXISTS;


        return Validator.SUCCESS_ADD;

    }

    public Validator isUnique(String login, Category category, Project project) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);

        boolean isUnique = WorkSpaces.get(stringCategoryHashMap).stream().anyMatch(e -> e.getProjectName().equalsIgnoreCase(project.getProjectName()));

        if (isUnique)
            return Validator.ERROR_ALREADY_EXISTS;


        return Validator.SUCCESS_ADD;

    }

    public Validator isUnique(String login, Category category, String project) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);

        boolean isUnique = WorkSpaces.get(stringCategoryHashMap).stream().anyMatch(e -> e.getProjectName().equalsIgnoreCase(project));

        if (isUnique)
            return Validator.ERROR_ALREADY_EXISTS;


        return Validator.SUCCESS_ADD;

    }


    public Validator addWorkSpace(String login, Category category) {

        CategoryValidator categoryValidator = new CategoryValidator();
        Validator validator = categoryValidator.validate(login, category.getCategory());
        if (validator.isError())
            return validator;

        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);

        WorkSpaces.put(stringCategoryHashMap, new ArrayList<>());

        writeToFile(WorkSpaces);
        return Validator.SUCCESS_ADD;
    }

    public Validator addWorkSpace(String login, Category category, Project project) {

        ProjectValidator projectValidator = new ProjectValidator();
        Validator validator = projectValidator.validate(login, category, project);
        if (validator.isError())
            return validator;


        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);


        ArrayList<Project> projects = WorkSpaces.get(stringCategoryHashMap);

        projects.add(project);
        WorkSpaces.put(stringCategoryHashMap, projects);

        writeToFile(WorkSpaces);
        return Validator.SUCCESS_ADD;
    }


    public Validator updateWorkSpace(String login, Category categoryNew, Category categoryOld) {

        CategoryValidator categoryValidator = new CategoryValidator();
        Validator validator = categoryValidator.validate(login, categoryNew.getCategory());
        if (validator.isError())
            return validator;

        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, categoryNew);

        HashMap<String, Category> stringCategoryHashMapOld = new HashMap<>();
        stringCategoryHashMapOld.put(login, categoryOld);


        WorkSpaces.put(stringCategoryHashMap, WorkSpaces.get(stringCategoryHashMapOld));

        WorkSpaces.remove(stringCategoryHashMapOld);
        writeToFile(WorkSpaces);
        return Validator.SUCCESS_UPDATE;
    }


    public Validator updateWorkSpace(String login, Category category, Project projectOld, String projectNew) {
        ProjectValidator projectValidator = new ProjectValidator();
        Validator validator = projectValidator.validate(login, category, projectNew);
        if (validator.isError())
            return validator;

        if (projectNew.trim().isEmpty())
            return Validator.ERROR_NO_DATA;

        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);


        ArrayList<Project> projects = WorkSpaces.get(stringCategoryHashMap);
        projects.stream().forEach(project -> {
            if (project.getProjectName().equalsIgnoreCase(projectOld.getProjectName())) {
                project.setProjectName(projectNew);
            }
        });
        WorkSpaces.put(stringCategoryHashMap, projects);
        writeToFile(WorkSpaces);
        return Validator.SUCCESS_UPDATE;
    }


    public Validator updateWorkSpace(String login, Category category, Project projectNew) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);

        ArrayList<Project> projects = WorkSpaces.get(stringCategoryHashMap);
        projects.stream().forEach(project -> {
            if (project.getProjectName().equalsIgnoreCase(projectNew.getProjectName())) {
                project.setProject(projectNew);
            }
        });
        WorkSpaces.put(stringCategoryHashMap, projects);
        writeToFile(WorkSpaces);
        return Validator.SUCCESS_UPDATE;
    }

    public Validator deleteWorkSpace(String login, Category category, Project projectToDelete) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);

        ArrayList<Project> projects = WorkSpaces.get(stringCategoryHashMap);
        ArrayList<Project> collect = projects.stream().filter(project -> {
            return !project.getProjectName().equalsIgnoreCase(projectToDelete.getProjectName());
        }).collect(Collectors.toCollection(ArrayList::new));
        WorkSpaces.put(stringCategoryHashMap, collect);
        writeToFile(WorkSpaces);
        return Validator.SUCCESS_DELETE;
    }


    public Validator deleteWorkSpace(String login, Category category) {
        HashMap<String, Category> stringCategoryHashMap = new HashMap<>();
        stringCategoryHashMap.put(login, category);
        WorkSpaces.remove(stringCategoryHashMap);
        writeToFile(WorkSpaces);
        return Validator.SUCCESS_DELETE;
    }

}

