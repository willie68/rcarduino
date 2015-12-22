/* Create a WiFi access point and provide a tcp/ip server on it. */

#include <ESP8266WiFi.h>
#include <WiFiClient.h> 

/* Set these to your desired credentials. */
const char *ssid = "RCArduino";
const char *password = "rcarduino";

WiFiServer server(3456);

/* Just a little test message.  Go to http://192.168.4.1 in a web browser
 * connected to this access point to see it.
 */
void setup() {
	delay(1000);
	Serial.begin(115200);
	Serial.println();
	Serial.print("Configuring access point...");
	/* You can remove the password parameter if you want the AP to be open. */
	WiFi.softAP(ssid, password);

	IPAddress myIP = WiFi.softAPIP();
	Serial.print("AP IP address: ");
	Serial.println(myIP);

	server.begin();
	Serial.println("Server started");
}

WiFiClient client;

void loop() {
	if (!client.connected()) {
        // try to connect to a new client
        client = server.available();
    } else {
        // read data from the connected client
        if (client.available() > 0) {
            Serial.write(client.read());
        }
    }
}
