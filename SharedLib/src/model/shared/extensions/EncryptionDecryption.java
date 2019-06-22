package model.shared.extensions;

import java.io.IOException;

public final class EncryptionDecryption {

    public String EncodeDecode(String _string, int type) throws IOException {

        String _result = null;
        if (type == 1) {
            _result = new sun.misc.BASE64Encoder().encode(_string.getBytes());
        } else {
            byte[] decode = new sun.misc.BASE64Decoder().decodeBuffer(_string);
            _result = new String(decode);
        }
        return _result;
    }

   /*public static void main(String arg[]) {
        EncryptionDecryption xx = new EncryptionDecryption();
        try {
            System.out.println(xx.EncodeDecode("wael123==", 1));
        } catch (IOException e) {
        }
    }*/
    // Wael Abdeen
}
