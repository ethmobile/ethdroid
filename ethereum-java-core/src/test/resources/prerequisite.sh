#!/bin/bash

echo "#!/bin/bash" > killPrerequisite.sh
echo "kill " $$ >> killPrerequisite.sh
chmod +x killPrerequisite.sh

#CONSTANTS
passwd="admin"
rpc_provider_port="8547"
rpc_provider_addr="0.0.0.0"
rpc_provider_api="admin,eth,net,web3,personal"
eth_port=30303
eth_network_id=100


trap 'kill -TERM $PID' TERM INT

cp genesis.json genesis.json.backup
cp config.json config.json.backup

addr0=$(geth --datadir ./data --password <(echo $passwd) account new | grep -o -P '(?<={).*(?=})')
sed -i -- "s/TEST_USER0_ADDRESS/$addr0/g" genesis.json

addr1=$(geth --datadir ./data --password <(echo $passwd) account new | grep -o -P '(?<={).*(?=})')
sed -i -- "s/TEST_USER1_ADDRESS/$addr1/g" genesis.json

geth --datadir ./data init genesis.json
geth --datadir ./data --networkid $eth_network_id --unlock 0 --password <(echo $passwd) js deployContract.js > contract.log


contractAddr=$(cat contract.log | grep -o -P '(?<=CONTRACT{).*(?=})')
#eth_difficulty=$(cat contract.log | grep -o -P '(?<=DIFFICULTY{).*(?=})')
#eth_ip=$(cat contract.log | grep -o -P '(?<=IP{).*(?=})')


#Set config file

#sed -i -- "s/TEST_RPC_PORT/$rpc_provider_port/g" config.json
#sed -i -- "s/TEST_ETH_PORT/$eth_port/g" config.json
#sed -i -- "s/TEST_NETWORK_ID/$eth_network_id/g" config.json
#sed -i -- "s/TEST_DIFFICULTY/$eth_difficulty/g" config.json

sed -i -- "s/CONTRACT_ADDRESS/$contractAddr/g" config.json
sed -i -- "s/TEST_USER0_ADDRESS/$addr0/g" config.json
sed -i -- "s/TEST_USER1_ADDRESS/$addr1/g" config.json
sed -i -- "s/TEST_USER0_PASSWORD/$passwd/g" config.json
sed -i -- "s/TEST_USER1_PASSWORD/$passwd/g" config.json



geth --datadir ./data --networkid $eth_network_id --rpc --rpcaddr $rpc_provider_addr --rpcport $rpc_provider_port --rpcapi $rpc_provider_api --rpccorsdomain "*" --nodiscover js mineOnlyWhenTx.js &


#Wait terminaison signal and kill geth when got it
PID=$!
wait $PID
trap - TERM INT
wait $PID
EXIT_STATUS=$?

# Reset files 
rm -rf ./data ./genesis.json 
mv genesis.json.backup genesis.json 
rm contract.log
rm config.json
mv config.json.backup config.json
rm killPrerequisite.sh

