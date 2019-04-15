import java.io.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

public class Checking {
    static int size = 214748363;
    public static void main(String[] args) throws IOException {
        System.out.println(LocalDateTime.now());
        System.out.println("Enter file path for get sign: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        System.out.println(path);
        File file = new File(path);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось получить доступ к файлу");
        }
        System.out.println(LocalDateTime.now());

        ElGamal elGamal = new ElGamal();
        Sign sign = elGamal.getSign(raf);
        System.out.println(checkSign());

    }


   public static boolean checkSign(){
        ElGamal elGamal = new ElGamal();
        Sign sign = new Sign();
       System.out.println("Enter file path for check sign(path to file will be hashed): ");
       Scanner scanner = new Scanner(System.in);
       String path = scanner.nextLine();
       System.out.println(path);
       File file = new File(path);
       long pos = 0l;
       byte[] buffer = new byte[size];
       long h = 0l;
       RandomAccessFile raf = null;
       try {
           raf = new RandomAccessFile(file, "rw");
       } catch (FileNotFoundException e) {
           System.out.println("Не удалось получить доступ к файлу");
       }
       long H1 = 0;
       try {
           H1 = Hash.getHashFromFile(raf);
       } catch (IOException e) {
           e.printStackTrace();
       }
       System.out.println("Enter Y:");
        elGamal.y = new BigInteger(scanner.nextLine(),16);
       System.out.println("Enter K:");
        sign.setK(new BigInteger(scanner.nextLine(),16));
       System.out.println("Enter S:");
        sign.setS(new BigInteger(scanner.nextLine(),16));
        BigInteger left = elGamal.a.modPow(sign.getK(),elGamal.p).multiply(elGamal.y.modPow(ElGamal.getBigIntegerFromHash(H1).multiply(sign.getS().multiply(elGamal.a.modPow(sign.getK(),elGamal.p))),elGamal.p)).mod(elGamal.p);
        BigInteger right = sign.getS().mod(elGamal.p);
        System.out.println(left);
        System.out.println(right);

        return left.equals(right);
    }

}
