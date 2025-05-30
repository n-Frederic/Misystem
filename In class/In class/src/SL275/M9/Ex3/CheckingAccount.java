package SL275.M9.Ex3;

public class CheckingAccount extends Account {
    double overdraftProtection;

    //������
    public CheckingAccount(double balance) {
        super(balance);
        this.balance = balance;
    }

    //��һ��������
    public CheckingAccount(double balance, double protect) {
        super(balance);
        this.balance = balance;
        this.overdraftProtection = protect;
    }

    //ȡǮ
    public void withdraw(double amt) throws OverdraftException {
        //�����㣬ȡ��ɹ�
        if (amt <= balance) {
            balance -= amt;
        } else {
            //���������
            if (overdraftProtection > 0) {
                //�ж��Ƿ񳬹�����
                if (overdraftProtection + balance >= amt) {
                    overdraftProtection -= (amt - balance);
                    balance = 0;
                } else {
                    double deficit;
                    deficit = amt - balance;
                    throw new OverdraftException("Insufficient funds for overdraft protection", deficit);
                }
            //�����׳����������쳣
            } else {
                throw new OverdraftException("No overdraft protection", amt - balance);
            }
        }
    }
}
