export const PROD = process.env.NODE_ENV === 'production';
export const DEV = process.env.NODE_ENV === 'development';

export const DEVICE = process.env.NODE_DEVICE  === 'DESKTOP';


export const API_PATH = '/api';
export const API_USER = '/api/v1/users';
export const API_AUTH = '/api/v1/auths';
export const API_AUTH_CODE = '/api/v1/auth-codes';

export const API_IMAGE = '/api/v1/images';

export const API_HASH_TAG = 'api/v1/hashTags';

export const API_SNAP  = 'api/v1/snaps';

export const API_STYLE_BOARD = 'api/v1/styleBoards';

export const API_CATEGORY  = 'api/v1/categories';

export const API_LIKE = 'api/v1/likes';

export const API_TALK = 'api/v1/rooms';
export const API_CHAT = 'api/v1/chats';

export const BASE_URL = DEV
  ? DEVICE ? 'http://localhost:9000' : 'http://172.30.1.94:9000'
  : `${window.location.protocol};//${process.env.AXIOS_PROD_BASE_URL}`

export const AXIOS_BASE_URL = DEV
  ? DEVICE ? 'http://localhost:9000' : 'http://172.30.1.94:9000'
  : `${window.location.protocol};//${process.env.AXIOS_PROD_BASE_URL}`


export const END_POINTS = {
  USERS: '/users',

  // API REQUEST URL
  // email API
  GET_EMAIL_AUTH_CODE: `${API_AUTH_CODE}`,
  VERIFY_AUTH_CODE : `${API_AUTH_CODE}/verify`,
  DELETE_AUTH_CODE : `${API_AUTH_CODE}`,


  //USER API
  CHECK_USER_ID : `${API_USER}/check-duplication/userId`,
  CHECK_USER_NICKNAME :`${API_USER}/check-duplication/nickname`,
  GET_USER_ID : `${API_USER}/userId`,
  CHECK_USER_ID_EXISTS : `${API_USER}/check-userId`,

  //user profile
  GET_PROFILE_IN_CHATROOM: (roomId, username) => `${API_TALK}/${roomId}/users/${username}/profile`,
  GET_ALL_PROFILES_IN_CHATROOM: (roomId) => `${API_TALK}/${roomId}/users/profiles`,
  DELETE_PROFILE_IN_CHATROOM: (roomId, username) => `${API_TALK}/${roomId}/users/${username}/profile`,
  DELETE_ALL_PROFILES_IN_CHATROOM: (roomId) => `${API_TALK}/${roomId}/users/profiles`,

  //token API
  REISSUE_TOKEN : `${API_AUTH}/reissue-token`,
  VERIFY_TOKEN : `${API_AUTH}/verify-token`,

  //SIGN UP API
  SIGN_UP : `${API_USER}`,

  //User Login API
  LOGIN : `${API_USER}/login`,

  //Images
  UPLOAD_IMAGES : `${API_IMAGE}/multiple`,
  UPLOAD_IMAGE : (category) => `${API_IMAGE}?category=${category}`,
  DELETE_IMAGE : `${API_IMAGE}`,
  DELETE_IMAGES : `${API_IMAGE}/multiple`,


  //HashTag
  UPLOAD_HASH_TAG : `${API_HASH_TAG}`,

  //SNAP
  POST_SNAP : `${API_SNAP}`,
  UPDATE_SNAP_IMAGE_RELATIONSHIP: `${API_IMAGE}/images/link`,
  SNAP_DETAIL: `${API_SNAP}/detail`,
  MORE_THUMBNAILS: (page) => `${API_SNAP}/thumbnails?page=${page + 1}`,

  //Category
  UPLOAD_CATEGORY : `${API_CATEGORY}`,

  //like request
  LIKE_CLICK: (snapId) => `${API_LIKE}/${snapId}`,
  UNLIKE_CLICK: (snapId) => `${API_LIKE}/${snapId}`,
  GET_LIKE_COUNT: (snapId) => `${API_LIKE}/${snapId}`,

  //follow
  FOLLOW : `${API_USER}/follow`,
  UNFOLLOW : (toUsername) => `${API_USER}/follow/${toUsername}`,
  CHECK_FOLLOW : (toUsername) => `${API_USER}/follow/${toUsername}`,
  GET_FOLLOWING : (userId) => `${API_USER}/${userId}/following`,
  GET_FOLLOWERS : (userId) => `${API_USER}/${userId}/follower`,

  // Following and Followers (test용)
  GET_RELATIONSHIPS : (userId) => `${API_USER}/${userId}/relationships`,
  //location
  SNAP_DETAIL_LOCATION : `/snaps/detail`,

  //styleTalks
  GET_ALL_TALKS: (type, page) => `${API_TALK}?type=${type}&page=${page + 1}`,
  CREATE_CHAT_ROOM : `${API_TALK}`,
  CHECK_CHAT_ROOM_ACCESS : (roomId) => `${API_TALK}/${roomId}/validate-user`,

  //chats
  GET_CHATS : (roomId) => `${API_CHAT}/${roomId}/messages`,
  POST_CHAT : (roomId) => `${API_CHAT}/${roomId}`,

  //StyleBoard

  //StyleBoard
  CREATE_STYLE_BOARD : `${API_STYLE_BOARD}/create`,
  GET_STYLE_BOARD_DETAIL : (styleBoardId, category) => `${API_STYLE_BOARD}/detail/${styleBoardId}?category=${category}`,
  CHECK_IS_STYLE_BOARD_OWNER : (styleBoardId) => `${API_STYLE_BOARD}/detail/${styleBoardId}/check-owner`,

  CREATE_STYLE_BOARD_COMMENT : (styleBoardId) => `${API_STYLE_BOARD}/detail/${styleBoardId}/comments`,
  CREATE_STYLE_BOARD_COMMENT_REPLY : (styleBoardId, commentId) => `${API_STYLE_BOARD}/detail/${styleBoardId}/comments/${commentId}/replies`,
  GET_STYLE_BOARD_COMMENTS : (styleBoardId) => `${API_STYLE_BOARD}/detail/${styleBoardId}/comments`,

  GET_STYLE_BOARDS : `${API_STYLE_BOARD}`,
  GET_STYLE_BOARD_DATA : (boardId) => `${API_STYLE_BOARD}/${boardId}`,
  GET_STYLE_BOARD_EDIT_DATA : (boardId) => `${API_STYLE_BOARD}/${boardId}/edit`,
  UPDATE_STYLE_BOARD_DATA : (boardId) => `${API_STYLE_BOARD}/${boardId}`,
  DELETE_STYLE_BOARD : (boardId) => `${API_STYLE_BOARD}/${boardId}`,

  /**
  *  좋아요 요청
  *
  */
  CREATE_LIKE : (objectType , objectId) => `${API_LIKE}?objectType=${objectType}&objectId=${objectId}`,
  CANCEL_LIKE : (objectType , objectId) => `${API_LIKE}?objectType=${objectType}&objectId=${objectId}`,
};


