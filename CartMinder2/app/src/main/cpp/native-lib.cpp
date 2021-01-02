#include <jni.h>
#include <string>
#include <cstdlib>
#include <math.h>
#include <cmath>

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_cartminder2_glscreen_shape_Pos_add2dArray(
        JNIEnv *pEnv,
        jobject _this,
        jfloatArray _pos,
        jfloatArray _otherPos
) {
    jfloat *pos = pEnv->GetFloatArrayElements(_pos, nullptr);
    jfloat *otherPos = pEnv->GetFloatArrayElements(_otherPos, nullptr);
    jfloat temp_res[2] = {pos[0] + otherPos[0], pos[1] + otherPos[1]};
    jfloatArray res = pEnv->NewFloatArray(2);
    pEnv->SetFloatArrayRegion(res, 0, 2, temp_res);
    return res;
}

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_cartminder2_glscreen_shape_Pos_sub2dArray(
        JNIEnv *pEnv,
        jobject _this,
        jfloatArray _pos,
        jfloatArray _otherPos
) {
    jfloat *pos = pEnv->GetFloatArrayElements(_pos, nullptr);
    jfloat *otherPos = pEnv->GetFloatArrayElements(_otherPos, nullptr);
    jfloat temp_res[2] = {pos[0] - otherPos[0], pos[1] - otherPos[1]};
    jfloatArray res = pEnv->NewFloatArray(2);
    pEnv->SetFloatArrayRegion(res, 0, 2, temp_res);
    return res;
}

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinlh_cartminder2_glscreen_shape_Pos_rotate2dArray(
        JNIEnv *pEnv,
        jobject _this,
        jfloatArray _pos,
        jfloat deg) {
    jfloat *pos = pEnv->GetFloatArrayElements(_pos, nullptr);
    jfloat temp_res[2] = {};

    jfloat radian = (deg * M_PI) / 180;
    temp_res[0] = pos[0] * cos(radian) + pos[1] * -sin(radian);
    temp_res[1] = pos[0] * sin(radian) + pos[1] * cos(radian);

    jfloatArray res = pEnv->NewFloatArray(2);
    pEnv->SetFloatArrayRegion(res, 0, 2, temp_res);
    return res;
}