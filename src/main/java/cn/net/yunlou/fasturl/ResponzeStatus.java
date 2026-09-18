/*
 *
 *     Copyright (c) 2019 - forever, javaeer All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the haoheio.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * Author: javaeer (javaeer@aliyun.com)
 *
 */

package cn.net.yunlou.fasturl;

import cn.net.yunlou.common.beans.ICommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author laughtiger
 * @see org.springframework.http.HttpStatus
 * 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
 * 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
 * 202 ACCEPTED - [*]：表示一个请求已经进入后台排队（异步任务）
 * 204 NO CONTENT - [DELETE]：用户删除数据成功。
 * 423 LOCKED - "Locked" 被锁定
 * 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
 * 401 UNAUTHORIZED - [*]：表示用户没有权限（令牌、用户名、密码错误）。
 * 402 PAYMENT_REQUIRED -[*]:表示请求被拒绝，访问的资源需要付款
 * 403 FORBIDDEN - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
 * 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
 * 406 NOT ACCEPTABLE - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
 * 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
 * 422 UNPROCESABLE ENTITY - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
 * 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
 */
@AllArgsConstructor
public enum ResponzeStatus implements ICommonEnum<Integer> {
    SUCCESS(200, "Success"),

    ALREADY_EXISTS(22600, "Already Exists"),

    //400错误码衍生
    REQUEST_PARAM_OLD_PASSWORD_INVALID(40006, "Old password is Wrong"),
    REQUEST_PARAM_ENTERED_PASSWORDS_INVALID(40005, "Entered passwords differ"),

    REQUEST_PARAM_VALIDATE_CODE_INVALID(40004, "Validate Code Invalid"),
    REQUEST_PARAM_ACCESS_TOKEN_EXPIRED(40002, "AccessToken been expired."),
    REQUEST_PARAM_BLANK(40001, "Request Param Must be Not Blank"),
    REQUEST_PARAM_INVALID(40000, "Request Param Invalid"),

    //401错误衍生
    UNAUTHORIZED_BAD_SIGN(40106, "Unauthorized (Sign verification failed)"),
    UNAUTHORIZED_BAD_CREDENTIALS(40105, "Unauthorized (Bad Credentials)"),
    UNAUTHORIZED_GRANT_TYPE(40104, "Unauthorized (Bad Grant Type)"),
    UNAUTHORIZED_INVALID_EXPIRED(40103, "Unauthorized (token Invalid or Expired)"),
    UNAUTHORIZED_CLIENT(40102, "Unauthorized (clientId/clientSecret is Wrong)"),
    UNAUTHORIZED_ACCOUNT(40101, "Unauthorized (Account/Password is Wrong)"),
    UNAUTHORIZED(40100, "Unauthorized"),

    //402错误衍生
    PAYMENT_REQUIRED_PAYMENT(40205, "Payment Required(In Payment)"),
    PAYMENT_REQUIRED_DELIVERED(40204, "Payment Required(Already Delivered)"),
    PAYMENT_REQUIRED_NSF(40203, "Payment Required(Not Sufficient Funds)"),
    PAYMENT_REQUIRED_ALREADY(40202, "Payment Required(Already Payment)"),
    PAYMENT_REQUIRED_WAITING(40201, "Payment Required(Waiting Payment)"),
    PAYMENT_REQUIRED(40200, "Payment Required"),

    //403错误衍生
    FORBIDDEN_NO_PERMISSION(40302, "Forbidden(You Are no Permission)"),
    FORBIDDEN_LOCKED(40301, "Forbidden(You Are been Locked)"),
    FORBIDDEN(40300, "Forbidden"),

    //404错误衍生
    NOT_FOUND_SMS_TEMPLATE(40402, "Not Found Sms template"),
    NOT_FOUND_RECORD_ACCOUNT(40401, "Not Found account"),
    NOT_FOUND_RECORD(40400, "Not Found Record"),


    //405错误衍生
    METHOD_NOT_ALLOWED(40500, "Method Not Allowed"),

    //406错误衍生
    NOT_ACCEPTABLE(40600, "Not Acceptable"),

    //415错误衍生
    UNSUPPORTED_MEDIA_TYPE(41500, "Unsupported Media Type"),

    //423错误衍生
    LOCKED_CREDENTIALS_EXPIRED(42304, "Credentials been expired."),
    LOCKED_ACCOUNT_EXPIRED(42303, "Account been expired."),
    LOCKED_ACCOUNT_RELEASE(42302, "Account been locked and Waiting to be released."),
    LOCKED_ACCOUNT(42301, "Account been locked."),
    LOCKED(42300, "Locked"),


    //500错误衍生
    INTERNAL_SERVER_UPDATE_ERROR(50004, "Internal Server Error,Can't Update a Record"),
    INTERNAL_SERVER_DELETE_ERROR(50003, "Internal Server Error,Can't Delete a Record"),
    INTERNAL_SERVER_CREATE_ERROR(50002, "Internal Server Error,Can't Create a Record"),
    INTERNAL_SERVER_UNDEFINED_ERROR(50001, "Internal Server Undefined Error,Send massage for javaeer@aliyun.com"),
    INTERNAL_SERVER_ERROR(50000, "Internal Server Error,all controller method must be annotation by @Authorization ");

    @Getter
    private Integer value;

    @Getter
    private String label;
}
