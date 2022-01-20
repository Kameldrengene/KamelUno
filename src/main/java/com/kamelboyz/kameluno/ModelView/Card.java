package com.kamelboyz.kameluno.ModelView;

public class Card {
    public String color;
    public String value;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() { return color; }
    public String getValue() { return value; }

    public boolean equals(Card card) {
        return (this.color.equals(card.color) &&
                this.value.equals(card.value));
    }
}
