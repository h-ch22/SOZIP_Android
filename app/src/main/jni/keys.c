#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_eje_sozip_frameworks_helper_AES256Util_00024Companion_getNativeKey1(JNIEnv *env, jobject instance){
    return(*env) -> NewStringUTF(env, "EJEDLWPDLthwjdckdwls991006991019");

}

JNIEXPORT jstring JNICALL
Java_com_eje_sozip_frameworks_helper_AES256Util_00024Companion_getNativeKey2(JNIEnv *env, jobject instance){
    return(*env) -> NewStringUTF(env, "2DLWPDLghkdlxld2");
}