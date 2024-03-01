package com.dailystudy.swinglab.service.framework.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtil
{
    // 이름 가운데 글자 마스킹
    public static String nameMasking(String name)
    {
        if(name == null) {
            return null;
        }
        // 한글만 (영어, 숫자 포함 이름은 제외)
        String regex = "(^[가-힣]+)$";

        Matcher matcher = Pattern.compile(regex).matcher(name);
        if (matcher.find())
        {
            int length = name.length();

            String middleMask = "";
            if (length > 2)
            {
                middleMask = name.substring(1, length - 1);
            } else
            { // 이름이 외자
                middleMask = name.substring(1, length);
            }

            String dot = "";
            for (int i = 0; i < middleMask.length(); i++)
            {
                dot += "*";
            }

            if (length > 2)
            {
                return name.substring(0, 1) + middleMask.replace(middleMask, dot) + name.substring(length - 1, length);
            } else
            { // 이름이 외자 마스킹 리턴
                return name.substring(0, 1) + middleMask.replace(middleMask, dot);
            }
        }
        return name;
    }

    // 휴대폰번호 마스킹(가운데 숫자 4자리 마스킹)
    public static String phoneNoMasking(String phoneNo)
    {
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

        Matcher matcher = Pattern.compile(regex).matcher(phoneNo);
        if (matcher.find())
        {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return phoneNo.replace(target, String.valueOf(c));
        }
        return phoneNo;
    }

    // 이메일 마스킹(앞3자리 이후 '@'전까지 마스킹)
    public static String emailMasking(String email)
    {
//        return RegExUtils.replaceAll(email, "(?<=.{3}).(?=.*@)", "*");
        
//        if(email == null) {
//            return null;
//        }
//        String regex = "\\b(\\s+)+@(\\s+.\\s+)";
//
//        Matcher matcher = Pattern.compile(regex).matcher(email);
//        if (matcher.find())
//        {
//            String target = matcher.group(1);
//            int length = target.length();
//            if (length > 3)
//            {
//                char[] c = new char[length - 3];
//                Arrays.fill(c, '*');
//
//                return email.replace(target, target.substring(0, 3) + String.valueOf(c));
//            }
//        }
//        return email;
        
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
           String id = matcher.group(1); // 마스킹 처리할 부분인 userId
           /*
           * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
           * 세글자인 경우 뒤 두글자만 마스킹, 
           * 세글자 미만인 경우 모두 마스킹 처리
           */
           int length = id.length();
           if (length < 3) {
              char[] c = new char[length];
              Arrays.fill(c, '*');
              return email.replace(id, String.valueOf(c));
           } else if (length == 3) {
              return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
           } else {
              return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
           }
        }
        return email;
    }

    // 계좌번호 마스킹(뒤 5자리)
    public static String bankAccountNoMasking(String bankAccountNo)
    {
        if(bankAccountNo == null) {
            return null;
        }
        
        // 계좌번호는 숫자만 파악하므로
        String regex = "(^[0-9]+)$";

        Matcher matcher = Pattern.compile(regex).matcher(bankAccountNo);
        if (matcher.find())
        {
            int length = bankAccountNo.length();
            if (length > 5)
            {
                char[] c = new char[5];
                Arrays.fill(c, '*');

                return bankAccountNo.replace(bankAccountNo, bankAccountNo.substring(0, length - 5) + String.valueOf(c));
            }
        }
        return bankAccountNo;
    }

    // 카드번호 가운데 8자리 마스킹
    public static String cardNoMasking(String cardNo)
    {
        if(cardNo == null) {
            return null;
        }
        
        // 카드번호 16자리 또는 15자리 '-'포함/미포함 상관없음
        String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";

        Matcher matcher = Pattern.compile(regex).matcher(cardNo);
        if (matcher.find())
        {
            String target = matcher.group(2) + matcher.group(3);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return cardNo.replace(target, String.valueOf(c));
        }
        return cardNo;
    }

    // 주소 마스킹(신주소, 구주소, 도로명 주소 숫자만 전부 마스킹)
    public static String addressMasking(String address)
    {
        if(address == null) {
            return null;
        }
        
        // 신(구)주소, 도로명 주소
        String regex =
            "(([가-힣]+(\\d{1,5}|\\d{1,5}(,|.)\\d{1,5}|)+(읍|면|동|가|리))(^구|)((\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})(가|리|)|))([ ](산(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))|)|";
        String newRegx = "(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))";

        Matcher matcher = Pattern.compile(regex).matcher(address);
        Matcher newMatcher = Pattern.compile(newRegx).matcher(address);
        if (matcher.find())
        {
            return address.replaceAll("[0-9]", "*");
        } else if (newMatcher.find())
        {
            return address.replaceAll("[0-9]", "*");
        }
        return address;
    }
}
