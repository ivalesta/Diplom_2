package ru.practicum.basis;

import java.util.List;

public class OrderBasis {
    private List<String> ingredients;

    public OrderBasis(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderBasis() {
    }

    public List<String> getIngredientsList() {
        return ingredients;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredients = ingredientsList;
    }
}
