package club.usql.mybatis.generator.handler;

import club.usql.exception.EncryptException;
import club.usql.mybatis.generator.type.AESString;
import club.usql.util.EncryptUtil;
import club.usql.util.PropertyUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author far.liu
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(AESString.class)
public class AutoAESCryptTypeHandler implements TypeHandler<AESString> {
    private static final String ENCRYPT_KEY = "generator.enc.db.aeskey";
    private static final String ENCRYPT_E = "generator.enc.db.aese";

    @Override
    public void setParameter(PreparedStatement ps, int i, AESString parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, encrypt(parameter));
    }

    @Override
    public AESString getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return decrypt(value);
    }

    @Override
    public AESString getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return decrypt(value);
    }

    @Override
    public AESString getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return decrypt(value);
    }

    private String encrypt(AESString value) {
        if (value == null) {
            return null;
        }
        try {
            String key = PropertyUtil.get(ENCRYPT_KEY);
            String e = PropertyUtil.get(ENCRYPT_E);
            byte[] temp = EncryptUtil.aesCBCEncrypt(value.toString(), key, e);
            String result = EncryptUtil.encodeBase64(temp);
            return result;
        } catch (Exception e) {
            throw new EncryptException(String.format("call AutoAESCryptTypeHandler.encrypt, e.getMessage:[%s]", e.getMessage()), e);
        }
    }

    private AESString decrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            String key = PropertyUtil.get(ENCRYPT_KEY);
            String e = PropertyUtil.get(ENCRYPT_E);
            byte[] temp = EncryptUtil.decodeBase64(value);
            String result = EncryptUtil.aesCBCDecrypt(temp, key, e);
            return new AESString(result);
        } catch (Exception e) {
            throw new EncryptException(String.format("call AutoAESCryptTypeHandler.decrypt, e.getMessage:[%s]", e.getMessage()), e);
        }
    }
}
