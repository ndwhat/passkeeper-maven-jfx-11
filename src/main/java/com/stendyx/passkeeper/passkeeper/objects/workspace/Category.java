package com.stendyx.passkeeper.passkeeper.objects.workspace;

import java.io.Serializable;
import java.util.Objects;

public class Category   implements Serializable {

    private String category;
  //  private HashMap<HashMap<String, String>, Project> Projects = new HashMap<>();

    public String getCategory() {
        return category;
    }


    public Category(String category) {
    //    super(passkeeper.properties.Objects.WORKSPACES.getFile());
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return Objects.equals(category, category1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
}
