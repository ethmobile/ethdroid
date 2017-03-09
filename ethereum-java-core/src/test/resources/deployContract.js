var identifier = "Test Contract" ;
var testcontractContract = web3.eth.contract([{"constant":false,"inputs":[],"name":"testFunctionOutputsPrimitive","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"testFunctionOutputsVoid","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"a","type":"uint8[3]"}],"name":"testFunctionInputsArray","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"x","type":"uint256"},{"name":"y","type":"uint256"}],"name":"testFunctionInputsPrimitives","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"throwEventReturnsBool","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"throwEventReturnsMatrix","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"testFunctionOutputsBool","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"throwEventReturnsUInt","outputs":[],"payable":false,"type":"function"},{"anonymous":false,"inputs":[{"indexed":false,"name":"","type":"uint256"}],"name":"testEventReturnsUInt","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"","type":"bool"}],"name":"testEventReturnsBool","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"","type":"int256[3][3]"}],"name":"testEventReturnsMatrix","type":"event"}]);
var testcontract = untitled4_testcontractContract.new(
   identifier,
   {
     from: web3.eth.accounts[0],
     data: '0x6060604052341561000c57fe5b5b6103608061001c6000396000f3006060604052361561008c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fb3bcf21461008e5780634a2c57c8146100b4578063770ca5d1146100c657806377651a99146101095780637bc76d2d146101325780639549568014610144578063ba8cad3814610156578063cbed653f14610180575bfe5b341561009657fe5b61009e610192565b6040518082815260200191505060405180910390f35b34156100bc57fe5b6100c461019c565b005b34156100ce57fe5b6101076004808060600190600380602002604051908101604052809291908260036020028082843782019150505050509190505061019f565b005b341561011157fe5b61013060048080359060200190919080359060200190919050506101a3565b005b341561013a57fe5b6101426101a8565b005b341561014c57fe5b6101546101e7565b005b341561015e57fe5b6101666102ef565b604051808215151515815260200191505060405180910390f35b341561018857fe5b6101906102f9565b005b6000600390505b90565b5b565b5b50565b5b5050565b7fb624b8b35d86a9486cc0aac46c4027c73e383690a155ff9adec74184012fec756001604051808215151515815260200191505060405180910390a15b565b7fcdf5d08b0aeb9fd3a6d59e3c045b6279bb9afc642e9036eed9dfbd297a328d3e60606040519081016040528060606040519081016040528060008152602001600181526020016002815250815260200160606040519081016040528060028152602001600181526020016001815250815260200160606040519081016040528060028152602001600081526020016001815250815250604051808260036000925b818410156102dd57828460200201516003602002808383600083146102cd575b8051825260208311156102cd576020820191506020810190506020830392506102a9565b5050509050019260010192610289565b9250505091505060405180910390a15b565b6000600190505b90565b7fda0c3045bd39f801fee3be0f9a530b49a6e97c50c31dfaf2cba91ba92b28adeb60026040518082815260200191505060405180910390a15b5600a165627a7a723058207203d88e5c2df11e88184f79c86d9f4846ed1a2c48d0176a0a5f3f246fb036790029',
     gas: '4700000'
   }, function (e, contract){
    console.log(e, contract);
    if (typeof contract.address !== 'undefined') {
        console.log('CONTRACT{' + contract.address + '}');
        miner.stop();
    }
 })

miner.start();



