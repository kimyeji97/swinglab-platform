package com.dailystudy.swinglab.service.framework.utils;

/*
*/

import org.apache.commons.lang3.time.DateUtils;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** 
 * DateUtil 클래스 
 * 날짜와 관련된 유틸리티 
 */	
public class DateUtil {

	/**
	 * 밀리세컨드 구분자.
	 */
	public static final int MILLISECOND = 1;
	/**
	 * 초 구분자.
	 */
	public static final int SECOND = 1000;
	/**
	 * 분 구분자.
	 */
	public static final int MINUTE = DateUtil.SECOND * 60;
	/**
	 * 시간 구분자.
	 */
	public static final int HOUR = DateUtil.MINUTE * 60;
	/**
	 * 일 구분자.
	 */
	public static final int DAY = DateUtil.HOUR * 24;
	/**
	 * 주 구분자.
	 */
	public static final int WEEK = DateUtil.DAY * 7;
	/**
	 * 월 구분자.
	 */
	public static final int MONTH = DateUtil.DAY * 31;
	/**
	 * 분기 구분자.
	 */
	public static final int QUARTER = DateUtil.MONTH * 3;
	/**
	 * 년 구분자.
	 */
	public static final int YEAR = DateUtil.DAY * 365;
	private static final int[] LastDayOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static final String[] LunarData = {
		"1212122322121", "1212121221220", "1121121222120", "2112132122122", "2112112121220", "2121211212120", "2212321121212", "2122121121210", "2122121212120", "1232122121212",
		"1212121221220", "1121123221222", "1121121212220", "1212112121220", "2121231212121", "2221211212120", "1221212121210", "2123221212121", "2121212212120", "1211212232212",
		"1211212122210", "2121121212220", "1212132112212", "2212112112210", "2212211212120", "1221412121212", "1212122121210", "2112212122120", "1231212122212", "1211212122210",
		"2121123122122", "2121121122120", "2212112112120", "2212231212112", "2122121212120", "1212122121210", "2132122122121", "2112121222120", "1211212322122", "1211211221220",
		"2121121121220", "2122132112122", "1221212121120", "2121221212110", "2122321221212", "1121212212210", "2112121221220", "1231211221222", "1211211212220", "1221123121221",
		"2221121121210", "2221212112120", "1221241212112", "1212212212120", "1121212212210", "2114121212221", "2112112122210", "2211211412212", "2211211212120", "2212121121210",
		"2212214112121", "2122122121120", "1212122122120", "1121412122122", "1121121222120", "2112112122120", "2231211212122", "2121211212120", "2212121321212", "2122121121210",
		"2122121212120", "1212142121212", "1211221221220", "1121121221220", "2114112121222", "1212112121220", "2121211232122", "1221211212120", "1221212121210", "2121223212121",
		"2121212212120", "1211212212210", "2121321212221", "2121121212220", "1212112112210", "2223211211221", "2212211212120", "1221212321212", "1212122121210", "2112212122120",
		"1211232122212", "1211212122210", "2121121122210", "2212312112212", "2212112112120", "2212121232112", "2122121212110", "2212122121210", "2112124122121", "2112121221220",
		"1211211221220", "2121321122122", "2121121121220", "2122112112322", "1221212112120", "1221221212110", "2122123221212", "1121212212210", "2112121221220", "1211231212222",
		"1211211212220", "1221121121220", "1223212112121", "2221212112120", "1221221232112", "1212212122120", "1121212212210", "2112132212221", "2112112122210", "2211211212210",
		"2221321121212", "2212121121210", "2212212112120", "1232212122112", "1212122122120", "1121212322122", "1121121222120", "2112112122120", "2211231212122", "2121211212120",
		"2122121121210", "2124212112121", "2122121212120", "1212121223212", "1211212221220", "1121121221220", "2112132121222", "1212112121220", "2121211212120", "2122321121212",
		"1221212121210", "2121221212120", "1232121221212", "1211212212210", "2121123212221", "2121121212220", "1212112112220", "1221231211221", "2212211211220", "1212212121210",
		"2123212212121", "2112122122120", "1211212322212", "1211212122210", "2121121122120", "2212114112122", "2212112112120", "2212121211210", "2212232121211", "2122122121210",
		"2112122122120", "1231212122212", "1211211221220", "2121121321222", "2121121121220", "2122112112120", "2122141211212", "1221221212110", "2121221221210", "2114121221221"
	};
	private static final int[] LunarDaysOfYear = {
		384, 355, 354, 384, 354, 354, 384, 354, 355, 384,
		355, 384, 354, 354, 383, 355, 354, 384, 355, 384,
		354, 355, 383, 354, 355, 384, 354, 355, 384, 354,
		384, 354, 354, 384, 355, 354, 384, 355, 384, 354,
		354, 384, 354, 354, 385, 354, 355, 384, 354, 383,
		354, 355, 384, 355, 354, 384, 354, 384, 354, 354,
		384, 355, 355, 384, 354, 354, 384, 354, 384, 354,
		355, 384, 355, 354, 384, 354, 384, 354, 354, 384,
		355, 354, 384, 355, 353, 384, 355, 384, 354, 355,
		384, 354, 354, 384, 354, 384, 354, 355, 384, 355,
		354, 384, 354, 384, 354, 354, 385, 354, 355, 384,
		354, 354, 383, 355, 384, 355, 354, 384, 354, 354,
		384, 354, 355, 384, 355, 384, 354, 354, 384, 354,
		354, 384, 355, 384, 355, 354, 384, 354, 354, 384,
		354, 355, 384, 354, 384, 355, 354, 383, 355, 354,
		384, 355, 384, 354, 354, 384, 354, 354, 384, 355,
		355, 384, 354, 384, 354, 354, 384, 354, 355, 384
	};
	private static final int[] LunarLeapMonthOfYear = {
		6, 12, 12, 4, 12, 12, 3, 12, 12, 1,
		12, 5, 12, 12, 4, 12, 12, 2, 12, 7,
		12, 12, 4, 12, 12, 3, 12, 12, 1, 12,
		5, 12, 12, 4, 12, 12, 1, 12, 6, 12,
		12, 4, 12, 12, 3, 12, 12, 1, 12, 5,
		12, 12, 4, 12, 12, 2, 12, 6, 12, 12,
		5, 12, 12, 3, 12, 12, 1, 12, 6, 12,
		12, 4, 12, 12, 2, 12, 7, 12, 12, 5,
		12, 12, 3, 12, 12, 2, 12, 6, 12, 12,
		4, 12, 12, 3, 12, 7, 12, 12, 5, 12,
		12, 3, 12, 9, 12, 12, 5, 12, 12, 4,
		12, 12, 2, 12, 7, 12, 12, 4, 12, 12,
		3, 12, 12, 1, 12, 6, 12, 12, 4, 12,
		12, 2, 12, 8, 12, 12, 4, 12, 12, 3,
		12, 12, 1, 12, 5, 12, 12, 4, 12, 12,
		2, 12, 6, 12, 12, 5, 12, 12, 4, 12,
		12, 1, 12, 6, 12, 12, 4, 12, 12, 2
	};
	private static final int COMPUTABLE_LUNAR_DAY_OFFSET_FROM_THE_FIRST_DAY_OF_ANNO_DOMINI = 686685;
	private int lunarYear = 1881;
	private int lunarMonth = 0;
	private int lunarDay = 1;
	private boolean boolLunarLeapMonth = false;
	private Calendar cal;

