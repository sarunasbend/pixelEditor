public class Driver {
    public static void main(String[] args) {
        Converter imageConverter = new Converter("components\\resources\\ColourPreview.jpg");
        GUI t = new GUI(1200, 800, imageConverter.getWidth(), imageConverter.getHeight(), imageConverter.getPixels());
    }
}