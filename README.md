Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.rtdevs21:neontoast:$versionCode' 
	}

Leatest Version:
[![](https://jitpack.io/v/rtdevs21/neontoast.svg)](https://jitpack.io/#rtdevs21/neontoast)
