package com.epam.esm.entity;

import java.util.Objects;

public class Tag {

    private Long id;
    private String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag() {
    }

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Tag{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }

    public static class TagBuilder {

        private Long id;
        private String name;

        TagBuilder() {
        }

        public TagBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TagBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Tag build() {
            return new Tag(id, name);
        }
    }
}
