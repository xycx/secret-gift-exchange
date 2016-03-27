# secret-gift-exchange
This is a web app for secret gift exchange game admin to randomly match the participants.

## prerequisites
This a java web app, to run it you will need the following prerequisites
1. Apache Maven, you can find it here https://maven.apache.org/download.cgi
2. Java 8, you can find it here http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

My environment: Apache Maven 3.1.1, Java version: 1.8.0_40

## How to install the app
1. Checkout the branch and run `mvn clean install` from terminal
2. To start server, run `mvn exec:java -Dexec.mainClass="secret_gift_exchange.App"`
3. Keep the server running, open another tab in terminal and put the participant data into data.txt
4. Run `curl -X POST -H "Content-Type: text/plain" -v "http://localhost:3333/secret-gift-exchange/build-exchange-pairs?allowSameFamily=true" --data-binary @data.txt` this will send a POST request to the local server and return the result in json format.
5. You can set `allowSameFamily=false` to get the result, in this case the gifter and recipient won't be in same family ( don't have same last name )
6. This web app will also send an email to each gifter which containing the recipient for their gift. However you will need have SMTP server running and config host and port correctly in `Config.class`
