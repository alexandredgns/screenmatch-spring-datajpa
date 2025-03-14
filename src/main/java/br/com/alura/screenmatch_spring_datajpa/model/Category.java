package br.com.alura.screenmatch_spring_datajpa.model;

public enum Category {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String omdbCategory;

    Category(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for(Category category : Category.values()) {
            if(category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category was found!");
    }
}
