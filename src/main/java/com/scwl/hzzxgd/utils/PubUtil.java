package com.scwl.hzzxgd.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

public class PubUtil {
    public static boolean isNotEmpty(Object src) {
        return !isEmpty(src);
    }
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
//        if (value instanceof JSONNull)
//            return true;

        if ((value instanceof String)) {
            if (((String) value).trim().length() <= 0) {
                return true;
            } else if (((String) value).trim().toLowerCase().equals("null")) {
                return true;
            } else {
                return false;
            }
        }

        if ((value instanceof Object[]) && (((Object[]) value).length <= 0)) {
            return true;
        }
        // 判断数组中的值是否全部为空null.
        if (value instanceof Object[]) {
            Object[] t = (Object[]) value;
            for (int i = 0; i < t.length; i++) {
                if (t[i] != null) {
                    return false;
                }
            }
            return true;
        }
        if ((value instanceof Collection) && ((Collection) value).size() <= 0) {
            return true;
        }
        if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0) {
            return true;
        }
        if ((value instanceof Map) && ((Map) value).size() <= 0) {
            return true;
        }
        if ((value instanceof StringBuffer) && ((StringBuffer) value).length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为数值
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 删除最后一个字符
     *
     * @param str
     * @return
     */
    public static String delLastChar(String str) {
        if (str == null) {
            return str;
        }

        return str.length() > 0 ? str.substring(0, str.length() - 1) : str;
    }

    public static String ListToString(List<Integer> openBalls) {
        StringBuffer buffer = new StringBuffer();
        if (isEmpty(openBalls)) {
            return "";
        } else {
            openBalls.forEach(aLong -> {
                buffer.append(aLong);
                buffer.append(",");
            });
            return delLastChar(buffer.toString());
        }
    }

    /**
     * List 转成字符串
     *
     * @param list
     * @return
     */
    public static String strListToString(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        if (isEmpty(list)) {
            return "";
        } else {
            list.forEach(a -> {
                buffer.append(a);
                buffer.append(",");
            });
            return delLastChar(buffer.toString());
        }
    }

    /**
     * "1,2,3,4" 这样的字串转成List
     *
     * @param strValue
     * @return
     */
    public static List<Integer> strSplitToIntegerList(String strValue) {
        String[] split = strValue.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            list.add(Integer.valueOf(s));
        }

        return list;
    }

    /**
     * "1,2,3,4" 这样的字串转成List<String>
     *
     * @param strValue
     * @return
     */
    public static List<String> strSplitToStringList(String strValue) {
        String[] split = strValue.split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            list.add(String.valueOf(s));
        }

        return list;
    }

    /**
     * 判断是否为true
     *
     * @param value
     * @return
     */
    public static boolean isTrue(Object value) {
        if (isEmpty(value))
            return false;

        if (value instanceof Integer) {
            return ((Integer) value).intValue() == 1;
        }

        if (value instanceof String) {
            if (((String) value).toUpperCase().equals("TRUE") || ((String) value).equals("1")) {
                return true;
            }
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        return false;
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);   // (int) 强转 ,不进行四舍五入
    }

    public static BigDecimal random(BigDecimal min, BigDecimal max) {
        return max.subtract(min).multiply(new BigDecimal(Math.random())).add(min);
    }

    /**
     * 字符串 转 list
     *
     * @param srcStr
     * @param splitStr
     * @return
     */
    public static List<String> strToList(String srcStr, String splitStr) {
        List<String> strList = new ArrayList<>();
        if (PubUtil.isNotEmpty(srcStr)) {
            String[] ids = srcStr.split(splitStr);
            for (String id : ids) {
                strList.add(id);
            }
        }
        return strList;
    }

    /**
     * 转成sql
     * 例： list a,b,c   TO  'a','b','c'
     *
     * @param strList
     * @return
     */
    public static String toSqlStr(List<String> strList) {
        StringBuffer buf = new StringBuffer();
        strList.forEach(item -> {
            buf.append("'").append(item).append("'").append(",");
        });

        return delLastChar(buf.toString());
    }

    /**
     * 转成定长的数字中
     *
     * @param no   数字
     * @param size
     * @return
     */

    public static String getFixedSizeNum(int no, int size) {
        return getFixedSizeNum(no, size, "0");
    }

    /**
     * 转成定长的数字中
     *
     * @param no     数字
     * @param size
     * @param preStr 前置字符
     * @return
     */
    private static String getFixedSizeNum(int no, int size, String preStr) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < size; j++) {
            buf.append(preStr);
        }
        DecimalFormat decimalFormat = new DecimalFormat(buf.toString()); //"00000000"
        return decimalFormat.format(no);
    }

    public static Integer getFixedSizeNum9(int size) {
        return Integer.valueOf(getFixedSizeNum(9, size - 1, "9"));
    }

    public static String getUnionString(Collection<String> strCols, String unionChar, String appendChar) {
        if (PubUtil.isEmpty(strCols))
            return null;
        StringBuffer ret = new StringBuffer();
        int i = 0;
        for (String str : strCols) {
            if (PubUtil.isEmpty(str))
                continue;

            if (i != 0)
                ret.append(unionChar);
            ret.append(appendChar + str + appendChar);
            i++;
        }
        return ret.toString();
    }


    /**
     * 安全比较
     */
    public static int safeCompare(BigDecimal d1, BigDecimal d2) {
        d1 = (d1 == null) ? new BigDecimal(0) : d1;
        d2 = (d2 == null) ? new BigDecimal(0) : d2;
        return d1.compareTo(d2);
    }

    public static int safeCompare(String d1, String d2) {
        d1 = (d1 == null) ? "" : d1;
        d2 = (d2 == null) ? "" : d2;
        return d1.compareTo(d2);
    }

    /**
     * 查找某个字符串是否在数组中
     *
     * @param strs
     * @param key
     * @return
     */
    public static boolean find(String[] strs, String key) {
        return find(strs, key, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.equals(o2) == true ? 0 : -1;
            }
        });
    }

    /**
     * 查找某个数字是否在数组中
     *
     * @param intnumbers
     * @param key
     * @return
     */
    public static boolean find(int[] intnumbers, int key) {
        if (intnumbers != null && intnumbers.length > 0) {
            for (int obj : intnumbers) {
                if (obj == key) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找某对象是否在对象数组中
     *
     * @param objs
     * @param key
     * @param comparator
     * @return
     */
    public static boolean find(Object[] objs, Object key, Comparator comparator) {
        if (objs != null && objs.length > 0 && key != null) {
            for (Object obj : objs) {
                if (comparator.compare(obj, key) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否零值或空值
     * 零值或空值时反回 true
     */
    public static boolean isZeroOrNull(BigDecimal d1) {
        if (d1 == null) return true;
        if (d1 != null && d1.compareTo(BigDecimal.ZERO) == 0)
            return true;
        return false;
    }

    /**
     * 判断是否零值
     */
    public static boolean isZero(BigDecimal d1) {
        if (d1 != null && d1.compareTo(BigDecimal.ZERO) == 0)
            return true;
        return false;
    }

    public static boolean isTrue(Boolean value) {
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }
}
