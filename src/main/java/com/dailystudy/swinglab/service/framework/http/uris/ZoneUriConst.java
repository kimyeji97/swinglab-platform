package com.dailystudy.swinglab.service.framework.http.uris;

/**
 * 타석 URI
 */
public class ZoneUriConst {

    /**
     * 타석
     */
    public static final String GET_ZONE_LIST = "/v1/zone"; // 타석 목록 조회
    public static final String GET_ZONE_DETAIL = "/v1/zone/{zoneId}"; // 타석 상세 조회


    /**
     * 예약 관련
     */
    public static final String GET_BOOKING_LIST = "/v1/booking/list"; // 전체 예약 현황 조회
    public static final String GET_ZONE_BOOK_LIST = "/v1/zone/{zoneId}/book"; // 해당 타석 예약 목록 조회
    public static final String POST_ZONE_BOOKABLE_CHECK = "/v1/zone/{zoneId}/bookable/check"; // 해당 타석 예약 가능한지 체크
    public static final String POST_ZONE_BOOK = "/v1/zone/{zoneId}/book"; // 해당 타석 예약
    public static final String PUT_ZONE_BOOK = "/v1/zone/{zoneId}/book/{bookId}"; // 해당 타석 예약 정보 변경
    public static final String PUT_ZONE_BOOK_CANCEL = "/v1/book/{bookId}/cancel"; // 해당 타석 예약 취소

    public static final String PUT_ZONE_BOOK_CHECK_IN ="/v1/book/{bookId}/check/in"; // 입실
    public static final String PUT_ZONE_BOOK_CHECK_OUT ="/v1/book/{bookId}/check/out"; // 퇴실



    /**
     * 이용 관련
     */
    public static final String GET_MY_USAGE_HIST_LIST = "/v1/my/usage/history"; // 내 이용내역 조회
    public static final String GEt_MY_USAGE_HIST_DETAIL = "/v1/my/usage/{bookId}/history"; // 내 이용내역 상세 조회

}
