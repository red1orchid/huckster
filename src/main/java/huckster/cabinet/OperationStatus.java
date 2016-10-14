package huckster.cabinet;

/**
 * Created by PerevalovaMA on 12.10.2016.
 */
public class OperationStatus {
    private boolean success;
    private String error;

    public OperationStatus(boolean success) {
        this.success = success;
    }

    public OperationStatus(boolean success, String error) {
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
