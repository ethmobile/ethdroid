function checkWork() {
    if (eth.getBlock("pending").transactions.length > 0) {
        if (eth.mining) return;
        console.log("== Pending transactions! Mining...");
        miner.start();
    } else {
        miner.stop();
    }
}

while(true){
	checkWork();
	admin.sleep(0.5);
}