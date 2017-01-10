package ethereumjava.module.objects;

import ethereumjava.module.Eth;

/**
 * Created by gunicolas on 20/10/16.
 */
//String generic type is the new block hashes since last poll
public class BlockFilter extends AbstractFilter<String> {

    public BlockFilter(Eth eth) {
        super(eth);
        filterId = eth.newBlockFilter().toBlocking().first();
    }

    @Override
    protected Class<String> getLogType() {
        return String.class;
    }

}
