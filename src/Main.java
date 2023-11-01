import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;


//ERROR:
//1.Toolbox无法更新软件: 无法提取软件包 (Toolbox cannot update software: Unable to extract package)
// 内存太小，从8G升级为16G，报错消失

//2. Java Stream API 和 Java I/O Stream 是两个不同的概念，用于不同的目的。
//
//        1. **Java Stream API**:
//        - Java Stream API 是 Java 8 引入的一项功能，用于处理集合数据的高级抽象。它提供了一种声明性的、函数式编程风格的方式来操作数据集合。
//        - Stream API 用于对集合数据执行过滤、映射、归约等操作，通常用于集合数据的处理和转换。
//        - Stream API 不直接与文件或网络进行交互，而是专注于处理内存中的数据。它提供了 `Stream` 类型，可用于处理集合数据，如 `List`、`Set` 和 `Map`。
//        - 示例操作包括 `filter`、`map`、`reduce`、`collect` 等，用于处理集合数据的转换和筛选。
//
//        2. **Java I/O Stream**:
//        - Java I/O Stream 是 Java 中用于处理输入和输出（I/O）的一种机制，用于文件、网络、键盘和屏幕等的输入输出操作。
//        - I/O Stream 用于实际的输入和输出任务，例如从文件中读取数据、向文件写入数据、与网络进行通信以及从键盘读取用户输入。
//        - Java I/O Stream 提供了 `InputStream`、`OutputStream`、`Reader` 和 `Writer` 等类，用于处理不同类型的输入和输出。
//        - 示例操作包括 `read`、`write`、`close` 等，用于执行实际的输入和输出操作。
//
//        总结来说，Java Stream API 用于处理集合数据的高级函数式编程操作，
//        而 Java I/O Stream 用于实际的输入和输出任务，包括文件处理、网络通信、键盘输入等。
//        这两个 API 具有不同的用途，不应混淆。但请注意，它们都在 Java 编程中扮演重要的角色。
public class Main {
    public static void main(String[] args) {
        //introduce lambda
        List<Apple> inventory = new ArrayList<>();
        //inventory.sort();

        //method1: 行为参数化
        class AppleComparator implements Comparator<Apple> {
            public int compare(Apple a1, Apple a2){
                return a1.getWeight().compareTo(a2.getWeight());
            }
        }
        inventory.sort(new AppleComparator());


        //method2: 匿名类行为参数化
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        //method3: Lambda
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

        //method4: comparing
        inventory.sort(comparing(apple -> apple.getWeight()));

        //method5: 方法引用
        inventory.sort(comparing(Apple::getWeight));


        //Lambda的复合用法
        //比较器的复合
        //谓词复合
        //函数复合


        //introduce Stream 简短的定义就是“从支持数据处理操作的源生成的元素序列”
        // no stream ,热量小于400卡，对热量排名，并给出食物的名字
        List<Dish> lowCaloricDishes = new ArrayList<>();
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH) );
        //热量小于400卡
        for (Dish dish: menu){
            if (dish.getCalories() < 400){
                lowCaloricDishes.add(dish);
            }
        }
        //对热量排名
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(),o2.getCalories());
            }
        });
        //并给出食物的名字
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish dish: lowCaloricDishes){
            lowCaloricDishesName.add(dish.getName());
        }

        //using stream

        List<String> lowCaloricDishesName_steam =
                menu.stream()
                        .filter(dish -> dish.getCalories() < 400)
                        .sorted(comparing(Dish::getCalories))
                        .map(Dish::getName)
                        .collect(toList());
        List<String> lowCaloricDishesName_parallelSteam =
                menu.parallelStream()//并行执行的底层模型
                        .filter(dish -> dish.getCalories() < 400)
                        .sorted(comparing(Dish::getCalories))
                        .map(Dish::getName)
                        .toList();//Java 16
