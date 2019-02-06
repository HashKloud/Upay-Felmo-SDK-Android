# Upay-Felmo-SDK-Android

Send Remittance For Third Party Library. Read the documentation below for implementation in Android Project 



[![](https://jitpack.io/v/HashKloud/Upay-Felmo-SDK-Android.svg)](https://jitpack.io/#HashKloud/Upay-Felmo-SDK-Android)

![UpayFelmoLib-Android](https://github.com/HashKloud/Upay-Felmo-SDK-Android/blob/master/mobizen_20190206_123728.gif)

## Usage 

### Step 1. Add the JitPack repository & dependencies to your build file 

- Add below code in your  project level `build.gradle` file,  At the end of repositories: </br> 

```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ``` 
- Add the dependency to your app level `build.gradle` file. 
```java
dependencies {
    implementation 'com.github.HashKloud:Upay-Felmo-SDK-Android:V-0.0.2-beta'
}
 ```

 ###  Step 2. For Sending Remittance: 
 
 ```java 
  UpayFelmoWebBuilder upayFelmoWebBuilder = new UpayFelmoWebBuilder(this);
  upayFelmoWebBuilder.sendRemittance(new UserAuth("YOUR_SIGNATURE","YOUR_CLIENT_KEY","COUNTRY_CODE- MY"), this,true);

 ```
**Details**
```java
UpayFelmoWebBuilder(this); 
```
- Use `this` or Activity `Context`. </br>

```java 
sendRemittance(new UserAuth("YOUR_SIGNATURE","YOUR_CLIENT_KEY","COUNTRY_CODE"), this,true);
``` 
- **1st Parameter** takes  `UserAuth` object. You have to send three params in this object constructor. 
      
        YOUR_SIGNATURE : ex. "djfalkfjdalsjflksajfljj34343434\n"
        YOUR_CLIENT_KEY: ex.  "dfjkjerrrre"
        COUNTRY_CODE : ex. For Malaysia - MY and For Bangladesh- BD.
        
- **2nd Parameter** takes `this` for listener.  

- **3rd Parameter** takes a boolean. True for Sandboxmode/UAT and false for Production Mode. 

### Step 3.  Implement listener  for success & failure callback 

```java 
public class MainActivity extends AppCompatActivity implements UpayFelmoListener{
        @Override
    public void onSuccess(HashMap<String, String> values) {
        
    }


    @Override
    public void onFailure(HashMap<String,String> values) {
       
    }

} 
```

**You are Good To Go. Happy Coding**
 
 
**MIT License**

**Copyright (c) 2018 HashKloud**
