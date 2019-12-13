package org.ethan.eRpc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * 
 * <p>
 * 密码学6位随机数 用于验证码
 * </p>
 * @version 1.0 2018年11月29日 ggh 修改内容:初版
 */
public class RandomUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(RandomUtils.class);
	
	private static SecureRandom r = new SecureRandom();
	
	
	/**
	 * 获取一个随机串
	 * @param length
	 * @param type 0--仅字符   1--仅数字    2--数字+字符
	 * 
	 * @return
	 */
	public static String getRandomString(int length,int type){
		if(length < 1){
			LOGGER.error("length[{}] is too small when getRandomString",length);
			throw new RuntimeException("length is too small when getRandomString:"+length);
		}
		
		
		
		int numericLength = 0;//数字字符个数
		int charLength = 0;//字母字符个数
		
		if(RandomStrType.TYPE_CHAR_ONLY == type){
			charLength = length;
		}else if(RandomStrType.TYPE_NUMERIC_ONLY == type){
			numericLength = length;
		}else if(RandomStrType.TYPE_MIX_NUMERIC_CHAR == type){
			while(numericLength == 0 || charLength == 0){
				numericLength = r.nextInt(length);//数字字符个数
				charLength = length - numericLength;//字母字符个数
				if(length == 1){
					break;
				}
			}
		}else{
			LOGGER.error("type[{}] is not support when getRandomString",type);
			throw new RuntimeException("type is not support when getRandomString:"+type);
		}
		
		char[] numericArray = numericLength == 0 ? null : getRandomNumerics(numericLength);
		char[] characterArray = charLength == 0 ? null : getRandomChars(charLength);
		
		return new String(mix(numericArray,characterArray));
	}
	
	private static char[] getRandomChars(int length){
		char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		char[] result = new char[length];
		SecureRandom r = new SecureRandom();
		for(int i=0;i<length;i++) {
			result[i] = chars[r.nextInt(chars.length)];
		}
		return result;
	}
	
	private static char[] getRandomNumerics(int length){
		char[] numeric = {'1','2','3','4','5','6','7','8','9','0'};
		char[] result = new char[length];
		
		for(int i=0;i<length;i++) {
			result[i] = numeric[r.nextInt(numeric.length)];
		}
		return result;
	}
	
	private static char[] mix(char[] c1,char[] c2){
		if(c1 == null){
			return c2;
		}
		if(c2 == null){
			return c1;
		}
		char[] result = new char[c1.length+c2.length];
		System.arraycopy(c1, 0, result, 0, c1.length);
		System.arraycopy(c2, 0, result, c1.length, c2.length);
		shuffle(result);
		return result;
	}
	
	private static void swap(char[] a, int i, int j){
		char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

	private static void shuffle(char[] arr) {
        int length = arr.length;
        for ( int i = length; i > 0; i-- ){
            int randInd = r.nextInt(i);
            swap(arr, randInd, i - 1);
        }
    }
	
	public static void main(String[] args) {
		System.out.println(getRandomString(4,RandomStrType.TYPE_CHAR_ONLY));
		System.out.println(getRandomString(4,RandomStrType.TYPE_NUMERIC_ONLY));
		System.out.println(getRandomString(4,RandomStrType.TYPE_MIX_NUMERIC_CHAR));
	}
}
