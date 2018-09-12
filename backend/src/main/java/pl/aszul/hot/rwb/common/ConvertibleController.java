package pl.aszul.hot.rwb.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Common system ability to convert from/to db/rest model.
 *
 * @param <DB> DB model class
 * @param <V>  rest/View model class
 * @param <W>  rest/Write model class
 */
public abstract class ConvertibleController<DB, V, W> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<DB> dbClass;
    private final Class<V> vClass;
    private final Class<W> wClass;

    public ConvertibleController(Class<DB> dbClass, Class<V> vClass, Class<W> wClass) {
        this.dbClass = dbClass;
        this.vClass = vClass;
        this.wClass = wClass;
    }

    public static <D> D genericConvert(Object source, Class<D> aClass) {
        return OBJECT_MAPPER.convertValue(source, aClass);
    }

    protected V convertToView(final DB dbObject) {
        return genericConvert(dbObject, this.vClass);
    }

    protected List<V> convertToView(final Collection<DB> dbCollection) {
        return dbCollection.stream()
                .map(this::convertToView)
                .collect(toList());
    }

    protected DB convertToDbModel(final W writeObject) {
        return genericConvert(writeObject, dbClass);
    }

    protected List<DB> convertToDbModel(final Collection<W> writeCollection) {
        return writeCollection.stream()
                .map(this::convertToDbModel)
                .collect(toList());
    }

}
