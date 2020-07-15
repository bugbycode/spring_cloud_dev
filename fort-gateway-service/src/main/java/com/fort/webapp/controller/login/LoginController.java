package com.fort.webapp.controller.login;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.util.ImgCodeUtil;

@RestController
@RequestMapping("/login")
public class LoginController {

	@GetMapping("/imgCode")
    public void imgCode(HttpServletResponse response, HttpSession session) throws IOException {
        String codeKey = "abcdefghkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        OutputStream os = response.getOutputStream();
        Random random = new SecureRandom();
        int width = 100, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ImgCodeUtil imgCodeUtil = new ImgCodeUtil();

        Graphics g = imgCodeUtil.getImgCode(image.getGraphics(), width, height);

        char[] arr = codeKey.toCharArray();

        String sRand = "";

        StringBuffer buff = new StringBuffer();

        for (int j = 0; j < 4; j++) {

            int code = (int) (random.nextDouble() * 53);

            char c = arr[code];

            String rand = String.valueOf(c);

            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));

            g.drawString(rand, 22 * j + 10, 25);

            buff.append(rand);
        }

        sRand = buff.toString();

        session.setAttribute("rand", sRand.toUpperCase());

        g.dispose();

        ImageIO.write(image, "JPEG", os);

        os.flush();
        os.close();
        os = null;

        response.flushBuffer();
    }
}
