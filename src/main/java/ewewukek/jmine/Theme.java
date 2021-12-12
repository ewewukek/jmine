package ewewukek.jmine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Theme {
    public final int fieldPatchLeft;
    public final int fieldPatchTop;
    public final int fieldPatchRight;
    public final int fieldPatchBottom;
    public final int buttonMarginTop;
    public final int counterMarginTop;
    public final int counterMarginLeft;
    public final int counterMarginRight;
    public final int counterPaddingTop;
    public final int counterPaddingLeft;
    public final int counterDigitSpacing;

    public Theme(String path) throws IOException {
        Properties properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IOException("could not load " + path);
        }
        properties.load(is);

        fieldPatchLeft = getInteger(properties, "fieldPatchLeft");
        fieldPatchTop = getInteger(properties, "fieldPatchTop");
        fieldPatchRight = getInteger(properties, "fieldPatchRight");
        fieldPatchBottom = getInteger(properties, "fieldPatchBottom");
        buttonMarginTop = getInteger(properties, "buttonMarginTop");
        counterMarginTop = getInteger(properties, "counterMarginTop");
        counterMarginLeft = getInteger(properties, "counterMarginLeft");
        counterMarginRight = getInteger(properties, "counterMarginRight");
        counterPaddingTop = getInteger(properties, "counterPaddingTop");
        counterPaddingLeft = getInteger(properties, "counterPaddingLeft");
        counterDigitSpacing = getInteger(properties, "counterDigitSpacing");
    }

    // wrap into IOException for convenience
    private static int getInteger(Properties properties, String key) throws IOException {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IOException("missing property: " + key);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IOException("bad integer \"" + value + "\" in property " + key, e);
        }
    }
}
