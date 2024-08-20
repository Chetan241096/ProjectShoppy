package com.shoppy.global;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface Constants {
    String BASE_URL = "http://192.168.0.103:8000/";
    String RELEVANT_URL = "secound_url";

    String MESSAGE_KEY = "message";
    String STATUS_CODE_KEY = "status";
    String SUCCESS_KEY = "success";

    String USER_ID_KEY = "userId";
    String MOBILE_NUMBER_KEY = "mobileNumber";
    String CALLED_FROM_KEY = "CALLED_FROM";
    String PASSWORD_KEY = "password_key";

    String GRANT_TYPE = "grant_type";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String POST = "POST";
    String GET = "GET";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String MULTIPART = "Multipart";
    int BLOCK_USER_CODE = 413;

    String ORDER_PENDING = "Pending";
    String ORDER_CONFIRMED = "Confirmed";
    String ORDER_SHIPPED = "Dispatched";
    String ORDER_COMPLETED = "Completed";
    String ORDER_CANCELED = "Canceled";

    String REGISTRATION_URL = "public/registerUser";
    String LOGIN_URL = "oauth/token";
    String OTP_SEND_URL = "public/sendOtp";
    String OTP_VERIFY_URL = "public/verifyOtp";
    String FORGOT_PASSWORD_URL = "public/forgotPassword";
    String GET_USER_BY_PHONE = "public/getUserByPhone";

    String SORT_TYPE_PRICE_ACS = "asc";
    String SORT_TYPE_PRICE_DESC = "desc";
    String PRODUCT_WALL_URL = "api/getProductWall";
    String GET_PRODUCT_BY_CATEGORIES = "api/getProductsByCategory";
    String GET_PRODUCT_DETAILS = "api/getProductDetails";
    String CART_CRUD_URL = "api/getUserCart";
    String LOGOUT_URL = "api/logout";
    String UPDATE_USER_URL = "api/updateUser";
    String GET_ALL_SUB_CATEGORIES_URL = "api/getCategories";


    String USER_ORDER_CRUD_URL = "api/getOrder";
    String ORDER_CRUD_URL = "api/order";
    String CANCEL_ORDER_URL = "api/cancelOrderByUser";
    String GET_PRODUCT_REVIEW_URL = "api/reviews";
    String CHANGE_PASSWORD_URL = "public/changePassword";
    String GET_USER_HOME = "api/getUserHome";
    String SEARCH_PRODUCT_URL = "api/searchProduct";

    String ADD_TO_CART_URL = "api/addToCart";
    String ADD_ADDRESS_URL = "api/addUserAddress";
    String UPDATE_ADDRESS_URL = "api/updateUserAddress";
    String PLACE_ORDER = "api/placeOrder";
    String UPDATE_QUANTITY_IN_CART = "api/updateCart";
    String DELETE_FROM_CART = "api/deleteFromCart";
//    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;
//    public static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
//            "AMEX",
//            "DISCOVER",
//            "JCB",
//            "MASTERCARD",
//            "VISA");
//    public static final List<String> SUPPORTED_METHODS =
//            Arrays.asList(
//                    "PAN_ONLY",
//                    "CRYPTOGRAM_3DS");
//    public static final String CURRENCY_CODE = "AUD";
//    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList("AU");
//    public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";
//    public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS =
//            new HashMap<String, String>() {
//                {
//                    put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
//                    put("gatewayMerchantId", "exampleGatewayMerchantId");
//                    // Your processor may require additional parameters.
//                }
//            };
//    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";
//    public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS =
//            new HashMap<String, String>() {
//                {
//                    put("protocolVersion", "ECv2");
//                    put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
//                }
//            };
//    String PAYPAL_CLIENT_ID = "AW56LgFS_rASUO3eDU9UBeyFE4imFYP1nFFYlXBaegdaOsHrOOmyPKyksar8Vy_RZLcqiDPEi_joQOF7";Live
    String PAYPAL_CLIENT_ID = "AajDpD2kXyYRapQdvy0UtwsQbrWq5IPikbcXolhcPd30wYf-vJj3Yu30L-yonLpHQVzfCCrXvKh-QeVS";//Sand box(test)

    String GET_ORDER_DETAILS = "api/getOrderDetails";
    String ADD_REVIEW_RATING = "api/addRatingReview";
    String GET_REVIEWS = "api/getUserRatingReview";
    String CALL_BUY_NOW = "api/buyNowProduct";
}