// Scala Demo
//              case class Dish(name: String, calories: Int)
//              val menu: List[Dish] = List(
//                Dish("Salad", 350),
//                Dish("Burger", 600),
//                Dish("Pizza", 1200),
//                Dish("Fruit Salad", 250),
//                Dish("Chicken Sandwich", 400)
//              )
//             val lowCaloricDishesName: List[String] = menu
//                .par//并行执行
//                .filter(dish => dish.calories < 400)
//                .sortWith((a, b) => a.calories < b.calories)
//                .map(dish => dish.name)
        List<String> threeHighCaloricDishNames =
                menu.stream()
                        .filter(dish -> dish.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        .collect(toList());
        //stream形成流
        //filter,map,limit流水线 lazy
        //collect触发流水线执行并关闭


        // collection and stream
        // 1.stream只能消费一次
        // 2.collection外部迭代，下达命令并给出实施步骤，stream内部迭代，下达命令无需给出实施步骤,声明式编成的典型特点
        // 只要发现有针对集合的迭代，就应该考虑用stream来优化代码
        List<String> names = new ArrayList<>();
        for (Dish dish: menu){
            names.add(dish.getName());
        }

        List<String> names_steam = menu.stream()
                .map(Dish::getName)
                .collect(toList());
        //中间操作发生了什么时期
        List<String> names_middle =
                menu.stream()
                        .filter(dish -> {
                            System.out.println("filtering:" + dish.getName());
                            return dish.getCalories() > 300;
                        })
                .map(dish -> {
                    System.out.println("mapping:" + dish.getName());
                    return dish.getName();
                })
                .limit(3)
                .collect(toList());
        System.out.println(names);
//        filtering:pork
//        mapping:pork
//        filtering:beef
//        mapping:beef
//        filtering:chicken
//        mapping:chicken
//       [pork, beef, chicken]


        //filter和map两个独立的操作合并到一次遍历中，这样的技术叫循环引用
        //中间操作：filter,map,limit,sorted,distinct
        //终端操作：forEach,count,collect

        //谓词筛选
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);
        //2,4




        //流切片
        //1.用谓词切片
        // no takeWhile
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));
        List<Dish> filteredMenu
                = specialMenu.stream()
                .filter(dish -> dish.getCalories() < 320)   //单线程
                .collect(toList());
        // use takeWhile JDk 9
        List<Dish> slicedMenu1
                = specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() < 320) //切片并发
                .collect(toList());
        // use dropWhile
        List<Dish> slicedMenu2
                = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320) //切片并发，takeWhile的否操作
                .collect(toList());

        //2.截断流
        List<Dish> dishes = specialMenu
                .stream()
                .filter(dish -> dish.getCalories() > 300)
                .limit(3)  // mySQL and HANA: limit, sqlServer: top, ABAP: up to n rows
                .collect(toList());
        //3.跳过元素
        List<Dish> dishes_skip = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2) // Jump
                .collect(toList());

        //映射
        //1.对流中每个元素应用函数
        List<String> dishNames = menu.stream()
                .map(Dish::getName)
                .collect(toList());

        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
        //2.流的扁平化
        List<String> words = Arrays.asList("Modern", "Java", "In", "Action");
        List<String[]> uni = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(toList());

        List<String> uniqueCharacters_flat =
                words.stream()
                        .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());

        //查找和匹配
        //1.anyMatch
        if(menu.stream().anyMatch(Dish::isVegetarian)){
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }
        //2.allMatch
        boolean isHealthy = menu.stream()
                .allMatch(dish -> dish.getCalories() < 1000);
        //3.noneMatch
        boolean isHealthy_none = menu.stream()
                .noneMatch(dish -> dish.getCalories() >= 1000);
        //4.findAny
        Optional<Dish> dish =
                menu.stream()
                        .filter(Dish::isVegetarian)
                        .findAny();
        //5.findFirst
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree =
                someNumbers.stream() //顺序操作，禁用parallel
                        .map(n -> n * n)
                        .filter(n -> n % 3 == 0)
                        .findFirst();



        //归约
        //1.元素求和
        // no reduce
        int sum = 0;
        for (int x: someNumbers) {
            sum += x;
        }
        // reduce
        int sum_reduce_methodRef = someNumbers.stream().reduce(0, Integer::sum);
        int sum_reduce = someNumbers.parallelStream().reduce(0, (a, b) -> a + b);
        // no reduce vs reduce
        // 小数据reduce无优势，大数据reduce可并发
    }
}