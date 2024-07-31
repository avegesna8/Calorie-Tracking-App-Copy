package com.cs2340aG49.greenPlate.ui.model;

import androidx.annotation.NonNull;

import com.cs2340aG49.greenPlate.ui.view.DatabaseObserver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Database {

    // Singleton
    private static Database database;
    private final DatabaseReference db;
    private DataSnapshot accountData;
    private DataSnapshot mealData;
    private DataSnapshot userData;
    private DataSnapshot cookBookData;
    private DataSnapshot pantryData;
    private DataSnapshot shoppingListData;
    private List<DatabaseObserver> observers;

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }
    private Database() {
        observers = new ArrayList<>();

        db = FirebaseDatabase.getInstance("https://cs2340db-default-rtdb.firebaseio.com/")
                .getReferenceFromUrl("https://cs2340db-default-rtdb.firebaseio.com/");

        db.child("accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });

        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });

        db.child("meals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mealData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });

        db.child("cookbook").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cookBookData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });

        db.child("pantry").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pantryData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });

        db.child("shoppinglist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingListData = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }
        });
    }

    public void updateObservers() {
        for (DatabaseObserver o : observers) {
            o.update();
        }
    }

    public void addObserver(DatabaseObserver o) {
        observers.add(o);
    }

    // METHODS FOR USER DATABASE, @yolomep
    // The database is at db.child("users"). To access the data, use the snapshot userData.
    public void addUser(User user) {
        DatabaseReference userReference = db.child("users").child(user.getUsername());
        userReference.setValue(user.getUsername());
        userReference.child("height").setValue(user.getHeight());
        userReference.child("weight").setValue(user.getWeight());
        userReference.child("gender").setValue(user.getGender());
    }

    public User getUser(String userName) {
        double height = userData.child(userName).child("height").getValue(Double.class);
        double weight = userData.child(userName).child("weight").getValue(Double.class);
        String gender = userData.child(userName).child("gender").getValue(String.class);
        return new User(userName, height, weight, gender);
    }

    // METHODS FOR COOK BOOK

    public void addRecipe(Recipe recipe) {
        DatabaseReference recipes = db.child("cookbook").child("recipes");
        DatabaseReference recipeReference = recipes.child(recipe.getName());
        recipeReference.child("instructions").setValue(recipe.getInstructions());
        recipeReference.child("user").setValue(recipe.getUser());
        DatabaseReference ingredientReference = recipeReference.child("ingredients");
        for (AbstractIngredient ingredient : recipe.getIngredients()) {
            ingredientReference.child(ingredient.getIngredientName())
                    .setValue(ingredient.getIngredientName());
            ingredientReference.child(ingredient.getIngredientName()).child("count")
                    .setValue(ingredient.getIngredientCount());
            ingredientReference.child(ingredient.getIngredientName()).child(
                    "calories").setValue(ingredient.getIngredientCalories());
        }
    }

    public void addCookBook(CookBook cookBook) {
        DatabaseReference userReference = db.child("cookbook");
        DatabaseReference recipes = userReference.child("recipes");
        for (Recipe recipe : cookBook.getAllRecipes()) {
            DatabaseReference recipeReference = recipes.child(recipe.getName());
            recipeReference.setValue(recipe.getName());
            recipeReference.child("instructions").setValue(recipe.getInstructions());
            recipeReference.child("user").setValue(recipe.getUser());
            DatabaseReference ingredientReference = recipeReference.child("ingredients");
            for (AbstractIngredient ingredient : recipe.getIngredients()) {
                ingredientReference.child(ingredient.getIngredientName())
                        .setValue(ingredient.getIngredientName());
                ingredientReference.child(ingredient.getIngredientName()).child("count")
                        .setValue(ingredient.getIngredientCount());
                ingredientReference.child(ingredient.getIngredientName()).child(
                        "calories").setValue(ingredient.getIngredientCalories());
            }
        }
    }

    public CookBook getCookBook() {
        CookBook cookBook = new CookBook();
        DataSnapshot cookBookDataTemp = cookBookData.child("recipes");
        for (DataSnapshot recipes : cookBookDataTemp.getChildren()) {
            ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
            String name = recipes.getKey();
            String user = recipes.child("user").getValue(String.class);
            String instructions = recipes.child("instructions").getValue(String.class);
            DataSnapshot ingredientData = recipes.child("ingredients");
            for (DataSnapshot ingredient : ingredientData.getChildren()) {
                String ingredientName = ingredient.getKey();
                int count = ingredient.child("count").getValue(Integer.class);
                int calories = ingredient.hasChild("calories")
                        ? ingredient.child("calories").getValue(Integer.class) : 0;
                ingredients.add(new ExpirableIngredient(ingredientName, count, calories));
            }
            cookBook.addRecipe(new Recipe(ingredients, name, instructions, user));
        }
        return cookBook;
    }

    public void addPantry(Pantry pantry) {
        DatabaseReference pantryReference = db.child("pantry").child(pantry.getUser());
        for (AbstractIngredient ingredient : pantry.getIngredients()) {
            pantryReference.child(ingredient.getIngredientName()).
                    setValue(ingredient.getIngredientName());
            pantryReference.child(ingredient.getIngredientName()).child(
                    "count").setValue(ingredient.getIngredientCount());
            pantryReference.child(ingredient.getIngredientName()).child(
                    "calories").setValue(ingredient.getIngredientCalories());
            if (ingredient instanceof ExpirableIngredient) {
                ExpirableIngredient eIngredient = (ExpirableIngredient) ingredient;
                if (eIngredient.getIngredientExpirationDate() != null) {
                    pantryReference.child(eIngredient.getIngredientName()).child(
                            "expiration date").setValue(eIngredient
                            .getIngredientExpirationDate().toString());
                }
            }

        }
    }

    public void removeIngredient(String ingredientName, String username) {
        DatabaseReference pantryReference = db.child("pantry").child(username);
        pantryReference.child(ingredientName).removeValue();
    }

    public Pantry getPantry(String user) {
        Pantry ret = new Pantry(user);
        DataSnapshot ingredients = pantryData.child(user);
        for (DataSnapshot ingredient : ingredients.getChildren()) {
            if (ingredient.child("expiration date").exists()) {
                ret.addIngredient(new ExpirableIngredient(ingredient.getKey(),
                        ingredient.child("count").getValue(Integer.class),
                        ingredient.child("calories").getValue(Integer.class),
                        LocalDate.parse(ingredient.child("expiration date")
                                .getValue(String.class))));
            } else {
                ret.addIngredient(new DefaultIngredient(ingredient.getKey(),
                        ingredient.child("count").getValue(Integer.class),
                        ingredient.child("calories").getValue(Integer.class)));
            }
        }
        return ret;
    }

    public void addNewShoppingList(ShoppingList shoppinglist) {
        DatabaseReference pantryReference = db.child("shoppinglist").child(shoppinglist.getUser());
        for (AbstractIngredient ingredient : shoppinglist.getIngredients()) {
            pantryReference.child(ingredient.getIngredientName()).
                    setValue(ingredient.getIngredientName());
            pantryReference.child(ingredient.getIngredientName()).child(
                    "count").setValue(ingredient.getIngredientCount());
            pantryReference.child(ingredient.getIngredientName()).child(
                    "calories").setValue(ingredient.getIngredientCalories());
            if (ingredient instanceof CheckBoxIngredient) {
                CheckBoxIngredient eIngredient = (CheckBoxIngredient) ingredient;
                pantryReference.child(eIngredient.getIngredientName()).child(
                            "checked").setValue(eIngredient
                            .getChecked());

            }

        }
    }

    public ShoppingList getShoppingList(String user) {
        CheckboxIngredientFactory c = new CheckboxIngredientFactory();
        ShoppingList ret = new ShoppingList(user);
        DataSnapshot ingredients = shoppingListData.child(user);
        for (DataSnapshot ingredient : ingredients.getChildren()) {
            if (ingredient.child("checked").exists()) {
                ret.addIngredient(c.makeIngredient(ingredient.getKey(),
                        ingredient.child("count").getValue(Integer.class),
                        ingredient.child("calories").getValue(Integer.class),
                        ingredient.child("checked").getValue(Boolean.class)));
            } else {
                ret.addIngredient(c.makeIngredient(ingredient.getKey(),
                        ingredient.child("count").getValue(Integer.class),
                        ingredient.child("calories").getValue(Integer.class)));
            }
        }
        return ret;
    }

    public void removeShoppingListItem(String ingredientName, String username) {
        DatabaseReference shoppingListReference = db.child("shoppinglist").child(username);
        shoppingListReference.child(ingredientName).removeValue();
    }

    public void removePantryItem(String ingredientName, String username) {
        DatabaseReference shoppingListReference = db.child("pantry").child(username);
        shoppingListReference.child(ingredientName).removeValue();
    }

    public void updateCheckBoxItem(String ingredientName, String username, boolean checked) {
        DatabaseReference shoppingListReference = db.child("shoppinglist").child(username);
        shoppingListReference.child(ingredientName).child("checked").setValue(checked);
    }

    //METHODS FOR PERSONAL INFORMATION
    public void updatePersonalInfo(String userName, Double height, Double weight, String gender) {
        db.child("users").child(userName).child("height").setValue(height);
        db.child("users").child(userName).child("weight").setValue(weight);
        db.child("users").child(userName).child("gender").setValue(gender);
    }

    // METHODS FOR MEAL DATABASE
    public void addMeal(String username, Meal meal) {
        // get mealIDCounter
        int ctr = Integer.parseInt(Objects.requireNonNull(mealData.child(username).
                child("mealIDCounter").getValue(String.class)));
        ctr++;
        db.child("meals").child(username).child("mealIDCounter").setValue(String.valueOf(ctr));
        db.child("meals").child(username).child(String.valueOf(ctr)).setValue(meal);
    }

    public List<Meal> getMeals(String username) {
        List<Meal> meals = new ArrayList<>();
        int ctr = Integer.parseInt(Objects.requireNonNull(
                mealData.child(username).child("mealIDCounter")
                .getValue(String.class)));
        for (int i = 1; i <= ctr; i++) {
            Meal m = mealData.child(username).child(String.valueOf(i)).getValue(Meal.class);
            meals.add(m);
        }
        return meals;
    }

    public List<Meal> getTodayMeals(String username) {
        List<Meal> meals = getMeals(username);
        List<Meal> filtered = new ArrayList<>();
        Date today = new Date();
        for (Meal meal : meals) {
            if (meal.getDate().getDate() == (today.getDate())) {
                filtered.add(meal);
            }
        }
        return filtered;
    }

    public double getTodayMealsCal(String username) {
        List<Meal> todayMeals = getTodayMeals(username);
        double todayCal = 0;

        for (Meal meal : todayMeals) {
            todayCal += meal.getCalorieCount();
        }

        return todayCal;
    }



    // METHODS FOR ACCOUNTS DATABASE
    public boolean accountExists(String username) {
        return accountData.hasChild(username);
    }

    public void addAccount(Account a) {
        db.child("accounts").child(a.getUsername()).setValue(a);
        db.child("meals").child(a.getUsername())
                .child("mealIDCounter").setValue("0");
        this.addUser(new User(a.getUsername(), 0, 0, "None"));
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        String storedPassword = accountData.child(username)
                .child("password").getValue(String.class);
        if (storedPassword == null) {
            return false;
        }
        return storedPassword.equals(password);
    }

    public void deleteAccount(String username) {
        db.child("accounts").child(username).removeValue();
        db.child("users").child(username).removeValue();
        db.child("meals").child(username).removeValue();
    }

    public void deleteRecipe(String recipeName) {
        db.child("cookbook").child("recipes").child(recipeName).removeValue();
    }


}
