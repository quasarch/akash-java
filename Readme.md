# Akask SDK for Java

The **Akask SDK for Java** enables Java developers to easily with with [Akash][akash] and build solutions
on top of akash network.

## Release Notes ##

Changes are tracked in [CHANGELOG.md][changes-file].

## Getting Started

#### Minimum requirements ####

To run the SDK you will need **Java 11+**. For more information about the requirements and optimum
settings for the SDK, please see the [Installing a Java Development Environment][docs-java-env]
section of the developer guide.

## Features

Currently supported features include:

* R_01 Create deployment Create a deployment based on an SDL file. Request through RPC. 1
* R_02 Close deployment Close a deployment. Request through RPC
* R_03 List deployments List all deployments matching the filters with consideration for page limits. Request through
  REST. 1
* R_04 Get deployment Request through REST. 1
* R_05 List bids Get the list of bids based on a set of filters. Request through REST. 1
* R_06 Create lease Request through RPC. 1
* R_07 Get lease Get information regarding a lease such as its status. Request through REST. 1
* R_08 Send manifest Request through RPC.

## Spec information

This client was built based on
this [https://github.com/akash-network/community/blob/main/sig-clients/client-libraries/prd.md][spec]

### Rest endpoint
[Swagger][https://akash.c29r3.xyz/api/swagger/] 
