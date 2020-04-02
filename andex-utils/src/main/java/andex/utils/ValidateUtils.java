package andex.utils;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class ValidateUtils {


    /**
     * 判断手机号
     * 以 1 开始的 11 位数字
     *
     * @param number
     * @return
     */
    public static boolean isMobileNumber(String number) {
        return !StringUtils.isEmpty(number) && number.length() == 11 && number.trim().startsWith("1");
    }


    /**
     * 验证邮箱格式是否正确
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

}
