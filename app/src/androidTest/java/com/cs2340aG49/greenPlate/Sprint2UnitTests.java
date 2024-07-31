package com.cs2340aG49.greenPlate;
import java.lang.Thread;
import java.util.List;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.cs2340aG49.greenPlate.ui.model.Account;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.Meal;
import com.cs2340aG49.greenPlate.ui.viewmodel.LoginViewModel;
import com.cs2340aG49.greenPlate.ui.model.User;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Sprint2UnitTests {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.cs2340aG49.greenPlate", appContext.getPackageName());
    }

    /**
     * Test meal input into database with sample values.
     */
    @Test
    public void inputAndRetrieve2Meals() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000); // give time for database to init
        database.addAccount(new Account("units", "unittest123"));
        Meal meal1 = new Meal("meal1", 1234);
        Meal meal2 = new Meal("meal2", 600);
        Thread.sleep(300); // give time for database to init
        database.addMeal("units", meal1);
        database.addMeal("units", meal2);
        Thread.sleep(500);
        List<Meal> meals = database.getMeals("units");
        System.out.println(meals.get(0).getDate());
        System.out.println(meals.size());
        assert meal2.equals(meals.get(1));
        assert meal1.equals(meals.get(0));
        database.deleteAccount("units");
    }

    /**
     * Test meal retrieval from database with sample values.
     */
    @Test
    public void loginSuccessful() throws InterruptedException {
        LoginViewModel lvm = new LoginViewModel();
        Thread.sleep(1000);
        lvm.createAccount("jeremy123", "jeremypassword");
        Thread.sleep(400);
        assert lvm.login("jeremy123","jeremypassword");
        assert !lvm.login("jeremy123","notjeremeypassword");
    }
    @Test
    public void addUsers() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addUser(new User("Mao",0,120,"cat" ));
        database.addUser(new User("Miao",10000,10,"sound"));
        database.addUser(new User("Jiao",-1,-1,"" ));

        Thread.sleep(1000);
        User user = database.getUser("Mao");
        User user2 = database.getUser("Jiao");
        assertTrue(user.getHeight() == 0);
        assertTrue(user2.getWeight() == -1);
    }

    @Test
    public void modifyUsers() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addUser(new User("Sleep",0,20,"bead" ));
        database.addUser(new User("ing",1,10,"sound"));
        database.addUser(new User("Sleep",0,20,"bed"));
        Thread.sleep(1000);
        User user = database.getUser("Sleep");
        assertTrue(user.getGender().equals("bed"));
    }

    @Test
    public void testTodayMeals() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addAccount(new Account("unittest", "password"));
        Thread.sleep(300);
        User user = database.getUser("unittest");
        database.addMeal("unittest", new Meal("meal1", 100));
        database.addMeal("unittest", new Meal("meal2", 200));
        Thread.sleep(300);
        List<Meal> todayMeals = database.getTodayMeals("unittest");
        List<Meal> meals = database.getMeals("unittest");
        Thread.sleep(300);
        assertEquals(todayMeals, meals);
        database.deleteAccount("unittest");
    }

    @Test
    public void testTodayMealsCalories() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addAccount(new Account("unittest", "password"));
        Thread.sleep(300);
        User user = database.getUser("unittest");
        database.addMeal("unittest", new Meal("meal1", 100));
        Thread.sleep(300);
        database.addMeal("unittest", new Meal("meal2", 200));
        Thread.sleep(300);
        List<Meal> todayMeals = database.getTodayMeals("unittest");
        int calorieCount = 0;
        for (Meal m : todayMeals) {
            calorieCount += m.getCalorieCount();
        }
        assertEquals(calorieCount, 300);
        database.deleteAccount("unittest");
    }

    @Test
    public void testMealClass() {
        Meal meal = new Meal("test", 100);
        assertEquals(meal.getMealName(), "test");
        assert meal.getCalorieCount() == 100;
    }

    @Test
    public void testUserClass() {
        User user = new User("test", 6, 150, "testGender");
        assertEquals(user.getUsername(), "test");
        assert user.getHeight() == 6;
        assert user.getWeight() == 150;
        assertEquals(user.getGender(), "testGender");
    }
    /**
     * Test Updating Personal Information
     */
    @Test
    public void updateHeightTest() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);

        database.addUser(new User("Steve",38,135.5,"male" ));
        Thread.sleep(300);

        User user = database.getUser("Steve");
        Double newHeight = 30.3;

        database.updatePersonalInfo(user.getUsername(), newHeight, user.getWeight(),
                user.getGender());
        Thread.sleep(300);

        User updatedUser = database.getUser("Steve");

        assert updatedUser.getHeight() == newHeight;
    }

    @Test
    public void updateWeight() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);

        database.addUser(new User("David",67.2,169.3,"male" ));
        Thread.sleep(300);

        User user = database.getUser("David");
        Double newWeight = 40.7;

        database.updatePersonalInfo(user.getUsername(), user.getHeight(), newWeight,
                user.getGender());
        Thread.sleep(300);
        User updatedUser = database.getUser("David");

        assert updatedUser.getWeight() == newWeight;
    }

    @Test
    public void updateGender() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);

        database.addUser(new User("John", 68, 150, "male"));
        Thread.sleep(300);

        User user = database.getUser("John");
        String newGender = "female";

        database.updatePersonalInfo(user.getUsername(), user.getHeight(), user.getWeight(),
                newGender);
        Thread.sleep(300);
        User updatedUser = database.getUser("John");

        assertEquals(updatedUser.getGender(), newGender);

    }

    @Test
    public void testAccountClass() throws InterruptedException {
        Account newAccount = new Account("testuser", "testpass");
        assertEquals(newAccount.getUsername(), "testuser");
        assertEquals(newAccount.getPassword(), "testpass");
    }

    @Test
    public void modifyWeight() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addUser(new User("Sleep",0,20,"bead" ));
        database.addUser(new User("ing",1,10,"sound"));
        database.addUser(new User("Sleep",0,10,"bead"));
        Thread.sleep(1000);
        User user = database.getUser("Sleep");
        assertTrue(user.getWeight() == 10);
    }

    @Test
    public void modifyHeight() throws InterruptedException {
        Database database = Database.getInstance();
        Thread.sleep(1000);
        database.addUser(new User("Sleep",0,20,"bead" ));
        database.addUser(new User("ing",1,10,"sound"));
        database.addUser(new User("Sleep",2,20,"bead"));
        Thread.sleep(1000);
        User user = database.getUser("Sleep");
        assertTrue(user.getHeight() == 2);
    }

}
