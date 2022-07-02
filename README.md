# Sign Language Detection App

***Master Thesis** - Aplicación móvil didáctica para el aprendizaje del lenguaje de signos mediante la integración de un modelo de IA*

This repository present the contents developed for my Master Thesis, titled: ***"Aplicación móvil didáctica para el aprendizaje del lenguaje de signos mediante la integración de un modelo de IA"***. Mainly, the project code for the Android application developed.

TODO Application Video 
***

## Table of Contents

  * [Application Screenshots](#application-screenshots)
  * [Android Project Structure](#android-project-structure)
  * [AI Model Development](#ai-model-development)
  * [Getting Started - Application Installation](#application-screenshots)
    * [Prerequisites](#application-screenshots)
    * [Install via Android Studio compilation](#application-screenshots)
    * [Install via APK](#application-screenshots)
  * [Planned features (future work development)](#application-screenshots)


## Application Screenshots

TODO

## Android Project Structure

This application project file structure is explained as follows:

TODO Explain
```
.
├── main
│   ├── AndroidManifest.xml
│   ├── assets/: Folder containing AI tensorflow model and labels file.
│   │   ├── detect_sign_V4B_meta.tflite
│   │   └── labelmap_signs.txt
│   ├── java/
│   │   └── com/
│   │       └── andreaziqing/
│   │           └── signlanguagedetectionapp/
│   │               ├── Authentication/: Firebase Auth -related code.
│   │               ├── Databases/: Session management data code
│   │               ├── DetectionGames/: Lessons and Games main business logic code.
│   │               ├── Detector/: AI model recognition logic (Tensorflow Lite code).
│   │               ├── HelperClasses/: Adapters, view, environment and tracking helper classes.
│   │               ├── Navigation/: Navigation tabs controller code.
│   │               ├── OnBoarding/: Onboarding screen cards code.
│   │               └── Tabs/: Navigation tabs code.
│   └── res/: Resources folder with animation, drawables, app layout, images, etc.
│       ├── anim/: Button animation xml files.
│       ├── drawable/: Contains most images shown in games/lessons.
│       ├── layout/ : Contains app activity layout xml files
│       ├── menu/: nav view menu icons.
│       ├── mipmap/ folders: app icon files. 
│       ├── raw/ : Holds lottie .json files and sound effect files.
│       ├── values/: Localization strings and color/theme xml files.
└──androidTest/ and test/ folders: placeholder test folders for testing purposes.

```

## AI Model Development Folder

The folder `SignLanguageModel` contains the resources that have been developed for the Sign Language detection neural network that has been trained and exported to be used in this application.

Where:
 - `ModelTraining.ipynb`: Python (Jupyter Notebook format) code that has been used for the model training.
 - `ModelExport.ipynb`: Python (Jupyter Notebook format) code that has been used for the model export as tflite to be used in 
 - `detect_sign_V4B_meta.tflite`: TFLite model exported file.
 - `labels.txt`: "Labels" file containing the classes that the model has been trained on to detect. Namely, the sign language 

To achieve a sign language detection AI model, a MobileNet SSD V2 neural network has been fine tuned (trained) over this [a dataset of 1728 labeled images](https://public.roboflow.com/object-detection/american-sign-language-letters/1), and its resulting model has been exported to a Tensorflow Lite model format that has been integrated into a generic object detection framework in [Android](https://github.com/tensorflow/examples/tree/3c3806673635b702d5ce936f6f2235b84a937777/lite/examples/object_detection/android).

The main code regarding the "low level" TensorflowLite model integration in Java/Android has been obtained from the [TFLite Official Object Detection sample](https://github.com/tensorflow/examples/tree/3c3806673635b702d5ce936f6f2235b84a937777/lite/examples/object_detection/android); more specifically from the "lib_task_api" API and the "tensorflow/lite/examples/detection/" sections; those regarding the `customview/`,  `env/`,  `tracking/` helpers as well as the `CameraActivity` and `DetectorActivity` core API classes.

## Getting Started - Application Installation

### Prerequisites
- Android device with at least Android TODO version.
- Android Studio (if chose to install via Android Studio compilation).

You can install the application on your android smartphone in two ways:

### Install via Android Studio compilation

1. Clone this repository into a local folder of your computer with `git clone https://github.com/andrezg98/sign-language-detection.git`
2. Load the project into Android Studio.
3. Build and Install the application to your smartphone provided is connected to your computer and Android Studio has connection to it (i.e. appears as a Build target).

### Install via APK

You can find the latest `.apk` file with the compiled application [here](). To install it to your Android phone, you can simply (TODO)

## Planned features (future work development)

- [ ] TODO 1
- [ ] TODO 2
- [ ] TODO 3
- [ ] TODO 4