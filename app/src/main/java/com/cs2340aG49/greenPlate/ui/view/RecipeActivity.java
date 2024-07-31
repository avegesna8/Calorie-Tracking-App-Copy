package com.cs2340aG49.greenPlate.ui.view;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cs2340aG49.greenPlate.R;
import com.cs2340aG49.greenPlate.databinding.ActivityRecipeBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;
import com.cs2340aG49.greenPlate.ui.model.CheckboxIngredientFactory;
import com.cs2340aG49.greenPlate.ui.model.CookBook;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredient;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredient;
import com.cs2340aG49.greenPlate.ui.model.Meal;
import com.cs2340aG49.greenPlate.ui.model.Pantry;
import com.cs2340aG49.greenPlate.ui.model.Recipe;
import com.cs2340aG49.greenPlate.ui.model.ShoppingList;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilter;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilterAvailable;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilterDefault;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilterUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends NavigationBar {

    private ActivityRecipeBinding activityRecipeBinding;
    private EditText recipeName;
    private EditText ingredientName;
    private EditText ingredientCount;
    private EditText ingredientCalories;
    private TextView currentRecipeDisplay;
    private Button addRecipe;
    private Button addIngredient;
    private Button filterAvail;
    private Button filterUser;
    private Button filterDefault;
    private List<AbstractIngredient> currIngredients;

    private LinearLayout recipeLayout;

    private Database database;

    private Pantry pantry;

    private String userName;
    private Recipe mostRecentRecipe;

    private RecipeFilter recipeFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRecipeBinding = activityRecipeBinding.inflate(getLayoutInflater());
        setContentView(activityRecipeBinding.getRoot());
        allocatedActivityTitle("Recipe");
        database = Database.getInstance();
        userName = NavigationBar.getUsername();
        try {
            pantry = database.getPantry(userName);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }
        addRecipe = activityRecipeBinding.addRecipe;
        addIngredient = activityRecipeBinding.addIngredient;
        filterAvail = activityRecipeBinding.filter1;
        filterUser = activityRecipeBinding.filter2;
        filterDefault = activityRecipeBinding.filter3;
        recipeName = activityRecipeBinding.recipeNameInput;
        ingredientName = activityRecipeBinding.ingredientNameInput;
        ingredientCount = activityRecipeBinding.ingredientCountInput;
        ingredientCalories = activityRecipeBinding.ingredientCalorieInput;
        currentRecipeDisplay = activityRecipeBinding.currentRecipeInputDisplay;
        recipeLayout = findViewById(R.id.recipeListLayout);
        addRecipe.setEnabled(false);
        addIngredient.setEnabled(false);
        mostRecentRecipe = null;
        recipeFilter = new RecipeFilterDefault();
        initRecipes();
        String prevRecipe = currentRecipeDisplay.getText().toString();
        String[] splitIngredients;
        currIngredients = new ArrayList<>();
        if (!prevRecipe.trim().isEmpty()) {
            splitIngredients = prevRecipe.split("\n");
            for (String currIngredient : splitIngredients) {
                currIngredients.add(new DefaultIngredient(currIngredient.split(",")[0].trim(),
                        Integer.parseInt(currIngredient.split(",")[1].trim()),
                        Integer.parseInt(currIngredient.split(",")[2].trim())));
            }
        }
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Validate username
                String recipe = recipeName.getText().toString();
                String ingredient = ingredientName.getText().toString();
                String ingredientNum = ingredientCount.getText().toString();
                String ingredientCals = ingredientCalories.getText().toString();
                if (recipe.trim().isEmpty()) {
                    recipeName.setError("Recipe name cannot be empty");
                } else if (currIngredients.size() == 0) {
                    recipeName.setError("Ingredient list cannot be empty");
                } else {
                    if (currIngredients.size() > 0) {
                        addRecipe.setEnabled(true);
                    }
                }
                if (ingredient.trim().isEmpty()) {
                    ingredientName.setError("Ingredient name cannot be empty");
                }
                if (ingredientNum.trim().isEmpty()) {
                    ingredientCount.setError("Ingredient count cannot be empty");
                } else if (!ingredientNum.matches("\\d+")) {
                    ingredientCount.setError("Invalid ingredient count.");
                } else if (ingredientCals.trim().isEmpty()) {
                    ingredientCalories.setError("Ingredient calories cannot be empty");
                } else if (!ingredientCals.matches("\\d+")) {
                    ingredientCalories.setError("Invalid ingredient calories.");
                } else {
                    if (!ingredient.trim().isEmpty()) {
                        addIngredient.setEnabled(true);
                    }
                }
            }
        };
        recipeName.addTextChangedListener(afterTextChangedListener);
        ingredientName.addTextChangedListener(afterTextChangedListener);
        ingredientCount.addTextChangedListener(afterTextChangedListener);
        ingredientCalories.addTextChangedListener(afterTextChangedListener);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipe = recipeName.getText().toString().trim();
                mostRecentRecipe = new Recipe(currIngredients, recipe,
                        "None Provided", getUsername());
                database.addRecipe(mostRecentRecipe);
                addRecipe.setEnabled(false);
                ingredientCount.setText("");
                ingredientName.setText("");
                ingredientCalories.setText("");
                recipeName.setText("");
                currentRecipeDisplay.setText("");
                currIngredients = new ArrayList<>();
                initRecipes();
            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbstractIngredient newIngredient = new DefaultIngredient(
                        ingredientName.getText().toString(),
                        Integer.parseInt(ingredientCount.getText().toString()),
                        Integer.parseInt(ingredientCalories.getText().toString()));
                currIngredients.add(newIngredient);
                ingredientName.setText("");
                ingredientCount.setText("");
                ingredientCalories.setText("");
                addIngredient.setEnabled(false);
                if (!recipeName.getText().toString().trim().isEmpty()) {
                    addRecipe.setEnabled(true);
                }
                StringBuilder currRecipe = new StringBuilder();
                for (AbstractIngredient ingredient : currIngredients) {
                    currRecipe.append(ingredient.displayIngredient());
                }
                currentRecipeDisplay.setText(currRecipe);
            }
        });

        filterAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeFilter = new RecipeFilterAvailable();
                initRecipes();
            }
        });

        filterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeFilter = new RecipeFilterUser();
                initRecipes();
            }
        });

        filterDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeFilter = new RecipeFilterDefault();
                initRecipes();
            }
        });
    }

    private void addRecipeItem(LinearLayout recipeListLayout, Recipe recipe) {
        // Create a horizontal LinearLayout to hold the recipe item
        LinearLayout recipeItemLayout = new LinearLayout(this);
        recipeItemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        recipeItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        recipeItemLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Indicator
        GradientDrawable indicatorDrawable = new GradientDrawable();
        indicatorDrawable.setShape(GradientDrawable.OVAL);
        indicatorDrawable.setSize(100, 100); // Set the size of the indicator (adjust as needed)
        indicatorDrawable.setCornerRadius(50); // Set the corner radius to make it circular
        indicatorDrawable.setStroke(2, Color.BLACK); // Set border stroke

        boolean ingreCheck = true;
        List<AbstractIngredient> recipeIngre = recipe.getIngredients();

        ingreCheck = recipeIndicator(recipeIngre, pantry);

        int indicatorColor = ingreCheck ? Color.GREEN : Color.RED;
        indicatorDrawable.setColor(indicatorColor); // Set color to green or red

        View indicatorView = new View(this);
        // Set the size of the indicator (adjust as needed)
        indicatorView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        indicatorView.setBackground(indicatorDrawable);
        recipeItemLayout.addView(indicatorView);

        TextView textView = new TextView(this);
        textView.setText(recipe.getName());
        textView.setTextSize(18); // Set text size as desired
        textView.setPadding(16, 16, 16, 16); // Set padding for text view

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);

        recipeItemLayout.addView(textView);

        // Add the "Add to Cart" button to the recipe item layout
        if (!ingreCheck) {
            ImageButton addToCartButton2 = new ImageButton(this);

            addToCartButton2.setImageResource(R.drawable.shopping_icon);
            LinearLayout.LayoutParams buttonParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            buttonParams2.gravity = Gravity.END;
            addToCartButton2.setLayoutParams(buttonParams2);

            addToCartButton2.setOnClickListener(v -> {
                buyIngredientsForRecipe(recipe);
            });
            recipeItemLayout.addView(addToCartButton2);
        } else {
            ImageButton cookButton = new ImageButton(this);

            cookButton.setImageResource(R.drawable.cooking_24px);
            LinearLayout.LayoutParams buttonParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            buttonParams2.gravity = Gravity.END;
            cookButton.setLayoutParams(buttonParams2);

            cookButton.setOnClickListener(v -> {

                pantry = cookIngredientsForRecipe(recipe);
                boolean ingreCheck2 = recipeIndicator(recipeIngre, pantry);
                int indicatorColor2 = ingreCheck2 ? Color.GREEN : Color.RED;
                indicatorDrawable.setColor(indicatorColor2);

                if (!ingreCheck2) {
                    cookButton.setImageResource(R.drawable.shopping_icon);
                    cookButton.setOnClickListener(r -> {
                        buyIngredientsForRecipe(recipe);
                    });
                }
            });
            recipeItemLayout.addView(cookButton);

        }

        recipeItemLayout.setOnClickListener(v -> {
            showRecipeDetailsPopup(recipe);
        });

        recipeListLayout.addView(recipeItemLayout);
    }

    public static void buyIngredientForRecipeForUser(Recipe r, String username) {
        // solely for testing
        Database database = Database.getInstance();
        List<AbstractIngredient> recipeIngredients = r.getIngredients();
        Pantry pantry = database.getPantry(username);
        ShoppingList shoppingList = database.getShoppingList(username);

        CheckboxIngredientFactory cb = new CheckboxIngredientFactory();

        for (AbstractIngredient rIngredient : recipeIngredients) {
            int sQuantity = shoppingList.containsIngredient(rIngredient.getIngredientName())
                    ? shoppingList.getIngredient(rIngredient.getIngredientName())
                    .getIngredientCount() : 0;
            int pQuantity = pantry.containsIngredient(rIngredient.getIngredientName())
                    ? pantry.getIngredient(rIngredient.getIngredientName()).getIngredientCount()
                    : 0;
            if (rIngredient.getIngredientCount() > pQuantity) {
                shoppingList.addIngredient(cb.makeIngredient(rIngredient
                                .getIngredientName(), rIngredient.getIngredientCount()
                                - pQuantity + sQuantity, rIngredient.getIngredientCalories(),
                        false));
            }
        }

        database.addNewShoppingList(shoppingList);
    }
    private void buyIngredientsForRecipe(Recipe r) {
        Toast.makeText(getApplicationContext(),
                "Added to Shopping List.", Toast.LENGTH_SHORT).show();
        List<AbstractIngredient> recipeIngredients = r.getIngredients();
        Pantry pantry = database.getPantry(userName);
        ShoppingList shoppingList = database.getShoppingList(userName);

        CheckboxIngredientFactory cb = new CheckboxIngredientFactory();

        for (AbstractIngredient rIngredient : recipeIngredients) {
            int sQuantity = shoppingList.containsIngredient(rIngredient.getIngredientName())
                    ? shoppingList.getIngredient(rIngredient.getIngredientName())
                    .getIngredientCount() : 0;
            int pQuantity = pantry.containsIngredient(rIngredient.getIngredientName())
                    ? pantry.getIngredient(rIngredient.getIngredientName()).getIngredientCount()
                    : 0;
            if (rIngredient.getIngredientCount() > pQuantity) {
                shoppingList.addIngredient(cb.makeIngredient(rIngredient
                        .getIngredientName(), rIngredient.getIngredientCount()
                        - pQuantity + sQuantity, rIngredient.getIngredientCalories(),
                        false));
            }
        }

        database.addNewShoppingList(shoppingList);
    }

    private Pantry cookIngredientsForRecipe(Recipe r) {
        boolean flag = false;
        Toast.makeText(getApplicationContext(),
                "Cooked the Recipe.", Toast.LENGTH_SHORT).show();
        List<AbstractIngredient> recipeIngredients = r.getIngredients();
        Pantry pantry = database.getPantry(userName);
        Meal meal = new Meal(r.getName(),
                r.getIngredients().stream().
                        mapToInt(AbstractIngredient::getTotalCalories).sum());
        for (AbstractIngredient rIngredient : recipeIngredients) {
            AbstractIngredient pantryIngredient =
                    pantry.getIngredient(rIngredient.getIngredientName());
            if (pantryIngredient.getIngredientCount() == rIngredient.getIngredientCount()) {
                pantry.removeIngredient(pantryIngredient);
                database.removePantryItem(pantryIngredient.getIngredientName(), userName);
            } else {
                pantryIngredient.setIngredientCount(
                        pantryIngredient.getIngredientCount() - rIngredient.getIngredientCount());
            }
        }

        database.addPantry(pantry);
        database.addMeal(userName, meal);
        return pantry;
    }

    private void initRecipes() {
        CookBook cb = database.getCookBook();

        List<Recipe> recipes = cb.getAllRecipes();

        // Cache most recently entered recipe
        if (mostRecentRecipe != null) {
            recipes.add(mostRecentRecipe);
        }

        recipeLayout.removeAllViews();

        recipes = recipeFilter.filterRecipes(recipes, NavigationBar.getUsername());

        for (Recipe recipe : recipes) {
            addRecipeItem(recipeLayout, recipe);
        }
    }

    private void showRecipeDetailsPopup(Recipe recipe) {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView popupTextView = new TextView(this);
        popupTextView.setText(recipe.toString());
        popupTextView.setPadding(40, 16, 40, 16);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setColor(Color.rgb(0x75, 0xc9, 0x4d)); // Set background color to green
        backgroundDrawable.setCornerRadius(20);

        popupTextView.setBackground(backgroundDrawable);
        popupTextView.setTextColor(Color.BLACK);

        popupWindow.setContentView(popupTextView);
        View rootView = getWindow().getDecorView().getRootView();

        int[] location = new int[2];
        rootView.getLocationOnScreen(location);
        int centerX = location[0] + rootView.getWidth() / 2;
        int centerY = (int) (location[1] + rootView.getHeight() * .18);

        popupWindow.setWidth(1000);
        popupWindow.setHeight(600);

        popupWindow.setOutsideTouchable(true); // dismiss if clicked off
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, centerY);
    }

    private boolean recipeIndicator(List<AbstractIngredient> recipeIngre,
                                    Pantry pantry) {
        for (AbstractIngredient ingredient : recipeIngre) {
            if (pantry.containsIngredient(ingredient.getIngredientName())) {
                if (pantry.getIngredient(ingredient.getIngredientName())
                        instanceof ExpirableIngredient) {
                    if (((ExpirableIngredient) ingredient).
                            getIngredientExpirationDate().isBefore(LocalDate.now())) {
                        return false;
                    }
                }
                if (pantry.getIngredientCount(ingredient.getIngredientName())
                        < ingredient.getIngredientCount()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }



}