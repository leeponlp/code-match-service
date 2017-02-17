package com.qybx.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理工具类
 * 
 * @author leepon
 *
 */

public class StringTool {

	private final static String EMPTY = "";

	/**
	 * 清除search字符
	 * 
	 */
	public static String clear(String src, String search) {
		if (src == null)
			return null;
		return StringUtils.replace(src, search, EMPTY);
	}

	/**
	 * 清除空格" "
	 *
	 **/
	public static String clearBlank(String src) {
		if (src == null)
			return null;
		return clear(src, " ");
	}

	/**
	 * 清除字符集
	 *
	 */
	public static String clear(String src, String... searchs) {
		if (src == null)
			return null;
		if (searchs == null || searchs.length <= 0)
			return src;
		for (String s : searchs) {
			src = clear(src, s);
		}
		return src;
	}

	/**
	 * 先Trim 再清除search字符
	 * 
	 */
	public static String trimClear(String src, String search) {
		if (src == null)
			return null;
		src = StringUtils.trim(src);
		return clear(src, search);
	}

	/**
	 * 替换search字符
	 * 
	 */
	public static String replace(String src, String search, String rep) {
		if (rep == null)
			rep = EMPTY;
		if (src == null || search == null)
			return src;
		return StringUtils.replace(src, search, rep);
	}

	/**
	 * 去掉字符串中的指定字符.
	 * 
	 * @param src
	 *            输入字符串
	 * @param chars
	 *            要去除的字符
	 * 
	 *            例：trim("  abcdxxxx", ' ', 'x') ==> "abcd"
	 * 
	 * @return 输出字符串
	 * 
	 */
	public static String trim(String src, char... chars) {
		if (src == null) {
			return EMPTY;
		}
		int count = src.length();
		int len = src.length();
		int st = 0;

		while (st < len) {
			if (src.charAt(st) < ' ') {// 非打印字符
				st++;
			} else {
				boolean found = false;
				for (char c : chars) {
					if (src.charAt(st) == c) {
						found = true;
						break;
					}
				}
				if (found) {
					st++;
				} else {
					break;
				}
			}
		}
		while (st < len) {
			if (src.charAt(len - 1) < ' ') {
				len--;
			} else {
				boolean found = false;
				for (char c : chars) {
					if (src.charAt(len - 1) == c) {
						found = true;
						break;
					}
				}
				if (found) {
					len--;
				} else {
					break;
				}
			}
		}
		return ((st > 0) || (len < count)) ? src.substring(st, len) : src;
	}

	/**
	 * 去掉字符串左边的指定字符.
	 * 
	 * @param src
	 *            输入字符串
	 * @param chars
	 *            要去除的字符
	 * 
	 *            例：trimLeft("  abcdxxxx", ' ', 'x') ==> "abcdxxxx"
	 * 
	 * @return 输出字符串
	 * 
	 */
	public static String trimLeft(String src, char... chars) {
		if (src == null) {
			return EMPTY;
		}
		int count = src.length();
		int len = src.length();
		int st = 0;

		while (st < len) {
			if (src.charAt(st) < ' ') {// 非打印字符
				st++;
			} else {
				boolean found = false;
				for (char c : chars) {
					if (src.charAt(st) == c) {
						found = true;
						break;
					}
				}
				if (found) {
					st++;
				} else {
					break;
				}
			}
		}
		return ((st > 0) || (len < count)) ? src.substring(st, len) : src;
	}

	/**
	 * 去掉字符串右边的指定字符.
	 * 
	 * @param src
	 *            输入字符串
	 * @param chars
	 *            要去除的字符
	 * 
	 *            例：trimRight("  abcdxxxx", ' ', 'x') ==> "  abcd"
	 * 
	 * @return 输出字符串
	 * 
	 */
	public static String trimRight(String src, char... chars) {
		if (src == null) {
			return EMPTY;
		}
		int count = src.length();
		int len = src.length();
		int st = 0;

		while (st < len) {
			if (src.charAt(len - 1) < ' ') {
				len--;
			} else {
				boolean found = false;
				for (char c : chars) {
					if (src.charAt(len - 1) == c) {
						found = true;
						break;
					}
				}
				if (found) {
					len--;
				} else {
					break;
				}
			}
		}
		return ((st > 0) || (len < count)) ? src.substring(st, len) : src;
	}

	private static int compare(String str, String target) {
		int d[][]; // 矩阵
		int n = str.length();
		int m = target.length();
		int i; // 遍历str的
		int j; // 遍历target的
		char ch1; // str的
		char ch2; // target的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++) { // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++) {
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}

				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
			}
		}
		return d[n][m];
	}

	private static int min(int one, int two, int three) {
		return (one = one < two ? one : two) < three ? one : three;
	}

	/**
	 * 
	 * 获取两字符串的相似度
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @param target
	 * 
	 * @return
	 * 
	 */

	public static float getSimilarity(String str, String target) {
		return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
	}

	public static void main(String[] args) {
		
		String s1 = "慢性支肺气肿感染";
		String s2 = "急性支肺气肿感染";
		System.out.println("similarity="+ getSimilarity(s1, s2));
	}

}
