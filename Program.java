import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {
    static Scanner in = new Scanner(System.in);
    static int n = in.nextInt();
    static int m = in.nextInt();

    static Item[] items = new Item[n];

    static Backpack[][] bp = new Backpack[n + 1][m + 1];

    public static void main(String[] args) {

        int[] w = new int[n];
        int[] c = new int[n];

        for (int i = 0; i < n; i++) {
            w[i] = in.nextInt();
        }

        for (int i = 0; i < n; i++) {
            c[i] = in.nextInt();
        }

        for (int i = 0; i < n; i++) {
            items[i] = new Item(i + 1 + "", w[i], c[i]);
        }

        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < m + 1; j++) {
                if (i == 0 || j == 0) {
                    bp[i][j] = new Backpack(new Item[]{}, 0);
                } else if (i == 1) {
                    bp[1][j] = items[0].getWeight() <= j ? new Backpack(new Item[]{items[0]}, items[0].getPrice())
                            : new Backpack(new Item[]{}, 0);
                } else {
                    if (items[i - 1].getWeight() > j)
                        bp[i][j] = bp[i - 1][j];
                    else {
                        int newPrice = items[i - 1].getPrice() + bp[i - 1][j - items[i - 1].getWeight()].getPrice();
                        if (bp[i - 1][j].getPrice() > newPrice)
                            bp[i][j] = bp[i - 1][j];
                        else {
                            bp[i][j] = new Backpack(Stream.concat(Arrays.stream(new Item[]{items[i - 1]}),
                                    Arrays.stream(bp[i - 1][j - items[i - 1].getWeight()].getItems())).toArray(Item[]::new), newPrice);
                        }
                    }
                }
            }
        }

        displayResult();
    }

    public static void displayResult() {
        List<Backpack> lastColumn = Arrays.stream(bp).map(row -> row[row.length - 1]).collect(Collectors.toList());
        Backpack backpackWithMax = lastColumn.stream().max(Comparator.comparing(Backpack::getPrice)).orElse(new Backpack(null, 0));
        String result = backpackWithMax.getDescription();

        for (int j = 0; j < result.length(); j++) {
            char ch = result.charAt(j);
            if (ch == '-') break;
            if (ch != '+') {
                System.out.println(ch);
            }
        }
    }


}

class Item {
    private final String name;
    private final int weight;
    private final int price;

    public Item(String name, int weight, int price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }
}


class Backpack {
    private final Item[] items;
    private final int price;

    public Backpack(Item[] items, int price) {
        this.items = items;
        this.price = price;
    }

    public Item[] getItems() {
        return items;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return items == null ? "" : Arrays.stream(items).map(Item::getName).collect(Collectors.joining("+")) + "-" + getPrice();
    }
}
