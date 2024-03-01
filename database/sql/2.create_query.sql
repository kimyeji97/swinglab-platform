-- 사용자
CREATE TABLE TB_USER
(
    USER_ID    SID       NOT NULL, -- 사용자ID
    LOGIN_ID   VARCHAR(50)  NOT NULL, -- 로그인ID
    PWD        VARCHAR(255) NOT NULL, -- 비밀번호
    NAME       VARCHAR(50)  NOT NULL, -- 이름
    NICK_NM    VARCHAR(50)  NULL,     -- 닉네임
    SIGNUP_DT  TIMESTAMP    NULL,     -- 회원가입일시
    SVC_ST_DAY DATE         NULL,     -- 서비스시작일
    SVC_ED_DAY DATE         NULL,     -- 서비스종료일
    DEL_YN     BOOLEAN      NOT NULL DEFAULT FALSE, -- 삭제여부
    REG_DT     TIMESTAMP    NOT NULL DEFAULT NOW(), -- 등록일시
    UPD_DT     TIMESTAMP    NULL      -- 수정일시
);

-- 사용자
COMMENT ON TABLE TB_USER IS '사용자';

-- 사용자ID
COMMENT ON COLUMN TB_USER.USER_ID IS '사용자ID';

-- 로그인ID
COMMENT ON COLUMN TB_USER.LOGIN_ID IS '로그인ID';

-- 비밀번호
COMMENT ON COLUMN TB_USER.PWD IS '비밀번호';

-- 이름
COMMENT ON COLUMN TB_USER.NAME IS '이름';

-- 닉네임
COMMENT ON COLUMN TB_USER.NICK_NM IS '닉네임';

-- 회원가입일시
COMMENT ON COLUMN TB_USER.SIGNUP_DT IS '회원가입일시';

-- 서비스시작일
COMMENT ON COLUMN TB_USER.SVC_ST_DAY IS '서비스시작일';

-- 서비스종료일
COMMENT ON COLUMN TB_USER.SVC_ED_DAY IS '서비스종료일';

-- 삭제여부
COMMENT ON COLUMN TB_USER.DEL_YN IS '삭제여부';

-- 등록일시
COMMENT ON COLUMN TB_USER.REG_DT IS '등록일시';

-- 수정일시
COMMENT ON COLUMN TB_USER.UPD_DT IS '수정일시';

-- 사용자 기본키
CREATE UNIQUE INDEX PK_TB_USER
    ON TB_USER
        ( -- 사용자
         USER_ID ASC -- 사용자ID
            )
;
-- 사용자
ALTER TABLE TB_USER
    ADD CONSTRAINT PK_TB_USER
        -- 사용자 기본키
        PRIMARY KEY
    USING INDEX PK_TB_USER;

-- 사용자 기본키
COMMENT ON CONSTRAINT PK_TB_USER ON TB_USER IS '사용자 기본키';

-- 타석
CREATE TABLE TB_SWING_ZONE
(
    ZONE_ID SID      NOT NULL, -- 타석ID
    ZONE_NM VARCHAR(50) NOT NULL, -- 타석명
    REG_DT  TIMESTAMP   NOT NULL DEFAULT NOW(), -- 등록일시
    UPD_DT  TIMESTAMP   NULL      -- 수정일시
);

-- 타석
COMMENT ON TABLE TB_SWING_ZONE IS '타석';

-- 타석ID
COMMENT ON COLUMN TB_SWING_ZONE.ZONE_ID IS '타석ID';

-- 타석명
COMMENT ON COLUMN TB_SWING_ZONE.ZONE_NM IS '타석명';

-- 등록일시
COMMENT ON COLUMN TB_SWING_ZONE.REG_DT IS '등록일시';

-- 수정일시
COMMENT ON COLUMN TB_SWING_ZONE.UPD_DT IS '수정일시';

-- 타석 기본키
CREATE UNIQUE INDEX PK_TB_SWING_ZONE
    ON TB_SWING_ZONE
        ( -- 타석
         ZONE_ID ASC -- 타석ID
            )
;
-- 타석
ALTER TABLE TB_SWING_ZONE
    ADD CONSTRAINT PK_TB_SWING_ZONE
        -- 타석 기본키
        PRIMARY KEY
    USING INDEX PK_TB_SWING_ZONE;

-- 타석 기본키
COMMENT ON CONSTRAINT PK_TB_SWING_ZONE ON TB_SWING_ZONE IS '타석 기본키';

-- 이용 이력
CREATE TABLE TB_USAGE_HIST
(
    BOOK_ID         SID    NOT NULL, -- 예약ID
    CHK_IN_DT       TIMESTAMP NULL,     -- 입실일시
    CHK_OUT_DT      TIMESTAMP NULL,     -- 퇴실일시
    AUTO_CHK_OUT_YN BOOLEAN   NULL,     -- 자동퇴실여부
    REG_DT          TIMESTAMP NOT NULL DEFAULT NOW(), -- 등록일시
    UPD_DT          TIMESTAMP NULL      -- 수정일시
);

