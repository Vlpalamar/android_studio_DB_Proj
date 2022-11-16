package com.example.myapplication.model;

import android.widget.ImageButton;
import android.widget.TextView;

public class CoffeeTypeButtons {
    ImageButton coffeeButton;
    TextView coffeeName;

    public ImageButton getCoffeeButton() {
        return coffeeButton;
    }

    public void setCoffeeButton(ImageButton coffeeButton) {
        this.coffeeButton = coffeeButton;
    }

    public TextView getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(TextView coffeeName) {
        this.coffeeName = coffeeName;
    }

    public CoffeeTypeButtons(ImageButton coffeeButton, TextView coffeeName) {
        this.coffeeButton = coffeeButton;
        this.coffeeName = coffeeName;
    }
}
