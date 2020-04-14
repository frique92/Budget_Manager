import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] tempArr = scanner.nextLine().split(" ");

        int[] array = new int[tempArr.length];

        for (int i = 0; i < tempArr.length; i++) {
            array[i] = Integer.parseInt(tempArr[i]);
        }

        int count = 0;

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] < array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    count++;
                }
            }
        }

        System.out.println(count);
    }
}