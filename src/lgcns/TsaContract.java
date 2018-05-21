
package lgcns;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>
 * Auto generated code.
 * <p>
 * <strong>Do not modify!</strong>
 * <p>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the <a
 * href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>
 * Generated with web3j version 3.1.1.
 */
public final class TsaContract extends Contract {

    static final String BINARY = "0x6080604052604051610590380380610590833981018060405281019080805182019291906020018051820192919060200180518201929190505050826000908051906020019061005092919061008e565b50816001908051906020019061006792919061008e565b50806002908051906020019061007e92919061008e565b5042600381905550505050610133565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100cf57805160ff19168380011785556100fd565b828001600101855582156100fd579182015b828111156100fc5782518255916020019190600101906100e1565b5b50905061010a919061010e565b5090565b61013091905b8082111561012c576000816000905550600101610114565b5090565b90565b61044e806101426000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301656c871461006757806309bd5a60146100f7578063669ba8a614610187578063a66d91e614610217575b600080fd5b34801561007357600080fd5b5061007c610242565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100bc5780820151818401526020810190506100a1565b50505050905090810190601f1680156100e95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561010357600080fd5b5061010c6102e0565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561014c578082015181840152602081019050610131565b50505050905090810190601f1680156101795780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561019357600080fd5b5061019c61037e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101dc5780820151818401526020810190506101c1565b50505050905090810190601f1680156102095780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561022357600080fd5b5061022c61041c565b6040518082815260200191505060405180910390f35b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102d85780601f106102ad576101008083540402835291602001916102d8565b820191906000526020600020905b8154815290600101906020018083116102bb57829003601f168201915b505050505081565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103765780601f1061034b57610100808354040283529160200191610376565b820191906000526020600020905b81548152906001019060200180831161035957829003601f168201915b505050505081565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104145780601f106103e957610100808354040283529160200191610414565b820191906000526020600020905b8154815290600101906020018083116103f757829003601f168201915b505050505081565b600354815600a165627a7a723058209ffd6e0c84c0f7fbaa67f4255d65a5fae5616123b8f8563be00c76538ef61b4d0029";

    TsaContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    RemoteCall<String> getStringMethod(String method) {
        Function function = new Function(method, Arrays.<Type> asList(), Arrays.<TypeReference<?>> asList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    static RemoteCall<TsaContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _param[]) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type> asList(new org.web3j.abi.datatypes.Utf8String(_param[0]), new org.web3j.abi.datatypes.Utf8String(_param[1]),
                new org.web3j.abi.datatypes.Utf8String(_param[2])));
        return deployRemoteCall(TsaContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    static RemoteCall<TsaContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _greeting) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type> asList(new org.web3j.abi.datatypes.Utf8String(_greeting)));
        return deployRemoteCall(TsaContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    static TsaContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TsaContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }
}