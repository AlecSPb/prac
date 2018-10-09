package com.example.user.dprac;

public class Constants {

    public static final String live_url = "http://orders.ekuep.com/api/";
    public static final String dev_url = "http://dev-orders.ekuep.com/api/";

    /**
     * Services of Login and Phone number Login
     */
    public static final String login_url =live_url+"login/driver";
    public static final String send_verification_code_url = live_url+"phone/send/code/mobile";
    public static final String verify_code_url= live_url+"phone/verification/mobile";

    /**
     * Service to get order details
     */
    public static final String get_order_detail_url = live_url+"get-order-product-details/";

    /**
     * Serivce to update status of order
     */
    public static final String update_order_status_url = live_url+"update-order-status";

    /**
     * Service to get assigned orders
     */

    public static final String get_assigned_orders = live_url+"get-assignees-orders/";

    /**
     * Constants to deal with login fragement
     */
    public static int login_fragment_position = 1;
    public static boolean homePressed = false;

    /**
     * Constants to deal with the inner fragments
     */
    public static int inner_fragment_position = 0;
}
