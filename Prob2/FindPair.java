import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Pair<T> {
    T a;
    T b;
    public Pair(final T a, final T b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return a;
    }

    public void setA(final T a) {
        this.a = a;
    }

    public T getB() {
        return b;
    }

    public void setB(final T b) {
        this.b = b;
    }
}

class Item implements Comparable<Item> {
    String name;
    Integer price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    @Override
    public int compareTo(Item i){
        return price.compareTo(i.price);
    }

}

public class FindPair {

    private static Pair<Item> findPair(final List<Item> items, final int targetPrice){
        /* Crux of the algorithm 
        Sort the list (O(nlogn)).
        Have two pointers - one at 0 and other at the length of the list.
        If the sum of the two values is > target: decrease j
        If the sum is less: save it as an option and and increase i

        ************
        Runtime to Sort - O(nlog(n))
        Finding the items - O(n)
        Total Runtime is O(nlog(n)) 
        *************
        */
        //Sort the items
        Collections.sort(items);
        printList(items);
        Pair<Item> smallerPair = null;
        int i = 0;
        int j = items.size()-1;
        while (i < j){
            if (items.get(i).price + items.get(j).price == targetPrice){
                return new Pair<Item>(items.get(i), items.get(j));
            }
            else if (items.get(i).price + items.get(j).price < targetPrice){
                smallerPair = new Pair<Item>(items.get(i), items.get(j));
                i += 1;
            }
            else{
                j -= 1;
            }
        }
        return smallerPair;
    }

    private static void printList(List<Item> items){
        for (Item i: items){
            System.out.println(i.name + ":" + i.price);
        }
    }

    public static void main(String[] args) throws IOException {
        assert args[0] != null;
        assert args[1] != null;
        final String pricesFileName = args[0];
        final Integer targetPrice = Integer.parseInt(args[1]);
        Stream<String> lines = Files.lines(Paths.get(pricesFileName));
        List<String> content = lines.collect(Collectors.toList());
        List<Item> items = new ArrayList<>();
        for (String l: content){
            String[] split = l.split(",");
            items.add(new Item(split[0].strip(), Integer.parseInt(split[1].strip())));
        }
        lines.close();
        Pair<Item> p = findPair(items, targetPrice);
        if (p == null){
            System.out.println("Not Possible");
        }
        else {
            System.out.println(p.getA().name + " " + p.getA().price + ", " + p.getB().name + " " + p.getB().price);
        }
    }
}