	/**
	 * DateUtil constructor.
	 * @param date
	 */
	public DateUtil(DateUtil date) {
		cal = Calendar.getInstance();
		cal.setTime(new Date(date.getTimeInMillis()));
		computeLunar();
	}

	/**
	 * DateUtil constructor.
	 * @param date Date
	 */
	public DateUtil(Date date) {
		cal = Calendar.getInstance();
		cal.setTime(date);
		computeLunar();
	}

	/**
	 * DateUtil constructor.
	 * @param value String
	 * @param pattern String
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	public DateUtil(@NotNull String value,@NotNull String pattern)
			  throws IllegalArgumentException, ParseException {
		cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat(pattern).parse(value));
		computeLunar();
	}

	/**
	 * DateUtil constructor.
	 */
	public DateUtil() {
		cal = Calendar.getInstance();
		cal.setTime(new Date());
		computeLunar();
	}

	/**
	 * 년도값을 반환한다.
	 * @return int
	 */
	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 년도값을 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setYear(int value) {
		cal.set(Calendar.YEAR, value);
		computeLunar();
		return this;
	}

	/**
	 * 음력 년도값을 반환한다.
	 * @return int
	 */
	public int getLunarYear() {
		return lunarYear;
	}

	/**
	 * 월 값을 반환한다 (1 ~ 12).
	 * @return int
	 */
	public int getMonth() {
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 월 값을 설정한다 (1 ~ 12).
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setMonth(int value) {
		cal.set(Calendar.MONTH, value - 1);
		computeLunar();
		return this;
	}

	/**
	 * 음력 월 값을 반환한다.
	 * @return int
	 */
	public int getLunarMonth() {
		return lunarMonth + 1;
	}

	/**
	 * 일 값을 반환한다.
	 * @return int
	 */
	public int getDay() {
		return cal.get(Calendar.DATE);
	}

	/**
	 * 일 값을 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setDay(int value) {
		cal.set(Calendar.DATE, value);
		computeLunar();
		return this;
	}

	/**
	 * 음력 일 값을 반환한다.
	 * @return int
	 */
	public int getLunarDay() {
		return lunarDay;
	}

	/**
	 * 시간 값을 반환한다(0 ~ 23).
	 * @return int
	 */
	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 시간 값을 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setHour(int value) {
		cal.set(Calendar.HOUR_OF_DAY, value);
		return this;
	}

	/**
	 * 분 값을 구한다(0 ~ 59).
	 * @return int
	 */
	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 분 값을 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setMinute(int value) {
		cal.set(Calendar.MINUTE, value);
		return this;
	}

	/**
	 * 초 값을 구한다(0 ~ 59).
	 * @return int
	 */
	public int getSecond() {
		return cal.get(Calendar.SECOND);
	}

	/**
	 * 초 값을 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setSecond(int value) {
		cal.set(Calendar.SECOND, value);
		return this;
	}

	/**
	 * 밀리초를 구한다(0~ 999).
	 * @return int
	 */
	public int getMillisecond() {
		return cal.get(Calendar.MILLISECOND);
	}

	/**
	 * 밀리초를 설정한다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil setMillisecond(int value) {
		cal.set(Calendar.MILLISECOND, value);
		return this;
	}

	/**
	 * 시간값을 long 값으로 구한다.
	 * @return long
	 */
	public long getTimeInMillis() {
		return cal.getTime().getTime();
	}

	/**
	 *  long 값으로 시간값을 설정한다.
	 * @param value long
	 */
	public void setTimeInMillis(long value) {
		cal.setTime(new Date(value));
		computeLunar();
	}

	/**
	 * 시간값을 구한다.
	 * @return Date
	 */
	public Date getTime() {
		return cal.getTime();
	}

	/**
	 * Date 객체로 시간값을 설정한다.
	 * @param date Date
	 */
	public void setTime(Date date) {
		cal.setTime((Date) date.clone());
		computeLunar();
	}

	/**
	 * 음력 윤달인지의 여부를 반환한다.
	 * @return boolean
	 */
	public boolean isLunarLeapMonth() {
		return boolLunarLeapMonth;
	}

	/**
	 * 월의 마지막 일을 반환한다.
	 * @return int
	 */
	public int getLastDayOfMonth() {
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 지정된 년도와 월의 마지막일을 반환한다.
	 * @param year int
	 * @param month int
	 * @return int
	 */
	public static int getLastDayOfMonth(int year, int month) {
		Calendar tcal = Calendar.getInstance();
		tcal.set(Calendar.DATE, 1);
		tcal.set(Calendar.YEAR, year);
		tcal.set(Calendar.MONTH, month - 1);
		return tcal.getActualMaximum(Calendar.DATE);
	}

	private static int getLunarDaysOfYear(int year) {
		int index = year - 1881;
		if (index < 0 || index >= LunarDaysOfYear.length) {
			return -1;
		}
		return LunarDaysOfYear[index];
	}

	private static int getLunarLeapMonthOfYear(int year) {
		int index = year - 1881;
		if (index < 0 || index >= LunarDaysOfYear.length) {
			return -1;
		}
		return LunarLeapMonthOfYear[index];
	}

	private static int getLastDayOfLunarMonth(DateUtil date) {
		int leap;
		int lmonth = DateUtil.getLunarLeapMonthOfYear(date.lunarYear);
		if (lmonth == -1) {
			return -1;
		}
		int index = date.lunarYear - 1881;
		if (index < 0 || index >= LunarData.length) {
			return -1;
		}
		if (date.lunarMonth <= lmonth && !date.boolLunarLeapMonth) {
			leap = 0;
		} else {
			leap = 1;
		}
		return 29 + ((int) (DateUtil.LunarData[index].charAt(date.lunarMonth + leap)) + 1) % 2;
	}

	private static int getDaysAfterTheFirstDayOfAnnoDomini(DateUtil date) {
		int sum = 0;
		int year = date.getYear();
		int month = date.getMonth() - 1;
		for (int i = 0; i < month; i++) {
			sum = sum + DateUtil.getLastDayOfMonth(year, i);
		}
		return (year - 1) * 365 + sum + (int) ((year - 1) / 4) - (int) ((year - 1) / 100) + (int) ((year - 1) / 400) + date.getDay() - 1;
	}

	private void computeLunar() {
		this.lunarYear = 1881;
		this.lunarMonth = 0;
		this.lunarDay = 1;
		this.boolLunarLeapMonth = false;
		if (getYear() < 1881 || getYear() > 2051 || (getYear() == 1881 && getMonth() == 1 && getDay() < 30) || (getYear() == 2051 && getMonth() >= 2 && getDay() > 10)) {
			this.lunarYear = 0;
			this.lunarMonth = 0;
			this.lunarDay = 0;
			this.boolLunarLeapMonth = false;
			return;
		}
		int days, tmp, lyear, lmonth, lday;
		days = DateUtil.getDaysAfterTheFirstDayOfAnnoDomini(this) - DateUtil.COMPUTABLE_LUNAR_DAY_OFFSET_FROM_THE_FIRST_DAY_OF_ANNO_DOMINI;

		do {
			tmp = days;
			lyear = DateUtil.getLunarDaysOfYear(this.lunarYear);
			if (lyear == -1) {
				this.lunarYear = 0;
				this.lunarMonth = 0;
				this.lunarDay = 0;
				this.boolLunarLeapMonth = false;
				return;
			}
			days -= lyear;
			if (days < 0) {
				days = tmp;
				break;
			}
			this.lunarYear++;
		} while (true);

		do {
			tmp = days;
			lmonth = DateUtil.getLunarLeapMonthOfYear(this.lunarYear);
			lday = DateUtil.getLastDayOfLunarMonth(this);
			if (lday == -1) {
				this.lunarYear = 0;
				this.lunarMonth = 0;
				this.lunarDay = 0;
				this.boolLunarLeapMonth = false;
				return;
			}
			days -= lday;
			if (days < 0) {
				days = tmp;
				break;
			}
			if (this.lunarMonth == lmonth && !this.boolLunarLeapMonth) {
				this.boolLunarLeapMonth = true;
			} else {
				this.lunarMonth++;
				this.boolLunarLeapMonth = false;
			}
		} while (true);
		this.lunarDay = days + 1;
	}

	/**
	 * 현재 시간의 DateUtil 객체를 반환한다.
	 * @return DateUtil
	 */
	public static DateUtil now() {
		return new DateUtil();
	}

	/**
	 * 현재 DateUtil의 값을 현재 시간으로 재설정한다.
	 * @return DateUtil
	 */
	public DateUtil current() {
		cal.setTime(new Date());
		computeLunar();
		return this;
	}

	/**
	 * 현재 DateUtil의 값을 현재일로 설정하고 시간,분,초,밀리초를 0으로 설정한다.
	 * @return DateUtil
	 */
	public DateUtil today() {
		return new DateUtil().setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
	}

	/**
	 * DateUtil 객체릐 값을 가지는 새로운 DateUtil 객체를 반환한다.
	 * @param date DateUtil
	 * @return DateUtil
	 */
	public static DateUtil clone(DateUtil date) {
		return new DateUtil(date);
	}

	/**
	 * 지정된 값만큼 일을 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil add(int amount) {
		cal.add(Calendar.DATE, amount);
		computeLunar();
		return this;
	}

	/**
	 * 지정된 필드의 값에 지정된 값만큼 구간값을 더하거나 뺀다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil add(int field, int amount) {
		switch (field) {
			case DateUtil.MILLISECOND:
				cal.add(Calendar.MILLISECOND, amount);
				break;
			case DateUtil.SECOND:
				cal.add(Calendar.SECOND, amount);
				break;
			case DateUtil.MINUTE:
				cal.add(Calendar.MINUTE, amount);
				break;
			case DateUtil.HOUR:
				cal.add(Calendar.HOUR_OF_DAY, amount);
				break;
			case DateUtil.DAY:
				cal.add(Calendar.DATE, amount);
				break;
			case DateUtil.WEEK:
				//cal.add(Calendar.DATE, amount * 7);
				cal.add(Calendar.WEEK_OF_YEAR, amount);
				break;
			case DateUtil.MONTH:
				cal.add(Calendar.MONTH, amount);
				break;
			case DateUtil.QUARTER:
				cal.add(Calendar.MONTH, amount * 3);
				break;
			case DateUtil.YEAR:
				cal.add(Calendar.YEAR, amount);
				break;
			default : 
				break;
		}
		computeLunar();
		return this;
	}

	/**
	 * 년도를 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil yearAdd(int amount) {
		cal.add(Calendar.YEAR, amount);
		computeLunar();
		return this;
	}

	/**
	 * 월을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil monthAdd(int amount) {
		cal.add(Calendar.MONTH, amount);
		computeLunar();
		return this;
	}

	/**
	 * 일을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil dayAdd(int amount) {
		cal.add(Calendar.DATE, amount);
		computeLunar();
		return this;
	}

	/**
	 * 시간을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil hourAdd(int amount) {
		cal.add(Calendar.HOUR_OF_DAY, amount);
		computeLunar();
		return this;
	}

	/**
	 * 분을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil minuteAdd(int amount) {
		cal.add(Calendar.MINUTE, amount);
		computeLunar();
		return this;
	}

	/**
	 * 초를 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public DateUtil secondAdd(int amount) {
		cal.add(Calendar.SECOND, amount);
		computeLunar();
		return this;
	}

	/**
	 * 일을 지정된 값으로 바꾼다.
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil set(int value) {
		cal.set(Calendar.DATE, value);
		computeLunar();
		return this;
	}

	/**
	 * 지정된 필드의 값을 지정된 값으로 설정한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @param value int
	 * @return DateUtil
	 */
	public DateUtil set(int field, int value) {
		switch (field) {
			case DateUtil.MILLISECOND:
				cal.set(Calendar.MILLISECOND, value);
				break;
			case DateUtil.SECOND:
				cal.set(Calendar.SECOND, value);
				break;
			case DateUtil.MINUTE:
				cal.set(Calendar.MINUTE, value);
				break;
			case DateUtil.HOUR:
				cal.set(Calendar.HOUR_OF_DAY, value);
				break;
			case DateUtil.DAY:
				cal.set(Calendar.DATE, value);
				break;
			case DateUtil.WEEK:
				cal.set(Calendar.WEEK_OF_YEAR, value);
				break;
			case DateUtil.MONTH:
				cal.set(Calendar.MONTH, value - 1);
				break;
			case DateUtil.QUARTER:
				cal.set(Calendar.MONTH, (value - 1) * 3);
				break;
			case DateUtil.YEAR:
				cal.set(Calendar.YEAR, value);
				break;
			default : 
				break;
		}
		computeLunar();
		return this;
	}

	/**
	 * 지정된 필드의 값을 반환한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @return int
	 */
	public int get(int field) {
		switch (field) {
			case DateUtil.MILLISECOND:
				return cal.get(Calendar.MILLISECOND);
			case DateUtil.SECOND:
				return cal.get(Calendar.SECOND);
			case DateUtil.MINUTE:
				return cal.get(Calendar.MINUTE);
			case DateUtil.HOUR:
				return cal.get(Calendar.HOUR_OF_DAY);
			case DateUtil.DAY:
				return cal.get(Calendar.DATE);
			case DateUtil.WEEK:
				return cal.get(Calendar.WEEK_OF_YEAR);
			case DateUtil.MONTH:
				return cal.get(Calendar.MONTH) + 1;
			case DateUtil.QUARTER:
				return (int) Math.ceil((cal.get(Calendar.MONTH) + 1) / (float) 3);
			case DateUtil.YEAR:
				return cal.get(Calendar.YEAR);
			default : 
				break;
		}
		return -1;
	}

	/**
	 * 다음 날자로 이동한다.
	 * @return DateUtil
	 */
	public DateUtil next() {
		cal.add(Calendar.DATE, 1);
		computeLunar();
		return this;
	}

	/**
	 * 지정된 필드의 값의 다음 값으로 이동한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @return DateUtil
	 */
	public DateUtil next(int field) {
		switch (field) {
			case DateUtil.MILLISECOND:
				cal.add(Calendar.MILLISECOND, 1);
				break;
			case DateUtil.SECOND:
				cal.add(Calendar.SECOND, 1);
				break;
			case DateUtil.MINUTE:
				cal.add(Calendar.MINUTE, 1);
				break;
			case DateUtil.HOUR:
				cal.add(Calendar.HOUR_OF_DAY, 1);
				break;
			case DateUtil.DAY:
				cal.add(Calendar.DATE, 1);
				break;
			case DateUtil.WEEK:
				cal.add(Calendar.WEEK_OF_YEAR, 1);
				break;
			case DateUtil.MONTH:
				cal.add(Calendar.MONTH, 1);
				break;
			case DateUtil.QUARTER:
				cal.add(Calendar.MONTH, 3);
				break;
			case DateUtil.YEAR:
				cal.add(Calendar.YEAR, 1);
				break;
			default : 
				break;
		}
		computeLunar();
		return this;
	}

	/**
	 * 이전 날자로 이동한다.
	 * @return DateUtil
	 */
	public DateUtil previous() {
		cal.add(Calendar.DATE, -1);
		computeLunar();
		return this;
	}

	/**
	 * 지정된 필드의 값의 이전 값으로 이동한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @return DateUtil
	 */
	public DateUtil previous(int field) {
		switch (field) {
			case DateUtil.MILLISECOND:
				cal.add(Calendar.MILLISECOND, -1);
				break;
			case DateUtil.SECOND:
				cal.add(Calendar.SECOND, -1);
				break;
			case DateUtil.MINUTE:
				cal.add(Calendar.MINUTE, -1);
				break;
			case DateUtil.HOUR:
				cal.add(Calendar.HOUR_OF_DAY, -1);
				break;
			case DateUtil.DAY:
				cal.add(Calendar.DATE, -1);
				break;
			case DateUtil.WEEK:
				cal.add(Calendar.WEEK_OF_YEAR, -1);
				break;
			case DateUtil.MONTH:
				cal.add(Calendar.MONTH, -1);
				break;
			case DateUtil.QUARTER:
				cal.add(Calendar.MONTH, -3);
				break;
			case DateUtil.YEAR:
				cal.add(Calendar.YEAR, -1);
				break;
			default : 
				break;
		}
		computeLunar();
		return this;
	}

	/**
	 * 현재 설정된 날자의 가장 첫 시간값으로 이동한다.
	 * @return DateUtil
	 */
	public DateUtil first() {
		setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
		return this;
	}

	/**
	 * 지정된 필드의 값의 최소 시간으로 이동한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @return DateUtil
	 */
	public DateUtil first(int field) {
		switch (field) {
			case DateUtil.MILLISECOND:
				break;
			case DateUtil.SECOND:
				setMillisecond(0);
				break;
			case DateUtil.MINUTE:
				setSecond(0).setMillisecond(0);
				break;
			case DateUtil.HOUR:
				setMinute(0).setSecond(0).setMillisecond(0);
				break;
			case DateUtil.DAY:
				setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
				break;
			case DateUtil.WEEK:
				this.add(-cal.get(Calendar.DAY_OF_WEEK) + 1);
				setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
				computeLunar();
				break;
			case DateUtil.MONTH:
				cal.set(Calendar.DATE, 1);
				setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
				computeLunar();
				break;
			case DateUtil.QUARTER:
				setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
				cal.set(Calendar.DATE, 1);
				int quarter = (int) Math.ceil(this.getMonth() / (float) 3);
				cal.set(Calendar.MONTH, quarter == 1 ? 0 : quarter * 3 - 1);
				computeLunar();
				break;
			case DateUtil.YEAR:
				setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.MONTH, 0);
				computeLunar();
				break;
			default : 
				break;
		}
		return this;
	}

	/**
	 * 현재 설정된 날자의 가장 마지막 시간으로 이동한다.
	 * @return DateUtil
	 */
	public DateUtil last() {
		setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
		return this;
	}

	/**
	 * 지정된 필드의 값의 최대 시간으로 이동한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @return DateUtil
	 */
	public DateUtil last(int field) {
		switch (field) {
			case DateUtil.MILLISECOND:
				break;
			case DateUtil.SECOND:
				setMillisecond(999);
				break;
			case DateUtil.MINUTE:
				setSecond(59).setMillisecond(999);
				break;
			case DateUtil.HOUR:
				setMinute(59).setSecond(59).setMillisecond(999);
				break;
			case DateUtil.DAY:
				setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
				break;
			case DateUtil.WEEK:
				this.add(-cal.get(Calendar.DAY_OF_WEEK) + 7);
				setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
				computeLunar();
				break;
			case DateUtil.MONTH:
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
				setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
				computeLunar();
				break;
			case DateUtil.QUARTER:
				setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
				cal.set(Calendar.DATE, 1);
				int quarter = (int) Math.ceil(this.getMonth() / (float) 3);
				cal.set(Calendar.MONTH, quarter == 1 ? 2 : quarter * 3 + 1);
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
				computeLunar();
				break;
			case DateUtil.YEAR:
				setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.MONTH, 11);
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
				computeLunar();
				break;
			default : 
				break;
		}
		return this;
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param pattern String
	 * @return String
	 */
	public String format(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(cal.getTime());
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param pattern String
	 * @param locale Locale
	 * @return String
	 */
	public String format(String pattern, Locale locale) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(cal.getTime());
	}
	
	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param pattern String
	 * @param locale Locale
	 * @param timeZone TimeZone
	 * @return String
	 */
	public String format(String pattern, Locale locale, TimeZone timeZone) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		formatter.setTimeZone(timeZone);
		return formatter.format(cal.getTime());
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @throws ParseException
	 * @return DateUtil
	 */
	public DateUtil parse(String value, String pattern) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		cal.setTime(formatter.parse(value));
		computeLunar();
		return this;
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @param locale Locale
	 * @throws ParseException
	 * @return DateUtil
	 */
	public DateUtil parse(String value, String pattern, Locale locale) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		cal.setTime(formatter.parse(value));
		computeLunar();
		return this;
	}
	
	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @param locale Locale
	 * @param timeZone TimeZone
	 * @throws ParseException
	 * @return DateUtil
	 */
	public DateUtil parse(String value, String pattern, Locale locale, TimeZone timeZone) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		formatter.setTimeZone(timeZone);
		cal.setTime(formatter.parse(value));
		computeLunar();
		return this;
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 지정된 패턴의 문자값으로 반환한다.
	 * @param value String
	 * @param in_pattern String
	 * @param out_pattern String
	 * @throws ParseException
	 * @return String
	 */
	public static String parseDate(String value, String in_pattern, String out_pattern) throws ParseException {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(in_pattern);
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(formatter.parse(value));
		formatter = new SimpleDateFormat(out_pattern);
		return formatter.format(tcal.getTime());
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 반환한다.
	 * @param value String
	 * @param pattern String
	 * @throws ParseException
	 * @return Date
	 */
	public static Date parseDate(String value, String pattern) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return tcal.getTime();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		tcal.setTime(formatter.parse(value));
		return tcal.getTime();
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @param locale Locale
	 * @throws ParseException
	 * @return Date
	 */
	public static Date parseDate(String value, String pattern, Locale locale) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return tcal.getTime();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		tcal.setTime(formatter.parse(value));
		return tcal.getTime();
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 반환한다.
	 * @param value String
	 * @param pattern String
	 * @throws ParseException
	 * @return Timestamp
	 */
	public static Timestamp parseTimestamp(String value, String pattern) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return new Timestamp(tcal.getTimeInMillis());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		tcal.setTime(formatter.parse(value));
		return new Timestamp(tcal.getTimeInMillis());
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @param locale Locale
	 * @throws ParseException
	 * @return Timestamp
	 */
	public static Timestamp parseTimestamp(String value, String pattern, Locale locale) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return new Timestamp(tcal.getTimeInMillis());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		tcal.setTime(formatter.parse(value));
		return new Timestamp(tcal.getTimeInMillis());
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 반환한다.
	 * @param value String
	 * @param pattern String
	 * @throws ParseException
	 * @return Time
	 */
	public static Time parseTime(String value, String pattern) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return new Time(tcal.getTimeInMillis());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		tcal.setTime(formatter.parse(value));
		return new Time(tcal.getTimeInMillis());
	}

