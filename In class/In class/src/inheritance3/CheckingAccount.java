package inheritance3;

public class CheckingAccount extends Account {
    double overdraftProtection;
    private SavingsAccount protectedBy;

    public void setProtectedBy(SavingsAccount protectedBy) {
        this.protectedBy = protectedBy;
    }

    public SavingsAccount getProtectedBy() {
        return protectedBy;
    }

    //构造器
    public CheckingAccount(double balance) {
        super(balance);
    }

    public CheckingAccount(double balance, SavingsAccount savingsAccount) {
        super(balance);
        this.protectedBy = savingsAccount; // 设置透支保护账户
    }

    //取钱
    public boolean withdraw(double amt) {
        //活期余额充足，取款成功
        if (amt <= balance) {
            balance -= amt;
            return true;
        } else {
            // 判断是否有透支保护
            if (protectedBy != null) {
                double overdraftAmount = amt - balance; // 需要透支的金额
                if (protectedBy.getBalance() >= overdraftAmount) {
                    // 从定期中扣款
                    protectedBy.withdraw(overdraftAmount);
                    balance = 0; // 检查账户余额设为0
                    return true;
                } else {
                    // 定期存款余额不足，取款失败
                    return false;
                }
            } else {
                // 没有透支保护，取款失败
                return false;
            }
        }
    }
}
