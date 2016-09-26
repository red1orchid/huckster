package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 26.09.2016.
 */
public class Response {
    private boolean success;
    private String error;

    public Response(boolean success) {
        this.success = success;
    }

    public Response(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
