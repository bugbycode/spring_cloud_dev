package com.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.security.SecureRandom;
import java.util.Random;

public class ImgCodeUtil {
	
	private final Random random = new SecureRandom();
	
	public Graphics getImgCode(Graphics g,  int width, int height) {
		g.setColor(getRandColor(150, 200,random));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(getRandColor(180, 200,random));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		return g;
	}

	private Color getRandColor(int fc, int bc, Random random) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
