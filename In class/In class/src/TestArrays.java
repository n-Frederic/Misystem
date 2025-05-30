public class TestArrays {
    public static void main(String[] args) {
        //定义两个一维数组
        int[] array1;
        int[] array2;
        array1 = new int[]{2, 3, 5, 7, 11, 13, 17, 19};
        //调用printArray方法打印数组
        printArray(array1);
        //array2指向array1的地址
        array2 = array1;
        for (int i = 0; i < array2.length; i += 2) {
            array2[i] = i;
        }
        //再次打印array1，其值随array2修改而变化
        printArray(array1);

        //定义一个二维数组
        int[][] matrix = new int[5][];
        //初始化并填充数组

    }

    public static void printArray(int[] array) {
        System.out.print('<');
        for (int i = 0; i < array.length; i++) {
            // print an element
            System.out.print(array[i]);
            // print a comma delimiter if not the last element
            if ((i + 1) < array.length) {
                System.out.print(", ");
            }
        }
        System.out.print('>' + "\n");
    }
}
