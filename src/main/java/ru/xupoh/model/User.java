package ru.xupoh.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * �����-������ ��� �������� (Person).
 */
public class User {

	private final StringProperty nickName;
	private final StringProperty firstName;
	private final StringProperty ip;

	/**
	 * ����������� �� ���������.
	 */
	public User() {
		this(null);
	}

	/**
	 * ����������� � ���������� ���������� �������.
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public User(String nickName) {
		this.nickName = new SimpleStringProperty(nickName);
		this.firstName = new SimpleStringProperty();
		this.ip = new SimpleStringProperty();
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	/**
	 * @return the ip
	 */
	public StringProperty getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip.set(ip);
	}

	/**
	 * @return the nickName
	 */
	public StringProperty getNickName() {
		return nickName;
	}

	public void setNickname(String nickName) {
		this.nickName.set(nickName);
	}
}