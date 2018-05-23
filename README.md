# 블럭체인 TSA ( web3j + 시점확인 + IPFS ) 
   
  1) w3b3j : Java용  web3 API JDK8이상 

    https://docs.web3j.io/getting_started.html
    
    Usage
	    Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
	    Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
	    YourSmartContract contract = YourSmartContract.deploy(
	    <web3j>, <credentials>,
	    GAS_PRICE, GAS_LIMIT,
	    <param1>, ..., <paramN>).send();  // constructor params

  2) ipfs :
   
    https://github.com/ipfs/java-ipfs-api
    
    	Usage
		//Create an IPFS instance with:
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
	
		
		ipfs.refs.local();
		//To add a file use (the add method returns a list of merklenodes, in this case there is only one element):
		
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("hello.txt"));
		MerkleNode addResult = ipfs.add(file).get(0);
    