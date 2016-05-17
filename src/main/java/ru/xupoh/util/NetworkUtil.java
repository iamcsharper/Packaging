package ru.xupoh.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import ru.xupoh.network.ByteBufUtils;

@SuppressWarnings("unchecked")
public class NetworkUtil {
	private static final Map<Class<?>, BiConsumer<Object, ByteBuf>> writers = new HashMap<Class<?>, BiConsumer<Object, ByteBuf>>();
	private static final Map<Class<?>, Function<ByteBuf, Object>> readers = new HashMap<Class<?>, Function<ByteBuf, Object>>();

	public static void registerWriter(Class<?> classFor, BiConsumer<Object, ByteBuf> writer) {
		writers.put(classFor, writer);
	}

	public static void unregisterWriter(Class<?> classFor, BiConsumer<Object, ByteBuf> writer) {
		writers.remove(classFor, writer);
	}

	public static void registerReader(Class<?> classFor, Function<ByteBuf, Object> reader) {
		readers.put(classFor, reader);
	}

	public static void unregisterReader(Class<?> classFor, Function<ByteBuf, Object> reader) {
		readers.remove(classFor, reader);
	}

	public static void encodeData(ByteBuf buffer, Collection<Object> sendData) throws IOException {
		for (Object object : sendData) {
			assert object != null : "Null packet data, check bug!";

			// Writers something
			writers.get(object.getClass()).accept(object, buffer);
		}
	}

	public static ArrayList<Object> decodeData(Class<?>[] types, ByteBuf buffer) throws IOException {
		ArrayList<Object> objList = new ArrayList<Object>();

		for (Class<?> clazz : types) {
			assert clazz != null : "Null class can't decode!";

			// Writers something
			objList.add(readers.get(clazz).apply(buffer));
		}

		return objList;
	}
	
	public static <T> T formatClass(T type, List<Object> list) {
		return type;
	}

	static {
		writers.put(Integer.class, (obj, buffer) -> {
			buffer.writeInt((Integer) obj);
		});

		readers.put(Integer.class, (buffer) -> {
			return buffer.readInt();
		});

		writers.put(Float.class, (obj, buffer) -> {
			buffer.writeFloat((Float) obj);
		});

		readers.put(Float.class, (buffer) -> {
			return buffer.readFloat();
		});

		writers.put(Double.class, (obj, buffer) -> {
			buffer.writeDouble((Double) obj);
		});

		readers.put(Double.class, (buffer) -> {
			return buffer.readDouble();
		});

		writers.put(Byte.class, (obj, buffer) -> {
			buffer.writeByte((Byte) obj);
		});

		readers.put(Byte.class, (buffer) -> {
			return buffer.readByte();
		});

		writers.put(Boolean.class, (obj, buffer) -> {
			buffer.writeBoolean((Boolean) obj);
		});

		readers.put(Boolean.class, (buffer) -> {
			return buffer.readBoolean();
		});

		writers.put(String.class, (obj, buffer) -> {
			ByteBufUtils.writeUTF8String(buffer, (String) obj);
		});

		readers.put(String.class, (buffer) -> {
			return ByteBufUtils.readUTF8String(buffer);
		});

		writers.put(Short.class, (obj, buffer) -> {
			buffer.writeShort((Short) obj);
		});

		readers.put(Short.class, (buffer) -> {
			return buffer.readShort();
		});

		writers.put(Long.class, (obj, buffer) -> {
			buffer.writeLong((Long) obj);
		});

		readers.put(Long.class, (buffer) -> {
			return buffer.readLong();
		});

		writers.put(byte[].class, (obj, buffer) -> {
			buffer.writeInt(((byte[]) obj).length);
			for (int i = 0; i < ((byte[]) obj).length; i++) {
				buffer.writeByte(((byte[]) obj)[i]);
			}
		});
		
		readers.put(byte[].class, (buffer) -> {
			byte[] bytes = new byte[buffer.readInt()];
			
			for (int i = 0; i < bytes.length; ++i) {
				bytes[i] = buffer.readByte();	
			}
			
			return buffer.readBoolean();
		});

		writers.put(Integer[].class, (obj, buffer) -> {
			Integer[] array = (Integer[]) obj;
			buffer.writeInt(array.length);

			for (int i = 0; i < array.length; i++) {
				buffer.writeInt(array[i]);
			}
		});
		
		readers.put(byte[].class, (buffer) -> {
			byte[] bytes = new byte[buffer.readInt()];
			
			for (int i = 0; i < bytes.length; ++i) {
				bytes[i] = buffer.readByte();	
			}
			
			return buffer.readBoolean();
		});

		writers.put(Collection.class, (obj, buffer) -> {
			try {
				NetworkUtil.encodeData(buffer, (Collection<Object>) obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
