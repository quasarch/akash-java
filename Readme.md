# Akask SDK for Java

The **Akask SDK for Java** enables Java developers to easily with [Akash](https://akash.network/) and build solutions
on top of akash network.

## Release Notes ##

Changes are tracked in [Change log file](CHANGELOG.md)

## Getting Started

### Example
A way of getting started might be to have a look at the example folder. In there you will find 
an example project which makes use of the operations supported by this library.  
For instance, to get a git one would write code similar to what can be found in GetBid.java:

```java
public class GetBid {
    ///...
    public static void main(String[] args) {
        var akash = ExampleHelper.getClient();
        var result = akash.getBid(owner, deploymentSequence, null, null, providerId);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("fetched bid: {}", result.get());
    }
}
```

### Dependencies
To import the library using maven, please add to your pom.xml:

```xml
<!-- https://mvnrepository.com/artifact/cloud.quasarch/akash-java -->
<dependency>
    <groupId>cloud.quasarch</groupId>
    <artifactId>akash-java</artifactId>
    <version>0.0.1</version>
</dependency>

```

#### Minimum requirements ####

To run the SDK you will need **Java 17+**. For more information about the requirements and optimum
settings for the SDK, please see the [Installing](https://openjdk.org/install/) page.

## Features

Currently supported features include:

* R_03 List deployments List all deployments matching the filters with consideration for page limits. Request through
  REST. 1
* R_04 Get deployment Request through REST. 1
* R_05 List bids Get the list of bids based on a set of filters. Request through REST. 1

* R_07 Get lease Get information regarding a lease such as its status. Request through REST. 1

Future support will also include:
* 
* R_01 Create deployment Create a deployment based on an SDL file. Request through RPC. 1
* R_02 Close deployment Close a deployment. Request through RPC
* R_06 Create lease Request through RPC. 1
* R_08 Send manifest Request through RPC.

## Spec information

This client was built based on
this [spec](https://github.com/akash-network/community/blob/main/sig-clients/client-libraries/prd.md)

### Rest endpoint

[Swagger](https://akash.c29r3.xyz/api/swagger/)

# Open issues

## json format on deployments

There's a "resources" inside of resources [swagger](https://akash.c29r3.xyz/api/swagger/#/Query/Deployments)

# WIP - error reference

Error model
From the current akash SDK, those are the expected errors:

| Error                      | Code   | Description                                                                               | Should Retry | Mapped to                |
|----------------------------|--------|-------------------------------------------------------------------------------------------|--------------|--------------------------|
| errInternal                | 1      | Internal - not supposed to happen                                                         | Yes          | AkashNetworkError        |
| ErrTxDecode                | 2      | tx parse error                                                                            | Yes          | AkashNetworkError        |
| ErrInvalidSequence         | 3      | invalid sequence                                                                          | Yes          | ?? what is a sequence ?? |
| ErrUnauthorized            | 4      | unauthorized                                                                              | No           | AuthError                |
| ErrInsufficientFunds       | 5      | Not enough funds                                                                          | No           | StateError               |
| ErrUnknownRequest          | 6      | unkown request                                                                            | No           | BadRequest               |
| RootCodespace              | 7      | invalid address                                                                           | No           | BadRequest               |
| ErrInvalidPubKey           | 8      | invalid pubkey                                                                            | No           | BadRequest               |
| ErrUnknownAddress          | 9      | unknown address                                                                           | No           | BadRequest               |
| ErrInvalidCoins            | 10     | invalid coins                                                                             | No           | BadRequest               |
| ErrOutOfGas                | 11     | out of gas                                                                                | No           | StateError               |
| ErrMemoTooLarge            | 12     | memo too large                                                                            | No           | BadRequest               |
| ErrInsufficientFee         | 13     | insufficient fee                                                                          | No           | StateError               |
| ErrTooManySignatures       | 14     | maximum number of signatures exceeded                                                     | No           | ??                       |
| ErrNoSignatures            | 15     | no signatures supplied                                                                    | No           | BadRequest               |
| ErrJSONMarshal             | 16     | failed to marshal JSON bytes                                                              | No           | BadRequest               |
| ErrJSONUnmarshal           | 17     | failed to unmarshal JSON bytes                                                            | No           | BadRequest               |
| ErrInvalidRequest          | 18     | invalid request                                                                           | No           | BadRequest               |
| ErrTxInMempoolCache        | 19     | tx already in mempool                                                                     | No           | BadRequest               |
| ErrMempoolIsFull           | 20     | mempool is full                                                                           | Yes!         | AkashNetworkError        |
| ErrTxTooLarge              | 21     | tx too large                                                                              | No           | BadRequest               |
| ErrKeyNotFound             | 22     | key not found                                                                             | No           | BadRequest               |
| ErrWrongPassword           | 23     | invalid account password                                                                  | No           | AuthError                |
| ErrorInvalidSigner         | 24     | tx intended signer does not match the given signer                                        | No           | BadRequest               |
| ErrorInvalidGasAdjustment  | 25     | invalid gas adjustment                                                                    | No           | StateError               |
| ErrInvalidHeight           | 26     | -- Retry: The message itself doesnt need modification but the header needs to be resigned | No           | BadRequest ??            |
| ErrInvalidVersion          | 27     | invalid version                                                                           | No           | BadRequest               |
| ErrInvalidChainID          | 28     | invalid chain-id                                                                          | No           | BadRequest               |
| ErrInvalidType             | 29     | invalid type                                                                              | No           | BadRequest               |
| ErrTxTimeoutHeight         | 30     | tx timeout height                                                                         | No           | BadRequest               |
| ErrUnknownExtensionOptions | 31     | Unknown extension options                                                                 | No           | BadRequest               |
| ErrWrongSequence           | 32     | incorrect account sequence                                                                | No           | BadRequest               |
| ErrPackAny                 | 33     | failed packing protobuf message to Any                                                    | No           | BadRequest               |
| ErrUnpackAny               | 34     | failed unpacking protobuf message from Any                                                | No           | BadRequest               |
| ErrLogic                   | 35     | internal logic error                                                                      | No           | AkashNetworkError        |
| ErrConflict                | 36     | conflict                                                                                  | Yes ??       | BadRequest               |
| ErrNotSupported            | 37     | feature not supported                                                                     | No           | BadRequest               |
| ErrNotFound                | 38     | not found                                                                                 | No           | BadRequest               |
| ErrIO                      | 39     | Internal IO error                                                                         | Yes          | AkashNetworkError        |
| ErrAppConfig               | 40     | error in app.toml                                                                         | ??           | ??                       |
| ErrPanic                   | 111222 | panic                                                                                     | Yes          | AkashNetworkError        |

# Deploy new version

After having the required configuration in local settings.xml:

## Staging

After making sure gpg is installed and local maven settings.xml is configured with the required information of gpg:

```xml

<profile>
    <id>ossrh</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>${PASSPHRASE}</gpg.passphrase>
    </properties>
</profile>
```

One can run:

```shell
mvn deploy
```

## Release

```shell
mvn clean deploy -P release
```

# References

[Sign and release using GitHub Actions #gpg](https://gist.github.com/sualeh/ae78dc16123899d7942bc38baba5203c#how-to-sign-and-release-to-the-central-repository-with-github-actions)
[Publishing packages using git](https://docs.github.com/en/actions/publishing-packages/publishing-java-packages-with-maven)

[Create, manage and upload private key](https://central.sonatype.org/publish/requirements/gpg/#distributing-your-public-key)

