package com.dailystudy.swinglab.service.framework.utils;

import java.lang.reflect.Array;
import java.util.*;

/** 
 * ArrayUtil 클래스 
 * 배열에 관련된 유틸리티 
 */	
public class ArrayUtil {

	private ArrayUtil() {
	}

	/**
	 * 배열안에서 해당 객체의 첫번째 인덱스 번호.
	 * @param array Object[]
	 * @param object Object
	 * @return int
	 */
	public static int indexOf(Object[] array, Object object) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 객체 배열을 문자 배열로 바꿔준다.
	 * @param objects Object[]
	 * @return String[]
	 */
	public static String[] toStringArray(Object[] objects) {
		int length = objects.length;
		String[] result = new String[length];
		for (int i = 0; i < length; i++) {
			if (objects[i] != null) {
				result[i] = objects[i].toString();
			} else {
				result[i] = null;
			}
		}
		return result;
	}

	/**
	 * 지정된 길이의 문자열 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value String 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return String[]
	 */
	public static String[] fillArray(String value, int length) {
		String[] result = new String[length];
		Arrays.fill(result, value);
		return result;
	}
	
	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value byte 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return byte[]
	 */
	public static byte[] fillArray(byte value, int length) {
		byte[] result = new byte[length];
		Arrays.fill(result, value);
		return result;
	}
	
	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value short 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return short[]
	 */
	public static short[] fillArray(short value, int length) {
		short[] result = new short[length];
		Arrays.fill(result, value);
		return result;
	}

	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value int 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return int[]
	 */
	public static int[] fillArray(int value, int length) {
		int[] result = new int[length];
		Arrays.fill(result, value);
		return result;
	}
	
	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value long 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return long[]
	 */
	public static long[] fillArray(long value, int length) {
		long[] result = new long[length];
		Arrays.fill(result, value);
		return result;
	}
	
	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value float 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return float[]
	 */
	public static float[] fillArray(float value, int length) {
		float[] result = new float[length];
		Arrays.fill(result, value);
		return result;
	}
	
	/**
	 * 지정된 길이의 배열을 만들어서 주어진 값으로 각 배열 요소를 초기화 한다.
	 * @param value double 초기화값
	 * @param length length 생성할 배열의 길이
	 * @return double[]
	 */
	public static double[] fillArray(double value, int length) {
		double[] result = new double[length];
		Arrays.fill(result, value);
		return result;
	}

	/**
	 * 컬렉션을 문자배열로 바꾼다.
	 * @param coll Collection
	 * @return String[]
	 */
	@SuppressWarnings("unchecked")
	public static String[] toStringArray(Collection coll) {
		return (String[]) coll.toArray(new String[0]);
	}

	/**
	 * 컬렉션을 문자배열로 바꾼다.
	 * @param coll Collection
	 * @return String[][]
	 */
	@SuppressWarnings("unchecked")
	public static String[][] to2DStringArray(Collection coll) {
		return (String[][]) coll.toArray(new String[coll.size()][]);
	}

	/**
	 * 컬렉션을 정수배열로 바꾼다.
	 * @param coll Collection
	 * @return int[][]
	 */
	@SuppressWarnings("unchecked")
	public static int[][] to2DIntArray(Collection coll) {
		return (int[][]) coll.toArray(new int[coll.size()][]);
	}

	/**
	 * 컬렉션을 정수배열로 바꾼다.
	 * @param coll Collection
	 * @return int[]
	 */
	public static int[] toIntArray(Collection coll) {
		Iterator iter = coll.iterator();
		int[] arr = new int[coll.size()];
		int i = 0;
		while (iter.hasNext()) {
			arr[i++] = ((Integer) iter.next()).intValue();
		}
		return arr;
	}

	/**
	 * 컬렉션을 boolean배열로 바꾼다.
	 * @param coll Collection
	 * @return boolean[]
	 */
	public static boolean[] toBooleanArray(Collection coll) {
		Iterator iter = coll.iterator();
		boolean[] arr = new boolean[coll.size()];
		int i = 0;
		while (iter.hasNext()) {
			arr[i++] = ((Boolean) iter.next()).booleanValue();
		}
		return arr;
	}

	/**
	 * 배열의 각 요소를 지정된 타입으로 바꾼다.
	 * @param array Object[]
	 * @param to Object[]
	 * @return Object[]
	 */
	public static Object[] typecast(Object[] array, Object[] to) {
		return Arrays.asList(array).toArray(to);
	}

