package controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.bignerdranch.android.delizioso.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Order;
import model.food.Food;

import static controller.database.DeliziosoDatabaseScheme.*;

public class DeliziosoDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int DB_VERSION = 1;

    public DeliziosoDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i , i1);
    }

    private static void insertFoodToDB(SQLiteDatabase db,
                                      String name,
                                      String description,
                                      int resourceId,
                                      String type,
                                      List<String> composition,
                                      double timeOfCooking,
                                      double price){

        ContentValues values = new ContentValues();
        values.put(FoodScheme.Cols.NAME, name);
        values.put(FoodScheme.Cols.DESCRIPTION, description);
        values.put(FoodScheme.Cols.IMAGE_RESOURCE_ID, resourceId);
        values.put(FoodScheme.Cols.TYPE, type);
        values.put(FoodScheme.Cols.TIME_OF_COOKING, timeOfCooking);
        values.put(FoodScheme.Cols.PRICE, price);

        String compStr = "";
        for (int i = 0; i < composition.size(); i++) {
            compStr = compStr.concat(composition.get(i)).concat(", ");
        }

        compStr = compStr.substring(0, compStr.length() - 2);

        values.put(FoodScheme.Cols.COMPOSITION, compStr);

        db.insert(FoodScheme.TABLE_NAME, null, values);
    }

    private static void insertOrderToDB(SQLiteDatabase db,
                                        Order order){
        ContentValues values = new ContentValues();
        values.put(OrderScheme.Cols.ID, order.getId());
        values.put(OrderScheme.Cols.TOTAL_PRICE, order.getTotalPrice());
        values.put(OrderScheme.Cols.TOTAL_TIME, order.getTotalTime());

        db.insert(OrderScheme.TABLE_NAME, null, values);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        Resources resources = context.getResources();
        String[] foodNames = resources.getStringArray(R.array.food_names);
        String[] foodDescriptions = resources.getStringArray(R.array.food_descriptions);
        String[] foodComposition = resources.getStringArray(R.array.composition);

        if(oldVersion < 1){
            db.execSQL("CREATE TABLE " + FoodScheme.TABLE_NAME +  " " +
                    "(" + FoodScheme.Cols.ID
                     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FoodScheme.Cols.NAME + " TEXT, " +
                    FoodScheme.Cols.DESCRIPTION + " TEXT," +
                    FoodScheme.Cols.IMAGE_RESOURCE_ID + " INTEGER, " +
                    FoodScheme.Cols.TYPE + " TEXT, " +
                    FoodScheme.Cols.COMPOSITION + " TEXT, " +
                    FoodScheme.Cols.TIME_OF_COOKING + " REAL," +
                    FoodScheme.Cols.PRICE + " REAL);");

            insertFoodToDB(db, foodNames[0], foodDescriptions[0],
                    R.drawable.strawberry_focaccia,"pizza",
                    Arrays.asList(foodComposition[0].split(", ")),
                    20, 3.85);

            insertFoodToDB(db, foodNames[1], foodDescriptions[1],
                    R.drawable.diablo, "pizza",
                    Arrays.asList(foodComposition[1].split(", ")),
                            15, 4);

            insertFoodToDB(db, foodNames[2], foodDescriptions[2],
                    R.drawable.capricioasa, "pizza",
                    Arrays.asList(foodComposition[2].split(", ")),
                    18, 4);

            insertFoodToDB(db, foodNames[3], foodDescriptions[3], R.drawable.pepperoni,"pizza",
                    Arrays.asList(foodComposition[3].split(", ")), 20, 4);

            insertFoodToDB(db, foodNames[4], foodDescriptions[4], R.drawable.neapolitana,
                    "pizza", Arrays.asList(foodComposition[4].split(", ")),
                    25, 4.25);

            insertFoodToDB(db, foodNames[5], foodDescriptions[5], R.drawable.mario, "pizza",
                    Arrays.asList(foodComposition[5].split(", ")), 30, 5);

            insertFoodToDB(db, foodNames[6], foodDescriptions[6], R.drawable.margherita,
                    "pizza", Arrays.asList(foodComposition[6].split(", ")),
                    25,4);
            insertFoodToDB(db, foodNames[7], foodNames[7], R.drawable.shrimp, "pasta",
                    Arrays.asList(foodComposition[7].split(", ")),
                    30,5);

            insertFoodToDB(db, foodNames[8], foodDescriptions[7], R.drawable.cheesecake, "dessert",
                    Arrays.asList(foodComposition[8].split(", ")),
                    5,2);
            insertFoodToDB(db, foodNames[9], foodDescriptions[8], R.drawable.musscat_rose,
                    "drink",Arrays.asList(foodComposition[9].split(", ")),
                    3,20);

            db.execSQL("CREATE TABLE " + OrderScheme.TABLE_NAME + " (" +
                    OrderScheme.Cols.ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    OrderScheme.Cols.TOTAL_PRICE + " REAL," +
                    OrderScheme.Cols.TOTAL_TIME + " REAL);");

            db.execSQL("CREATE TABLE " + FoodOrderScheme.TABLE_NAME + " (" +
                    FoodOrderScheme.Cols.FOOD_ID + " INTEGER NOT NULL, " +
                    FoodOrderScheme.Cols.ORDER_ID + " INTEGER NOT NULL," +
                    FoodOrderScheme.Cols.QUANTITY + " INTEGER, " +
                    "FOREIGN KEY(" + FoodOrderScheme.Cols.FOOD_ID + ") " +
                    "REFERENCES " + FoodScheme.TABLE_NAME + " (" + FoodScheme.Cols.ID + ")," +
                    "FOREIGN KEY(" + FoodOrderScheme.Cols.FOOD_ID + ") REFERENCES "
                    + OrderScheme.TABLE_NAME + " (" + OrderScheme.Cols.ID + ") );");

            insertOrderToDB(db, new Order(1, new ArrayList<>()));
        }

        if(oldVersion < 2){
            //code that should upgrade our database
        }
    }
}
