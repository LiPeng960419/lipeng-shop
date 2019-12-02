package com.lipeng.web.utils;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.misc.BASE64Encoder;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/2 10:13
 */
public class SlidingImage {

    public static final int targetWidth = 60; // 滑动的宽
    public static final int targetHeight = 60; // 滑动图的高
    public static final int circleR = 6; // 小圈的半径
    public static final int r1 = 3; // 距离点

    public static ConvolveOp getGaussianBlurFilter(int radius,
            boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public static void simpleBlur(BufferedImage src, BufferedImage dest) {
        BufferedImageOp op = getGaussianBlurFilter(2, false);
        op.filter(src, dest);
    }

    /**
     * 生成滑动对应图片
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return {backImage: base64, slidingImage: base64'}
     */
    public static Map<String, String> create(int x, int y) throws IOException {
        int[][] blockData = getBlockData();
        // 这里的图片可以准备几张随机获取
        Resource resourceOri = new ClassPathResource("sliderimage/image.png");
        File fileOri = resourceOri.getFile();
        BufferedImage oriImage = ImageIO.read(fileOri);

        BufferedImage targetImage = new BufferedImage(targetWidth, targetHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        cutByTemplate(oriImage, targetImage, blockData, x, y);

        Map<String, String> result = new HashMap<>();

        result.put("backImage", getImageBASE64(oriImage));
        result.put("slidingImage", getImageBASE64(targetImage));
        return result;
    }

    private static int[][] getBlockData() {
        int[][] data = new int[targetWidth][targetHeight];
        double x2 = targetWidth - circleR - 2;
        //随机生成圆的位置
        double h1 = circleR + Math.random() * (targetHeight - 3 * circleR - r1);
        double po = circleR * circleR;

        double xbegin = targetWidth - circleR - r1;
        double ybegin = targetHeight - circleR - r1;

        for (int i = 0; i < targetWidth; i++) {
            for (int j = 0; j < targetHeight; j++) {
                //右边○
                double d3 = Math.pow(i - x2, 2) + Math.pow(j - h1, 2);
                double d2 = Math.pow(j - 2, 2) + Math.pow(i - h1, 2);

                if ((j <= ybegin && d2 <= po) || (i >= xbegin && d3 >= po)) {
                    data[i][j] = 0;
                } else {
                    data[i][j] = 1;
                }
            }
        }
        return data;
    }

    private static void cutByTemplate(BufferedImage oriImage, BufferedImage targetImage,
            int[][] templateImage, int x, int y) {
        for (int i = 0; i < targetWidth; i++) {
            for (int j = 0; j < targetHeight; j++) {
                int rgb = templateImage[i][j];
                // 原图中对应位置变色处理
                int rgb_ori = oriImage.getRGB(x + i, y + j);

                if (rgb == 1) {
                    //抠图上设置对应颜色值
                    targetImage.setRGB(i, j, rgb_ori);
                    int r = (0xff & rgb_ori);
                    int g = (0xff & (rgb_ori >> 8));
                    int b = (0xff & (rgb_ori >> 16));
                    rgb_ori = r + (g << 8) + (b << 16) + (200 << 24);
                    //原图对应位置颜色变化
                    oriImage.setRGB(x + i, y + j, rgb_ori);
                }
            }
        }
    }

    public static String getImageBASE64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] b = out.toByteArray();//转成byte数组
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);//生成base64编码
    }

}