package com.cs2340aG49.greenPlate.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cs2340aG49.greenPlate.R;
import com.cs2340aG49.greenPlate.databinding.ActivityShoppingListBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;
import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.CheckBoxIngredient;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredient;
import com.cs2340aG49.greenPlate.ui.model.Pantry;
import com.cs2340aG49.greenPlate.ui.model.ShoppingList;

import java.util.List;

public class ShoppingListActivity extends NavigationBar {

    private ActivityShoppingListBinding activityShoppingListBinding;
    private Database database;
    private ShoppingList shoppingList;
    private Pantry pantry;
    private String userName;
    private Button addIngredientButton;
    private Button buyItemsButton;
    private ListView ingredientListView;
    private CustomAdapter listViewAdapter;

    private EditText ingredientName;
    private EditText ingredientQuantity;
    private EditText ingredientCalories;

    private CheckBoxIngredient newIngredient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityShoppingListBinding = activityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(activityShoppingListBinding.getRoot());
        allocatedActivityTitle("Shopping List");

        ingredientListView = activityShoppingListBinding.ingredientListView;
        addIngredientButton = activityShoppingListBinding.displayIngredientFormButton;
        buyItemsButton = activityShoppingListBinding.buyItemsButton;

        database = Database.getInstance();
        userName = NavigationBar.getUsername();
        try {
            pantry = database.getPantry(userName);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }

        try {
            shoppingList = database.getShoppingList(userName);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }

        listViewAdapter = new CustomAdapter(getApplicationContext(), shoppingList.getIngredients());
        ingredientListView.setAdapter(listViewAdapter);

