package com.xjcy.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCodeUtils {
	private static final LoggerUtils logger = LoggerUtils.from(VerifyCodeUtils.class);

	private static final Random d = new Random();
	private static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	private static final int codesLen = VERIFY_CODES.length();

	public static String generateCode(int len) {
		String str = "";
		for (int i = 0; i < 4; i++) {
			int num = d.nextInt(10);
			str += num + "";
		}
		return str;
	}
	
	public static String generateVerifyCode(int len) {
        StringBuilder str = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            str.append(VERIFY_CODES.charAt(d.nextInt(codesLen - 1)));
        }
        return str.toString();
    }

	public static void generateImage(int width, int height, OutputStream os, String code) {
		try {
			ImageIO.write(toImage(code, width, height), "jpg", os);
		} catch (IOException e) {
			logger.error("生成验证码失败", e);
		}
	}

	private static RenderedImage toImage(String code, int width, int height) {
		// 字体大小
		int fontSize = (int) Math.ceil(height * 0.8);
		if (fontSize < 18) {
			fontSize = 18;
		}

		// 字体在Y坐标上的位置
		int positionY = (int) Math.ceil(height * 0.8);

		int lenCode = code.length();

		// 计算字体宽度
		int fontWidth = width / (lenCode + 2);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();

		// 图片背景随机颜色
		g.setColor(randomColor(200, 250));
		g.fillRect(0, 0, width, height);

		// 设置字体
		g.setFont(new Font("Times New Roman", Font.BOLD, fontSize));

		// 在图片上画纵横交错的线，达到混淆效果
		drawLines(g, width, height);

		// 在图片上画验证码
		drawString(g, code, fontWidth, positionY);
		
		//shear(g, width, height, null);

		g.dispose();

		return image;
	}

	/**
	 * 静态方法 画纵横交错的线
	 * 
	 * @param g
	 * @param width
	 *            验证码图片宽度
	 * @param height
	 *            验证码图片高度
	 */
	private static void drawLines(Graphics g, int width, int height) {
		// 线的数量
		int count = ((int) (Math.ceil(d.nextDouble() * 100))) + 100;

		for (int i = 0; i < count; i++) {
			int fc = 160 + (int) Math.ceil(d.nextDouble() * 40);
			int bc = 200 + (int) Math.ceil(d.nextDouble() * 55);
			g.setColor(randomColor(fc, bc));

			int x = d.nextInt(width);
			int y = d.nextInt(height);
			int xl = d.nextInt(width / 5);
			int yl = d.nextInt(height / 5);
			g.drawLine(x, y, x + xl, y + yl);
		}
	}

	private static void drawString(Graphics g, String code, int fontWidth, int positionY) {
		int len = code.length();
		for (int i = 0; i < len; i++) {
			g.setColor(randomColor());
			g.drawString(String.valueOf(code.charAt(i)), (i + 1) * fontWidth, positionY);
		}
	}

	private static Color randomColor() {
		int r = d.nextInt(255);
		int g = d.nextInt(255);
		int b = d.nextInt(255);
		return new Color(r, g, b);
	}

	private static Color randomColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}

		if (bc > 255) {
			bc = 255;
		}

		int diff = bc - fc;
		int r = fc + d.nextInt(diff);
		int g = fc + d.nextInt(diff);
		int b = fc + d.nextInt(diff);
		return new Color(r, g, b);
	}
	
	/**
	 * 使图片扭曲
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	@SuppressWarnings("unused")
	private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }
 
    private static void shearX(Graphics g, int w1, int h1, Color color) {
 
        int period = d.nextInt(2);
 
        boolean borderGap = true;
        int frames = 1;
        int phase = d.nextInt(2);
 
        for (int i = 0; i < h1; i++) {
            double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                //g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
 
    }
 
    private static void shearY(Graphics g, int w1, int h1, Color color) {
 
        int period = d.nextInt(40) + 10; // 50;
 
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                //g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
 
        }
 
    }
}
