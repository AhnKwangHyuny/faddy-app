package faddy.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    /**
     * @Title : 서버 에러
     * */
    FAIL_SAVE_ENTITY(500 , "엔티티 생성에 실패했습니다. "),


    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    NOT_FOUND_USER_ID(1001, ""),
    NOT_FOUND_DAY_LOG_ID(1002, "요청한 ID에 해당하는 데이로그가 존재하지 않습니다."),
    NOT_FOUND_TRIP_ITEM_ID(1003, "요청한 ID에 해당하는 여행 아이템이 존재하지 않습니다."),
    NOT_FOUND_EXPENSE_ID(1004, "요청한 ID에 해당하는 금액이 존재하지 않습니다."),

    /**
    * @Return enttiy exception
    *
    * */
    NOT_SAVE_USER(3000 , "회원가입 처리 중 서버 오류가 발생했습니다. 잠시 후 다시 시도 부탁드립니다."),
    FAIL_DELETE_ENTITY(3001 , "엔티티 삭제에 실패했습니다."),

    /**
     * @Return User info request exception : 4000~ 5000
     * */
    INVALID_INPUT_DATA(4001 , "유효하지 않은 데이터입니다."),
    DUPLICATED_INPUT_DATA(4002 , "이미 사용 중인 데이터 입니다."),

    INVALID_USER_ID (4003 , "유효하지 않은 아이디 입니다. "),
    DUPLICATED_USER_ID(4004 , "이미 사용 중인 아이디가 존재합니다."),

    INVALID_NICKNAME (4005 , "유효하지 않은 닉네임 입니다. "),

    DUPLICATED_NICKNAME(4006 , "이미 사용 중인 닉네임이 존재합니다."),

    /*
    * DB ACCESS ERROR
    * */
    DUPLICATE_EMAIL_REDIS(2001, "이미 사용 중인 이메일 입니다."),
    DUPLICATE_EMAIL_MYSQL(2002, "이미 사용 중인 이메일 입니다"),

    /*
     * email
     * */
    INVALID_EMAIL_FORMAT(5003 , "유효하지 않은 이메일 형식입니다."),

    /**
     * JWT VALIDATION ERROR
     * */
    EXPIRED_JWT_TOKEN(7001, "만료된 JWT 토큰입니다."),
    INVALID_JWT_TOKEN(7002 , "유효하지 않은 토큰입니다."),
    ERROR_TOKEN_CREATE(7010, "토큰 생성시 문제가 발생했습니다."),
    INVALID_REFRESH_TOKEN(7003 , "[ERROR] 유효하지 않은 Refresh Token입니다!"),
    INVALID_OR_EMPTY_JWT_TOKEN(7004, "유효하지 않거나 없는 JWT 토큰입니다."),
    TOKEN_OWNER_MISMATCH(7005, "[ERROR] 로그인한 사용자의 Refresh Token이 아닙니다!"),
    ALREADY_LOGOUT(7006, "[ERROR] 이미 로그아웃한 사용자입니다."),


    /* @@@ server exception : 6000~   @@@@*/

    /*
     * token & auth-code
     * */
    INVALID_TOKEN(6001, "유효하지 않은 토큰입니다."),
    INVALID_AUTH_CODE(6002, "유효하지 않은 인증 코드 입니다."),
    INVALID_EMAIL_AUTH_CODE(6003, "유효하지 않은 이메일 인증 코드 입니다."),
    INVALID_PASSWORD_AUTH_CODE(6004, "유효하지 않은 비밀번호 인증 코드 입니다."),

    /**
     *  server resource creating error
     * */
    TOKEN_GENERATION_ERROR(6010 , "인증 코드 확인에 실패했습니다. 재요청 부탁드립니다."),

    /**
     *  authentication , authorization exception error
     * */
    AUTHENTICATION_ERROR(8001 , "사용자 인증에 실패했습니다."),
    AUTHORIZATION_ERROR(8002 , "접근 권한이 없거나 만료되었습니다."),

    /**
     *  image에 대한 처리 과정 중 생기는 오류 메세지 모음
     * */
    NULL_IMAGE(9001, "이미지가 존재하지 않습니다."),
    INVALID_IMAGE_FORMAT(9002, "지원하지 않는 파일 형식입니다. JPEG 또는 PNG 파일만 업로드 가능합니다."),
    FILE_SIZE_TOO_LARGE(9003, "파일 크기가 너무 큽니다. 최대 크기: 10MB"),
    FAIL_IMAGE_NAME_HASH(9004, "fail image name hashing"),
    FAIL_IMAGE_UPLOADING(9005, "fail image uploading"),
    SAVE_IMAGE_ERROR(9006, "이미지 저장 중 에러가 발생했습니다."),
    IMAGE_CONVERT_ERROR( 9007 , "이미지 변환 중 예상치 못한 에러가 발생했습니다"),
    IMAGE_DECODE_ERROR(9008 , "Error while decoding the file name"),
    IMAGE_NOT_FOUND(9009 , "해당 이미지를 찾을 수 없습니다."),
    FAIL_DELETE_IMAGE_FILE(9010 , "s3에서 이미지 삭제에 실패했습니다."),
    FAIL_DELETE_IMAGE_INFO(9011 , "DB에서 이미지 삭제에 실패했습니다."),
    NOT_FOUND_IMAGE_CATEGORY(400 , "지원하지 않는 없는 이미지 카테고리 입니다."),

    /**
     *  대칭키 암호화 에러
     * */
    GENERATE_KEY_PAIR_ERROR(9100 ,"Failed to generate RSA key pair" ),
    ENCRYPT_USER_ID_ERROR(9101 , "Failed to encrypt user ID"),
    DECRYPT_USER_ID_ERROR(9102 , "Failed to decrypt user ID"),

    /**
     *  snap 예외 처리
     * */
    FAIL_CREATE_SNAP(500 , "서버 오류로 인해 snap 포스팅에 실패했습니다."),
    FAIL_PAGING_ERROR(500 , "썹네일 데이터 페이징에 실패했습니다. "),

    /**
     * Follow 에러
     * */
    FAIL_SELF_FOLLOW_ERROR(400 , "자기 자신을 팔로우 할 수 없습니다."),
    FAIL_FOLLOW_ERROR(400 , "팔로우에 실패했습니다."),
    ALREADY_FOLLOW_ERROR(400 , "이미 팔로우된 유저입니다."),

    /**
     * common
     * */
    MAPPING_ERROR_SNAP_TO_THUMBNAIL(500 , "Snap을 ThumbnailResponseDto로 변환하는데 실패했습니다."),

    /**
     * chat
     * */
    CHATROOM_NOT_FOUND_ERROR(400 , "해당 채팅방이 존재하지 않습니다."),
    CHAT_FOUND_ERROR(400 , "채팅을 불러오는 중 오류가 발생했습니다."),
    CHAT_SAVE_ERROR(500 , "메세지 전송에 실패했습니다. [생성 오류]"),


    /**
     * HashTag
     * */
    FAIL_CREATE_HASH_TAG(500 , "해시태그 엔티티 생성에 실패했습니다."),


    /**
     *  db async 동기화 에러
     *
     * */
    FAIL_DB_ASYNC_ERROR(500 , "DB 동기화 처리 중 에러가 발생했습니다.");



    private final int code;
    private final String message;

    ExceptionCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }





}
