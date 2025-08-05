# Rate Limiter

## Context
A rate limiter is a component that limits the number of user's requests allowed over a period of time.
The advantages of this service is to prevent resource starvation in case of a DoS (Denial of Service) attack, 
reducing costs by limiting the number of calls to a thirty-party api and prevent servers overload.

## Technologies
LocalStack | Terraform | S3 | Redis | Jobrunr | SLF4J | Spring

## System design
[Link](https://github.com/jhonata-gutemberg/system-design/tree/main/rate-limiter)

## Software architecture
This project is organized in the following modules domain, entrypoints and vendors.

### Domain
There you will find the business logic implementation. This module is divided into models 
(contains the data used in the use cases) and use cases (implements the business capabilities).

### Entrypoints
There is the entrypoints of the application, like api and workers. This objects calls the domain use cases.

### Vendors
This module is responsible to implement the application's contracts. Which module are isolated from others inside vendors.
