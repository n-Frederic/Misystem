package Date;

public class Date {
    private int year;
    private int month;
    private int day;
    int[] Days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //构造函数，初始化成员变量
    public Date(int year, int month, int day) {
        if (Legal(year, month, day)) {
            this.year = year;
            this.month = month;
            this.day = day;
        } else {
            System.out.println("错误：初始化日期非法");
        }
    }

    //改变成员变量的值
    public void setDate(int year, int month, int day) {
        if (Legal(year, month, day)) {
            this.year = year;
            this.month = month;
            this.day = day;
        } else {
            System.out.println("错误：设置日期非法");
            return;
        }
    }

    //检验日期是否合法
    private boolean Legal(int y, int m, int d) {
        this.year = y;
        this.month = m;
        this.day = d;
        if (y < 0 || m < 1 || m > 12 || d < 1 || d > Days[m - 1]) {
            return false;
        } else {
            return true;
        }
    }

    //将一天添加到原始的日期中
    public void addOneDay() {
        if (Legal(year, month, day)) {
            //判断是否为闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                Days[1] = 29;  //如果是闰年，二月为29天
            }
        } else {
            return;
        }
    }

    //以日/月/年的形式显示日期
    public void display() {
        if (Legal(year, month, day)) {
            System.out.println("设置的的日期:" + day + " / " + month + " / " + year);
            System.out.println("当年二月的最后一天：" + Days[1] + " / " + 2 + " / " + year);
        } else {
            return;
        }
    }
}

