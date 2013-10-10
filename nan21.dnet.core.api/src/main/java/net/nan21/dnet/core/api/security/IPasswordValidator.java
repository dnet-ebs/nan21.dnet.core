package net.nan21.dnet.core.api.security;

public interface IPasswordValidator {

	public void validate(String passwordToValidate) throws Exception;
}
