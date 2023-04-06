import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Trader {
    private final String name;
    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Trader : " + this.name + " in " + this.city;
    }

    public static void main(String[] args) {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = List.of(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        List<Transaction> tr2011 = 
                transactions.stream()
                        .filter(e -> e.getYear() == 2011)
                        .sorted(Comparator.comparing(Transaction::getValue))
                        .collect(Collectors.toList());

        List<String> collect = transactions.stream().map(e -> e.getTrader().getCity()).distinct().collect(Collectors.toList());

        List<Trader> collect1 = transactions.stream().map(Transaction::getTrader).filter(e -> "Cambridge".equals(e.getCity())).sorted(Comparator.comparing(Trader::getCity)).collect(Collectors.toList());

        List<Transaction> collect2 = transactions.stream().sorted(Comparator.comparing(e -> e.getTrader().getName())).collect(Collectors.toList());

        boolean present = transactions.parallelStream().anyMatch(e -> "Milan".equals(e.getTrader().getCity()));

        transactions.parallelStream().filter(e -> "Cambridge".equals(e.getTrader().getCity())).map(Transaction::getTrader).collect(Collectors.toList());

        transactions.parallelStream().max(Comparator.comparing(Transaction::getValue)).map(Transaction::getValue);

        int sum = transactions.stream().mapToInt(Transaction::getValue).sum();


    }
}
