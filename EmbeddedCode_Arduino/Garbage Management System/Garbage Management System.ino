#include <stdio.h>
#include <string.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <Servo.h>

#define FIREBASE_HOST "mylednode-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "qzJ55v2TgRSp0ZWLLCr93jpDJzCxzonfqFrR32ni"
#define WIFI_SSID "SLT-4G_DaSuN"
#define WIFI_PASSWORD "9991327866"
#define BIN_ID "B001"

LiquidCrystal_I2C lcd(0x3F, 16, 2);
const double dblLatitude =6.8528;
const double dblLongitude = 79.9036;
FirebaseData firebaseData;
Servo servo;
bool isSendLastUpdate = false;
int lock = 0;


char path4[] = "/Bins/";

//ultrasonic
const int trigPin = 12;
const int echoPin = 14;

//motion
const int Status = 16;
const int sensor = 13;

void setup() {
  Serial.begin(9600);

  servo.attach(2, 500, 2400);
  servo.write(180);

  lcd.begin();
  lcd.backlight();
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Welcome...");
  delay(1000);

  Serial.println("Serial communication started\n\n");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  char path1[] = "/Bins/";
  strcat(path1, BIN_ID);
  strcat(path1, "/latitude");
  Firebase.set(firebaseData, path1, dblLatitude);

  char path2[] = "/Bins/";
  strcat(path2, BIN_ID);
  strcat(path2, "/longitude");
  Firebase.set(firebaseData, path2, dblLongitude);

  char path5[] = "/Bins/";
  strcat(path5, BIN_ID);
  strcat(path5, "/id");
  Firebase.set(firebaseData, path5, BIN_ID);

  strcat(path4, BIN_ID);
  strcat(path4, "/lock");
  Firebase.set(firebaseData, path4, lock);


  //ultrasonic
  pinMode(trigPin, OUTPUT);  // Sets the trigPin as an Output
  pinMode(echoPin, INPUT);   // Sets the echoPin as an Input

  //motion
  pinMode(sensor, INPUT);   // declare sensor as input
  pinMode(Status, OUTPUT);  // declare LED as output

  delay(5000);
}

void loop() {
  if (lock == 1) {
    waitForunlock();

  } else {
    checkFillingPercentage();
    checkMotionPIR();
  }

  delay(1000);
}

void checkFillingPercentage() {
  double duration = getUltrasonicDuration();

  char path3[] = "/Bins/";
  strcat(path3, BIN_ID);
  strcat(path3, "/duration");
  Firebase.set(firebaseData, path3, duration);

  if (duration <= 300) {
    lock = 1;
    Serial.println("100% Full Bin is Lock");
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("100% Full !!!");
    lcd.setCursor(0, 1);
    lcd.print("Bin is Lock");

    Firebase.set(firebaseData, path4, lock);
    waitForunlock();

  } else {

    if (duration > 300 && duration <= 400) {
      Serial.println("90% Full");
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("90% Full");
    } else if (duration > 400 && duration <= 500) {
      Serial.println("75% Full");
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("75% Full");
    } else if (duration > 500 && duration <= 600) {
      Serial.println("50% Full");
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("50% Full");
    } else if (duration > 600 && duration <= 800) {
      Serial.println("25% Full");
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("25% Full");
    } else if (duration > 800) {
      Serial.println("Bin is Empty");
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Bin is Empty");
    }
  }
}

double getUltrasonicDuration() {
  double duration;
  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  // Prints the distance on the Serial Monitor
  Serial.print("Duration (micro seconds): ");
  Serial.println(duration);
  return duration;
}

void checkMotionPIR() {
  do {
    long state = digitalRead(sensor);
    if (state == HIGH) {
      digitalWrite(Status, HIGH);
      Serial.println("Motion detected!");

      isSendLastUpdate = false;
      //open lid
      servo.write(0);

    } else if (state == LOW) {

      state = digitalRead(sensor);
      if (state == HIGH) {
        digitalWrite(Status, HIGH);
        Serial.println("Motion detected2!");

        //open lid
        servo.write(0);
      } else {
        digitalWrite(Status, LOW);
        Serial.println("Motion absent!");

        //close lid
        servo.write(180);
        if (isSendLastUpdate == false) {
          checkFillingPercentage();
          isSendLastUpdate = true;
        }
      }
    }
    delay(1000);
  } while (1);
}

void waitForunlock() {
  do {
    Firebase.getInt(firebaseData, path4);
    lock = firebaseData.intData();
    Serial.print(lock);

    if (lock == 0) {
      break;
    } 
  }while(1);
}