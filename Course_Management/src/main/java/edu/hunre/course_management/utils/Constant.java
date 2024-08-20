package edu.hunre.course_management.utils;




public class Constant {
    public static final String ROLE_ADMIN="ADMIN";
    public static final String ROLE_USER="USER";
    public static final String ROLE_EMPLOYEE="EMPLOYEE";

    public class HTTP_MESSAGE{
        public static final String SUCCESS="request successfully";

        public static final String FAILED="request failed";

        public static final String FAILEDPW="The old password is incorrect";
        public static final String CHECKPASSWORD="The new password must not be the same as the old password";
        public static final String EXITS_ITEM="Item already exists";

    }

    public static String VNP_TMN_CODE = "B0L2A3BX";
    public static String VNP_HASH_SECRET = "AHXYEVPIZJZDUWCKCTQVRPIMZTDMGMFT";
    public static String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String VNP_PAY_VERSION = "2.1.0";

}
