# UpayFelmoRemit-Android




[![](https://jitpack.io/v/HashKloud/Upay-Felmo-SDK-Android.svg)](https://jitpack.io/#HashKloud/Upay-Felmo-SDK-Android)


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
	        implementation 'com.github.HashKloud:Upay-Felmo-SDK-Android:V-1.0.0'
	}
 ```

### Step 2. Add Runtime Permission

Remember to add below  permission in `Manifest.xml file` .
 ```java
     <uses-permission android:name="android.permission.INTERNET"/>
  ```

 ###  Step 3. For requesting UPay purchase use below code:

 ```java
    RemitUserAuthenticator userAuthenticator = new RemitUserAuthenticator(this, true);
	userAuthenticator.authenticate(new UserAuth(SIGNATURE,CLIENT_KEY, COUNTRY_CODE));
 ```
**Details**
```java
RemitUserAuthenticator(this, true);
```
- **1st Parameter -  Use `this` or Activity `Context`. </br>
- **2nd Parameter - Boolean, `true` for Sandbox Mode,  `false` for Production Mode.
```java
authenticate(new UserAuth(SIGNATURE,CLIENT_KEY, COUNTRY_CODE));
```
This method is used to validate a user identity.

### Step 4.  Implement `RemitUserAuthenticator.Listener`  for  callbacks

```java
public class MainFragment extends BaseFragment implements RemitUserAuthenticator.Listener{
        @Override
	public void onLoginBegin(){}

	@Override
	public void onLoginEnd(){}

	@Override
	public void onLoginError(Throwable throwable){}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onLoginSuccess(LoginResponse response){}
}
```
### Step 5. Attach `RemitWebHost`

After getting success response on `onLoginSuccess(LoginResponse response){}` you need to attach `RemitWebHost` on another activity/fragment. Pass the response object ot that activity/fragment. In sample app we are using fragment.

```java
        RemitWebHost webHost = new RemitWebHost();
	webHost.attach(getActivity(), webView, this);
	webHost.start(response, Constants.KEY_LANGUAGE_ENGLISH);
```
**Details**
```java
webHost.attach(getActivity(), webView, this);
```
By calling `attach()` we are attaching the `webview` reference of our Fragment/Activity  to load the remittance module and listen all callbacks. It takes three parameter `Context`, `WebView`, `Listener` .

```java
webHost.start(response, Constants.KEY_LANGUAGE_ENGLISH);
```
By calling `start()` it will initiate start & load the module in the webview. Takes two parameters `response` that we get from the `onLoginSuccess(LoginResponse response)`.

### Step 6. Implement `RemitWebHost.Listener` for callbacks
```java
public class RemitWebHostFragment extends BaseFragment implements RemitWebHost.Listener{

	/**
	* Called when web page loading is started.
	*/
	void onPageStarted(){};

	/**
	* Called when web page loading is finished.
	*/
	void onPageFinished(){};

	/**
	* Called when error occurs during web page loading.
	* @param errorCode Error code.
	* @param text Error text.
	*/
	void onPageError(int errorCode, String text){};

	/**
	* Called when remit is successful.
	* @param values Returned data.
	*/
	void onRemitSuccess(HashMap<String, String> values){};

	/**
	* Called when remit is failed.
	* @param values Returned data.
	*/
	void onRemitFailure(HashMap<String, String> values){};

	/**
	* Called when FPX view need to be opened.
	* @param fpxWebHost Web host that handles FPX interaction.
	*/
	void onOpenFpxView(FpxWebHost fpxWebHost){};

	/**
	* Called when FPX view need to be closed.
	*/
	void onCloseFpxView(){};

	/**
	* Invite friends to share the app.
	* @param text Text.
	* @param url URL.
	*/
	void onInviteFriends(String text, String url){};
}
```

### Step 7. Open another fragment for FPX

Basically in our sample app we are opening another fragment and managing this.

```java
	@Override
	public void onOpenFpxView(FpxWebHost fpxWebHost)
	{
		fpxWebHostDialogFragment = FpxWebHostDialogFragment.show(this, fpxWebHost);
	}
```
### Step 8. Attach `FpxWebHost`
```java
	webHost.attach(getActivity(), webView, this);
	webHost.start();
```
### Step 9. Implement `FpxWebHost.Listener` for callbacks.
 ```java
     public class FpxWebHostDialogFragment extends BaseDialogFragment implements FpxWebHost.Listener{
     		/**
		 * Called when web page loading is started.
		 */
		void onPageStarted();

		/**
		 * Called when web page loading is finished.
		 */
		void onPageFinished();

		/**
		 * Called when error occurs during web page loading.
		 * @param errorCode Error code.
		 * @param text Error text.
		 */
		void onPageError(int errorCode, String text);
     }

 ```

For details implementation clone this project and run the sample app.


**You are Good To Go. Happy Coding**


**MIT License**

**Copyright (c) 2018 HashKloud**
