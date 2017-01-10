package ethereumjava.module.objects;

import ethereumjava.module.Eth;

public class DefaultFilter extends AbstractFilter<Log> {

    public DefaultFilter(FilterOptions options, Eth eth) {
        super(eth);
        this.filterId = eth.newFilter(options).toBlocking().first();
    }

    @Override
    protected Class<Log> getLogType() {
        return Log.class;
    }
}
