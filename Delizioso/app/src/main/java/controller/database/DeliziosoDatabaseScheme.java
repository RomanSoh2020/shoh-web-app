package controller.database;

public final class DeliziosoDatabaseScheme {
    public static final String DB_NAME = "delizioso";

    public static final class FoodScheme{
        public static final String TABLE_NAME = "FOOD";

        public static final class Cols{
            public static final String ID = "_id";
            public static final String NAME = "name";
            public static final String DESCRIPTION = "description";
            public static final String IMAGE_RESOURCE_ID = "image_resource_id";
            public static final String TYPE = "type";
            public static final String COMPOSITION = "composition";
            public static final String TIME_OF_COOKING = "time_of_cooking";
            public static final String PRICE = "price";
        }
    }

    public static final class OrderScheme{
        public static final String TABLE_NAME = "ORDERS";

        public static final class Cols{
            public static final String ID = "_id";
            public static final String TOTAL_TIME = "total_time";
            public static final String TOTAL_PRICE = "total_price";
        }
    }

    public static class FoodOrderScheme{
        public static final String TABLE_NAME = "FOOD_ORDER";

        public static final class Cols{
            public static final String FOOD_ID = "food_id";
            public static final String ORDER_ID = "order_id";
            public static final String QUANTITY = "quantity";
        }
    }
}
