package project.team.ondo.global.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

@Converter
public class UuidBinaryConverter implements AttributeConverter<UUID, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(UUID attribute) {
        if (attribute == null) return null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(attribute.getMostSignificantBits());
        byteBuffer.putLong(attribute.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @Override
    public UUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) return null;
        if (dbData.length != 16) throw new IllegalArgumentException("Invalid UUID binary length: " + dbData.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(dbData);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