-- 이용 이력
COMMENT ON TABLE TB_USAGE_HIST IS '이용 이력';

-- 예약ID
COMMENT ON COLUMN TB_USAGE_HIST.BOOK_ID IS '예약ID';

-- 입실일시
COMMENT ON COLUMN TB_USAGE_HIST.CHK_IN_DT IS '입실일시';

-- 퇴실일시
COMMENT ON COLUMN TB_USAGE_HIST.CHK_OUT_DT IS '퇴실일시';

-- 자동퇴실여부
COMMENT ON COLUMN TB_USAGE_HIST.AUTO_CHK_OUT_YN IS '자동퇴실여부';

-- 등록일시
COMMENT ON COLUMN TB_USAGE_HIST.REG_DT IS '등록일시';

-- 수정일시
COMMENT ON COLUMN TB_USAGE_HIST.UPD_DT IS '수정일시';

-- 이용 이력 기본키
CREATE UNIQUE INDEX PK_TB_USAGE_HIST
    ON TB_USAGE_HIST
        ( -- 이용 이력
         BOOK_ID ASC -- 예약ID
            )
;
-- 이용 이력
ALTER TABLE TB_USAGE_HIST
    ADD CONSTRAINT PK_TB_USAGE_HIST
        -- 이용 이력 기본키
        PRIMARY KEY
    USING INDEX PK_TB_USAGE_HIST;

-- 이용 이력 기본키
COMMENT ON CONSTRAINT PK_TB_USAGE_HIST ON TB_USAGE_HIST IS '이용 이력 기본키';

-- API로그
CREATE TABLE TB_API_LOG
(
    TX_ID         VARCHAR(36)  NOT NULL, -- 트랜젝션ID
    REQ_DT        TIMESTAMP    NOT NULL, -- 요청일시
    CLINT_IP      VARCHAR(15)  NOT NULL, -- 클라이언트 IP
    API           VARCHAR(100) NOT NULL, -- API
    REQ_BODY      TEXT         NULL,     -- 요청Body
    RES_DT        TIMESTAMP    NULL,     -- 응답일시
    RES_YN        BOOLEAN      NULL,     -- 응답성공여부
    HTTP_STTUS_CD INTEGER      NULL,     -- HTTP코드
    RES_CD        VARCHAR(50)  NULL,     -- 응답코드
    RES_BODY      TEXT         NULL,     -- 응답Body
    RES_BODY_LEN  INTEGER      NULL,     -- 응답길이
    REG_DT        TIMESTAMP    NOT NULL DEFAULT NOW(), -- 등록일시
    UPD_DT        TIMESTAMP    NULL      -- 수정일시
);

-- API로그
COMMENT ON TABLE TB_API_LOG IS 'API로그';

-- 트랜젝션ID
COMMENT ON COLUMN TB_API_LOG.TX_ID IS '트랜젝션ID';

-- 요청일시
COMMENT ON COLUMN TB_API_LOG.REQ_DT IS '요청일시';

-- 클라이언트 IP
COMMENT ON COLUMN TB_API_LOG.CLINT_IP IS '클라이언트 IP';

-- API
COMMENT ON COLUMN TB_API_LOG.API IS 'API';

-- 요청Body
COMMENT ON COLUMN TB_API_LOG.REQ_BODY IS '요청Body';

-- 응답일시
COMMENT ON COLUMN TB_API_LOG.RES_DT IS '응답일시';

-- 응답성공여부
COMMENT ON COLUMN TB_API_LOG.RES_YN IS '응답성공여부';

-- HTTP코드
COMMENT ON COLUMN TB_API_LOG.HTTP_STTUS_CD IS 'HTTP코드';

-- 응답코드
COMMENT ON COLUMN TB_API_LOG.RES_CD IS '응답코드';

-- 응답Body
COMMENT ON COLUMN TB_API_LOG.RES_BODY IS '응답Body';

-- 응답길이
COMMENT ON COLUMN TB_API_LOG.RES_BODY_LEN IS '응답길이';

-- 등록일시
COMMENT ON COLUMN TB_API_LOG.REG_DT IS '등록일시';

-- 수정일시
COMMENT ON COLUMN TB_API_LOG.UPD_DT IS '수정일시';

-- API로그 기본키
CREATE UNIQUE INDEX PK_TB_API_LOG
    ON TB_API_LOG
        ( -- API로그
         TX_ID ASC -- 트랜젝션ID
            )
;
-- API로그
ALTER TABLE TB_API_LOG
    ADD CONSTRAINT PK_TB_API_LOG
        -- API로그 기본키
        PRIMARY KEY
    USING INDEX PK_TB_API_LOG;

