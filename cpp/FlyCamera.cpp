// FlyCamera.cpp : Defines the exported functions for the DLL   // 2017 February 27

#define _CRT_SECURE_NO_WARNINGS // dunno what this does, but FLIR's FlyCap did it

#include "fly2cam_FlyCamera.h" // #includes <jni.h> (sb "jni.h"), "jni_md.h"
#include "FlyCapture2_C.h" // #includes "FlyCapture2Defs_C.h","FlyCapture2Platform_C.h"

#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif

	// These are the instance variables (in Java)..
	//   private int rose, colz, tile, errn;
	//   private long stuff; // used for error reporting, or not at all

	fc2Context theContext = NULL; // gotta preserve this across multiple images
	fc2Image rawImage; // this should be valid if theContext != NULL
	fc2Image RGBimage; // this should be valid if nRGBpixels>0
	int nRGBpixels = 0;

	/*
	* Class:     fly2cam_FlyCamera
	* Method:    Connect                                                     // Connect(..
	* Signature: ()V
	*/
	JNIEXPORT jboolean JNICALL Java_fly2cam_FlyCamera_Connect
	(JNIEnv *env, jobject thisObj, jint frameRate, jint exposure, jint shutter, jint gain) {
		int why = 0, rose = 0, colz = 0, tile = 0, form = 0, sofar = 0, DebugShow = 0;
		int nx, stride = 0;
		fc2Error nerror = FC2_ERROR_OK;
		fc2PGRGuid camGuid; // only used to select the single camera
		fc2Config info; // so we can look at what it thinks it will do
		fc2Property propty; // to set frame rate

		// Get a reference to this object's class
		jclass thisClass = env->GetObjectClass(thisObj);
		// Get the Field ID of the instance variables..
		jfieldID stuffID = env->GetFieldID(thisClass, "stuff", "J");
		jfieldID errnID = env->GetFieldID(thisClass, "errn", "I");
		jfieldID roseID = env->GetFieldID(thisClass, "rose", "I");
		jfieldID colzID = env->GetFieldID(thisClass, "colz", "I");
		jfieldID tileID = env->GetFieldID(thisClass, "tile", "I");

		while (true) {
			nRGBpixels = 0;
			why++; // why = 1
			nerror = fc2CreateContext(&theContext); // this creates a reference linking calls
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 2
			nerror = fc2GetNumOfCameras(theContext, &nx); // find all cameras
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 3
			if (nx == 0) break;
			why++; // why = 4
			nerror = fc2GetCameraFromIndex(theContext, 0, &camGuid); // choose first camera
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 5
			nerror = fc2Connect(theContext, &camGuid); // make it current
			if (nerror != FC2_ERROR_OK) break;

			// we use defaults, but could do other configuration/setups here, frex..
			nerror = fc2GetConfiguration(theContext, &info);
			if (nerror == FC2_ERROR_OK) if (DebugShow>0) {
				sofar = info.grabTimeout;
				if (stuffID != NULL) env->SetLongField(thisObj, stuffID, (jlong)sofar);
				sofar = 0;
			}

			if (frameRate != 0) {
				why = 26;
				sofar = (int)frameRate;
				propty.type = FC2_FRAME_RATE;
				propty.present = TRUE;
				propty.absControl = FALSE;
				propty.onePush = FALSE;
				propty.onOff = FALSE;
				propty.autoManualMode = FALSE;
				propty.valueA = 0;
				propty.valueB = 0;
				propty.absValue = 0;
				nerror = fc2GetProperty(theContext, &propty);
				if (nerror != FC2_ERROR_OK) break;
				why++; // why = 27
				if (sofar == FC2_FRAMERATE_7_5) {
					propty.valueA = (int)FC2_FRAMERATE_7_5; // =2
					propty.absValue = 7.5;
				}
				else if (sofar == FC2_FRAMERATE_15) {
					propty.valueA = (int)FC2_FRAMERATE_15; // =3
					propty.absValue = 15;
				}
				else if (sofar == FC2_FRAMERATE_30) {
					propty.valueA = (int)FC2_FRAMERATE_30; // =4
					propty.absValue = 30;
				}
				else break;
				why++; // why = 28
				propty.autoManualMode = FALSE;
				propty.absControl = TRUE;
				propty.onePush = TRUE;
				nerror = fc2SetProperty(theContext, &propty);
				if (nerror != FC2_ERROR_OK) break;
				why = 5;
				sofar = 0;
			}

			nerror = (fc2Error) Java_fly2cam_FlyCamera_SetExposure(env, thisObj, exposure);
			if (nerror != FC2_ERROR_OK) break;
			nerror = (fc2Error) Java_fly2cam_FlyCamera_SetShutter(env, thisObj, shutter);
			if (nerror != FC2_ERROR_OK) break;
			nerror = (fc2Error) Java_fly2cam_FlyCamera_SetGain(env, thisObj, gain);
			if (nerror != FC2_ERROR_OK) break;

			why++; // why = 6
			nerror = fc2StartCapture(theContext); // uses current (initial) video mode
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 7
			nerror = fc2CreateImage(&rawImage);
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 8
			nerror = fc2RetrieveBuffer(theContext, &rawImage);
			if (nerror == FC2_ERROR_OK) { // GetImDim is broken, rtns all 0s..
										  // nerror = fc2GetImageDimensions(&rawImage,&rose,&colz,&stride,&form,&tile);
				sofar = (int)rawImage.receivedDataSize; // true data size
				rose = (int)rawImage.rows; // came through double,
				colz = (int)rawImage.cols; //   = number of sensors, not pixels
				tile = (int)rawImage.bayerFormat; // default 1=RG/GB, 3=GB/RG
				form = (int)rawImage.format; // default FC2_PIXEL_FORMAT_RAW8
				if (form == 0) why = 10;
				else if (form != FC2_PIXEL_FORMAT_RAW8) why = (int)form;
			}
			else why++; // why = 9
			if (why == 8) why = 0;
			break;
		} //~while
		if (why>1) if (why<12) if (theContext != NULL) {
			if (why>6) nerror = fc2StopCapture(theContext);
			if (why>7) nerror = fc2DestroyImage(&rawImage);
			fc2DestroyContext(theContext);
			theContext = NULL;
		}
		if (false) if (why>0) {
			rose = 0;
			colz = 0;
			tile = 0;
		}
		if (stuffID != NULL) env->SetLongField(thisObj, stuffID, (jlong)sofar);
		if (tileID != NULL) env->SetIntField(thisObj, tileID, (jint)tile);
		if (colzID != NULL) env->SetIntField(thisObj, colzID, (jint)((colz&-16) >> 1)); // = number of pix
		if (roseID != NULL) env->SetIntField(thisObj, roseID, (jint)((rose&-16) >> 1));
		if (errnID != NULL) env->SetIntField(thisObj, errnID, (jint)why);
		return why == 0;
	} //~Connect

	  /*
	  * Class:     fly2cam_FlyCamera
	  * Method:    NextFrame                                          // NextFrame(..
	  * Signature: ([B)Z
	  */
	JNIEXPORT jboolean JNICALL Java_fly2cam_FlyCamera_NextFrame
	(JNIEnv *env, jobject thisObj, jbyteArray pixels) {
		int tile, rx, cx, nx = 0, roff = 0, coff = 0, sofar = 0, colz = 0, why = 19;
		int zx = 0;
		fc2Error nerror = FC2_ERROR_OK;
		jsize lxx = 0;
		jbyte * Jpix = NULL;  jbyte * Ipix = NULL;
		jboolean isCopy;
		unsigned char* camData;
		// Get a reference to this object's class
		jclass thisClass = env->GetObjectClass(thisObj);
		// Get the Field ID of the instance variable..
		jfieldID stuffID = env->GetFieldID(thisClass, "stuff", "J");
		jfieldID errnID = env->GetFieldID(thisClass, "errn", "I");
		jfieldID colzID = env->GetFieldID(thisClass, "colz", "I");
		// int colz = (int)(env->GetIntField(thisObj,colzID)), rose = colz>>1;
		while (true) {
			// if (colz != 320) if (colz != 640) break; // why = 19
			// colz = colz<<1; // now it should match what FlyCap2 gives us
			// rose = (rose<<1)+rose; // assume 3x4 ratio
			why = 11;
			if (pixels == NULL) break;
			why++; // why = 12
			if (theContext == NULL) break;
			why++; // why = 13
			nerror = fc2GetNumOfCameras(theContext, &zx); // see if ptr still works
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 14
			lxx = env->GetArrayLength(pixels);
			if (lxx <= 0) break;
			// why++; // why = 14
			if (lxx > 0xFFFFFF) break;
			nx = (int)lxx;
			why++; // why = 15
			Jpix = env->GetByteArrayElements(pixels, &isCopy);
			if (Jpix == NULL) break;
			Ipix = Jpix;
			why++; // why = 16
			nerror = fc2RetrieveBuffer(theContext, &rawImage);
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 17
			nerror = fc2GetImageData(&rawImage, &camData);
			if (nerror != FC2_ERROR_OK) break;
			why++; // why = 18
			sofar = rawImage.receivedDataSize; // we move bytes, it also seems to be in bytes
			if (sofar <= 0) break;
			rx = (int)rawImage.rows; // came through double,
			cx = (int)rawImage.cols; //   = number of sensors per row/col, not pixels
			tile = (int)rawImage.bayerFormat; // default 1=RG/GB (Firefly), 3=GB/RG (Chameleon)
			roff = rx & 15; // excess sensor rows
			coff = cx & 15; // excess sensor columns, =8 in Chameleon, =0 in FireFly
			colz = cx&-16; // =1280 in Chameleon, =640 in FireFly
			why++; // why = 19
			if ((cx & 1) != 0) break; // shouldn't be odd number of sensors in row
			if (colz >= 4096) break; // why = 19
									 // if ((colz&-colz)<32) break;
									 // if (colz+coff != cx) break; // else pace=0, same byte rate
			if (roff + coff != 0) { // Chameleon default has a few extra pixels around the edges..
				sofar = sofar - (roff*cx + (rx - roff)*coff); // = nominal image w/o excess bytes
				roff = (rx & 12) >> 1; // gotta take extra sensors off in pairs, half each side..
									   // coff = cx&15; // skip over this many bytes each row to center image in frame
				roff = roff*cx + ((cx & 12) >> 1); // skip over this many bytes before starting, ditto
				camData = (unsigned char*)(((long)camData) + ((long)roff));
			}
			why = ~why; // why = -20 // pixel array size != image data size (OK)..
			if (sofar == nx) { // capture 1st pixel for comparison in Java debugger..
				sofar = ((((int)camData[0]) & 255) << 24) | ((((int)camData[1]) & 255) << 16)
					| ((((int)camData[cx]) & 255) << 8) | ((int)camData[cx + 1]) & 255; // = RGGB 1st pix
				why = 0;
			}
			else if (sofar<nx) nx = sofar; // ..just use the smaller (report compared size)
			nx = nx&-2;
			cx = cx << 1; // = the number of bytes in a pair of camera rows
			rx = colz; // = the number of bytes wanted in this row, = (pixels in dest row)*2
			roff = -1; // counts sensor rows, so to double (or halve) in pairs

			while (nx-- >0) { // nx counts dest bytes
				if (rx>0) // still in the pixels of this row to copy..
					*Ipix++ = (jbyte)(*camData);
				camData++;
				rx--; // rx counts bytes in dest row (continuing over excess, rx<0)
				if (rx + coff>0) continue; // skips over excess pixels each row
				rx = colz;
			} //~while (nx) // starting new source row
			break;
		} //~while
		if (Jpix != NULL) env->ReleaseByteArrayElements(pixels, Jpix, 0); // update Java
		if (nerror != FC2_ERROR_OK) sofar = (int)nerror; // report any error..
		if (errnID != NULL) env->SetIntField(thisObj, errnID, (jint)why);
		// if (stuffID != NULL) env->SetLongField(thisObj,stuffID,(jlong)sofar);
		return why <= 0;
	} //~NextFrame // = true if OK

	  /*
	  * Class:     fly2cam_FlyCamera
	  * Method:    Finish                                                     // Finish
	  * Signature: ()V
	  */
	JNIEXPORT void JNICALL Java_fly2cam_FlyCamera_Finish(JNIEnv *env, jobject thisObj) {
		int why = 0;
		fc2Error nerror = FC2_ERROR_OK;
		// fc2Context myContext = NULL;
		// Get a reference to this object's class
		jclass thisClass = env->GetObjectClass(thisObj);
		// Get the Field ID of the instance variables..
		jfieldID roseID = env->GetFieldID(thisClass, "rose", "I");
		jfieldID colzID = env->GetFieldID(thisClass, "colz", "I");
		jfieldID tileID = env->GetFieldID(thisClass, "tile", "I");
		jfieldID errnID = env->GetFieldID(thisClass, "errn", "I");
		jfieldID stuffID = env->GetFieldID(thisClass, "stuff", "J");
		if (nRGBpixels>0) {
			nerror = fc2DestroyImage(&RGBimage);
			if (nerror != FC2_ERROR_OK) why = 20;
			nRGBpixels = 0;
		}
		if (theContext != NULL) {
			nerror = fc2StopCapture(theContext);
			if (nerror != FC2_ERROR_OK) why = why | 21;
			nerror = fc2DestroyImage(&rawImage);
			if (nerror != FC2_ERROR_OK) why = why | 22;
			fc2DestroyContext(theContext);
			theContext = NULL;
		}
		else if (why == 0) why--; // why = -1 // already closed
		if (stuffID != NULL) env->SetLongField(thisObj, stuffID, (jlong)nerror);
		if (tileID != NULL) env->SetIntField(thisObj, tileID, 0);
		if (colzID != NULL) env->SetIntField(thisObj, colzID, 0);
		if (roseID != NULL) env->SetIntField(thisObj, roseID, 0);
		if (errnID != NULL) env->SetIntField(thisObj, errnID, (jint)why);
	} //~Finish

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_SetExposure
	(JNIEnv *env, jobject thisObj, jint exposure)
	{
		fc2Property propty2; // to set auto exposure
		int nerror;

		propty2.type = FC2_AUTO_EXPOSURE; // 1 to 1023
		nerror = fc2GetProperty(theContext, &propty2);
		if (nerror != FC2_ERROR_OK) return (jint) nerror;
		propty2.onOff = true;
		propty2.autoManualMode = exposure < 1;
		propty2.onePush = exposure > 1023;
		propty2.absControl = false;
		if(exposure >= 1 && exposure <= 1023) propty2.valueA = exposure;
		return (jint) fc2SetProperty(theContext, &propty2);
	}

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_GetExposure
	(JNIEnv *env, jobject thisObj)
	{
		fc2Property propty2; // to set auto exposure
		int nerror;

		propty2.type = FC2_AUTO_EXPOSURE; // 1 to 1023
		nerror = fc2GetProperty(theContext, &propty2);
		return propty2.valueA;
	}

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_SetShutter
	(JNIEnv *env, jobject thisObj, jint shutter)
	{
		fc2Property propty3; // to set shutter
		int nerror;

		propty3.type = FC2_SHUTTER; // 1 to 966
		nerror = fc2GetProperty(theContext, &propty3);
		if (nerror != FC2_ERROR_OK) return (jint) nerror;
		propty3.onOff = true;
		propty3.autoManualMode = shutter < 1;
		propty3.onePush = shutter > 966;
		propty3.absControl = false;
		if(shutter >= 1 && shutter <= 966) propty3.valueA = shutter;
		return (jint) fc2SetProperty(theContext, &propty3);
	}

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_GetShutter
	(JNIEnv *env, jobject thisObj)
	{
		fc2Property propty3; // to set shutter
		int nerror;

		propty3.type = FC2_SHUTTER; // 1 to 966
		nerror = fc2GetProperty(theContext, &propty3);
		return propty3.valueA;
	}

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_SetGain
	(JNIEnv *env, jobject thisObj, jint gain)
	{
		fc2Property propty4; // to set gain
		int nerror;

		propty4.type = FC2_GAIN; // 256 to 814
		nerror = fc2GetProperty(theContext, &propty4);
		if (nerror != FC2_ERROR_OK) return (jint) nerror;
		propty4.onOff = true;
		propty4.autoManualMode = gain < 256;
		propty4.onePush = gain > 814;
		propty4.absControl = false;
		if(gain >= 256 && gain <= 814) propty4.valueA = gain;
		return (jint) fc2SetProperty(theContext, &propty4);
	}

	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_GetGain
	(JNIEnv *env, jobject thisObj)
	{
		fc2Property propty4; // to set gain
		int nerror;

		propty4.type = FC2_GAIN; // 256 to 814
		nerror = fc2GetProperty(theContext, &propty4);
		return propty4.valueA;
	}
	
	JNIEXPORT jint JNICALL Java_fly2cam_FlyCamera_ActivateHDR
	(JNIEnv *env, jobject thisObj)
	{
		const int HDRCtrl = 0x1800;
		const int HDRShutter1 = 0x1820;
		const int HDRShutter2 = 0x1840;
		const int HDRShutter3 = 0x1860;
		const int HDRShutter4 = 0x1880;
		const int HDRGain1 = 0x1824;
		const int HDRGain2 = 0x1844;
		const int HDRGain3 = 0x1864;
		const int HDRGain4 = 0x1884;
		const int HDROn = 0x82000000;
		const int HDROff = 0x80000000;
		
		// Initialize HDR Registers
		fc2Error error;
		
	    error = (fc2Error) WriteRegister(HDRShutter1, 0x000);
	    error = (fc2Error) WriteRegister(HDRShutter2, 0x120);
	    error = (fc2Error) WriteRegister(HDRShutter3, 0x240);
	    error = (fc2Error) WriteRegister(HDRShutter4, 0x360);
	
	    error = (fc2Error) WriteRegister(HDRGain1, 0x000);
	    error = (fc2Error) WriteRegister(HDRGain2, 0x0E3);
	    error = (fc2Error) WriteRegister(HDRGain3, 0x1C6);
	    error = (fc2Error) WriteRegister(HDRGain4, 0x2A9);

		if(error != FC2_ERROR_OK) return (jint) error;
		
		// Toggle HDR
	    error = (fc2Error) WriteRegister(HDRCtrl, HDROn);
		if(error != FC2_ERROR_OK) return (jint) error;
		
		
	}
	
	int WriteRegister(int reg, int val)
	{
		fc2Error error = FC2_ERROR_OK;
		int toWrite;
		
		error = fc2ReadRegister(reg, &toWrite);
		if(error != FC2_ERROR_OK) return (int) error;
		
		toWrite = val;
		return (int) fc2WriteRegister(reg, toWrite, false);	
	}

#ifdef __cplusplus
}
#endif // (FlyCamera DLL) [DLL]
