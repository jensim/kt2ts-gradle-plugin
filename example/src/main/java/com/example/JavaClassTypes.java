package com.example;

import java.util.Objects;

@MyJavaAnnotation
@ToTypescript
public class JavaClassTypes {
    private final String name;

    public JavaClassTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaClassTypes that = (JavaClassTypes) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "JavaClassTypes{" +
                "name='" + name + '\'' +
                '}';
    }
}
