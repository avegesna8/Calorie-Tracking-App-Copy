package com.cs2340aG49.greenPlate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.concurrent.futures.AbstractResolvableFuture;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cs2340aG49.greenPlate.ui.model.AbstractIngredient;
import com.cs2340aG49.greenPlate.ui.model.AbstractIngredientFactory;
import com.cs2340aG49.greenPlate.ui.model.CheckBoxIngredient;
import com.cs2340aG49.greenPlate.ui.model.CheckboxIngredientFactory;
import com.cs2340aG49.greenPlate.ui.model.CookBook;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredient;
import com.cs2340aG49.greenPlate.ui.model.DefaultIngredientFactory;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredient;
import com.cs2340aG49.greenPlate.ui.model.ExpirableIngredientFactory;
import com.cs2340aG49.greenPlate.ui.model.Meal;
import com.cs2340aG49.greenPlate.ui.model.Pantry;
import com.cs2340aG49.greenPlate.ui.model.Recipe;
import com.cs2340aG49.greenPlate.ui.model.ShoppingList;
import com.cs2340aG49.greenPlate.ui.view.DatabaseObserver;
import com.cs2340aG49.greenPlate.ui.view.RecipeActivity;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class Sprint4UnitTests {
    @Test
    public void testFactories() {
        CheckboxIngredientFactory cif = new CheckboxIngredientFactory();
        DefaultIngredientFactory dif = new DefaultIngredientFactory();
        ExpirableIngredientFactory eif = new ExpirableIngredientFactory();

        AbstractIngredient cifI = cif.makeIngredient("Ingredient 1", 10, 100, true);
        AbstractIngredient difI = dif.makeIngredient("Ingredient 2", 9, 79);
        AbstractIngredient eifI = eif.makeIngredient("Ingredient 3", 8, 12);

        assertEquals(cifI, new CheckBoxIngredient("Ingredient 1", 10, 100, true));
        assertEquals(difI, new DefaultIngredient("Ingredient 2", 9, 79));
        assertEquals(eifI, new ExpirableIngredient("Ingredient 3", 8, 12));

    }

    @Test
    public void testShoppingList() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(500);

        ArrayList<AbstractIngredient> ingredients = new ArrayList<>();
        CheckboxIngredientFactory c = new CheckboxIngredientFactory();
        AbstractIngredient i = c.makeIngredient("ingredient1", 1);
        AbstractIngredient j = c.makeIngredient("ingredient2", 200, true);
        ShoppingList shoppingList = new ShoppingList("unittest3");

        shoppingList.addIngredient(i);
        shoppingList.addIngredient(j);
        database.addNewShoppingList(shoppingList);
        Thread.sleep(500);
        ShoppingList gotten = database.getShoppingList("unittest3");
        List<AbstractIngredient> ing = gotten.getIngredients();
        assertEquals(i, ing.get(0));
        assertEquals(j, ing.get(1));
    }

    @Test
    public void testAddToEmptyShoppingListNoPantry() throws Exception {
        Database db = Database.getInstance();
        // Thread.sleep(500);


        List<Recipe> recipes = db.getCookBook().getAllRecipes();
        Recipe theRecipe = null;
        for (Recipe r : recipes) {
            if (r.getName().equals("Poker Chips and Not Chose")){
                theRecipe = r;
                break;
            }
        }
        assertNotNull(theRecipe);

        Pantry pantry = new Pantry("unittest4");
        ShoppingList sl = new ShoppingList("unittest4");

        for (AbstractIngredient i : db.getShoppingList("unittest4").getIngredients()){
            db.removeShoppingListItem(i.getIngredientName(), "unittest4");

        }
        for (AbstractIngredient i : theRecipe.getIngredients()){
            db.removeShoppingListItem(i.getIngredientName(), "unittest4");
            db.removePantryItem(i.getIngredientName(), "unittest4");
        }
        Thread.sleep(300);


        db.addPantry(pantry);
        db.addNewShoppingList(sl);
        RecipeActivity.buyIngredientForRecipeForUser(theRecipe, "unittest4");
        Thread.sleep(300);

        List<AbstractIngredient> slIngreds = db.getShoppingList("unittest4").getIngredients();
        System.out.println(slIngreds.size());

        for (AbstractIngredient ingredient : theRecipe.getIngredients()){
            boolean flag = false;
            for (AbstractIngredient i2 : slIngreds){
                if (i2.getIngredientName().equals(ingredient.getIngredientName())){
                    if (i2.getIngredientCount() >= ingredient.getIngredientCount()){
                        flag = true; break;
                    }

                }
            }
            assertTrue(flag);
        }

    }


    @Test
    public void testAddToEmptyShoppingListWithPantry() throws Exception {
        Database db = Database.getInstance();
        // Thread.sleep(500);
        List<Recipe> recipes = db.getCookBook().getAllRecipes();
        Recipe theRecipe = null;
        for (Recipe r : recipes) {
            if (r.getName().equals("Poker Chips and Not Chose")){
                theRecipe = r;
                break;
            }
        }
        assertNotNull(theRecipe);

        AbstractIngredient theIngredient = theRecipe.getIngredients().get(0);

        Pantry pantry = new Pantry("unittest4");
        pantry.addIngredient(new DefaultIngredient(theIngredient.getIngredientName(), theIngredient.getIngredientCount() - 1, theIngredient.getIngredientCalories()));
        ShoppingList sl = new ShoppingList("unittest4");

        for (AbstractIngredient i : theRecipe.getIngredients()){
            db.removeShoppingListItem(i.getIngredientName(), "unittest4");
        }
        Thread.sleep(300);


        db.addPantry(pantry);
        db.addNewShoppingList(sl);
        RecipeActivity.buyIngredientForRecipeForUser(theRecipe, "unittest4");
        Thread.sleep(300);

        List<AbstractIngredient> slIngreds = db.getShoppingList("unittest4").getIngredients();
        System.out.println(slIngreds.size());

        for (AbstractIngredient ingredient : theRecipe.getIngredients()){
            boolean flag = false;
            if (ingredient == theIngredient){
                for (AbstractIngredient i2 : slIngreds){
                    if (i2.getIngredientName().equals(ingredient.getIngredientName())){
                        if (i2.getIngredientCount() == 1){
                            flag = true; break;
                        }
                    }
                }
            }
            for (AbstractIngredient i2 : slIngreds){
                if (i2.getIngredientName().equals(ingredient.getIngredientName())){
                    if (i2.getIngredientCount() >= ingredient.getIngredientCount()){
                        flag = true; break;
                    }

                }
            }
            assertTrue(flag);
        }

    }

    @Test
    public void testIngredientsEquals() {
        DefaultIngredient defaultIngredient = new DefaultIngredient("ing", 3, 200);
        ExpirableIngredient expirableIngredient = new ExpirableIngredient("ing", 3, 200, LocalDate.now());
        CheckBoxIngredient checkBoxIngredient = new CheckBoxIngredient("ing", 3, 200, true);

        assertTrue(defaultIngredient.equals(expirableIngredient));
        assertTrue(expirableIngredient.equals(checkBoxIngredient));
        assertTrue(defaultIngredient.equals(checkBoxIngredient));

        defaultIngredient = new DefaultIngredient("ing", 3, 150);
        expirableIngredient = new ExpirableIngredient("ing", 2, 200, LocalDate.now());
        checkBoxIngredient = new CheckBoxIngredient("two", 3, 200, true);

        assertFalse(defaultIngredient.equals(expirableIngredient));
        assertFalse(defaultIngredient.equals(checkBoxIngredient));
        assertFalse(checkBoxIngredient.equals(expirableIngredient));

    }

    @Test
    public void testSameIngredient() {
        DefaultIngredient defaultIngredient = new DefaultIngredient("ing", 3, 200);
        ExpirableIngredient expirableIngredient = new ExpirableIngredient("ing", 3, 200, LocalDate.now());
        CheckBoxIngredient checkBoxIngredient = new CheckBoxIngredient("ing", 3, 200, true);

        assertTrue(defaultIngredient.sameIngredient(expirableIngredient));
        assertTrue(expirableIngredient.sameIngredient(checkBoxIngredient));
        assertTrue(defaultIngredient.sameIngredient(checkBoxIngredient));

        defaultIngredient = new DefaultIngredient("ing", 3, 200);
        expirableIngredient = new ExpirableIngredient("five", 3, 200, LocalDate.now());
        checkBoxIngredient = new CheckBoxIngredient("two", 3, 200, true);

        assertFalse(defaultIngredient.sameIngredient(expirableIngredient));
        assertFalse(defaultIngredient.sameIngredient(checkBoxIngredient));
        assertFalse(checkBoxIngredient.sameIngredient(expirableIngredient));
    }

    @Test
    public void testCookBook() throws Exception {
        Database db = Database.getInstance();
        Thread.sleep(1000);
        CookBook cookBook = db.getCookBook();
        List<AbstractIngredient> food = new ArrayList<>();
        food.add(new DefaultIngredient("food", 2, 2));
        Recipe re = new Recipe(food, "i like");
        cookBook.addRecipe(re);
        assertEquals("i like", re.getName());

    }

    @Test
    public void testMealCalc() {
        List<AbstractIngredient> food = new ArrayList<>();
        food.add(new DefaultIngredient("food", 2, 2));
        Recipe r = new Recipe(food, "i like");
        Meal meal = new Meal(r.getName(),
                r.getIngredients().stream().
                        mapToInt(AbstractIngredient::getIngredientCalories).sum());
        assertEquals(2, meal.getCalorieCount(), 0.001);
    }

    // Test if Changing "Checked" Attribute Works Correctly When Added to Shopping List
    @Test
    public void testChangingCheckBoxAttributeInShoppingList() throws InterruptedException {
        Database database = Database.getInstance();
        boolean checked = false;
        ShoppingList shoppingList = new ShoppingList("abhinn");
        CheckboxIngredientFactory checkboxIngredientCreator = new CheckboxIngredientFactory();

        //NewIngredient originally marked as false
        CheckBoxIngredient newIngredient = checkboxIngredientCreator.makeIngredient("Unit Test 1", 10);
        //NewIngredient now marked as true
        newIngredient.setChecked(true);
        shoppingList.addIngredient(newIngredient);
        database.addNewShoppingList(shoppingList);
        Thread.sleep(500);

        shoppingList = database.getShoppingList("abhinn");
        List<AbstractIngredient> ingredients = shoppingList.getIngredients();
        for (AbstractIngredient ingredient: ingredients) {
            CheckBoxIngredient checkedIngredient = (CheckBoxIngredient) ingredient;
            if (ingredient.sameIngredient(newIngredient)) {
                //Set value to current "check" value of newIngredient in shopping list
                checked = checkedIngredient.getChecked();
            }
        }

        //"checked" should be true
        assertTrue(checked);
    }

    // Test if Adding Ingredient to Pantry from Shopping List and deleting it from the Shopping
    // List works
    @Test
    public void testSameIngredientAttributesFromShoppingListToPantry() throws InterruptedException {
        Database database = Database.getInstance();
        ShoppingList shoppingList = new ShoppingList("abhinn");

        Pantry pantry = new Pantry("abhinn");

        CheckboxIngredientFactory checkboxIngredientCreator = new CheckboxIngredientFactory();

        int newIngredientCount = 20;
        int newIngredientCalories = 100;

        //Create new ingredient to add to shopping list
        CheckBoxIngredient newIngredient = checkboxIngredientCreator.makeIngredient("Unit Test 2",
                newIngredientCount, newIngredientCalories);
        shoppingList.addIngredient(newIngredient);
        database.addNewShoppingList(shoppingList);
        Thread.sleep(500);

        shoppingList = database.getShoppingList("abhinn");
        List<AbstractIngredient> ingredients = shoppingList.getIngredients();
        for (AbstractIngredient ingredient: ingredients) {
            if (ingredient.sameIngredient(newIngredient)) {
                //Search shopping list and add that newly added ingredient to pantry
                pantry.addIngredient(newIngredient);
            }
        }

        //Remove ingredient from shopping list
        database.removeShoppingListItem(newIngredient.getIngredientName(), "abhinn");

        //Update Pantry
        database.addPantry(pantry);
        Thread.sleep(400);

        pantry = database.getPantry("abhinn");


        int pantryIngredientCount = pantry.getIngredientCount(newIngredient.getIngredientName());

        int pantryIngredientCalories = pantry.getIngredient(newIngredient.getIngredientName()).getIngredientCalories();

        //Make sure Ingredient attributes are correct
        assertEquals(newIngredientCount == pantryIngredientCount, newIngredientCalories == pantryIngredientCalories);

        boolean foundIngredient = false;
        shoppingList = database.getShoppingList("abhinn");
        List<AbstractIngredient> newListIngredients = shoppingList.getIngredients();
        for (AbstractIngredient ingredient: newListIngredients) {
            if (ingredient.sameIngredient(newIngredient)) {
                //See if ingredient is still in shopping list
                foundIngredient = true;
            }
        }

        //Make sure ingredient no longer in shopping list
        assertFalse(foundIngredient);

    }

    @Test
    public void testIngredientPolymorphism() {
        DefaultIngredient d = new DefaultIngredient("carrot", 1);
        ExpirableIngredient e = new ExpirableIngredient();
        CheckBoxIngredient c = new CheckBoxIngredient();

        assert d instanceof AbstractIngredient;
        assert e instanceof AbstractIngredient;
        assert c instanceof AbstractIngredient;
    }

    @Test
    public void testObserver() {

        boolean[] updated = {false};
        DatabaseObserver o = new DatabaseObserver() {
            @Override
            public void update() {
                updated[0] = true;
            }
        };

        assertFalse(updated[0]);

        o.update();

        assertTrue(updated[0]);
    }
}