        //Makes sure that all ShoppingList Ingredients are Unchecked When Create Screen
        // unCheckAllItems();

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormEntry();
            }
        });

        buyItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyItems();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Makes sure that all ShoppingList Ingredients are Unchecked When User Leaves Screen
        // unCheckAllItems();
    }

    private void showFormEntry() {
        //Create AlertDialog popup
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_shopping_list_form, null);
        dialogBuilder.setView(dialogView);

        //User Inputted Information
        ingredientName = dialogView.findViewById(R.id.ingredientName);
        ingredientQuantity = dialogView.findViewById(R.id.ingredientQuantity);
        ingredientCalories = dialogView
                .findViewById(R.id.ingredientCalories);
        addIngredientButton = dialogView.findViewById(R.id.addIngredientButton);
        addIngredientButton.setEnabled(false);

        //Check whether User correctly filled out the input fields
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                addIngredientButton.setEnabled(false);
                boolean duplicateFound = false;
                //Create Temporary Ingredient just to check if same ingredient name
                // already in shopping list
                AbstractIngredient tempIngredient = new DefaultIngredient(
                        ingredientName.getText().toString(), 0);
                List<AbstractIngredient> ingredients = shoppingList.getIngredients();
                for (AbstractIngredient ingredient: ingredients) {
                    if (ingredient.sameIngredient(tempIngredient)) {
                        duplicateFound = true;
                    }
                }
                //Make Sure that Ingredient Name, Quantity, and Calories Per Serving are Declared
                if (ingredientName.getText().toString().equals("")) {
                    ingredientName.setError("name cannot be empty");
                } else if (ingredientQuantity.getText().toString().equals("")) {
                    ingredientQuantity.setError("quantity cannot be empty");
                } else if (ingredientCalories.getText().toString().equals("")) {
                    ingredientCalories.setError("calories cannot be empty");
                } else if (ingredientQuantity.getText().toString().substring(0, 1).equals("-")) {
                    //Make sure that quantity entered is not negative
                    ingredientQuantity.setError("quantity cannot be negative");
                } else if (Integer.parseInt(ingredientQuantity.getText().toString()) == 0) {
                    //Make sure that quantity entered is not zero
                    ingredientQuantity.setError("quantity cannot be zero");
                } else if (ingredientCalories.getText().toString().substring(0, 1).equals("-")) {
                    //Make sure that quantity entered is not negative
                    ingredientCalories.setError("quantity cannot be negative");
                } else if (Integer.parseInt(ingredientCalories.getText().toString()) == 0) {
                    //Make sure that quantity entered is not zero
                    ingredientCalories.setError("quantity cannot be zero");
                } else if (duplicateFound) {
                    ingredientName.setError("cannot have duplicate ingredients");
                } else {
                    addIngredientButton.setEnabled(true);
                }
            }
        };

        ingredientName.addTextChangedListener(watcher);
        ingredientQuantity.addTextChangedListener(watcher);
        ingredientCalories.addTextChangedListener(watcher);

        AlertDialog alertDialog = dialogBuilder.create();
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Extract the Name, Quantity, and Calories Per Serving

                String ingredientNameValue = ingredientName.getText().toString();

                int ingredientQuantityValue = Integer.parseInt(ingredientQuantity
                        .getText().toString());

                int ingredientCalorieValue = Integer.parseInt(ingredientCalories
                        .getText().toString());

                newIngredient = new CheckBoxIngredient(ingredientNameValue,
                        ingredientQuantityValue, ingredientCalorieValue, false);

                shoppingList.addIngredient(newIngredient);
                listViewAdapter.notifyDataSetChanged();

                database.addNewShoppingList(shoppingList);

                //Once ingredient added -> close popup
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setLayout(800, 1600);

    }
    private void buyItems() {
        ShoppingList currentShoppingList = database.getShoppingList(userName);
        List<AbstractIngredient> ingredients = currentShoppingList.getIngredients();

        // Search through ingredients in Shopping List
        for (AbstractIngredient ingredient: ingredients) {
            CheckBoxIngredient checkedIngredient = (CheckBoxIngredient) ingredient;

            //Check if Ingredient was "Checked" Indicating that It Was Bought
            if (checkedIngredient.getChecked()) {
                //Check if Ingredient Being Bought is Already in Pantry
                boolean duplicateFound = false;
                List<AbstractIngredient> pantryIngredients = pantry.getIngredients();
                for (AbstractIngredient pantryIngredient: pantryIngredients) {
                    if (pantryIngredient.sameIngredient(checkedIngredient)) {
                        //If item already in Pantry just update Ingredient's Quantity
                        duplicateFound = true;
                        pantryIngredient.setIngredientCount(checkedIngredient.getIngredientCount()
                                + pantryIngredient.getIngredientCount());
                        break;
                    }
                }
                //If Ingredient not a duplicate then just add it to the Pantry
                if (!duplicateFound) {
                    pantry.addIngredient(checkedIngredient);
                }

                //Update Pantry database
                database.addPantry(pantry);

                //Remove Ingredient from Shopping List and Update Screen and Database
                database.removeShoppingListItem(checkedIngredient.getIngredientName(),
                        shoppingList.getUser());
                shoppingList.removeIngredient(checkedIngredient);
                listViewAdapter.notifyDataSetChanged();
            }
        }
    }
    public void unCheckAllItems() {
        List<AbstractIngredient> ingredients = shoppingList.getIngredients();
        //Make all items in Shopping List Unchecked
        for (AbstractIngredient ingredient: ingredients) {
            database.updateCheckBoxItem(ingredient.getIngredientName(),
                    shoppingList.getUser(), false);
        }
    }

    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private List<AbstractIngredient> ingredients;
        private LayoutInflater inflater;

        public CustomAdapter(Context applicationContext, List<AbstractIngredient> ingredients) {
            this.context = applicationContext;
            this.ingredients = ingredients;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_shopping_list_item, null);
            TextView ingredientName = (TextView) view.findViewById(R.id.ingredientName);
            TextView ingredientCalories = (TextView) view.findViewById(R.id.ingredientCalories);
            EditText ingredientCount = (EditText) view.findViewById((R.id.ingredientCount));
            CheckBox buyItemCheckbox = (CheckBox) view.findViewById((R.id.buyItemCheckbox));

            AbstractIngredient ingredient = ingredients.get(i);

            ingredientName.setText(ingredient.getIngredientName());
            ingredientCalories.setText("" + ingredient.getIngredientCalories());
            ingredientCount.setText("" + ingredient.getIngredientCount());
            buyItemCheckbox.setChecked(((CheckBoxIngredient) ingredient).getChecked());
            ingredientCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int prevCount = ingredient.getIngredientCount();
                        int count = (int) Double.parseDouble(ingredientCount.getText().toString());

                        if (prevCount == count) {
                            return;
                        }

                        if (count > 0) {
                            ingredient.setIngredientCount(count);
                            database.addNewShoppingList(shoppingList);
                        } else {
                            database.removeShoppingListItem(ingredient.getIngredientName(),
                                    shoppingList.getUser());
                            ingredients.remove(ingredient);
                        }

                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            });

            buyItemCheckbox.setOnCheckedChangeListener(new CompoundButton.
                    OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkedButton, boolean isChecked) {
                    database.updateCheckBoxItem(ingredient.getIngredientName(),
                            shoppingList.getUser(), isChecked);
                    ((CheckBoxIngredient) ingredient).setChecked(isChecked);
                }
            });

            return view;
        }
    }

}