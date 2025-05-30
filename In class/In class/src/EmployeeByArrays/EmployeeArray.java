/**
 * This class contains methods to process arrays of {@link Employee} objects.
 *
 * @author author name
 * @version 1.0.0
 * @see Employee
 */
package EmployeeByArrays;

public class EmployeeArray {

    /**
     * Creates an array with three objects of class {@link Employee}.
     * <p>
     * The first element of the array is the object
     * <code>first</code>, the second element of the array is
     * the object <code>second</code>, and  the third element of the
     * array is the object <code>third</code>.
     * </p>
     *
     * @param first  a {@link Employee} object.
     * @param second a {@link Employee} object.
     * @param third  a {@link Employee} object.
     * @return an array with the objects <code>first</code>,
     * <code>second</code>, and <code>third</code>.
     */
    public static Employee[] makeArray(Employee first, Employee second,
                                       Employee third) {

        Employee[] em = new Employee[]{first, second, third};

        return em;
    }

    /**
     * Creates a new array from the specified array of {@link Employee} objects.
     * <p>
     * The elements in the new array have the same order as those in
     * the specified array.
     * </p>
     *
     * @param array an array that contains objects of class {@link Employee}.
     * @return a <i>new</i> array of the objects in the specified array.
     */
    public static Employee[] copyArray(Employee[] array) {
        //如果数组为空，返回null
        if (array == null) {
            return null;
        }
        //创建新的数组对象
        Employee[] newArray = new Employee[array.length];
        //复制粘贴并返回新数组
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;

    }


    /**
     * Returns the {@link Employee} object with the specified ID.
     *
     * @param array an array that contains objects of class {@link Employee}.
     * @param id    an employee ID.
     * @return The {@link Employee} object with the specifed
     * ID. Returns <code>null</code> if there are no employees
     * in the specified array with the specifed ID.
     */
    public static Employee getEmployee(Employee[] array, int id) {

        if (array == null) {
            return null;
        }
        //如果数组不为空，查找id相同的对象
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].getId() == id) {
                return array[i];
            }
        }
        //如果最终没找到，返回null
        return null;
    }

    /**
     * Returns the number of employees whose salary is higher than the specified
     * dollar amount.
     *
     * @param array  an array that contains objects of class {@link Employee}.
     * @param amount a dollar amount.
     * @return the number of employees whose salary is higher than the
     * specified dollar amount.
     */
    public static int countHigherSalaries(Employee[] array, double amount) {

        if (array == null) {
            return 0;
        }
        int num = 0;//计数器
        //如果工资比固定值多，计数器加一。最终返回人数。
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].getSalary() > amount) {
                num++;
            }
        }
        return num;
    }

    /**
     * Returns the sum of the salaries of the employees in the specified
     * array.
     *
     * @param array an array that contains objects of class {@link Employee}.
     * @return the sum of the salaries of the employees in the specified
     * array.
     */
    public static double sumSalaries(Employee[] array) {

        double consumption = 0;
        //遍历累加工资，返回工资总值
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                consumption += array[i].getSalary();
            }
        }
        return consumption;

    }

    /**
     * Obtains the highest salary in the specified array.
     *
     * @param array an array that contains objects of class {@link Employee}.
     * @return the highest salary in the specified array.
     */
    public static double getHighestSalary(Employee[] array) {

        double highest = 0;
        //遍历比较
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].getSalary() > highest) {
                highest = array[i].getSalary();
            }
        }
        return highest;

    }

    /**
     * Increases the salary of every employee in the specified array by the
     * specified amount.
     *
     * @param array an array that contains objects of class {@link Employee}.
     */
    public static void increaseAll(Employee[] array, double amount) {

        //遍历数组，在每个人原有的工资上加上amount
        for (int i = 0; i < array.length; i++) {
            array[i].setSalary(array[i].getSalary() + amount);
        }

    }

    /**
     * Returns a string representation of the specified
     * {@link Employee} array.
     * <p>
     * Uses the method <code>toString</code> in class <code>Employee</code>
     * to obtain the string representation of each object in the array.
     * </p>
     * A new line character ( \n ) separates the string representations
     * of each <code>Employee</code> object. For example:
     * </p>
     * <pre>
     * Employee[102,Mary Jones,68250.0]\n
     * Employee[101,Joe Smith,36000.0]\n
     * Employee[103,Richard Wong,92175.0]
     * </pre>
     * <p>
     * Note that the string returned does <i>not</i> end with a new line
     * character (\n).
     * </p>
     * <p>
     * This method assumes that every element in the specified array
     * contains a valid reference to an <code>Employee</code> object.
     * </p>
     *
     * @param array an array that contains objects of class {@link Employee}.
     * @return the string representation of the specified array
     */
    public static String displayAll(Employee[] array) {

        if (array == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        // 如果每个员工不为空，追加每个员工的字符串表示和换行符
        for (Employee employee : array) {
            if (employee != null) {
                sb.append(employee).append("\n");
            }
        }

        // 如果StringBuilder不为空，则移除最后一个换行符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}