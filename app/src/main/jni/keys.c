#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_eje_sozip_frameworks_helper_AES256Util_00024Companion_getNativeKey1(JNIEnv *env, jobject instance){
    return(*env) -> NewStringUTF(env, "01234567890123450123456789012345");
}

JNIEXPORT jstring JNICALL
Java_com_eje_sozip_frameworks_helper_AES256Util_00024Companion_getNativeKey2(JNIEnv *env, jobject instance){
    return(*env) -> NewStringUTF(env, "0123456789012345");
}