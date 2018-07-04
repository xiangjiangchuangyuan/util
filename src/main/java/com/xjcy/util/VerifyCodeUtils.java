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

import org.apache.log4j.Logger;

public class VerifyCodeUtils {
	private static final Logger logger = Logger.getLogger(VerifyCodeUtils.class);

	private static final Random d = new Random();

	public static String generateCode(int len) {
		String str = "";
		for (int i = 0; i < 4; i++) {
			int num = d.nextInt(10);
			str += num + "";
		}
		return str;
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
		int fontSize = (int) Math.ceil(height * 0.9);
		if (fontSize < 20) {
			fontSize = 20;
		}

		// 字体在Y坐标上的位置
		int positionY = (int) Math.ceil(height * 0.8);

		int lenCode = code.length();

		// 计算字体宽度
		int fontWidth = width / (lenCode + 2);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();

		// 图片背景随机颜色
		g.setColor(randomColor(new Random(), 200, 250));
		g.fillRect(0, 0, width, height);

		// 设置字体
		g.setFont(new Font("Times New Roman", Font.BOLD, fontSize));

		// 在图片上画纵横交错的线，达到混淆效果
		drawLines(g, width, height);

		// 在图片上画验证码
		drawString(g, code, fontWidth, positionY);

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
		Random random = new Random();

		// 线的数量
		int count = ((int) (Math.ceil(random.nextDouble() * 100))) + 100;

		for (int i = 0; i < count; i++) {
			int fc = 160 + (int) Math.ceil(random.nextDouble() * 40);
			int bc = 200 + (int) Math.ceil(random.nextDouble() * 55);
			g.setColor(randomColor(random, fc, bc));

			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(width / 5);
			int yl = random.nextInt(height / 5);
			g.drawLine(x, y, x + xl, y + yl);
		}
	}

	private static void drawString(Graphics g, String code, int fontWidth, int positionY) {
		int len = code.length();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			g.setColor(randomColor(random));
			g.drawString(String.valueOf(code.charAt(i)), (i + 1) * fontWidth, positionY);
		}
	}

	private static Color randomColor(Random random) {
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		return new Color(r, g, b);
	}

	private static Color randomColor(Random random, int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}

		if (bc > 255) {
			bc = 255;
		}

		int diff = bc - fc;
		int r = fc + random.nextInt(diff);
		int g = fc + random.nextInt(diff);
		int b = fc + random.nextInt(diff);
		return new Color(r, g, b);
	}
}
