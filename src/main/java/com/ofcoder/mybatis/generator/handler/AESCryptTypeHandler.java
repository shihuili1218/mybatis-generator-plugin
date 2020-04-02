package com.ofcoder.mybatis.generator.handler;

import com.ofcoder.exception.EncryptException;
import com.ofcoder.util.EncryptUtil;
import com.ofcoder.util.PropertyUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author far.liu
 */
public class AESCryptTypeHandler implements TypeHandler<String> {
    private static final String ENCRYPT_KEY = "generator.enc.db.aeskey";
    private static final String ENCRYPT_E = "generator.enc.db.aese";

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, encrypt(parameter));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return decrypt(value);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return decrypt(value);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return decrypt(value);
    }

    private String encrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            String key = PropertyUtil.get(ENCRYPT_KEY);
            String e = PropertyUtil.get(ENCRYPT_E);
            byte[] temp = EncryptUtil.aesCBCEncrypt(value, key, e);
            String result = EncryptUtil.encodeBase64(temp);
            return result;
        } catch (Exception e) {
            throw new EncryptException(String.format("call AESCryptTypeHandler.encrypt, e.getMessage:[%s]", e.getMessage()), e);
        }
    }

    private String decrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            String key = PropertyUtil.get(ENCRYPT_KEY);
            String e = PropertyUtil.get(ENCRYPT_E);
            byte[] temp = EncryptUtil.decodeBase64(value);
            String result = EncryptUtil.aesCBCDecrypt(temp, key, e);
            return result;
        } catch (Exception e) {
            throw new EncryptException(String.format("call AESCryptTypeHandler.decrypt, e.getMessage:[%s]", e.getMessage()), e);
        }
    }
}
