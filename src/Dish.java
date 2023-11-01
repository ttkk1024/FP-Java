public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;
    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public int getCalories() {
        return calories;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return name;
    }
    public enum Type { MEAT, FISH, OTHER }
}

// Java Record vs Scala Case Class
//public record Dish(String name, boolean vegetarian, int calories, Dish.Type type) {
//    @Override
//    public String toString() {
//        return name;
//    }
//
//    public enum Type {MEAT, FISH, OTHER}
//}

//    定义一个枚举（Enum）表示Dish的类型
//    sealed trait Type
//    case object MEAT extends Type
//    case object FISH extends Type
//    case object OTHER extends Type
//
//    定义一个Dish的Case类
//    case class Dish(name: String, vegetarian: Boolean, calories: Int, dishType: Type)
