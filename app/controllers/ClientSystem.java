package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.AppOptionsHelper;

import com.sun.istack.internal.logging.Logger;

import models.User;

public class ClientSystem{

	private Properties _prop;
	private String _patternEmail;
	private String _patternName;

	public ClientSystem(){
		_prop = AppOptionsHelper.getProperties();
		if (_prop == null){
			_patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			_patternName = "^[_A-Za-z0-9-\\+]";
		}
		else{
			_patternEmail = _prop.getProperty("emailPattern");
			_patternName = _prop.getProperty("namePattern");
		}
	}

	public int authentication(String parIdentificator, String password){
		if (Pattern.matches(_patternEmail,parIdentificator) && Pattern.matches(_patternName, parIdentificator)){
			//TODO: appeler DAOGEST.getUser(Email, password);
			//si retour valide alors creer User et set sa connection.
		
			return 0;
		}
		else{
			//TODO: changer syso en logger.warn
			System.out.println("L'user "+ parIdentificator+" n'est pas une entr�e valide");
			return -1;
		}
	}

	/**
	 * V�rifie l'int�grit� des valeurs entr�es et demande l'ajout de l'user en base
	 * 
	 * @param parUser
	 * @return
	 */
	public int addUser(User parUser) {
		if(!Pattern.matches(_patternEmail, parUser.getEmail())){
			//TODO: changer syso en logger.warn
			System.out.println("L'email " + parUser.getEmail()+ " n'est pas au bon format.");
			return -1;
		}
		if(!Pattern.matches(_patternName, parUser.getName())){
			//TODO: changer syso en logger.warn
			System.out.println("Le nom " + parUser.getName()+ " contient des caract�res interdits.");
			return -1;
		}
		// TODO Appeler cr�ation de user en base et v�rifier son type de retour
		//envoie mail confirmation ici?
		// return (DAOGEST.addUser());
		return 0;
	}

	/**
	 * V�rifie l'int�grit� du param�tre et demande au gestionnaire de base de renvoyer l'user cherch�
	 * 
	 * @param parIdentificator: soit le nom de l'user, soit son mail
	 * @return
	 */
	public User getUser(String parIdentificator) {
		if (Pattern.matches(_patternEmail,parIdentificator)){
			//TODO: appeler DAOGEST.getUser(Email, champEmail);
		}
		else if (Pattern.matches(_patternName, parIdentificator)){
			//TODO: appeler DAOGEST.getUser(Name, champName)
		}
		else{
			//TODO: changer syso en logger.warn
			System.out.println("L'user "+ parIdentificator+" n'est pas une entr�e valide");
			return null;
		}
		return null;
	}

}
