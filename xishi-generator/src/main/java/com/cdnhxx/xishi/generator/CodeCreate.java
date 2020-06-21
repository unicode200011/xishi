package com.cdnhxx.xishi.generator;

import com.common.generator.model.GenInfo;

/**
 * 生成
 *
 * @author: lx
 */
public class CodeCreate {
    public static void main(String[] args) {
        GenInfo.build()
                .setUrl("jdbc:mysql://localhost:3306/xishi?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUserName("remote")
                .setPassword("nahanMysqlPwd&&RemoteRemote2019nahan")
                .setDriverName("com.mysql.jdbc.Driver")
                //作者
                .setAuthor("LX")
                //数据库名
                .setDbName("xishi")
                //设置模块路径
                .setProjectPath("D:\\workspace\\program\\xishi\\xishi\\xishi-user\\xishi-user-rest")
                //设置controller包名
                .setControllerPackageName("com.xishi.user.controller")
                //设置service包名
                .setServicePackageName("com.xishi.user.service")
                //设置dao包名
                .setEntityPackageName("com.xishi.user.model.pojo")
                .setMapperPackageName("com.xishi.user.dao.mapper")
                .setXmlPackageName("com.xishi.user.dao.mapping")
                .setTablePrefix("eb_admin_wallet_record")
                //设置是否生成controller
                .setGenController(false)
                //设置是否生成service
                .setGenService(true)
                //设置是否覆盖已存在文件
                .setOverrideExistFile(true)
                //生成
                .over();
    }

}
