
plugins {
    id("com.android.application")
    
}

android {
    namespace = "id.pras.foreground_service"
    compileSdk = 33
    
    defaultConfig {
        applicationId = "id.pras.foreground_service"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
    
    dependencies{
      implementation("androidx.core:core:1.13.+")
    }
    
}


