import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexems {
    // количество цифр в строке
    public static int StringNumbersCounter(String str) {
        int counter = 0;
        Pattern pattern = Pattern.compile("[-+]?//d+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find())
            counter += matcher.group().length();
        return counter;
    }

    // количество латинских букв в строке
    public static int StringLettersCounter(String str) {
        int counter = 0;
        Pattern pattern = Pattern.compile("[-+]?//w+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find())
            counter += matcher.group().length();
        return counter;
    }

    public static boolean isPalyndrome(String str) {
        StringBuffer tmp = new StringBuffer(str).reverse();
        return str.equals(tmp.toString());
    }

    public static void main(String[] args) {
        // Запрашивает две строки и целое число P- для поиска.
        Scanner scanner = new Scanner(System.in);
        //Первая строка содержит лексемы, состоящие из любых символов, которые можно ввести с
        //клавиатуры
        System.out.println("Input string:");
        String input = scanner.nextLine();
        //Вторая строка содержит символы разделители
        System.out.println("Input determinators:");
        String determinators = scanner.nextLine();
        System.out.println("Input number to find:");
        String number_to_find = scanner.nextLine();
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        ArrayList<String> lexemas = new ArrayList<String>();
        ArrayList<String> lexemas_with_deleted_substr = new ArrayList<String>();
        ArrayList<String> non_palyndrom_lexemas = new ArrayList<String>();
        // если только всего один разделитель и по одному разделителю между лексемами.
        if (determinators.length() == 1) {
            String[] tmp = input.split(determinators);
            for (int i = 0; i < tmp.length; i++) {
                strings.add(tmp[i]);
            }
        } else {
            // если между лексемами стоит >1 разделителя и разные разделители).
            StringTokenizer tokenizer = new StringTokenizer(input, determinators);
            while (tokenizer.hasMoreTokens()) {
                strings.add(tokenizer.nextToken());
            }
        }
        // определить в ней целые числа 8-й с\с.
        for (int i = 0; i < strings.size(); i++) {

            try {
                int tmp = Integer.parseInt(strings.get(i).trim(), 8);
                //Числа записать в новый отдельные массив.
                numbers.add(Integer.parseInt(strings.get(i).trim()));
            } catch (NumberFormatException e) {
                lexemas.add(strings.get(i).trim());
            }
        }
        //  Среди лексем не являющихся числами, найти лексемы не являющиеся палиндромами.
        for (int i = 0; i < lexemas.size(); i++) {
            if (!isPalyndrome(lexemas.get(i))) {
                non_palyndrom_lexemas.add(lexemas.get(i));
            }
        }

        // Добавить в строку случайное число после числа Р или в середину строки(если нет P)
        StringBuffer tmp = new StringBuffer(input);
        if (input.contains(number_to_find))
            tmp.insert(input.indexOf(number_to_find), Math.random());
        else tmp.insert(input.length() / 2, Math.random());


//  Подстроку (с самой маленькой длиной), начинающуюся цифрой - удалить из строки
        int min_length = Integer.MAX_VALUE;
        int min_index = lexemas.size();
        for (int i = 0; i < lexemas.size(); i++) {
            Pattern pattern = Pattern.compile("^[0-9]");
            Matcher matcher = pattern.matcher(lexemas.get(i));
            while(matcher.find()){
                if (min_length > lexemas.get(i).length()){
                    min_length = lexemas.get(i).length();
                    min_index = i;
                }
            }
        }
        for (int i = 0; i < lexemas.size(); i++) {
            if (i != min_index){
                lexemas_with_deleted_substr.add(lexemas.get(i));
            }
        }


        // Отсортировать лексемы, используя лямбда-выражение:
        lexemas.sort((String lhs, String rhs) -> {
            // 1) По наличию в палиндроме среднего одиночного символа
            // (если оба числа палиндромы, то выше тот, длина которого нечетная
            if (isPalyndrome(lhs) && isPalyndrome(rhs)) {
                if (lhs.length() % 2 != 0)
                    return 1;
                else if (rhs.length() % 2 != 0)
                    return -1;
                else return 0;
            }
            // 2) По длине строки
            if (lhs.length() != rhs.length()) {
                return rhs.length() - lhs.length();
            }
            // 3) По первому элементу строки
            if (lhs.charAt(0) != rhs.charAt(0)) {
                return rhs.charAt(0) - lhs.charAt(0);
            }
            // 4) По последнему элементу строки
            if (lhs.charAt(lhs.length() - 1) != rhs.charAt(lhs.length() - 1)) {
                return rhs.charAt(lhs.length() - 1) - lhs.charAt(lhs.length() - 1);
            }
            // 5) Количеству цифр
            if (StringNumbersCounter(lhs) != StringNumbersCounter(rhs)) {
                return StringNumbersCounter(rhs) - StringNumbersCounter(lhs);
            }
            // 6) По количеству латинских символов
            else return StringLettersCounter(rhs) - StringLettersCounter(lhs);
        });

        System.out.println("\n8 numeric system numbers:");
        NumberFormat formatter = NumberFormat.getNumberInstance();
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(formatter.format(numbers.get(i)));
        }

        System.out.println("\nnon palyndrome lexemas:");
        for (int i = 0; i < non_palyndrom_lexemas.size(); i++) {
            System.out.print(non_palyndrom_lexemas.get(i) + " ");
        }

        System.out.println("\n\nlexemas with deleted minimal substring:");
        for (int i = 0; i < lexemas_with_deleted_substr.size(); i++) {
            System.out.print(lexemas_with_deleted_substr.get(i) + " ");
        }

        System.out.println("\n\nnumber P position in input string:");
        // Найти число Р(если есть, то должно совпадать с лексемой), вывести позицию в изначальной строке.
        System.out.println(input.indexOf(number_to_find));

        System.out.println("\ninput string w/ inserted random number after P:");
        System.out.println(tmp.toString());

        System.out.println("\nsorted lexemas:");
        for (int i = 0; i < lexemas.size(); i++) {
            System.out.print(lexemas.get(i) + " ");
        }
    }
}
