package com.wallstcn.util.connection;

/**
 * @author xiangdao
 */
public class ConnectionException extends RuntimeException {
    private static final long serialVersionUID = -6503525110247209484L;

    public ConnectionException() {
        super();
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable e) {
        super(e);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
