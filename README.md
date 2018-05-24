# 블럭체인 TSA ( web3j + 시점확인 + IPFS ) 
   
  1) web3j : Java용  web3 API JDK8이상  <https://docs.web3j.io/getting_started.html>
    
    
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

      ....
      
		String addr = TSAServlet.etherUrl;
		String BINARY = "0x6080604052604051610590380380610590833981018060405281019080805182019291906020018051820192919060200180518201929190505050826000908051906020019061005092919061008e565b50816001908051906020019061006792919061008e565b50806002908051906020019061007e92919061008e565b5042600381905550505050610133565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100cf57805160ff19168380011785556100fd565b828001600101855582156100fd579182015b828111156100fc5782518255916020019190600101906100e1565b5b50905061010a919061010e565b5090565b61013091905b8082111561012c576000816000905550600101610114565b5090565b90565b61044e806101426000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301656c871461006757806309bd5a60146100f7578063669ba8a614610187578063a66d91e614610217575b600080fd5b34801561007357600080fd5b5061007c610242565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100bc5780820151818401526020810190506100a1565b50505050905090810190601f1680156100e95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561010357600080fd5b5061010c6102e0565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561014c578082015181840152602081019050610131565b50505050905090810190601f1680156101795780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561019357600080fd5b5061019c61037e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101dc5780820151818401526020810190506101c1565b50505050905090810190601f1680156102095780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561022357600080fd5b5061022c61041c565b6040518082815260200191505060405180910390f35b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102d85780601f106102ad576101008083540402835291602001916102d8565b820191906000526020600020905b8154815290600101906020018083116102bb57829003601f168201915b505050505081565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103765780601f1061034b57610100808354040283529160200191610376565b820191906000526020600020905b81548152906001019060200180831161035957829003601f168201915b505050505081565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104145780601f106103e957610100808354040283529160200191610414565b820191906000526020600020905b8154815290600101906020018083116103f757829003601f168201915b505050505081565b600354815600a165627a7a723058209ffd6e0c84c0f7fbaa67f4255d65a5fae5616123b8f8563be00c76538ef61b4d0029";
		Web3j web3 = Web3j.build(new HttpService(addr)); // defaults to http://localhost:8545/
		Credentials credentials;
		credentials = WalletUtils.loadCredentials(TSAServlet.walletPw,TSAServlet.walletPh );
			             
		String[] _paramList = { ipfs, hash, createDate };
		TsaContract result;
		result = TsaContract.deploy(web3, credentials, BigInteger.valueOf(1000000), BigInteger.valueOf(1000000), _paramList).send();
		String contractAddress = result.getContractAddress();

	Contract
		pragma solidity ^0.4.0;
		
		contract CedaDocument {
		    string public ipfsRes ;
		    string public hash ;
		    string public cedaTime ;
		    uint public contractTime ;
		     
		    function CedaDocument(string _ipfsRes , string _hash, string _cedaTime) public payable{ 
		        ipfsRes = _ipfsRes;
		        hash = _hash;
		        cedaTime = _cedaTime;
		        contractTime = now; 
		    }
		}


  2) ipfs : <https://github.com/ipfs/java-ipfs-api> 
    
	Usage
		//Create an IPFS instance with:
		IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		
		//To add a file use (the add method returns a list of merklenodes, in this case there is only one element):
		ipfs.refs.local();
		 
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("hello.txt"));
		MerkleNode addResult = ipfs.add(file).get(0);
    