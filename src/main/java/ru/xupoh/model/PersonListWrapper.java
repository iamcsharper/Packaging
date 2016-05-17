package ru.xupoh.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Вспомогательный класс для обёртывания списка адресатов. Используется для
 * сохранения списка адресатов в XML.
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {

	private List<User> persons;

	@XmlElement(name = "person")
	public List<User> getPersons() {
		return persons;
	}

	public void setPersons(List<User> persons) {
		this.persons = persons;
	}
}