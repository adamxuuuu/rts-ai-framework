package UI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheet {

    /**
     * Sprite sheet
     */
    private final BufferedImage ss;
    private final Map<String, BufferedImage> spriteMap = new HashMap<>();

    public SpriteSheet(BufferedImage ss) {
        this.ss = ss;
        readFromXml();
    }

    public BufferedImage getSubSprite(String name) {
        return spriteMap.get(name);
    }

    private BufferedImage getSubSprite(int x, int y, int w, int h) {
        return ss.getSubimage(x, y, w, h);
    }

    private void readFromXml() {
        File file = new File("./resources/sprite/rts_spritesheet.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            NodeList subTextures = document.getElementsByTagName("SubTexture");

            for (int i = 0; i < subTextures.getLength(); i++) {
                Element subTextureElement = (Element) subTextures.item(i);
                String name = subTextureElement.getAttribute("name");
                String x = subTextureElement.getAttribute("x");
                String y = subTextureElement.getAttribute("y");
                String width = subTextureElement.getAttribute("width");
                String height = subTextureElement.getAttribute("height");

                BufferedImage subImg = getSubSprite(Integer.parseInt(x), Integer.parseInt(y),
                        Integer.parseInt(width), Integer.parseInt(height));
                spriteMap.put(name, subImg);
            }
        }
    }

    static class SpriteProperty {
        int startX, startY;
        int width, height;

        SpriteProperty(int x, int y, int width, int height) {
            startX = x;
            startY = y;
            this.width = width;
            this.height = height;
        }
    }

}
