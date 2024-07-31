package com.cs2340aG49.greenPlate;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.CookBook;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredient;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredient;
import com.cs2340aG49.greenPlate.ui.model.Pantry;
import com.cs2340aG49.greenPlate.ui.model.Recipe;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilterAvailable;
import com.cs2340aG49.greenPlate.ui.model.recipeSorting.RecipeFilterUser;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class Sprint3UnitTests {

    @Test
    public void addGetCookBook() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(2000);
        CookBook cookBook = new CookBook(); //"What I just cooked"
        ArrayList<AbstractIngredient> wIngredients = new ArrayList<>();
        wIngredients.add(new ExpirableIngredient("乌冬面", 2));
        // bruh why is this a number instead of measurement? whatever
        wIngredients.add(new ExpirableIngredient("酱油", 3));
        wIngredients.add(new ExpirableIngredient("味素", 2));
        wIngredients.add(new ExpirableIngredient("peas", 10));
        Recipe wudong = new Recipe(wIngredients, "烧乌冬",
                "Cook the udon with water and some oil then dump " +
                        "everything when the water is boiling. Takes around 5 minutes.",
                "Yolomep");
        ArrayList<AbstractIngredient> sIngredients = new ArrayList<>();
        sIngredients.add(new ExpirableIngredient("Skirt Steak", 1));
        sIngredients.add(new ExpirableIngredient("Salt", 10));
        sIngredients.add(new ExpirableIngredient("Pepper", 10));
        Recipe steak = new Recipe(sIngredients, "Steak", "Let steak rest, dry, beat, and " +
                "room temp, while coating with salt and pepper. oil, heat until sizzling" +
                "then put meat. Cook until feel like it, take out and rest", "Yolomep");

        cookBook.addRecipe(wudong);
        cookBook.addRecipe(steak);
        database.addCookBook(cookBook);
        Thread.sleep(500);
        CookBook get = database.getCookBook();
        assertTrue(get.getAllRecipes().contains(wudong));
        assertTrue(get.getAllRecipes().contains(steak));
    }

    @Test
    public void addGetPantry() throws InterruptedException {
        Database database = Database.getInstance();
        Pantry pantry = new Pantry("yolomep");
        pantry.addIngredient(new ExpirableIngredient("白菜", 2));
        database.addPantry(pantry);
        Thread.sleep(500);
        Pantry get = database.getPantry("yolomep");
        assertEquals(get.getUser(), "yolomep");
        assertEquals(get.getIngredients().get(0).getIngredientName(), "白菜");
    }

    @Test
    public void addRecipesDirectly() throws InterruptedException {
        ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
        ingredients.add(new ExpirableIngredient("Skirt Steak", 1));
        ingredients.add(new ExpirableIngredient("Salt", 10));
        ingredients.add(new ExpirableIngredient("Pepper", 10));

        Recipe r1 = new Recipe(ingredients, "DEF", "ABC", "QER");
        Recipe r2 = new Recipe(ingredients, "AAA", "BBB", "CCC");
        Database db = Database.getInstance();
        db.addRecipe(r1);
        db.addRecipe(r2);

        Thread.sleep(500);
        CookBook get = db.getCookBook();
        assertTrue(get.getAllRecipes().contains(r1));
        assertTrue(get.getAllRecipes().contains(r2));
    }
    @Test
    public void removeRecipesDirectly() throws InterruptedException {
        ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
        ingredients.add(new ExpirableIngredient("Skirt Steak", 1));
        ingredients.add(new ExpirableIngredient("Salt", 10));
        ingredients.add(new ExpirableIngredient("Pepper", 10));

        Recipe r1 = new Recipe(ingredients, "DEF", "ABC", "QER");
        Recipe r2 = new Recipe(ingredients, "AAA", "BBB", "CCC");


        Database db = Database.getInstance();

        db.deleteRecipe("DEF");
        db.deleteRecipe("AAA");

        Thread.sleep(500);
        CookBook get = db.getCookBook();
        assertFalse(get.getAllRecipes().contains(r1));
        assertFalse(get.getAllRecipes().contains(r2));
    }

    @Test
    public void testIngredientMatching() {
        AbstractIngredient rice = new ExpirableIngredient("rice", 2);
        AbstractIngredient big_rice = new ExpirableIngredient("rice", 3);

        assertTrue(rice.sameIngredient(big_rice));
        assertNotEquals(rice, big_rice);
    }

    @Test
    public void testIngredientClass() {
        AbstractIngredient rice = new ExpirableIngredient("rice", 2);
        assertEquals(rice.getIngredientName(), "rice");
        assertEquals(rice.getIngredientCount(), 2);
    }

    //Checks if Quantity, Calories, and Expiration Date are Properly Stored in Pantry
    @Test
    public void addIngredientAttributes() throws InterruptedException {
        Database database = Database.getInstance();

        Pantry pantry = new Pantry("abhinn");

        LocalDate date = LocalDate.of(2003, 10, 19);

        ExpirableIngredient newIngredient = (new ExpirableIngredient("unitIngredientTest", 9,
                500, date));

        pantry.addIngredient(newIngredient);

        database.addPantry(pantry);

        Thread.sleep(500);

        pantry = database.getPantry("abhinn");

        List<AbstractIngredient> ingredients = pantry.getIngredients();

        AbstractIngredient currIngredient = null;

        for (AbstractIngredient ingredient: ingredients) {
             if (ingredient.sameIngredient(newIngredient)) {
                 currIngredient = ingredient;
             }
        }

        assertEquals(currIngredient.getIngredientCount(), newIngredient.getIngredientCount());
        assertEquals(currIngredient.getIngredientCalories(), newIngredient.getIngredientCalories());

        ExpirableIngredient eCurrIngredient = (ExpirableIngredient) currIngredient;
        assertEquals(eCurrIngredient.getIngredientExpirationDate(),
                newIngredient.getIngredientExpirationDate());
    }

    //Check if Pantry stores no Expiration Date if value not known
    @Test
    public void noExpirationDate() throws InterruptedException {
        Database database = Database.getInstance();
        Pantry pantry = new Pantry("abhinn");
        AbstractIngredient newIngredient = (new DefaultIngredient("IngredientTest", 11,
                60));
        pantry.addIngredient(newIngredient);
        database.addPantry(pantry);
        Thread.sleep(500);
        pantry = database.getPantry("abhinn");
        List<AbstractIngredient> ingredients = pantry.getIngredients();
        AbstractIngredient currIngredient = null;
        for (AbstractIngredient ingredient: ingredients) {
            if (ingredient.sameIngredient(newIngredient)) {
                currIngredient = ingredient;
            }
        }
        assertFalse((currIngredient instanceof ExpirableIngredient));
    }

    @Test
    public void removeIngredientDB() throws InterruptedException {
        Database db = Database.getInstance();
        Pantry pantry = new Pantry("test");
        AbstractIngredient newIngredient = (new DefaultIngredient("IngredientTest", 11,
                60));
        pantry.addIngredient(newIngredient);

        db.addPantry(pantry);
        Thread.sleep(500);

        db.removeIngredient("IngredientTest", "test");
        Thread.sleep(500);

        Pantry dbPantry = db.getPantry("test");
        assert(dbPantry.getIngredients().size() == 0);
    }

    @Test
    public void removeIngredientPantry() {
        Pantry pantry = new Pantry("test");
        AbstractIngredient newIngredient = (new DefaultIngredient("IngredientTest", 11,
                60));
        pantry.addIngredient(newIngredient);
        assertNotNull(pantry.getIngredient("IngredientTest"));
        pantry.removeIngredient(newIngredient);
        assertNull(pantry.getIngredient("IngredientTest"));
    }

    @Test
    public void getUserRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
        ingredients.add(new DefaultIngredient("ingredient1", 1));
        ingredients.add(new DefaultIngredient("ingredient2", 2));
        Recipe testRecipe = new Recipe(ingredients, "testRecipe", "",
                "testtest2");
        recipes.add(testRecipe);

        List<Recipe> userRecipes = new RecipeFilterUser().filterRecipes(recipes, "testtest2");
        assertTrue(userRecipes.contains(testRecipe));

    }

    @Test
    public void testRecipeFilterAvailable() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(2000);

        List<Recipe> recipes = new ArrayList<>();
        ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
        ingredients.add(new DefaultIngredient("ingredient1", 1));
        ingredients.add(new DefaultIngredient("ingredient2", 2));
        Recipe testRecipe = new Recipe(ingredients, "testRecipe", "",
                "unittest3");
        recipes.add(testRecipe);

        Pantry pantry = new Pantry("unittest3");
        pantry.addIngredient(new DefaultIngredient("ingredient1", 3));
        pantry.addIngredient(new DefaultIngredient("ingredient2", 3));
        database.addPantry(pantry);
        Thread.sleep(500);

        List<Recipe> availableRecipes = new RecipeFilterAvailable().filterRecipes(recipes, "unittest3");

        assertTrue(availableRecipes.contains(testRecipe));
    }
}
