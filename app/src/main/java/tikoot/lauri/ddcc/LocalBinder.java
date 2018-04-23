package tikoot.lauri.ddcc;

import android.os.Binder;

/**
 * Class used for binding service handling database
 */
public class LocalBinder extends Binder {
    private DatabaseService service;

    public LocalBinder(DatabaseService service) {
        this.service = service;
    }

    public DatabaseService getService() {
        return this.service;
    }
}