-- API로그 기본키
COMMENT ON CONSTRAINT PK_TB_API_LOG ON TB_API_LOG IS 'API로그 기본키';

-- 예약 이력
CREATE TABLE TB_BOOK_HIST
(
    BOOK_ID      SID    NOT NULL, -- 예약ID
    USER_ID      BIGINT    NOT NULL, -- 사용자ID
    ZONE_ID      BIGINT    NOT NULL, -- 타석ID
    BOOK_DAY     DATE      NOT NULL, -- 예약일
    BOOK_ST_TIME TIME      NOT NULL, -- 예약시작시간
    BOOK_ED_TIME TIME      NOT NULL, -- 예약끝시간
    BOOK_CNCL_YN BOOLEAN   NOT NULL DEFAULT FALSE, -- 예약취소여부
    REG_DT       TIMESTAMP NOT NULL DEFAULT NOW(), -- 등록일시
    UPD_DT       TIMESTAMP NULL      -- 수정일시
);

-- 예약 이력
COMMENT ON TABLE TB_BOOK_HIST IS '예약 이력';

-- 예약ID
COMMENT ON COLUMN TB_BOOK_HIST.BOOK_ID IS '예약ID';

-- 사용자ID
COMMENT ON COLUMN TB_BOOK_HIST.USER_ID IS '사용자ID';

-- 타석ID
COMMENT ON COLUMN TB_BOOK_HIST.ZONE_ID IS '타석ID';

-- 예약일
COMMENT ON COLUMN TB_BOOK_HIST.BOOK_DAY IS '예약일';

-- 예약시작시간
COMMENT ON COLUMN TB_BOOK_HIST.BOOK_ST_TIME IS '예약시작시간';

-- 예약끝시간
COMMENT ON COLUMN TB_BOOK_HIST.BOOK_ED_TIME IS '예약끝시간';

-- 예약취소여부
COMMENT ON COLUMN TB_BOOK_HIST.BOOK_CNCL_YN IS '예약취소여부';

-- 등록일시
COMMENT ON COLUMN TB_BOOK_HIST.REG_DT IS '등록일시';

-- 수정일시
COMMENT ON COLUMN TB_BOOK_HIST.UPD_DT IS '수정일시';

-- 예약 이력 기본키
CREATE UNIQUE INDEX PK_TB_BOOK_HIST
    ON TB_BOOK_HIST
        ( -- 예약 이력
         BOOK_ID ASC -- 예약ID
            )
;
-- 예약 이력
ALTER TABLE TB_BOOK_HIST
    ADD CONSTRAINT PK_TB_BOOK_HIST
        -- 예약 이력 기본키
        PRIMARY KEY
    USING INDEX PK_TB_BOOK_HIST;

-- 예약 이력 기본키
COMMENT ON CONSTRAINT PK_TB_BOOK_HIST ON TB_BOOK_HIST IS '예약 이력 기본키';

-- 이용 이력
ALTER TABLE TB_USAGE_HIST
    ADD CONSTRAINT FK_TB_BOOK_HIST_TO_TB_USAGE_HIST
        -- 예약 이력 -> 이용 이력
        FOREIGN KEY (
                     BOOK_ID -- 예약ID
            )
            REFERENCES TB_BOOK_HIST ( -- 예약 이력
                                     BOOK_ID -- 예약ID
                );

-- 예약 이력 -> 이용 이력
COMMENT ON CONSTRAINT FK_TB_BOOK_HIST_TO_TB_USAGE_HIST ON TB_USAGE_HIST IS '예약 이력 -> 이용 이력';

-- 예약 이력
ALTER TABLE TB_BOOK_HIST
    ADD CONSTRAINT FK_TB_SWING_ZONE_TO_TB_BOOK_HIST
        -- 타석 -> 예약 이력
        FOREIGN KEY (
                     ZONE_ID -- 타석ID
            )
            REFERENCES TB_SWING_ZONE ( -- 타석
                                      ZONE_ID -- 타석ID
                );

-- 타석 -> 예약 이력
COMMENT ON CONSTRAINT FK_TB_SWING_ZONE_TO_TB_BOOK_HIST ON TB_BOOK_HIST IS '타석 -> 예약 이력';

-- 예약 이력
ALTER TABLE TB_BOOK_HIST
    ADD CONSTRAINT FK_TB_USER_TO_TB_BOOK_HIST
        -- 사용자 -> 예약 이력
        FOREIGN KEY (
                     USER_ID -- 사용자ID
            )
            REFERENCES TB_USER ( -- 사용자
                                USER_ID -- 사용자ID
                );

-- 사용자 -> 예약 이력
COMMENT ON CONSTRAINT FK_TB_USER_TO_TB_BOOK_HIST ON TB_BOOK_HIST IS '사용자 -> 예약 이력';