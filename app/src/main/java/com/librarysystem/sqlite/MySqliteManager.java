package com.librarysystem.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by g on 2016/11/21.
 */
public class MySqliteManager extends SQLiteOpenHelper {
    /**
     * PersonalMessage建表语句
     */
    private static final String CREATE_PERSONALMES = "create table PersonalMessages(user_id integer primary" +
            "key ,user_object text,user_password text,user_name text,user_sex text,user_profession text,user_description text," +
            "user_tel text,user_level integer,user_pastbooks integer,user_wpastbooks integer,rootmanager text,now_borrow integer)";

    /**
     * repertory建表语句
     */
    private static final String CREATE_REPERTORY = "create table BookRepertory(book_id integer primary" +
            "key ,book_object text,book_name text,book_author text,book_version text,book_press,book_status text,back_time text,book_description text,book_subscribe text,book_continue text,lent_time text)";
    /**
     * present建表语句
     */
    // private static final String CREATE_PRESENT = "create table PresentBooks(book_id integer primary" +
    //        "key ,reader_id integer ,book_name text,book_author text,book_version text,book_press,book_description text,borrow_start text,back_time text,book_subscribe text,book_continue text)";

    /**
     * passed建表语句
     */
    // private static final String CREATE_PAST = "create table PastBooks(book_id integer primary" +
    //        "key ,reader_id integer ,book_name text,book_author text,book_version text,book_press text,book_description text,borrow_start text,back_time text)";
//
    //   private static final String CREATE_BOOKS = "insert into BookRepertory(book_id,book_name,book_author," +
    //   "book_version,book_press,book_status,book_description,book_subscribe,book_continue)values(?,?,?,?,?,?,?,?,?)";
    // private static final String CREATE_USER = "insert into PersonalMessages(user_id,user_password,user_name," +
    //         "user_sex,user_profession,user_description,user_tel," +
    //       "user_level,user_pastbooks,user_wpastbooks,rootmanager,now_borrow)values(?,?,?,?,?,?,?,?,?,?,?,?)";
    public MySqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSONALMES);
        db.execSQL(CREATE_REPERTORY);
        //      db.execSQL(CREATE_PRESENT);
        //       db.execSQL(CREATE_PAST);
//        db.execSQL(CREATE_BOOKS, new String[]{"1001", "西游记", "吴承恩", "第三版", "清华大学出版社", "可借", "明清四大名著之一的神话小说", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1002", "红楼梦", "曹雪芹", "第2版", "北京大学出版社", "可借", "明清四大名著之一", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1004", "三国演义", "罗贯中", "第1版", "清华大学出版社", "可借", "明清四大名著之一，魏蜀吴三国鼎立", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1005", "聊斋志异", "蒲松龄", "第5版", "清华大学出版社", "可借", "志怪狐鬼，古代短篇小说巅峰之作", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1006", "java基础教程", "老蒋", "第7版", "北京大学出版社", "可借", "java初学者入门首先", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1007", "赵孟頫行书教程", "罗培源", "第1版", "长江出版社", "可借", "中国书法培训教程", "无", "无"});
//
//        db.execSQL(CREATE_BOOKS, new String[]{"1011", "第一行代码", "郭霖", "第1版", "人民邮电出版社", "可借", "本书是Android初学者的最佳入门之书。全书由浅及深系统全面地讲解了Android软件开发的方方面面。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1202", "心理学导论", "Dennis To Paychology", "第9版", "中国轻工业出版社", "可借", "思想与行为的认识之路。本书有三个特点：1.深入浅出、生动活泼的写作风格使得外行人也一读即懂；2.书在编写的同时也运用了心理学的学习原则，使读者能够掌握有效的学习技巧和科学的学习方法，易于理解记忆；3.每一章末都练习，和应用篇以及探索篇，学以致用。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1034", "真草千字文", "孙宝文", "第1版", "吉林文史出版社", "可借", "赵孟頫墨迹精品选，含楷书和草书两种字体。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1045", "Java核心技术 卷一", "Cay S.Horstmann  Gary Cornell", "第9版", "机械工业出版社", "可借", "java领域最有影响力和价值的著作之一，根据Java SE 7全面更新，系统讲解java语言的核心概念、语法、重要特性和开发方法，包含大量的案例，实践性强，", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1056", "神经网络与机器学习", "Simon Haykin", "第3版", "机械工业出版社", "可借", "一本关于神经网络的全面的、彻底的、可读性很强的最新论述。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1027", "道德经", "老子", "第1版", "吉林出版社", "可借", "丰富的辩证思想，人类哲学的两个源头之一，中国历史上首部完整的哲学著作。", "无", "无"});
//
//        db.execSQL(CREATE_BOOKS, new String[]{"1031", "周易", "伏羲/周文王", "第1版", "吉林出版社", "可借", "一部古老而又灿烂的文化瑰宝，最能体现中国文化的经典。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1022", "500个侦探推理游戏", "黄青翔", "第2版", "中国华侨出版社", "可借", "让侦探迷绞尽脑汁，比推理小说更真实，让推理高手大呼过瘾，比“杀人游戏更有趣。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1024", "数据结构与算法分析（java）", "Mark Allen Weiss", "第2版", "机械工业出版社", "可借", "本书是国外数据结构与算法分析课程的标准教材，通俗易懂地介绍了数据结构和算法分析，出讨论一般数据结构级其实现外，还专门讨论一些高级数据结构及其实现，并在程序中的代码充分体现出Java5.0的新特性。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1025", "Effective Java", "Joshua Bloch", "第2版", "机械工业出版社", "可借", "本书适合于在Java开发方面有一定的经验，而且像更加深入的了解java编程语言，成为一名更加优秀、更加高效的Java开发人员。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1026", "Android物联网开发从入门到实战", "孙光宇 张玲玲", "第1版", "清华大学出版社", "可借", "国内第一本Android物联网开发的书籍。", "无", "无"});
//        db.execSQL(CREATE_BOOKS, new String[]{"1027", "数据库原理与应用教程", "何玉洁", "第3版", "机械工业出版社", "可借", "本书可作为高校非计算机专业的数据库教程，也可作计算机专业学生的补充读物，并可供数据库初学者作为入门读物。", "无", "无"});
//
//        db.execSQL(CREATE_USER, new String[]{"12345", "12345", "老宋", "男", "电子信息", "心理学，刑侦学，科普，书画，文学等", "18814126594", "5", "0", "0", "管理员", "0"});
//        db.execSQL(CREATE_USER, new String[]{"123", "123", "老钟", "男", "电子信息", "心理学，刑侦学，科普，书画，文学等", "13670485601", "1", "0", "0", "普通用户", "0"});
//        db.execSQL(CREATE_USER, new String[]{"1234", "1234", "老浩", "男", "电子信息", "心理学，刑侦学，科普，书画，文学等", "13670485601", "1", "0", "0", "普通用户", "0"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
