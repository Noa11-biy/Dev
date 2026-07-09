package ToolsPackage;

public class Encoding {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static StringBuilder sb62 = new StringBuilder();

    /**
     * Encodes a long integer into a string using the ALPHABET
     * @param n The long integer to encode
     * @return The corresponding string
     */
    private static String encodeBase62(long n) {
        if (n == 0) return String.valueOf(ALPHABET.charAt(0));
        sb62.setLength(0);
        while (n > 0) {
            sb62.append(ALPHABET.charAt((int)(n % 62)));
            n /= 62;
        }
        return sb62.reverse().toString();
    }

    /**
     * Encodes a string passed as a parameter (consisting of the characters ‘0’ through ‘9’)
     * @param s The string to encode
     * @return The string encoded in base 62
     */
    public static String encode(String s){
        StringBuilder sb=new StringBuilder("");
        String ss;
        int index;
        int loop=0;
        while (s.length() > 0) {
            loop++;
            index=Math.min(18, s.length());
            ss = s.substring(0, index);
            sb.append(Encoding.encodeBase62(Long.parseLong(ss)));
            s = s.substring(index);
        }
        return sb.toString();
    }

}
