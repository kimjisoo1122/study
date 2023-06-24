package com.study.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionTest {

    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";

    public Connection getConnection() throws Exception{

        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return conn;
    }


    /**
     *      // 다운로드 취약점 살필 것 이미지가 뜨지 말게 하고 바로 다운로드로
     *                 // 파일 첨부 3개 까지
     *                 // 카테고리 db에서 관리
     *                 // 유효성 js java 모두 유효성 검증 실패 시 현 페이지유지
     *                 // 비밀번호 확인 레이어  -> 삭제할때 수정은 필요 x
     *                 // 쉬운건 보기, 등록
     *                 // 기획서 정독 후 erd 설계부터
     */

}
