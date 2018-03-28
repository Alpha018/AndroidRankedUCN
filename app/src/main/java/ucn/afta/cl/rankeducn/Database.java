package ucn.afta.cl.rankeducn;

/**
 * Created by Tomas on 28/03/2018.
 */

@com.raizlabs.android.dbflow.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database {

    /**
     * Key de la base de datos
     */
    public static final String NAME = "Database";

    /**
     * Version de la BD
     */
    public static final int VERSION = 1;

    /**
     * Tamanio del cache
     */
    public static final int CACHE_SIZE = 100;
}