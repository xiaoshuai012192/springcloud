package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.str.StrUtil;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/26 15:00
 */
public class SSLContextBuilder implements SSLProtocols, Builder<SSLContext> {
    private static final long serialVersionUID = 1L;

    private String protocol = TLS;
    private KeyManager[] keyManagers;
    private TrustManager[] trustManagers = {DefaultTrustManager.INSTANCE};
    private SecureRandom secureRandom = new SecureRandom();


    /**
     * 创建 SSLContextBuilder
     *
     * @return SSLContextBuilder
     */
    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    /**
     * 设置协议。例如TLS等
     *
     * @param protocol 协议
     * @return 自身
     */
    public SSLContextBuilder setProtocol(String protocol) {
        if (StrUtil.isNotBlank(protocol)) {
            this.protocol = protocol;
        }
        return this;
    }

    /**
     * 设置信任信息
     *
     * @param trustManagers TrustManager列表
     * @return 自身
     */
    public SSLContextBuilder setTrustManagers(TrustManager... trustManagers) {
        if (ArrayUtil.isNotEmpty(trustManagers)) {
            this.trustManagers = trustManagers;
        }
        return this;
    }

    /**
     * 设置 JSSE key managers
     *
     * @param keyManagers JSSE key managers
     * @return 自身
     */
    public SSLContextBuilder setKeyManagers(KeyManager... keyManagers) {
        if (ArrayUtil.isNotEmpty(keyManagers)) {
            this.keyManagers = keyManagers;
        }
        return this;
    }

    /**
     * 设置 SecureRandom
     *
     * @param secureRandom SecureRandom
     * @return 自己
     */
    public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
        if (null != secureRandom) {
            this.secureRandom = secureRandom;
        }
        return this;
    }

    /**
     * 构建{@link SSLContext}
     *
     * @return {@link SSLContext}
     */
    @Override
    public SSLContext build() {
        return buildQuietly();
    }

    /**
     * 构建{@link SSLContext}需要处理异常
     *
     * @return {@link SSLContext}
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws KeyManagementException   密钥管理异常
     * 
     */
    public SSLContext buildChecked() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(this.keyManagers, this.trustManagers, this.secureRandom);
        return sslContext;
    }

    /**
     * 构建{@link SSLContext}
     *
     * @return {@link SSLContext}
     * @throws IORuntimeException 包装 GeneralSecurityException异常
     */
    public SSLContext buildQuietly() throws IORuntimeException {
        try {
            return buildChecked();
        } catch (GeneralSecurityException e) {
            throw new IORuntimeException(e);
        }
    }
}
