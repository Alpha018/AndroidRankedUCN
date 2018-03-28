package ucn.afta.cl.rankeducn.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucn.afta.cl.rankeducn.Database;

/**
 * Created by Tomás on 28/03/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        database = Database.class,
        cachingEnabled = true,
        orderedCursorLookUp = true, // https://github.com/Raizlabs/DBFlow/blob/develop/usage2/Retrieval.md#faster-retrieval
        cacheSize = Database.CACHE_SIZE
)
public class Usuario extends BaseModel{

    /**
     * Identificador unico
     */
    @Getter
    @Column
    @PrimaryKey(autoincrement = true)
    Long id;

    /**
     * RUT del {@link Usuario}
     */
    @Getter
    @Column
    String rut;

    /**
     * Password del {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    String password;

    /**
     * Email del {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    String email;

    /**
     * Nombre del {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    String nombre;

    /**
     * Apellido del {@link Usuario}
     */
    @Getter
    @Column
    String apellido;

    /**
     * Partidas jugadas por el {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    int partidasJugadas;

    /**
     * Puntaje del {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    int puntaje;

    /**
     * Imagen de perfil del {@link Usuario}
     */
    @Getter
    @Setter
    @Column
    String avatar;
}
