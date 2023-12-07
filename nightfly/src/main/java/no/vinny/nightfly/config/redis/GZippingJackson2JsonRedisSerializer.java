package no.vinny.nightfly.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZippingJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {
    public GZippingJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public byte[] serialize(@Nullable Object source) throws SerializationException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        try(GZIPOutputStream output = new GZIPOutputStream(result)) {
            final byte[] serialized = super.serialize(source);
            if (serialized == null) {
                return null;
            }
            final InputStream input = new ByteArrayInputStream(serialized);
            input.transferTo(output);
            output.close();
            return result.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Unable to serialize", e);
        }
    }

    public Object deserialize(@Nullable byte[] source) throws SerializationException {
        if(isGZipped(source)) {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                final InputStream input = new GZIPInputStream(new ByteArrayInputStream(source));
                input.transferTo(output);
                output.close();
                return super.deserialize(output.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return super.deserialize(source);
        }
    }

    private boolean isGZipped(byte[] source) {
        return  source != null &&
                source.length > 2 &&
                source[0] == (byte) 0x1F &&
                source[1] == (byte) 0x8B;
    }
}
