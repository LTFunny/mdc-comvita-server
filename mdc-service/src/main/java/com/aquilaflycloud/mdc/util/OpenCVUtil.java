package com.aquilaflycloud.mdc.util;

import cn.hutool.system.SystemUtil;
import com.aquilaflycloud.util.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.IOException;

@Slf4j
public class OpenCVUtil {
    private static String frontalface_alt;

    static {
        String jarName;
        if (SystemUtil.getOsInfo().isWindows()) {
            jarName = "opencv_java342.dll";
        } else if (SystemUtil.getOsInfo().isLinux()) {
            jarName = "libopencv_java342.so";
        } else if (SystemUtil.getOsInfo().isMac()) {
            jarName = "libopencv_java342.dylib";
        } else {
            //获取不到系统类型时,默认是Linux系统
            jarName = "libopencv_java342.so";
        }
        try {
            NativeUtils.loadLibrary(AliOssUtil.getObject("opencv/" + jarName).getObjectContent(), jarName);
            frontalface_alt = NativeUtils.getTempFromJar("/opencv/haarcascades/haarcascade_frontalface_alt.xml", null);
        } catch (IOException e) {
            log.error("初始化opencv库失败", e);
        }
    }

    public static int getFaceCount(byte[] bytes) {
        Mat frame = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        int absoluteFaceSize = 0;
        int height = grayFrame.rows();
        if (Math.round(height * 0.1f) > 0) {
            absoluteFaceSize = Math.round(height * 0.1f);
        }

        // detect faces
        CascadeClassifier faceCascade = new CascadeClassifier(frontalface_alt);
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        return facesArray.length;
        /*for (Rect rect : facesArray) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);
        }*/
    }
}
