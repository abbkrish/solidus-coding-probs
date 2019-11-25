1) The endpoint is at http://mysterious-peak-90337.herokuapp.com/messages/

Example:
**POST**
curl -X POST -H "Content-Type: application/json" -d '{"message":"Hello"}'  https://mysterious-peak-90337.herokuapp.com/messages/

**GET**
curl http://mysterious-peak-90337.herokuapp.com/messages/185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969 && echo

**GET** (Not found)
curl http://mysterious-peak-90337.herokuapp.com/messages/185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381968 && echo


**Potential Bottlenecks**
In terms of bottlenecks, the server could encounter a lot of traffic/users at a time. 
To mitigate this one could use solutions from Azure/AWS/Google CP and so on. 

1) One solution - have multiple servers. Example since it's hexadecimal, there could be 16 servers and route to each server based on the start value of the hash. 
