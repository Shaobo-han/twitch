package com.laioffer.jupiter2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//这个class不要轻易运行，一般只用一次，因为它是 reset the database. 里面有drop已存在的过程
public class MySQLTableCreator {
    // 自带了main函数，可以不依赖于Tomcat来启动
    public static void main(String[] args) {
        try {

            // Step 1 Connect to MySQL. 这个过程非常固定，跟数据库链接都是这个过程！！

            System.out.println("Connecting to " + MySQLDBUtil.getMySQLAddress());
            //这句的功能是为了考虑一些Connor case，这个library有瑕疵！
            // 官方解释：The newInstance() call is a work around for som broken Java implementations.
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();//这个划线是，java版本超过8.0时，这个可以不写
            Connection conn = DriverManager.getConnection(MySQLDBUtil.getMySQLAddress());
            //注意，是直接调用了class MySQLDBUtil里的public static方法得到了数据库地址

            if (conn == null) {
                return;
            }

            // Step 2 Drop tables in case they exist.
            //注意！！！！，删除时有顺序，先drop有foreign key的，再drop它指向的table
            //          而创建时正相反！
            Statement statement = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS favorite_records";
            statement.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS items";
            statement.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS users";
            statement.executeUpdate(sql);


            // Step 3 Create new tables.
            //创建时也要注意顺序！！！
            sql = "CREATE TABLE items ("
                    + "id VARCHAR(255) NOT NULL,"
                    + "title VARCHAR(255),"
                    + "url VARCHAR(255),"
                    + "thumbnail_url VARCHAR(255),"
                    + "broadcaster_name VARCHAR(255),"
                    + "game_id VARCHAR(255),"
                    + "type VARCHAR(255) NOT NULL,"
                    + "PRIMARY KEY (id)"
                    + ")";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE users ("
                    + "id VARCHAR(255) NOT NULL,"
                    + "password VARCHAR(255) NOT NULL,"
                    + "first_name VARCHAR(255),"
                    + "last_name VARCHAR(255),"
                    + "PRIMARY KEY (id)"
                    + ")";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE favorite_records ("
                    + "user_id VARCHAR(255) NOT NULL,"
                    + "item_id VARCHAR(255) NOT NULL,"
                    //注意下这句话 TIMESTAMP NOT NULL DEFAULT
                    + "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (user_id, item_id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(id),"
                    + "FOREIGN KEY (item_id) REFERENCES items(id)"
                    + ")";
            statement.executeUpdate(sql);

            // Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050.
            sql = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
            statement.executeUpdate(sql);

            conn.close();
            System.out.println("Import done successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
