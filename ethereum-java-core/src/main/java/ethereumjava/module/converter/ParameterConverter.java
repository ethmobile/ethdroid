package ethereumjava.module.converter;

/**
 * Created by gunicolas on 25/08/16.
 */
public abstract class ParameterConverter<F, T> {

    public abstract T convertFrom(F parameter);

}
