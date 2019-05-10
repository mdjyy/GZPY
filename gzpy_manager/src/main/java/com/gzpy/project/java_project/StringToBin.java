package com.gzpy.project.java_project;
/**
 * String 转为 二进制
 * */
public class StringToBin {

	public static String StrToBinstr(String str) {
		char[] strChar=str.toCharArray();
		String result="";
		for(int i=0;i<strChar.length;i++){
			result +=Integer.toBinaryString(strChar[i])+ " ";
		}
		return result;
	}
	//二进制到String
	public static String BinstrtoSring(String binStr) {
		String str[] = StrToStrArray(binStr);
		String ret = "";
		for(int i=0;i<str.length;i++) {
			char c = BinstrToChar(str[i]);
			ret = ret + c;
		}
		return ret;
	}
	// 将初始二进制字符串转换成字符串数组，以空格相隔 
    private static String[] StrToStrArray(String str) { 
        return str.split(" "); 
    } 
	//二进制到char
	public static char BinstrToChar(String binStr) { 
        int[] temp = BinstrToIntArray(binStr); 
        int sum = 0; 
        for (int i = 0; i < temp.length; i++) { 
            sum += temp[temp.length - 1 - i] << i; 
        } 
        return (char) sum; 
    } 
	// 将二进制字符串转换成int数组 
    public static int[] BinstrToIntArray(String binStr) { 
        char[] temp = binStr.toCharArray(); 
        int[] result = new int[temp.length]; 
        for (int i = 0; i < temp.length; i++) { 
            result[i] = temp[i] - 48; 
        } 
        return result; 
    } 
    
	public static void main(String[] args) {
		//将数字10转为二进制
		System.out.println("int转为二进制:"+Integer.toBinaryString(10));
		//字符串10转为二进制
		System.out.println(StrToBinstr("10"));
		//1100110 1101111 1101111 1100010 1100001 1110010
		System.out.println("字符串转为二进制:"+StrToBinstr("foobar"));
		/**
		 * redis> SET mykey "foobar"
                  "OK"
           redis> BITCOUNT mykey
           (integer) 26
           redis> BITCOUNT mykey 0 0
           (integer) 4
           redis> BITCOUNT mykey 1 1
           (integer) 6
           redis>
            
		 * redis BITCOUNT计算的是字符串二进制1的数量
		 * 假设现在我们希望记录自己网站上的用户的上线频率，比如说，计算用户 A 上线了多少天，用户 B 上线了多少天，诸如此类，以此作为数据，从而决定让哪些用户参加 beta 测试等活动 —— 这个模式可以使用 SETBIT 和 BITCOUNT 来实现。 
                        比如说，每当用户在某一天上线的时候，我们就使用 SETBIT ，以用户名作为 key ，将那天所代表的网站的上线日作为 offset 参数，并将这个 offset 上的为设置为 1 。 
                       举个例子，如果今天是网站上线的第 100 天，而用户 peter 在今天阅览过网站，那么执行命令 SETBIT peter 100 1 ；如果明天 peter 也继续阅览网站，那么执行命令 SETBIT peter 101 1 ，以此类推。 
                        当要计算 peter 总共以来的上线次数时，就使用 BITCOUNT 命令：执行 BITCOUNT peter ，得出的结果就是 peter 上线的总天数。 
                        性能 
                       前面的上线次数统计例子，即使运行 10 年，占用的空间也只是每个用户 10*365 比特位(bit)，也即是每个用户 456 字节。对于这种大小的数据来说， BITCOUNT 的处理速度就像 GET 和 INCR 这种 O(1) 复杂度的操作一样快。 
		 * */
		System.out.println("二进制转为字符"+BinstrToChar("1000001"));
		System.out.println("二进制转为字符串:"+BinstrtoSring("00100110 01101111 01101111 01100010 01100001 01110010"));
	}
}
