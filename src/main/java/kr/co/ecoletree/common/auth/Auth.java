/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2017. 11. 01.
 * DESC : kr.co.ecoletree.common.auth Auth.java
*****************************************************************/
package kr.co.ecoletree.common.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name : Auth.java<br>
 * Description : 인증을 필요로 하는 컨트롤러 메소드에 @Auth 로 선언해서 적용<br>
 * value : 로그인 뒤 이동할 주소
 * msg : 로그인 페이지로 이동전 보여줄 메세지
 * 
 * @author dongsuk
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
@Documented
public @interface Auth {
	String value() default "";
	String msg() default "";//"로그인 후 이용가능합니다.";
}
