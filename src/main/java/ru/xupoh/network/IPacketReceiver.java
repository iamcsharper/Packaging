package ru.xupoh.network;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import ru.xupoh.model.User;

public interface IPacketReceiver {
	public void getNetworkedData(ArrayList<Object> sendData);

	public void decodePacketdata(ByteBuf buffer);

	public void handlePacketData(User user);
}