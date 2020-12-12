#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

#define WIFI_SSID "Readmi"
#define WIFI_PASSWORD "01234567"
#define FIREBASE_HOST "washing-timetable.firebaseio.com"
#define FIREBASE_AUTH "jhstNsBXERw9uhfLvb77K8mBNLbCIlzC90ir3FSb"

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

int LED1 = 16;
int LED2 = D6;
int LED3 = D7;
int LED4 = D8;
int StartB = D5; 
int Str;
int pot = A0;
int potential;
int set;

void wifiConnect()
{
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID); Serial.println(" ...");

  int teller = 0;
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(1000);
    Serial.print(++teller); Serial.print(' ');
  }

  Serial.println('\n');
  Serial.println("Connection established!");  
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());
}

void setup()
{
Serial.begin(115200);

pinMode(LED1, OUTPUT);
pinMode(LED2, OUTPUT);
pinMode(LED3, OUTPUT);
pinMode(LED4, OUTPUT);
pinMode(StartB, INPUT);
pinMode(pot, INPUT);
/*pinMode(LED1, INPUT);
pinMode(LED1, INPUT);
pinMode(LED1, INPUT);*/

  Serial.println('\n');
  
  wifiConnect();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  timeClient.begin();
  
  timeClient.setTimeOffset(+3);

  delay(10);
  
}
void firebasereconnect()
{
  Serial.println("Trying to reconnect");
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    Serial.println();
  }
void loop()
{
   while(WiFi.status() != WL_CONNECTED)
  {
    wifiConnect();
  }
  
  if (Firebase.failed()) {
      Serial.print("Failed to connect");
      Serial.println(Firebase.error());
      Serial.println();
      Serial.println();
      firebasereconnect();
      wifiConnect();
      delay(2000);
      return;
  }
  
  potential = analogRead(pot) / 4;
  Str = digitalRead(StartB); 
  if(potential > 150){
    set = 5400; 
    digitalWrite(LED2, HIGH); 
  }  
  else if(potential < 150){
    digitalWrite(LED2, LOW); 
  }  
  if(potential < 100){
    set = 1800;
    digitalWrite(LED3, HIGH); 
  }  
  else if(potential > 100){
    digitalWrite(LED3, LOW); 
  }  
  if(100 < potential&&potential < 150){
    set = 3600;
    digitalWrite(LED4, HIGH); 
  }  
  else if(100 > potential||potential > 150){
    digitalWrite(LED4, LOW); 
  }  
  if(Str != HIGH)
  {
    digitalWrite(LED1, HIGH);
  }
  if(Str == HIGH)
  {
    digitalWrite(LED1, LOW); 
    timeClient.update();
    int epochTime = timeClient.getEpochTime();
    Serial.print("Epoch Time: ");
    Serial.println(epochTime);
    Firebase.setString("hostels/hostel1/floors/floor1/washingMachines/washingMachine1/startTime",String(epochTime)); 
    Firebase.setString("hostels/hostel1/floors/floor1/washingMachines/washingMachine1/endTime",String(set + epochTime)); 
  }
  delay(50);
}