export const NETWORK = {
  RETRY_COUNT: 2,
  TIMEOUT: 15000,
};

export const HTTP_STATUS_CODE = {
  SUCCESS: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  NOT_FOUND: 404,
  CONTENT_TOO_LARGE: 413,
  INTERNAL_SERVER_ERROR: 500,
};

export const ERROR_CODE = {
  DUPLICATED_NICKNAME: 1013,
  LARGE_IMAGE_FILE: 5001,
  TOKEN_ERROR_RANGE: 9000,
  INVALID_REFRESH_TOKEN: 9101,
  INVALID_ACCESS_TOKEN: 9102,
  EXPIRED_REFRESH_TOKEN: 9103,
  EXPIRED_ACCESS_TOKEN: 9104,
  INVALID_TOKEN_VALIDATE: 9105,
  NULL_REFRESH_TOKEN: 9106,
  UNAUTHORIZED: 9201,
  UNEXPECTED_TOKEN_ERROR: 9999,
};

export const HTTP_ERROR_MESSAGE = {
  [HTTP_STATUS_CODE.NOT_FOUND]: {
    HEADING: '스타일이 빠진 곳',
    BODY: '요청하신 페이지를 찾을 수 없습니다.',
    BUTTON: '홈으로 가기',
  },
  [HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR]: {
    HEADING: '현재 페이지를 표시할 수 없습니다.',
    BODY: `잠시 후 다시 시도해주세요.`,
    BUTTON: '새로고침',
  },
  [HTTP_STATUS_CODE.BAD_REQUEST]: {
    HEADING: '잘못된 요청입니다.',
    BODY: '확인 후 다시 시도해주세요.',
    BUTTON: '홈으로 가기',
  },
};

export const ERROR_MESSAGE = '오류가 발생했습니다. 잠시 후 다시 시도해주세요.';

export const ACCESS_TOKEN_KEY = 'ACCESS_TOKEN';

