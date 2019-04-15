import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class ElGamal {
    public static final BigInteger p = new BigInteger("AF5228967057FE1CB84B92511BE89A47",16);
    public static final BigInteger q  = new BigInteger("57A9144B382BFF0E5C25C9288DF44D23",16);
    public static final BigInteger a  = new BigInteger("9E93A4096E5416CED0242228014B67B5",16);
    private BigInteger U;
    private BigInteger Z;
    private BigInteger x;
    private BigInteger g;
    public BigInteger y;
    public BigInteger H;
    public long h;
    private Random random = new Random();
    public static final BigInteger TWO = new BigInteger("2");
    public ElGamal(){
        do {
            x = new BigInteger(random.nextInt(128), random);
        }while (x.compareTo(p)>0);
        y = a.modPow(x,p);
    }

    public BigInteger getU() {
        return U;
    }

    public BigInteger getZ() {
        return Z;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getG() {
        return g;
    }

    public Sign getSign(RandomAccessFile raf) throws IOException {
        U = new BigInteger(100,random);
        Z = a.modPow(U,p);
        h = Hash.getHashFromFile(raf);
        System.out.println("Y = "+y.toString(16));
        System.out.println("H = "+Long.toHexString(h));
        H = getBigIntegerFromHash(h);
        BigInteger K = ((U.subtract(H.multiply(Z).multiply(x))).divide(TWO)).mod(q);
        System.out.println("K = "+K.toString(16));
        g = ((U.add(H.multiply(Z).multiply(x))).divide(TWO)).mod(q);
        BigInteger S = a.modPow(g,p);
        System.out.println("S = "+S.toString(16));
        return new Sign(K,S);//!!!!
    }

    public static BigInteger getBigIntegerFromHash(long h){
        byte[] bytes = ByteBuffer.allocate(8).putLong(h).array();
        long h2 = ByteBuffer.wrap(new byte[]{bytes[7],bytes[6],bytes[5],bytes[4],bytes[3],bytes[2],bytes[1],bytes[0]}).getLong();
        //String num = getStringByte(bytes[7])+getStringByte(bytes[6])+getStringByte(bytes[5])+getStringByte(bytes[4])+getStringByte(bytes[3])+getStringByte(bytes[2])+getStringByte(bytes[1])+getStringByte(bytes[0])+"00ffffffffffffff00";
        BigInteger result = new BigInteger(Long.toHexString(h2)+"00ffffffffffffff00",16);
        return result;
    }
    public static String getStringByte(byte b){
        return Integer.toHexString(Byte.toUnsignedInt(b));
    }
}