	/**
	 * 배열을 리스트로 바꾼다.
	 * @param array Object 배열 객체
	 * @return List
	 */
	public static List toList(Object array) {
		if (array instanceof Object[]) {
			return Arrays.asList((Object[]) array);
		}
		int size = Array.getLength(array);
		List<Object> list = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++) {
			list.add(Array.get(array, i));
		}
		return list;
	}

	/**
	 * 문자 배열을 지정된 위치부터 지정된 길이로 자른다.
	 * @param strings String[]
	 * @param begin int
	 * @param length int
	 * @return String[]
	 */
	public static String[] slice(String[] strings, int begin, int length) {
		String[] result = new String[length];
		for (int i = 0; i < length; i++) {
			result[i] = strings[begin + i];
		}
		return result;
	}

	/**
	 * 객체 배열을 지정된 위치부터 지정된 길이로 자른다.
	 * @param objects Object[]
	 * @param begin int
	 * @param length int
	 * @return Object[]
	 */
	public static Object[] slice(Object[] objects, int begin, int length) {
		Object[] result = new Object[length];
		for (int i = 0; i < length; i++) {
			result[i] = objects[begin + i];
		}
		return result;
	}

	/**
	 * 이터레이터를 리스트로 바꾼다.
	 * @param iter Iterator
	 * @return List
	 */
	public static List toList(Iterator iter) {
		List<Object> list = new ArrayList<Object>();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	/**
	 * 두 문자배열을 하나의 배열로 합친다.
	 * @param x String[]
	 * @param y String[]
	 * @return String[]
	 */
	public static String[] join(String[] x, String[] y) {
		String[] result = new String[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}

	/**
	 * 두 문자배열을 하나의 배열로 합친다. 다만 두번째 배열에서 합쳐질 요소들을 지정할 수 있다.
	 * @param x String[]
	 * @param y String[]
	 * @param use boolean[] y 배열에서 합칠 요소의 값. true : 합침, false : 버림.
	 * @return String[]
	 */
	public static String[] join(String[] x, String[] y, boolean[] use) {
		String[] result = new String[x.length + countTrue(use)];
		System.arraycopy(x, 0, result, 0, x.length);
		int k = x.length;
		for (int i = 0; i < y.length; i++) {
			if (use[i]) {
				result[k++] = y[i];
			}
		}
		return result;
	}
	
	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x byte[]
	 * @param y byte[]
	 * @return byte[]
	 */
	public static byte[] join(byte[] x, byte[] y) {
		byte[] result = new byte[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}
	
	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x short[]
	 * @param y short[]
	 * @return short[]
	 */
	public static short[] join(short[] x, short[] y) {
		short[] result = new short[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}

	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x int[]
	 * @param y int[]
	 * @return int[]
	 */
	public static int[] join(int[] x, int[] y) {
		int[] result = new int[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}
	
	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x long[]
	 * @param y long[]
	 * @return long[]
	 */
	public static long[] join(long[] x, long[] y) {
		long[] result = new long[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}
	
	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x float[]
	 * @param y float[]
	 * @return float[]
	 */
	public static float[] join(float[] x, float[] y) {
		float[] result = new float[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}
	
	/**
	 * 두개의 배열을 하나의 배열로 합친다.
	 * @param x double[]
	 * @param y double[]
	 * @return double[]
	 */
	public static double[] join(double[] x, double[] y) {
		double[] result = new double[x.length + y.length];
		System.arraycopy(x, 0, result, 0, x.length);
		System.arraycopy(y, 0, result, x.length, y.length);
		return result;
	}

	/**
	 * 객체 배열을 문자열화한다.
	 * @param array Object[]
	 * @return String
	 */
	public static String toString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * short 배열을 문자열화한다.
	 * @param array short[]
	 * @return String
	 */
	public static String toString(short[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * int 배열을 문자열화한다.
	 * @param array int[]
	 * @return String
	 */
	public static String toString(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * long 배열을 문자열화한다.
	 * @param array long[]
	 * @return String
	 */
	public static String toString(long[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * float 배열을 문자열화한다.
	 * @param array float[]
	 * @return String
	 */
	public static String toString(float[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * double 배열을 문자열화한다.
	 * @param array double[]
	 * @return String
	 */
	public static String toString(double[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 정수 배열의 모든 값이 음수인지 판단한다.
	 * @param array short[]
	 * @return boolean
	 */
	public static boolean isAllNegative(short[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 정수 배열의 모든 값이 음수인지 판단한다.
	 * @param array int[]
	 * @return boolean
	 */
	public static boolean isAllNegative(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 정수 배열의 모든 값이 음수인지 판단한다.
	 * @param array long[]
	 * @return boolean
	 */
	public static boolean isAllNegative(long[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 배열의 모든 값이 음수인지 판단한다.
	 * @param array float[]
	 * @return boolean
	 */
	public static boolean isAllNegative(float[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 배열의 모든 값이 음수인지 판단한다.
	 * @param array double[]
	 * @return boolean
	 */
	public static boolean isAllNegative(double[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * boolean 배열의 모든 값이 true 인지를 판단한다.
	 * @param array boolean[]
	 * @return boolean
	 */
	public static boolean isAllTrue(boolean[] array) {
		for (int i = 0; i < array.length; i++) {
			if (!array[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * boolean 배열의 true 값의 갯수를 구한다.
	 * @param array boolean[]
	 * @return int
	 */
	public static int countTrue(boolean[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				result++;
			}
		}
		return result;
	}

	/**
	 * boolean 배열의 모든 값이 false 인지를 판단한다.
	 * @param array boolean[]
	 * @return boolean
	 */
	public static boolean isAllFalse(boolean[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 컬렉션에 객체 배열의 모든 요소를 더한다.
	 * @param collection Collection
	 * @param array Object[]
	 */
	@SuppressWarnings("unchecked")
	public static void addAll(Collection collection, Object[] array) {
		for (int i = 0; i < array.length; i++) {
			collection.add(array[i]);
		}
	}
}
