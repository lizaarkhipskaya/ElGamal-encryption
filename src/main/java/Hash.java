import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hash {
    public final static int size = 214748363;
    public  final  static BigInteger FF = new BigInteger("ffffffffffffffff",16);
    public static byte[] padding(byte[]  b){
        int padding = 8-b.length-1;
        byte[] res = new byte[8];
        for( int i = 0 ; i< b.length;i++){
            res[i] = b[i];
        }
        if(b.length<8) {
            res[b.length] = (byte) 128;
            while (padding > 0) {
                res[8 - padding] = 0;
                padding--;
            }

        }
        return res;
    }
    private static long getHash(byte[] M, long H0,boolean flagBool){
        long H = H0;
        byte[] m ;
        long h ;
        if(flagBool) {
            for (int i = 0; i < M.length; i += 8) {//Check right order in m
                    m = Arrays.copyOfRange(M, i, i + 8);
                for (byte mm :
                        m) {
                }
                h = H;
                H = iterationFunctionG(ByteBuffer.wrap(m).getLong(), h);
            }
        }else{
            for (int i = 0; i < M.length; i += 8) {//Check right order in m

                    if (i + 8 < M.length) {
                        m = Arrays.copyOfRange(M, i, i + 8);
                    } else {
                        m = padding(Arrays.copyOfRange(M, i,
                                M.length));
                    }
                h = H;
                H = iterationFunctionG(ByteBuffer.wrap(m).getLong(), h);
            }
        }
        return  H;
    }
    public static long getHashFromFile(RandomAccessFile raf) throws IOException {
        long pos = 0l;
        byte[] buffer = new byte[size];
        long h = 0l;
        if(raf.length()==0){
            //System.out.println("empty file");
            h = Hash.getHashFromEmptyFile();
        }
        else {
            int i = 0;
            while (true) {
                if ((raf.length() - pos) <= size) {
                    raf.seek(pos);
                    buffer = new byte[new Long(raf.length() - pos).intValue()];
                    raf.read(buffer);
                    h = Hash.getHash(buffer, h, false);
                    break;
                }
                raf.seek(pos);
                raf.read(buffer);
                h = Hash.getHash(buffer, h, true);
                pos += size;
                i++;
            }
        }
        return h;
    }
    private static long getHashFromEmptyFile(){
        byte[] m = {(byte)128,0,0,0,0,0,0,0};
        long H = iterationFunctionG(ByteBuffer.wrap(m).getLong(),0l);
        return H;
    }
    private static long iterationFunctionG(long m, long h) {
        return Encryption.GetCrypto(m,h)^m;
    }
}
