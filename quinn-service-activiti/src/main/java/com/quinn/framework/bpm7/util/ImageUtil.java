package com.quinn.framework.bpm7.util;

import com.quinn.util.base.exception.BaseBusinessException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Simon.z
 * @since 2020/6/10
 */
public final class ImageUtil {

    private ImageUtil() {
    }

    public static void convertSvg2Png(InputStream svgIn, OutputStream pngOut) {
        PNGTranscoder transcoder = new PNGTranscoder();
        TranscoderInput input = new TranscoderInput(svgIn);
        TranscoderOutput output = new TranscoderOutput(pngOut);
        try {
            transcoder.transcode(input, output);
        } catch (TranscoderException e) {
            throw new BaseBusinessException();
        }
    }
}