	/**
	 * 지정된 문자열의 값을 지정된 패턴으로 해석하여 날자값을 세팅한다.
	 * @param value String
	 * @param pattern String
	 * @param locale Locale
	 * @throws ParseException
	 * @return Time
	 */
	public static Time parseTime(String value, String pattern, Locale locale) throws ParseException {
		Calendar tcal = Calendar.getInstance();
		if (value == null || "".equals(value.trim())) {
			return new Time(tcal.getTimeInMillis());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		tcal.setTime(formatter.parse(value));
		return new Time(tcal.getTimeInMillis());
	}

	public static int getYear(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.DATE);
	}

	public static int getHour(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.MINUTE);
	}

	public static int getSecond(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.SECOND);
	}
	
	public static int getMilliSecond(Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		return tcal.get(Calendar.MILLISECOND);
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param date Date
	 * @param pattern String
	 * @return String
	 */
	public static String formatDate(Date date, String pattern) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(tcal.getTime());
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param date Date
	 * @param pattern String
	 * @return String
	 */
	public static String formatDate(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param date Date
	 * @param pattern String
	 * @return String
	 */
	public static String formatDate(LocalDateTime date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}


	/**
	 * 시간값을 지정된 패턴으로 출력한다.
	 * @param time time
	 * @param pattern String
	 * @return String
	 */
	public static String formatDate(LocalTime time, String pattern) {
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 날자값을 지정된 패턴으로 출력한다.
	 * @param date Date
	 * @param pattern String
	 * @param locale Locale
	 * @return String
	 */
	public static String formatDate(Date date, String pattern, Locale locale) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(tcal.getTime());
	}

	/**
	 * 날짜 차이 값을 구한다.
	 * @param date long
	 * @return long
	 */
	public long diff(long date) {
		return diff(DateUtil.DAY, new Date(date));
	}

	/**
	 * 날짜 차이 값을 구한다.
	 * @param date DateUtil
	 * @return long
	 */
	public long diff(DateUtil date) {
		return diff(DateUtil.DAY, date.getTime());
	}

	/**
	 * 날짜 차이 값을 구한다.
	 * @param date Date
	 * @return long
	 */
	public long diff(Date date) {
		return diff(DateUtil.DAY, date);
	}

	/**
	 * 지정된 필드의 값에 대한 날짜 차이 값을 구한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @param date long
	 * @return long
	 */
	public long diff(int field, long date) {
		return diff(field, new Date(date));
	}

	/**
	 * 지정된 필드의 값에 대한 날짜 차이 값을 구한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @param date DateUtil
	 * @return long
	 */
	public long diff(int field, DateUtil date) {
		return diff(field, date.getTime());
	}

	/**
	 * 지정된 필드의 값에 대한 날짜 차이 값을 구한다.
	 * @param field int {@link #MILLISECOND},{@link #SECOND},{@link #MINUTE},{@link #HOUR},{@link #DAY},{@link #WEEK},{@link #MONTH},{@link #QUARTER},{@link #YEAR}
	 * @param date Date
	 * @return long
	 */
	public long diff(int field, Date date) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		switch (field) {
			case DateUtil.MILLISECOND:
			case DateUtil.SECOND:
			case DateUtil.MINUTE:
			case DateUtil.HOUR:
			case DateUtil.DAY:
			case DateUtil.WEEK:
				long diffTimeInMillis = Math.max(tcal.getTimeInMillis(), cal.getTimeInMillis()) - Math.min(tcal.getTimeInMillis(), cal.getTimeInMillis());
				return (long) Math.floor(diffTimeInMillis / field);
			case DateUtil.MONTH:
				int maxYear = Math.max(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR));
				int minYear = Math.min(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR));
				int thisMonth = this.getMonth() - 1;
				int targetMonth = tcal.get(Calendar.MONTH);
				if (maxYear == minYear) {
					return Math.max(thisMonth, targetMonth) - Math.min(thisMonth, targetMonth);
				} else {
					int monthInMaxYear = thisMonth;
					int monthInMinYear = targetMonth;
					if (minYear == cal.get(Calendar.YEAR)) {
						monthInMaxYear = targetMonth;
						monthInMinYear = thisMonth;
					}
					return ((maxYear - minYear - 1) * 12) + (12 - monthInMinYear) + monthInMaxYear;
				}
			case DateUtil.QUARTER:
				int maxQYear = Math.max(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR));
				int minQYear = Math.min(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR));
				int thisQMonth = this.getMonth() - 1;
				int targetQMonth = tcal.get(Calendar.MONTH);
				int thisQuarter = (int) Math.ceil((thisQMonth + 1) / 3);
				int targetQuarter = (int) Math.ceil((targetQMonth + 1) / 3);
				if (maxQYear == minQYear) {
					return Math.max(thisQuarter, targetQuarter) - Math.min(thisQuarter, targetQuarter);
				} else {
					int quarterInMaxYear = thisQuarter;
					int quarterInMinYear = targetQuarter;
					if (minQYear == cal.get(Calendar.YEAR)) {
						quarterInMaxYear = targetQuarter;
						quarterInMinYear = thisQuarter;
					}
					return (maxQYear - minQYear - 1) * 4 + (4 - quarterInMinYear) + quarterInMaxYear;
				}
			case DateUtil.YEAR:
				return Math.max(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR)) - Math.min(cal.get(Calendar.YEAR), tcal.get(Calendar.YEAR));
			default :
				break;
		}
		return 0;
	}

	/**
	 * 월을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public static Date yearAdd(Date date, int amount) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		tcal.add(Calendar.YEAR, amount);
		return tcal.getTime();
	}

	/**
	 * 년을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public static Date monthAdd(Date date, int amount) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		tcal.add(Calendar.MONTH, amount);
		return tcal.getTime();
	}

	/**
	 * 일을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public static Date dayAdd(Date date, int amount) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		tcal.add(Calendar.DATE, amount);
		return tcal.getTime();
	}
	
	/**
	 * 시간을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public static Date hourAdd(Date date, int amount) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		tcal.add(Calendar.HOUR_OF_DAY, amount);
		return tcal.getTime();
	}
	
	/**
	 * 분을 지정된 양만큼 더하거나 뺀다.
	 * @param amount int
	 * @return DateUtil
	 */
	public static Date minuteAdd(Date date, int amount) {
		Calendar tcal = Calendar.getInstance();
		tcal.setTime(date);
		tcal.add(Calendar.MINUTE, amount);
		return tcal.getTime();
	}
	
	public static boolean isValidFormat(String value, String format){
		try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(value);
            return true;
        } catch (ParseException ex) {
            return false;
        }
	}
}
