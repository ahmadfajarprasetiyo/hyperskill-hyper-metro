import java.lang.reflect.Field;

final class AccountUtils {

    private AccountUtils() { }

    public static void increaseBalance(Account account, long amount) {
        // write your code here
        Class accountClass = account.getClass();
        Field[] fields = accountClass.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("balance")) {
                    Long balance = field.getLong(account);
                    balance = balance + amount;

                    field.set(account, balance);
                }
            }
        } catch (Exception e) {

        }




    }
}
