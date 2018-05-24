# 블럭체인 TSA ( web3j + 시점확인 + IPFS ) 
   
  1) w3b3j : Java용  web3 API JDK8이상  <https://docs.web3j.io/getting_started.html>
    
    
	Usage
            // abstract class Contract 상속
		public final class TsaContract extends Contract { ... 
		
		// 생성자 
		TsaContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
		    super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
		}  
		
		 // 계약 
		static RemoteCall<TsaContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _param[]) {
		    String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type> asList(new org.web3j.abi.datatypes.Utf8String(_param[0]), new org.web3j.abi.datatypes.Utf8String(_param[1]),
		            new org.web3j.abi.datatypes.Utf8String(_param[2])));
		    return deployRemoteCall(TsaContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
		}


  2) ipfs : <https://github.com/ipfs/java-ipfs-api> 
    
	Usage
		//Create an IPFS instance with:
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		
		//To add a file use (the add method returns a list of merklenodes, in this case there is only one element):
		ipfs.refs.local();
		 
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("hello.txt"));
		MerkleNode addResult = ipfs.add(file).get(0);
    