//
// Created by kevin on 12/4/20.
//

#include <jni.h>
#include <string>
#include <cstdlib>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_wseeker_activity_MainActivity_stringFromJNI(JNIEnv *env,
                                                                                       jobject _this) {
    std::string hello = "Hello, World!";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_wseeker_activity_MainActivity_addTwoInt(
        JNIEnv *pEnv,
        jobject _this,
        jint a,
        jint b) {
    return a + b;
}

extern "C" JNIEXPORT jint JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_wseeker_activity_MainActivity_generateRandomInt(
        JNIEnv *pEnv,
        jobject _this) {
    return rand() % 10 + 1;
}