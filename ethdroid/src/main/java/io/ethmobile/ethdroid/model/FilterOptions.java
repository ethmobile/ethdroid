package io.ethmobile.ethdroid.model;

import org.ethereum.geth.Address;
import org.ethereum.geth.Addresses;
import org.ethereum.geth.FilterQuery;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Hashes;
import org.ethereum.geth.Topics;

import java.util.List;

/**
 * Created by gunicolas on 14/10/16.
 */
public class FilterOptions {

    FilterQuery query;

    public FilterOptions() {
        query = Geth.newFilterQuery();
        query.setTopics(Geth.newTopicsEmpty());
        query.setAddresses(Geth.newAddressesEmpty());
    }

    private static Hashes fromListToHashes(List<String> list) throws Exception {
        Hashes ret = Geth.newHashesEmpty();
        for (String hash : list) {
            ret.append(Geth.newHashFromHex(hash));
        }
        return ret;
    }

    public FilterOptions addTopics(List<String> topicList) throws Exception {
        Topics topics = query.getTopics();
        topics.append(fromListToHashes(topicList));
        query.setTopics(topics);
        return this;
    }

    public FilterOptions addAddress(Address address) {
        Addresses addresses = query.getAddresses();
        addresses.append(address);
        query.setAddresses(addresses);
        return this;
    }

    public FilterOptions addAddress(String address) throws Exception {
        return addAddress(Geth.newAddressFromHex(address));
    }

    public FilterOptions fromBlock(long blockNumber) {
        query.setFromBlock(Geth.newBigInt(blockNumber));
        return this;
    }

    public FilterOptions toBlock(long blockNumber) {
        query.setToBlock(Geth.newBigInt(blockNumber));
        return this;
    }

    public FilterQuery getQuery() {
        return query;
    }


}
