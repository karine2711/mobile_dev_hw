package com.example.homework.hw1;


public class IntegerToTextUtil {

    public static String constructString(int intNumber) {
        String millions = getStringFor3DigitInt(intNumber / 1_000_000, "million ");
        String thousands = getStringFor3DigitInt(intNumber % 1_000_000 / 1000, "thousand ");
        String hundreds = getStringFor3DigitInt(intNumber % 1000);
        return millions + thousands + hundreds;
    }

    private static String getStringFor3DigitInt(int threeDigitInt, String suffix) {
        String strForInt = getStringFor3DigitInt(threeDigitInt);
        if (strForInt.isEmpty()) {
            return "";
        } else {
            return strForInt + " " + suffix;
        }
    }

    private static String getStringFor3DigitInt(int threeDigitInt) {
        if (threeDigitInt == 0 || threeDigitInt > 999) {
            return "";
        }
        int hundreds = threeDigitInt % 1000 / 100;
        int tens = threeDigitInt % 100 / 10;
        int ones = threeDigitInt % 10;
        String hundredsStr = getStrForDigit(hundreds);
        String result = hundredsStr.isEmpty() ? "" : hundredsStr + " hundred ";
        return result + getStringFor2DigitInt(tens, ones);
    }

    private static String getStringFor2DigitInt(int tens, int ones) {
        String[] teens = new String[]{"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] tensStr = new String[]{"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        if (tens != 1) {
            return tensStr[tens].isEmpty() ? getStrForDigit(ones) :
                    tensStr[tens] + " " + getStrForDigit(ones);
        }
        return teens[ones];
    }

    private static String getStrForDigit(int digit) {
        String[] digits = new String[]{"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        return digits[digit];
    }
}
